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

import java.io.InputStream;
import java.io.PrintWriter;

import Login.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CosaStampaController implements Initializable{
    @FXML private Label nameUser;
    @FXML private ImageView mostraimage;
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
        //determino che difficoltà devo caricare
        int[] score = this.loggedUtente.getScore();
        int k = 0; // può assumetere 0 facile, 1 medio, 2 difficile
        while(k < 3){
            if(score[k] == 0){
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
            Image image = new Image("Data/Code_CosaStampa/"+key+".JPG");
            mostraimage.setImage(image);
        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
        }
        //TODO: gestire quando finiscono i tre livelli di domanda
        if (score[2] == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma azione");
            alert.setHeaderText("Titolo della conferma");
            alert.setContentText("Sei sicuro di voler procedere con questa azione?");
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
                    System.out.println("Errore: "+e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML private void checkAnswer(ActionEvent event){
        String rispostaUtente = answer.getText(); // estrapolo la risposta dell'utente
        if(rispostaUtente.equals(rightAnswer)){ // controllo se la risposta è corretta
            //caso in cui sia corretta
            //Incremento il punteggio della difficoltà corrente
            int[] score = loggedUtente.getScore();
            for(int i = 0; i < 3; i++){
                if (score[i] == 0) {
                    loggedUtente.setScore(i); // incremento il punteggio della prima occorrenza con 0
                    break; // esco dal ciclo
                }
            }
            loadDomanda(); // carico una nuova domanda
        }
        
    }

    @FXML private void save(ActionEvent event){
        //TODO: fare il metodo per salvare il punteggio
        try{
            Scanner scan = new Scanner("Data/users.csv");
            Set<String[]> line = new HashSet<>();
            while (scan.hasNextLine()) {
                line.add(scan.nextLine().split(",")); // aggiungo la riga al set
            }
            scan.close();
            //ora lavoro sul set
            PrintWriter writer = new PrintWriter("Data/users.csv");
            for(String[] s : line){
                if (s[0].equals(loggedUtente.getUsername()) && s[1].equals(loggedUtente.getPassword()) && s[2].equals(loggedUtente.getEmail())){ // eseguo il check sull'utente
                    s = loggedUtente.onFile().split(","); // aggiorno la riga
                }
                writer.println(s);
            }
            writer.close();

        }catch(Exception e){
            System.out.println("Errore: "+e.getMessage());
     
        }
    }


    }
