package Esercizi.Lippi;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;

import Esercizi.Front.FrontController;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;

import Login.Utente;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CosaStampaController implements Initializable{
    @FXML private Label nameUser;
    //@FXML private ImageView mostraimage;
    @FXML private TextArea codeTextArea;
    @FXML private TextField answer;
    @FXML private AnchorPane root; 
    @FXML private Label difficult;

    private Utente loggedUtente;

    private String rightAnswer;

    public void setUtente(Utente utente){
        this.loggedUtente = utente;
        nameUser.setText(utente.toString());
    }

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


    @FXML private void loadDomanda(){
        //Pulisco sia la textArea che il campo di risposta
        codeTextArea.clear();
        answer.clear();
        //determino che difficoltà devo caricare
        double[] score = this.loggedUtente.getScore();
        int k = 0; // può assumetere 0 facile, 1 medio, 2 difficile
        while(k < 3){
            if((int)score[k] == 0){
                break;
            }
            k++;
        }
        //carico le domande della difficoltà k

        try{
            //Scanner scanner; 
            InputStream stream;
            switch (k) { // scanner che legge da file delle domande della giusta difficoltà
                case 0:
                    //scanner = new Scanner(new File("Data/Code_CosaStampa/semplice/domande.txt"));
                    stream= getClass().getResourceAsStream("/Data/Code_CosaStampa/semplice/domande.txt");
                    this.difficult.setText("Facile");
                    this.difficult.setStyle("-fx-text-fill: green;");
                    break;

                case 1:
                    //scanner = new Scanner(new File("Data/Code_CosaStampa/medio/domande.txt"));
                    stream= getClass().getResourceAsStream("/Data/Code_CosaStampa/medio/domande.txt");
                    this.difficult.setText("Medio");
                    this.difficult.setStyle("-fx-text-fill: yellow;");
                    break;
            
                default:
                    //scanner = new Scanner(new File("Data/Code_CosaStampa/difficile/domande.txt"));
                    stream= getClass().getResourceAsStream("/Data/Code_CosaStampa/difficile/domande.txt");
                    this.difficult.setText("Difficile");
                    this.difficult.setStyle("-fx-text-fill: red;");
                    break;
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
            Iterator<String> Iterator = domande.keySet().iterator();
            int i = 0;
            String key = "";
            while(Iterator.hasNext()){
                key = Iterator.next();
                if(i == random){
                    this.rightAnswer = domande.get(key);
                    break;
                }
                i++;
            }
            File file = new File("Learn - Program/src/Data/Code_CosaStampa/"+key);
            scanner = new Scanner(file);
            String code = "";
            while(scanner.hasNextLine()){
                code += scanner.nextLine() + "\n";
            }
            scanner.close();
            //Image image = new Image("Data/Code_CosaStampa/"+key+".JPG");
            //mostraimage.setImage(image);
            if (score[2] == 1) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Complimenti hai completato il livello difficile!");
                alert.setHeaderText("Fine di CosaStampa!");
                alert.setContentText("Ora puoi tornare al menu principale, ricordati di salvare i progressi!");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    // Procedi con l'azione
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
                    try{
                        Parent root = loader.load();
                        FrontController frontController = loader.getController();
                        frontController.setUtente(loggedUtente);
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) this.root.getScene().getWindow(); //prova
                        stage.setScene(scene);
                        stage.show();
                    }catch(Exception e){
                        System.out.println("Errore in caricamento front da cosastampa: "+e.getMessage());
                        e.printStackTrace();
                    }
                }
    
            }else
                typeWriteEffect(code, codeTextArea, 50);
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }
    }

    private void typeWriteEffect(String code, TextArea textArea, int typingSpeed){
        final int[] indx = {0};
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(
            Duration.millis(typingSpeed), 
            event -> {
                if (indx[0] < code.length()) {
                    textArea.appendText(String.valueOf(code.charAt(indx[0]++)));
                }
            }));
            timeline.setCycleCount(code.length());
            timeline.play();
    }

    @FXML private void checkAnswer(ActionEvent event){
        String rispostaUtente = answer.getText(); // estrapolo la risposta dell'utente
        if(rispostaUtente.equals(rightAnswer)){ // controllo se la risposta è corretta
            //caso in cui sia corretta
            //Incremento il punteggio della difficoltà corrente
            double[] score = loggedUtente.getScore();
            for(int i = 0; i < 3; i++){
                if ((int)score[i] == 0) {
                    loggedUtente.setScore(i); // incremento il punteggio della prima occorrenza con 0
                    break; // esco dal ciclo
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Corretto!");
            alert.setContentText("La risposta è corretta!");
            alert.showAndWait();
            loadDomanda(); // carico una nuova domanda
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sbagliato!");
            alert.setContentText("La risposta è sbagliata, riprova!");
            alert.showAndWait();
            answer.clear();
            
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
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            for (String[] s : lines) {
                if (s[0].equals(loggedUtente.getUsername()) && s[1].equals(loggedUtente.getPassword()) && s[2].equals(loggedUtente.getEmail())) { // Eseguo il check sull'utente
                    s = loggedUtente.onFile().split(","); // Aggiorno la riga
                }
            // Controllo che l'array s abbia almeno 11 elementi prima di accedere agli indici
                if (s.length >= 11) {
                    writer.println(s[0] + "," + s[1] + "," + s[2] + "," + s[3] + "," + s[4] + "," + s[5] + "," + s[6] + "," + s[7] + "," + s[8]+ "," + s[9]+ "," + s[10]+ "," + s[11]);
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

    @FXML private void back(ActionEvent event){
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
