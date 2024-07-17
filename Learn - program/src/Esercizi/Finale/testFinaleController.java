package Esercizi.Finale;
import Login.Utente;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;

public class testFinaleController implements Initializable {
    @FXML private Label nameUser;
    @FXML private TextArea codeTextArea;
    @FXML private AnchorPane root;
    @FXML private RadioButton ans1;
    @FXML private RadioButton ans2;
    @FXML private RadioButton ans3;
    @FXML private RadioButton ans4;

    private ToggleGroup Answer;
    private Utente utente;
    private HashMap<String, Boolean> es_punteggio = new HashMap<>();  //lista di esercizi si può fare? o meglio map?
    private int rispostaCorretta; //è l'indice della risposta corretta

    public void setUtente(Utente user){
        this.utente = user;
        nameUser.setText(utente.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //logica
        //creare 4 esercizi per ogni tipo, gestirlo mostrandoli su una textArea e fare scegliere
        //all'utente la risposta con una combobox e radio button
        //IMPORTANTE: TEST DA FARE PER INTERO ENTRO 15 MINUTI? TEMPORIZZATORE? DA FARE PER INTERO
        
        //crea listener per il caricamento della pagina
        root.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        //qui da inserire le azioni da fare al caricamento della pagina
                        this.Answer = new ToggleGroup();
                        ans1.setToggleGroup(Answer);
                        ans2.setToggleGroup(Answer);
                        ans3.setToggleGroup(Answer);
                        ans4.setToggleGroup(Answer);

                        loadEsercizio();
                    }
                });
            }
        });
    }

    //metodo per caricare l'esercizio da risolvere
    private void loadEsercizio(){
        //così pulisco tutta la videata
        codeTextArea.clear();
        ans1.setSelected(false);
        ans1.setText("");
        ans2.setSelected(false);
        ans2.setText("");
        ans3.setSelected(false);
        ans3.setText("");
        ans4.setSelected(false);
        ans4.setText("");

        //qui devo caricare l'esercizio da risolvere
        try{
            Scanner scan = new Scanner(new File("Learn - program/src/Data/Code_VerificaFinale/domande.txt"));
            HashMap<String, List<String>> dom_risp = new HashMap<>(); //per tenere traccia delle domande e relative risposte
            ArrayList<Integer> indx_risp = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] token = line.split(",");//separa soluzioni domanda e indx risposta corretta
                String[] separaScelte = token[1].split("#");//questo è l'insieme della soluzioni
                indx_risp.add(Integer.parseInt(token[2]));

                dom_risp.put(token[0], Arrays.asList(separaScelte));
            }
            scan.close();
            int random = (int) (Math.random() * dom_risp.size());
            Iterator<String> it = dom_risp.keySet().iterator();
            int i = 0;
            String key = "";
            while (it.hasNext()) {
                key = it.next();
                if (i == random) {
                    this.rispostaCorretta = indx_risp.get(i);
                    //mostrare le opzioni di risposta
                    ans1.setText(dom_risp.get(key).get(0));
                    ans2.setText(dom_risp.get(key).get(1));
                    ans3.setText(dom_risp.get(key).get(2));
                    ans4.setText(dom_risp.get(key).get(3));
                    break;
                }
            }
            File file = new File("Learn - Program/src/Data/Code_VerificaFinale/"+key);
            scan = new Scanner(file);
            String code = "";
            while(scan.hasNextLine()){
                code += scan.nextLine() + "\n";
            }
            scan.close();
            //se non è presente nella lista degli esercizi fatti, allora lo scrivo
            if (!es_punteggio.containsKey(code)) {
                typeWriteEffect(code, codeTextArea, 50);                
            }

        }catch (Exception e){
            System.out.println("Errore try scanner file domande.txt");
            e.printStackTrace();
        }


    }

    //metodo per scrivere il codice a video
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
        RadioButton selectedRadioButton = (RadioButton) Answer.getSelectedToggle();
        if (selectedRadioButton != null ){
            int selectedIndx = Answer.getToggles().indexOf(selectedRadioButton);
            if (selectedIndx == rispostaCorretta) {
                es_punteggio.put(codeTextArea.getText(), true); 
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Risposta Corretta");
                alert.setHeaderText("Complimenti");
                alert.setContentText("Risposta corretta, continua così!");
                alert.showAndWait();
                if (es_punteggio.size() < 10) { //si prevede che il test finale sia di 10 domande
                    loadEsercizio();
                }
                else{ // quando si concludono le 10 domande
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Fine Test");
                    alert2.setHeaderText("Hai completato il test");
                    alert2.setContentText("Hai completato il test, clicca su OK e guarda la posta elettronica per il tuo punteggio");
                    alert2.showAndWait();
                    int punteggio = 0;
                    for (Boolean value : es_punteggio.values()) {
                        if (value == true) {
                            punteggio++;
                        }
                    }
                //TODO --> INVIARE UNA MAIL ALL'UTENTE CON IL PUNTEGGIO
                String destinatario = utente.getEmail();
                String oggetto = "Punteggio Test Finale";
                String contenuto = "Complimenti hai completato il test finale con un punteggio di: " + punteggio + " su 10";
                //invio mail
                sendEmail(destinatario, oggetto, contenuto);

            }
            }
            
            
        }
        else{
            es_punteggio.put(codeTextArea.getText(), false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Risposta Errata");
            alert.setHeaderText("Mi dispiace");
            alert.setContentText("Risposta errata, riprova!");
            alert.showAndWait();
            loadEsercizio();
        }
    }

    //metodo per inviare una mail
    private void sendEmail(String destinatario, String oggetto, String contenuto){
        //TODO --> IMPLEMENTARE L'INVIO DI UNA MAIL
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        //autenticazione utente
        String username = "dscwebconsulting@gmail.com";
        String password = "DioScalzo2024";

        //creazione di una sessione
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        //creazione dell'email
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(oggetto);
            message.setText(contenuto);

            //invio dell'email
            javax.mail.Transport.send(message);
            System.out.println("Email inviata con successo");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Errore nell'invio dell'email");
            e.printStackTrace();
        }
    }

}
