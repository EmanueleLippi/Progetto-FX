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
    @FXML private Label nameUser;
    @FXML private ImageView mostraimage;
    @FXML private TextField answer;
    @FXML private AnchorPane root; 
    @FXML private Label difficult;

    private Utente loggedUtente;
    private String rightAnswer;

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
                        loadDomanda();
                    }
                });
            }
        });
    }

    @FXML private void loadDomanda() {
        double[] score = this.loggedUtente.getScore();
        int difficultyLevel = 0;
        while (difficultyLevel < 3 && score[difficultyLevel] != 0) {
            difficultyLevel++;
        }
        loadQuestions(difficultyLevel);
        if (score[2] == 1) {
            showCompletionAlert();
        }
    }

    private void loadQuestions(int difficultyLevel) {
        String difficultyPath;
        String difficultyText;
        String difficultyStyle;
        
        switch (difficultyLevel) {
            case 0:
                difficultyPath = "/Data/Code_ConfrontaCodice/semplice/domande.txt";
                difficultyText = "Facile";
                difficultyStyle = "-fx-text-fill: green;";
                break;
                
            case 1:
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
        
        difficult.setText(difficultyText);
        difficult.setStyle(difficultyStyle);

        try (InputStream stream = getClass().getResourceAsStream(difficultyPath);
             Scanner scanner = new Scanner(stream)) {
            Map<String, String> questions = new HashMap<>();
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                questions.put(line[0], line[1]);
            }
            setRandomQuestion(questions);
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void setRandomQuestion(Map<String, String> questions) {
        List<String> keys = new ArrayList<>(questions.keySet());
        String randomKey = keys.get(new Random().nextInt(keys.size()));
        this.rightAnswer = questions.get(randomKey);
        mostraimage.setImage(new Image("Data/Code_ConfrontaCodice/" + randomKey + ".JPG"));
    }

    private void showCompletionAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma azione");
        alert.setHeaderText("Titolo della conferma");
        alert.setContentText("Sei sicuro di voler procedere con questa azione?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
                Parent root = loader.load();
                FrontController frontController = loader.getController();
                frontController.setUtente(loggedUtente);
                Stage stage = (Stage) this.root.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                System.out.println("Errore in check: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML private void checkAnswer(ActionEvent event) {
        if (answer.getText().equals(rightAnswer)) {
            double[] score = loggedUtente.getScore();
            for (int i = 0; i < 3; i++) {
                if (score[i] == 0) {
                    loggedUtente.setScore(i);
                    break;
                }
            }
            loadDomanda();
        }
    }

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

    @FXML private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
            Parent front = loader.load();
            FrontController frontController = loader.getController();
            frontController.setUtente(this.loggedUtente);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(front));
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
            e.printStackTrace();
        }
    }
}

