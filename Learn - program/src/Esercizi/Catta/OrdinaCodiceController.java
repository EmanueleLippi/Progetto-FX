package Esercizi.Catta;

// tutti gli import per il funzionamento del controller

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Esercizi.Front.FrontController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import Login.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class OrdinaCodiceController implements Initializable {
    @FXML private Label nameUser;
    @FXML private ImageView mostraimage;
    @FXML private TextField answer;
    @FXML private AnchorPane root;
    @FXML private Label difficult;
    @FXML private GridPane imageGrid;
    @FXML private GridPane codeGrid;
    @FXML private Label exerciseTitle;
    @FXML private TextField orderInput;
    @FXML private Button handleBackButton;
    @FXML private Button handleNextButton;
    
    private String difficulty; 
    private List<String> codeSegments;
    private String ordineCorretto;
    private Map<Character, String> letterToSegmentMap; // Mappa per associare lettere a segmenti di codice
    private Utente loggedUtente;
    private int currentExerciseIndex = 0;

    // setta l'utente corrente e recupera la difficoltà a cui l'utente era arrivato

    public void setUtente(Utente utente) {
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
        difficulty = loggedUtente.getDiffCOrrenteOrdinaCodice(); // Imposta la difficoltà corrente
    }

    // -----------------------------------------------------------------------------------------------------------------------------------

    @Override public void initialize(URL location, ResourceBundle resources) {
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

    // -----------------------------------------------------------------------------------------------------------------------------------
    // metodo principale per caricare la giusta domanda in base alla difficoltà

    private void loadDomanda() {
        String exerciseFilePath = "Learn - Program/src/Data/OrdinaCodice/" + difficulty + "/esercizi.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(exerciseFilePath))) {
            // Salta le righe fino all'indice corrente
            for (int i = 0; i <= currentExerciseIndex; i++) {
                // Leggi il titolo dell'esercizio (prima riga)
                String title = reader.readLine();
                if (title == null) {
                    return; // Se non ci sono più righe da leggere, esci
                }
                exerciseTitle.setText(title);

                // Leggi i segmenti di codice fino a trovare la riga vuota
                codeSegments = new ArrayList<>();
                String line;
                // aggiunge all'ArrayList le stringhe contenenti le parti di codice da ordinare
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    codeSegments.add(line);
                }

                // Recupera dal file di testo la soluzione dell'esercizio
                ordineCorretto = reader.readLine();

                // Associa in maniera univoca le lettere ai segmenti di codice
                letterToSegmentMap = new HashMap<>(); // mappa che associa ad ogni lettere maiuscola una stringa
                for (int j = 0; j < ordineCorretto.length(); j++) {
                    letterToSegmentMap.put(ordineCorretto.charAt(j), codeSegments.get(j));
                }
            }

            // Ordina i segmenti di codice in ordine alfabetico
            
            // letterToSegmentMap.entrySet() restituisce un set di tutte le coppie chiave-valore 
            List<Map.Entry<Character, String>> sortedSegments = new ArrayList<>(letterToSegmentMap.entrySet()); 
            
            // sortedSegments.sort(...) ordina la lista sortedSegments in base a un criterio specificato da Comparator
            // confronta le chiavi Character
            // Map.Entry::getKey restituisce la chiave di una Map.Entry
            sortedSegments.sort(Comparator.comparing(Map.Entry::getKey));

            // richiama il metodo per mostrare a schermo i segmenti
            displayCodeSegments(sortedSegments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo per mostrare a schermo i segmenti

    private void displayCodeSegments(List<Map.Entry<Character, String>> sortedSegments) {
        codeGrid.getChildren().clear();
        //Aggiunge il titolo dell'esercizio alla griglia nelle colonne 0 e 1, riga 0, il titolo occupa due colonne.
        codeGrid.add(exerciseTitle, 0, 0, 2, 1);

        int rowIndex = 1;
        for (Map.Entry<Character, String> entry : sortedSegments) {
            char letter = entry.getKey(); // Estrae la lettera dalla coppia corrente
            String segment = entry.getValue(); // Estrae il segmento di codice dalla coppia corrente

            // crea le label
            Label letterLabel = new Label(String.valueOf(letter));
            Label codeLabel = new Label(segment);

            // aggiunge le label
            codeGrid.add(letterLabel, 0, rowIndex);
            codeGrid.add(codeLabel, 1, rowIndex);
            rowIndex++;
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo richiamato quando l'utente clicca sul tasto "Avanti" presente nel file .fxml
    
    @FXML private void avanti() {
        // Controlla se la sequenza di lettere dell'utente è corretta
        String userOrder = orderInput.getText().trim().toUpperCase();
        if (userOrder.equals(ordineCorretto)) {
            currentExerciseIndex++;
            loggedUtente.aggiornaDiff(difficulty);

        // Se ha completato tutti gli esercizi della modalità difficile torna alla dashboard
        if (currentExerciseIndex == 4 && difficulty.equals("difficile")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Completato!");
            alert.setHeaderText("Hai completato tutti gli esercizi nella modalità difficile.");
            alert.setContentText("Ottimo lavoro!");

            // Mostra l'alert e gestisci la risposta dell'utente
            alert.showAndWait();
            return;
        }
    
            if (currentExerciseIndex == 4) {
                // Aggiorna la difficoltà solo dopo aver completato tutti e 4 gli esercizi
                if (difficulty.equals("semplice")) {
                    difficulty = "medio";
                } else if (difficulty.equals("medio")) {
                    difficulty = "difficile";
                }
                currentExerciseIndex = 0;
            }
    
            // Pulisci la casella di testo
            orderInput.clear();
            // Salva e carica la domanda successiva
            save();
            loadDomanda();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ordine Errato");
            alert.setHeaderText("L'ordine inserito non è corretto.");
            alert.setContentText("Riprova!");
            alert.showAndWait();
        }
    }
    
    
    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo per il salvataggio

    @FXML private void save() {
        // Prepara il file per la lettura
        File inputFile = new File("Learn - program/src/Data/users.csv");
        if (!inputFile.exists()) {
            System.out.println("Errore: il file di input non esiste.");
            return;
        }
    
        // Prepara una lista di righe aggiornate
        List<String> lines = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elements = line.split(",");
                if (elements.length >= 12) {
                    if (elements[0].equals(loggedUtente.getUsername()) && elements[1].equals(loggedUtente.getPassword()) && elements[2].equals(loggedUtente.getEmail())) {
                        // Aggiorna la linea dell'utente loggato
                        elements = loggedUtente.onFile().split(",");
                    }
                    lines.add(String.join(",", elements));
                } else {
                    System.out.println("Riga con formato errato: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Errore in save: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    
        // Prepara il file per la scrittura
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
            for (String s : lines) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore in save: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo richiamato quando l'utente clicca sul pulsante "torna alla dashboard"

    @FXML private void tornaDashboard(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
            Parent front = loader.load();
            FrontController frontController = loader.getController();
            frontController.setUtente(this.loggedUtente);
            Scene frontScene = new Scene(front);
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow(); //prova
            stage.setScene(frontScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
            e.printStackTrace();
        }
    }

}
