package Esercizi.Catta;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
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
    private String difficulty = "semplice"; // Change to "medio" or "difficile" as needed

    private List<String> codeSegments;
    private String ordineCorretto;
    private Map<Character, String> letterToSegmentMap; // Mappa per associare lettere a segmenti di codice

    private Utente loggedUtente;
    private int currentExerciseIndex = 0;

    public void setUtente(Utente utente) {
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void loadDomanda() {
        String exerciseFilePath = "Learn - Program/src/Data/OrdinaCodice/" + difficulty + "/esercizi.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(exerciseFilePath))) {
            // Salta le righe fino all'indice corrente
            for (int i = 0; i <= currentExerciseIndex; i++) {
                // Leggi il titolo dell'esercizio (prima riga)
                String title = reader.readLine();
                if (title == null) {
                    // Se non ci sono più righe da leggere, esci
                    return;
                }
                exerciseTitle.setText(title);

                // Leggi i segmenti di codice fino a trovare la riga vuota
                codeSegments = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null && !line.isEmpty()) {
                    codeSegments.add(line);
                }

                // Leggi l'ordine corretto
                ordineCorretto = reader.readLine();

                // Associa le lettere ai segmenti di codice
                letterToSegmentMap = new HashMap<>();
                for (int j = 0; j < ordineCorretto.length(); j++) {
                    letterToSegmentMap.put(ordineCorretto.charAt(j), codeSegments.get(j));
                }
            }

            // Mescola i segmenti di codice
            List<Map.Entry<Character, String>> shuffledSegments = new ArrayList<>(letterToSegmentMap.entrySet());
            Collections.shuffle(shuffledSegments);

            displayCodeSegments(shuffledSegments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayCodeSegments(List<Map.Entry<Character, String>> shuffledSegments) {
        codeGrid.getChildren().clear();
        codeGrid.add(exerciseTitle, 0, 0, 2, 1);

        int rowIndex = 1;
        for (Map.Entry<Character, String> entry : shuffledSegments) {
            char letter = entry.getKey();
            String segment = entry.getValue();

            Label letterLabel = new Label(String.valueOf(letter));
            Label codeLabel = new Label(segment);
            codeGrid.add(letterLabel, 0, rowIndex);
            codeGrid.add(codeLabel, 1, rowIndex);
            rowIndex++;
        }
    }

    @FXML private void avanti() {
        String userOrder = orderInput.getText().trim().toUpperCase();
        if (userOrder.equals(ordineCorretto)) {
            currentExerciseIndex++;
            if (currentExerciseIndex == 4) {
                if (difficulty.equals("semplice")) {
                    difficulty = "medio";
                } else if (difficulty.equals("medio")) {
                    difficulty = "difficile";
                }
                currentExerciseIndex = 0;
            }

            double[] score = loggedUtente.getScore();
            for (int i = 3; i < 6; i++) {
                if ((int) score[i] == 0) {
                    loggedUtente.setScore(i); // incremento il punteggio della prima occorrenza con 0
                    break; // esco dal ciclo
                }
            }

            loadDomanda();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ordine Errato");
            alert.setHeaderText("L'ordine inserito non è corretto.");
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
                if (elements.length >= 11) {
                    lines.add(elements);
                } else {
                    System.out.println("Riga con formato errato: " + line);
                }
            }
            scan.close();

            File outputFile = new File("Learn - program/src/Data/users.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            for (String[] s : lines) {
                if (s[0].equals(loggedUtente.getUsername()) && s[1].equals(loggedUtente.getPassword()) && s[2].equals(loggedUtente.getEmail())) {
                    s = loggedUtente.onFile().split(",");
                }
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
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(frontScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di OrdinaCodice: --> " + e.getMessage());
            e.printStackTrace();
        }
    }
}
