package Esercizi.Pontini;

import java.io.*;
import java.net.URL;
import java.util.*;

import Esercizi.Front.FrontController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Login.Utente;

public class ConfrontaCodiceController implements Initializable {
    // Dichiarazione degli elementi UI
    @FXML private Label nameUser;
    @FXML private ImageView mostraimage;
    @FXML private TextField answer;
    @FXML private AnchorPane root; 
    @FXML private Label difficult;

    // Variabili di istanza per l'utente loggato, la risposta corretta, un contatore e le chiavi delle domande
    private Utente loggedUtente;
    private String rightAnswer;
    private int cont = 0;
    private List<String> questionKeys;

    // Metodo per impostare l'utente e visualizzare il suo nome
    public void setUtente(Utente utente) {
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
    }

    // Metodo chiamato all'inizializzazione del controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        loadDomanda();  // Carica la domanda quando la scena è pronta
                    }
                });
            }
        });
    }

    // Metodo per caricare una nuova domanda
    @FXML private void loadDomanda() {
        answer.clear();  // Pulisce il campo di risposta
        double[] score = this.loggedUtente.getScore();  // Ottiene il punteggio dell'utente
        int difficultyLevel = 6;  // Livello di difficoltà iniziale
        // Incrementa il livello di difficoltà se il punteggio non è zero
        while (difficultyLevel < 9 && (int)score[difficultyLevel] != 0) {
            difficultyLevel++;
        }
        loadQuestions(difficultyLevel);  // Carica le domande per il livello di difficoltà corrente
        if (score[8] == 1) {
            showCompletionAlert();  // Mostra un avviso di completamento se tutte le domande sono state risposte correttamente
        }
    }

    // Metodo per caricare le domande in base al livello di difficoltà
    private void loadQuestions(int difficultyLevel) {
        String difficultyPath;
        String difficultyText;
        String difficultyStyle;
        
        // Imposta il percorso, il testo e lo stile della difficoltà in base al livello
        switch (difficultyLevel) {
            case 6:
                difficultyPath = "/Data/Code_ConfrontaCodice/semplice/domande.txt";
                difficultyText = "Facile";
                difficultyStyle = "-fx-text-fill: green;";
                break;
                
            case 7:
                difficultyPath = "/Data/Code_ConfrontaCodice/medio/domande.txt";
                difficultyText = "Medio";
                difficultyStyle = "-fx-text-fill: yellow;";
                break;
            default:
                difficultyPath = "/Data/Code_ConfrontaCodice/difficile/domande.txt";
                difficultyText = "Difficile";
                difficultyStyle = "-fx-text-fill: red;";
                break;
        }
        
        difficult.setText(difficultyText);  // Imposta il testo della difficoltà
        difficult.setStyle(difficultyStyle);  // Imposta lo stile della difficoltà

        try (InputStream stream = getClass().getResourceAsStream(difficultyPath);
             Scanner scanner = new Scanner(stream)) {
            Map<String, String> questions = new HashMap<>();  // Mappa per memorizzare le domande e le risposte
            // Legge le domande e le risposte dal file
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                questions.put(line[0], line[1]);
            }
            questionKeys = new ArrayList<>(questions.keySet());  // Ottiene le chiavi delle domande
            setQuestion(questions);  // Imposta la domanda corrente
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());  // Gestisce eventuali eccezioni
        }
    }

    // Metodo per impostare la domanda corrente
    private void setQuestion(Map<String, String> questions) {
        // Controllo per ricominciare quando si supera l'ultimo livello della difficoltà
        if (cont >= questionKeys.size()) {
            cont = 0;   
        }
        String questionKey = questionKeys.get(cont);  // Ottiene la chiave della domanda corrente
        this.rightAnswer = questions.get(questionKey);  // Ottiene la risposta corretta per la domanda corrente
        // Imposta l'immagine della domanda corrente
        mostraimage.setImage(new Image("Data/Code_ConfrontaCodice/" + questionKey + ".JPG"));
        cont++;  
    }

    // Metodo per mostrare un avviso di completamento
    private void showCompletionAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Esericizio completato");
        alert.setHeaderText("Complimenti, hai superato tutti i livelli!");
        alert.setContentText("Completa tutti gli altri esercizi!");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
                Parent root = loader.load();
                FrontController frontController = loader.getController();
                frontController.setUtente(loggedUtente);  // Imposta l'utente nel controller del Front
                Stage stage = (Stage) this.root.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Errore in check: " + e.getMessage());
                e.printStackTrace();
            }
        }
    } 
    

    // Metodo per verificare la risposta dell'utente
    @FXML private void checkAnswer(ActionEvent event) {
        if (answer.getText().equals(rightAnswer)) {
            double[] score = loggedUtente.getScore();
            // Aggiorna il punteggio dell'utente
            for (int i = 6; i < 9; i++) {
                if ((int)score[i] == 0) {
                    loggedUtente.setScore(i);
                    break;
                }
            }
            loadDomanda();  // Carica una nuova domanda
        } else {
            // Mostra un avviso di errore se la risposta è sbagliata
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore!");
            alert.setContentText("La risposta è sbagliata, riprova!");
            alert.showAndWait();
            answer.clear();  // Pulisce il campo di risposta
        }
    }

    // Metodo per salvare i dati dell'utente su file
    @FXML private void save(ActionEvent event) {
        try {
            File inputFile = new File("Learn - program/src/Data/users.csv");
            if (!inputFile.exists()) {
                System.out.println("Errore: il file di input non esiste.");
                return;
            }

            Set<String[]> lines = new HashSet<>();
            try (Scanner scan = new Scanner(inputFile)) {
                while (scan.hasNextLine()) {
                    String[] elements = scan.nextLine().split(",");
                    if (elements.length >= 11) {
                        lines.add(elements);
                    } else {
                        System.out.println("Riga con formato errato: " + Arrays.toString(elements));
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
                for (String[] elements : lines) {
                    if (elements[0].equals(loggedUtente.getUsername()) && 
                        elements[1].equals(loggedUtente.getPassword()) && 
                        elements[2].equals(loggedUtente.getEmail())) {
                        elements = loggedUtente.onFile().split(",");
                    }
                    if (elements.length >= 11) {
                        writer.write(String.join(",", elements));
                        writer.newLine();
                    } else {
                        System.out.println("Riga con formato errato dopo aggiornamento: " + Arrays.toString(elements));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Errore in save: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Metodo per tornare alla schermata principale
    @FXML private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
            Parent front = loader.load();
            FrontController frontController = loader.getController();
            frontController.setUtente(this.loggedUtente);  // Imposta l'utente nel controller del Front
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(front));
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
            e.printStackTrace();
        }
    }
}
