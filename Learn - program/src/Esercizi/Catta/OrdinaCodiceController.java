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
    @FXML private GridPane spazioCodice;
    @FXML private Label titoloEs;
    @FXML private TextField input;
    
    private String difficolta; 
    private List<String> segmentiCodice;
    private String ordineCorretto;
    private Map<Character, String> lettereSegmentiMap; // Mappa per associare lettere a segmenti di codice
    private Utente loggedUtente;
    private int IndiceEsercizioCorrente = 0;

    // setta l'utente corrente e recupera la difficoltà a cui l'utente era arrivato

    public void setUtente(Utente utente) {
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
        difficolta = getDiffCOrrenteOrdinaCodice(); // Imposta la difficoltà corrente
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
    /*
     * Logica del metodo loadDomanda
     * 
     * legge la prima riga e la imposta come titolo
     * dalla riga successiva alla prima riga vuota, aggiunge le righe in un ArrayList
     * la riga successiva a quella vuota contiene la soluzione (l'ordine corretto delle lettere) e viene memorizzata
     * crea una mappa che associa ogni lettera della soluzione al segmento di codice corrispondente
     * converte la mappa in una lista e ordina gli elementi di questa lista in base all'ordine alfabetico
     * In questo modo i segmenti saranno associati univocamente alle lettere e saranno visualizzati dall'utente in ordine alfabetico
     * chiama il metodo mostraSegmentiCodice 
     */

    private void loadDomanda() {

        // switch che serve a mostrare a schermo la giusta difficoltà e con il giusto colore 

        switch (difficolta) { 
            case "semplice":
                this.difficult.setText("Facile");
                this.difficult.setStyle("-fx-text-fill: green;");
                break;

            case "medio":
                this.difficult.setText("Medio");
                this.difficult.setStyle("-fx-text-fill: yellow;");
                break;
        
            default:
                this.difficult.setText("Difficile");
                this.difficult.setStyle("-fx-text-fill: red;");
                break;
        }

        // recupera in base a difficolta il giusto file
        String exerciseFilePath = "Learn - Program/src/Data/OrdinaCodice/" + difficolta + "/esercizi.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(exerciseFilePath))) {
            // Salta le righe fino all'indice corrente
            for (int i = 0; i <= IndiceEsercizioCorrente; i++) {
                // Leggi il titolo dell'esercizio (prima riga)
                String title = reader.readLine();
                if (title == null) {
                    return; // Se non ci sono più righe da leggere, esci
                }
                titoloEs.setText(title);

                // Leggi i segmenti di codice fino a trovare la riga vuota
                segmentiCodice = new ArrayList<>();
                String line;
                // aggiunge all'ArrayList le stringhe contenenti le parti di codice da ordinare
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    segmentiCodice.add(line);
                }

                // Recupera dal file di testo la soluzione dell'esercizio
                ordineCorretto = reader.readLine();

                // Associa in maniera univoca le lettere ai segmenti di codice
                lettereSegmentiMap = new HashMap<>(); // mappa che associa ad ogni lettere maiuscola una stringa
                for (int j = 0; j < ordineCorretto.length(); j++) {
                    lettereSegmentiMap.put(ordineCorretto.charAt(j), segmentiCodice.get(j));
                }
            }

            // Ordina i segmenti di codice in ordine alfabetico
            
            // lettereSegmentiMap.entrySet() restituisce un set di tutte le coppie chiave-valore 
            List<Map.Entry<Character, String>> sortedSegments = new ArrayList<>(lettereSegmentiMap.entrySet()); 
            
            // sortedSegments.sort(...) ordina la lista sortedSegments in base a un criterio specificato da Comparator
            // confronta le chiavi Character
            // Map.Entry::getKey restituisce la chiave di una Map.Entry
            sortedSegments.sort(Comparator.comparing(Map.Entry::getKey));

            // richiama il metodo per mostrare a schermo i segmenti
            mostraSegmentiCodice(sortedSegments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo per mostrare a schermo i segmenti

    private void mostraSegmentiCodice(List<Map.Entry<Character, String>> sortedSegments) {
        spazioCodice.getChildren().clear();
        //Aggiunge il titolo dell'esercizio alla griglia nelle colonne 0 e 1, riga 0, il titolo occupa due colonne.
        spazioCodice.add(titoloEs, 0, 0, 2, 1);

        int rowIndex = 1;
        for (Map.Entry<Character, String> entry : sortedSegments) {
            char letter = entry.getKey(); // Estrae la lettera dalla coppia corrente
            String segment = entry.getValue(); // Estrae il segmento di codice dalla coppia corrente

            // crea le label
            Label letterLabel = new Label(String.valueOf(letter));
            Label codeLabel = new Label(segment);

            // aggiunge le label
            spazioCodice.add(letterLabel, 0, rowIndex);
            spazioCodice.add(codeLabel, 1, rowIndex);
            rowIndex++;
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // metodo richiamato quando l'utente clicca sul tasto "Avanti" presente nel file .fxml
    
    @FXML private void avanti() {
        // Controlla se la sequenza di lettere dell'utente è corretta
        String userOrder = input.getText().trim().toUpperCase();
        if (userOrder.equals(ordineCorretto)) {
            // incrementa l'indice (da 1 a 4)
            IndiceEsercizioCorrente++;
            // aggiorna il punteggio nell'array relativo all'utente
            aggiornaPunteggio(difficolta);

            Alert alertGiusto = new Alert(Alert.AlertType.INFORMATION);
            alertGiusto.setTitle("Corretto!");
            alertGiusto.setContentText("La risposta è corretta!");
            alertGiusto.showAndWait();

        // Se ha completato tutti gli esercizi della modalità difficile avvisa l'utente
        if (IndiceEsercizioCorrente == 4 && difficolta.equals("difficile")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Completato!");
            alert.setHeaderText("Hai completato tutti gli esercizi nella modalità difficile.");
            alert.setContentText("Ottimo lavoro!");

            // Mostra l'alert e gestisci la risposta dell'utente
            alert.showAndWait();
            return;
        }
    
        if (IndiceEsercizioCorrente == 4) {
            // Aggiorna la difficoltà da cui dipende la scelta dell'esercizio solo dopo aver completato tutti e 4 le versioni 
            if (difficolta.equals("semplice")) {
                difficolta = "medio";
            } else if (difficolta.equals("medio")) {
                difficolta = "difficile";
            }
            IndiceEsercizioCorrente = 0;
        }
    
            // Pulisci la casella di testo
            input.clear();
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

    @FXML
    private void save() {
        // Prepara il file per la lettura
        File inputFile = new File("Learn - program/src/Data/users.csv");
        if (!inputFile.exists()) {
            // Controlla se il file di input esiste
            System.out.println("Errore: il file di input non esiste.");
            return;
        }
        // Prepara una lista di righe aggiornate
        List<String> lines = new ArrayList<>();
    
        // Lettura del file riga per riga
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Divide la riga in elementi utilizzando la virgola come delimitatore
                String[] elements = line.split(",");
                if (elements.length >= 12) {
                    // Controlla se la riga corrisponde all'utente loggato
                    if (elements[0].equals(loggedUtente.getUsername()) && elements[1].equals(loggedUtente.getPassword()) && elements[2].equals(loggedUtente.getEmail())) {
                        // Aggiorna la riga con le informazioni dell'utente loggato
                        elements = loggedUtente.onFile().split(",");
                    }
                    // Aggiunge la riga aggiornata o originale alla lista
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
            // Scrive ogni riga della lista nel file
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

    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // Metodo per ottenere la difficoltà corrente dell'utente 

    private String getDiffCOrrenteOrdinaCodice() {
        double[] score = loggedUtente.getScore();
        if (score[3] >= 1.0) { 
            if (score[4] >= 1.0) { 
                return "difficile"; // Ritorna "difficile" se entrambi i livelli precedenti sono completati
            }
            return "medio"; // Ritorna "medio" se solo il livello "semplice" è completato
        }
        return "semplice"; // Ritorna "semplice" se il livello "semplice" non è ancora completato
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------
    // Metodo per aggiornare la difficoltà 

    private void aggiornaPunteggio(String diff) {
        double[] score = loggedUtente.getScore();
        int index = -1;
        switch (diff) {
            case "semplice":
                index = 3;
                break;
            case "medio":
                index = 4;
                break;
            case "difficile":
                index = 5;
                break;
        }
        if (index != -1) {
            if(score[index] <= 0.75){
                score[index] += 0.25;
            }
        }
    }
}
