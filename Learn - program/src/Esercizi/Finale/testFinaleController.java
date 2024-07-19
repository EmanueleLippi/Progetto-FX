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
import javafx.stage.Stage;
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
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;

public class testFinaleController implements Initializable {
    @FXML private Label nameUser;
    @FXML private TextArea codeTextArea;
    @FXML private AnchorPane root;
    @FXML private RadioButton ans1;
    @FXML private RadioButton ans2;
    @FXML private RadioButton ans3;
    @FXML private RadioButton ans4;
    @FXML private Label timerLabel;

    private ScheduledExecutorService scheduler; //per il timer
    private long startTime; //per il timer
    private ToggleGroup Answer; //per le risposte
    private Utente utente;
    private HashMap<String, Boolean> es_punteggio = new HashMap<>();  //lista di esercizi si può fare? o meglio map?
    private int rispostaCorretta; //è l'indice della risposta corretta
    private int i = 0; //indice per scorrere le domande

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
                        this.Answer = new ToggleGroup(); //crea un gruppo per i radio button
                        ans1.setToggleGroup(Answer);
                        ans2.setToggleGroup(Answer);
                        ans3.setToggleGroup(Answer);
                        ans4.setToggleGroup(Answer);
                        startTimer(); //avvia il timer
                        loadEsercizio(); //carica l'esercizio da risolvere
                    }
                });
            }
        });
    }



    //metodo per avviare il timer
    private void startTimer(){
        scheduler = Executors.newScheduledThreadPool(1); //crea un thread per il timer
        startTime = System.currentTimeMillis(); //tempo di inizio
        
        scheduler.scheduleAtFixedRate(() -> {
            long elapsedTime = System.currentTimeMillis() - startTime; //calcola il tempo trascorso
            long seconds = elapsedTime / 1000 % 60; //calcola i secondi
            long minutes = elapsedTime / (1000 * 60) % 60; //calcola i minuti
            long hours = elapsedTime / (1000 * 60 * 60); //calcola le ore
            
            // Aggiornare l'interfaccia utente nel thread della piattaforma JavaFX
            Platform.runLater(() -> { 
                timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds)); //aggiorna il timer
            });
        }, 0, 1, TimeUnit.SECONDS); //aggiorna il timer ogni secondo
    }

    //metodo per fermare il timer
    private void stopTimer() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();//ferma il timer
        }
    }


   // Metodo per caricare l'esercizio da risolvere
private void loadEsercizio(){
    // Pulizia della TextArea e deselezione dei RadioButton
    codeTextArea.clear();
    ans1.setSelected(false);
    ans2.setSelected(false);
    ans3.setSelected(false);
    ans4.setSelected(false);

    try{
        Scanner scan = new Scanner(new File("Learn - program/src/Data/Code_VerificaFinale/domande.txt"));
        LinkedHashMap<String, List<String>> dom_risp = new LinkedHashMap<>(); // Mappa per tenere traccia delle domande e risposte
        ArrayList<Integer> indx_risp = new ArrayList<>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] token = line.split(","); // Separa soluzioni domanda e indice risposta corretta
            String[] separaScelte = token[1].split("#"); // Insieme delle soluzioni
            indx_risp.add(Integer.parseInt(token[2]));
            dom_risp.put(token[0], Arrays.asList(separaScelte));
        }
        scan.close();

        // Converti le chiavi in una lista ordinata
        List<String> keys = new ArrayList<>(dom_risp.keySet());

        // Verifica se l'indice è valido
        if (i < keys.size()) {
            String key = keys.get(i);
            this.rispostaCorretta = indx_risp.get(i);

            // Mostra le opzioni di risposta
            ans1.setText(dom_risp.get(key).get(0));
            ans2.setText(dom_risp.get(key).get(1));
            ans3.setText(dom_risp.get(key).get(2));
            ans4.setText(dom_risp.get(key).get(3));
            i++;

            // Carica il codice dell'esercizio da risolvere
            File file = new File("Learn - Program/src/Data/Code_VerificaFinale/" + key);
            scan = new Scanner(file);
            StringBuilder code = new StringBuilder();
            while (scan.hasNextLine()) {
                code.append(scan.nextLine()).append("\n");
            }
            scan.close();

            // Se non è presente nella lista degli esercizi fatti, lo scrive
            if (!es_punteggio.containsKey(code.toString())) {
                typeWriteEffect(code.toString(), codeTextArea, 5);
            }
        }
    } catch (Exception e) {
        System.out.println("Errore nel caricamento delle domande.");
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
    //metodo per controllare la risposta
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
                    stopTimer();
                    int punteggio = 0;
                    for (Boolean value : es_punteggio.values()) {
                        if (value == true) {
                            punteggio++;
                        }
                    }
                String destinatario = utente.getEmail();
                String oggetto = "Punteggio Test Finale";
                String contenuto = "Complimenti hai completato il test finale con un punteggio di: " + punteggio + " su 10, in " + timerLabel.getText();
                //invio mail
                sendEmail(destinatario, oggetto, contenuto);

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
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selezione Risposta");
            alert.setHeaderText("Mi dispiace");
            alert.setContentText("Seleziona una risposta!");
            alert.showAndWait();
        }
    }

    //metodo per inviare una mail
    private void sendEmail(String destinatario, String oggetto, String contenuto){
        Properties properties = new Properties(); //creazione di un oggetto properties
        properties.put("mail.smtp.auth", "true"); //autenticazione
        properties.put("mail.smtp.starttls.enable", "true"); //abilita tls
        properties.put("mail.smtp.host", "smtp.gmail.com"); //host
        properties.put("mail.smtp.port", "587"); //porta

        //autenticazione utente
        String username = "dscwebconsulting@gmail.com";
        String password = "rqeq zqme tgrg hniz";

        //creazione di una sessione
        Session session = Session.getInstance(properties, new Authenticator() { //creazione di una sessione
            @Override
            protected PasswordAuthentication getPasswordAuthentication() { //autenticazione
                return new PasswordAuthentication(username, password); //ritorna l'autenticazione
            }
        });

        //creazione dell'email
        try { //creazione dell'email
            Message message = new MimeMessage(session); //creazione di un oggetto message
            message.setFrom(new InternetAddress(username)); //mittente dell'email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario)); //destinatario dell'email
            message.setSubject(oggetto); //oggetto dell'email
            message.setText(contenuto); //contenuto dell'email

            //invio dell'email
            javax.mail.Transport.send(message); //invio dell'email
            
            // Chiudi la finestra dell'applicazione
            Stage stage = (Stage) root.getScene().getWindow();  // Ottieni la finestra corrente
            stage.close(); //chiudo la finestra dell'applicazione
        } catch (Exception e) {
            System.out.println("Errore nell'invio dell'email");
            e.printStackTrace();
        }
    }

}
