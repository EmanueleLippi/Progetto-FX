package Esercizi.Catta;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import Esercizi.Front.FrontController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import Login.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OrdinaCodiceController implements Initializable{
    @FXML private Label nameUser;
    @FXML private ImageView mostraimage;
    @FXML private TextField answer;
    @FXML private AnchorPane root; 
    @FXML private Label difficult;
    @FXML private GridPane imageGrid;


    private Utente loggedUtente;
    private String rightAnswer;
// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void setUtente(Utente utente){
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override 
    public void initialize(URL location, ResourceBundle resources){
        // Aggiungi un listener alla scena per chiamare loadDomanda quando la finestra viene mostrata
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        loadDomanda();  // Chiamare loadDomanda quando la finestra è mostrata
                    }
                });
            }
        });
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

@FXML private void loadDomanda() {
    // Determina la difficoltà da caricare
    int[] score = this.loggedUtente.getScore();
    int k = 0; // Può assumere 0 facile, 1 medio, 2 difficile
    while(k < 3){
        if(score[k] == 0){
            break;
        }
        k++;
    }

    // Carica le domande della difficoltà k
    try {
        InputStream stream = null;
        String resourcePath = "";
        switch (k) { // Scanner che legge da file delle domande della giusta difficoltà
            case 0:
                resourcePath = "/Data/OrdinaCodice/semplice/domande.txt";
                stream = getClass().getResourceAsStream(resourcePath);
                this.difficult.setText("Facile");
                this.difficult.setStyle("-fx-text-fill: green;");
                break;

            case 1:
                resourcePath = "/Data/OrdinaCodice/medio/domande.txt";
                stream = getClass().getResourceAsStream(resourcePath);
                this.difficult.setText("Medio");
                this.difficult.setStyle("-fx-text-fill: yellow;");
                break;

            default:
                resourcePath = "/Data/OrdinaCodice/difficile/domande.txt";
                stream = getClass().getResourceAsStream(resourcePath);
                this.difficult.setText("Difficile");
                this.difficult.setStyle("-fx-text-fill: red;");
                break;
        }

        if (stream == null) {
            throw new IllegalArgumentException("Invalid URL or resource not found: " + resourcePath);
        }

        Scanner scanner = new Scanner(stream);
        HashMap<String, String> domande = new HashMap<>();
        while(scanner.hasNextLine()) {
            String lineString = scanner.nextLine();
            String[] line = lineString.split(",");
            domande.put(line[0], line[1]);
        }
        scanner.close();

        int random = (int)(Math.random() * domande.size());
        Iterator<String> iterator = domande.keySet().iterator();
        int i = 0;
        String key = "";
        while(iterator.hasNext()){
            key = iterator.next();
            if(i == random){
                this.rightAnswer = domande.get(key);
                break;
            }
            i++;
        }

        int numImages;
        switch (k) {
            case 0:
                numImages = 3;
                break;
            case 1:
                numImages = 5;
                break;
            default:
                numImages = 7;
                break;
        }

        // Rimuovi eventuali immagini precedenti
        imageGrid.getChildren().clear();

        // Aggiungi le immagini e le lettere al GridPane
        String[] letters = {"A", "B", "C"};
        for (int j = 1; j <= numImages; j++) {
            String imagePath = "/Data/OrdinaCodice/" + key +"/"+j+".PNG";
            System.out.println("Loading image: " + imagePath); // Debug: stampa il percorso dell'immagine
            Image image = new Image(imagePath);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100); // Imposta l'altezza delle immagini
            imageView.setFitWidth(150);  // Imposta la larghezza delle immagini
            imageView.setPreserveRatio(true); // Mantieni il rapporto d'aspetto

            // Calcola l'indice della riga e della colonna
            int rowIndex = (j - 1) * 2; // Ogni immagine avrà una riga per sé
            int columnIndex = 0;

            // Aggiungi l'immagine al GridPane
            imageGrid.add(imageView, columnIndex, rowIndex);

            // Aggiungi la lettera sotto l'immagine
            Label letterLabel = new Label(letters[j - 1]);
            letterLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            imageGrid.add(letterLabel, columnIndex, rowIndex + 1);
        }

    } catch(Exception e) {
        System.out.println("Errore: " + e.getMessage());
        e.printStackTrace();
    }

    // TODO: Gestire quando finiscono i tre livelli di domanda
    if (score[2] == 1) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma azione");
        alert.setHeaderText("Titolo della conferma");
        alert.setContentText("Sei sicuro di voler procedere con questa azione?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Procedi con l'azione
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
                Parent root = loader.load();
                FrontController frontController = loader.getController();
                frontController.setUtente(loggedUtente);
                Scene scene = new Scene(root);
                Stage stage = (Stage) this.root.getScene().getWindow(); // prova
                stage.setScene(scene);
                stage.show();
            } catch(Exception e) {
                System.out.println("Errore in check: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

    
    

    @FXML private void checkAnswer(ActionEvent event){
        String rispostaUtente = answer.getText(); // estrapolo la risposta dell'utente
        if(rispostaUtente.equals(rightAnswer)){ // controllo se la risposta è corretta
            // Caso in cui sia corretta
            // Incremento il punteggio della difficoltà corrente
            int[] score = loggedUtente.getScore();
            for(int i = 3; i < 6; i++){
                if (score[i] == 0) {
                    loggedUtente.setScore(i); // incremento il punteggio della prima occorrenza con 0
                    break; // esco dal ciclo
                }
            }
            loadDomanda(); // carico una nuova domanda
        } else {
            // Gestisci il caso in cui la risposta sia sbagliata
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Risposta sbagliata");
            alert.setHeaderText("La tua risposta è sbagliata");
            alert.setContentText("Riprova!");
            alert.showAndWait();
        }
    }
    

    @FXML private void save(ActionEvent event) {
        try {
            File inputFile = new File("Learn - program/src/Data/users.csv");
            if (!inputFile.exists()) {
                System.out.println("Errore: il file di input non esiste.");
                return;
            }
    
            Scanner scan = new Scanner(inputFile);
            Set<String[]> lines = new HashSet<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] elements = line.split(",");
                if (elements.length >= 11) { // Verifica che ci siano almeno 11 elementi
                    lines.add(elements); // Aggiungo la riga al set
                } else {
                    System.out.println("Riga con formato errato: " + line);
                }
            }
            scan.close();
    
            // Ora lavoro sul set
            File outputFile = new File("Learn - program/src/Data/users.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            for (String[] s : lines) {
                if (s[0].equals(loggedUtente.getUsername()) && s[1].equals(loggedUtente.getPassword()) && s[2].equals(loggedUtente.getEmail())) { // Eseguo il check sull'utente
                    s = loggedUtente.onFile().split(","); // Aggiorno la riga
                }
                // Controllo che l'array s abbia almeno 11 elementi prima di accedere agli indici
                if (s.length >= 11) {
                    writer.write(String.join(",", s));
                    writer.newLine();
                } else {
                    System.out.println("Riga con formato errato dopo aggiornamento: " + String.join(",", s));
                }
            }
            writer.close();
    
        } catch (Exception e) {
            System.out.println("Errore in save: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML private void tornaDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
            Parent front = loader.load();
            FrontController frontController = loader.getController();
            frontController.setUtente(this.loggedUtente);
            Scene frontScene = new Scene(front);
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            stage.setScene(frontScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di OrdinaCodice: --> " + e.getMessage());
            e.printStackTrace();
        }
    }
    


}

