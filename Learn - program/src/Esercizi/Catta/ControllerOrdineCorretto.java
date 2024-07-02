package Esercizi.Catta;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import Login.Utente;


public class ControllerOrdineCorretto {

    private Utente utente;
    @FXML private ImageView imageA;
    @FXML private ImageView imageB;
    @FXML private ImageView imageC;
    @FXML private ImageView imageD;
    @FXML private TextField sequenceInput;

    @FXML public void initialize() {
        // Carica le immagini
        imageA.setImage(new Image("file:src\\img\\esCatta\\facile\\variante1\\1.png"));
        imageB.setImage(new Image("file:src/img/esCatta/facile/variante1/2.png"));
        imageC.setImage(new Image("file:src/img/esCatta/facile/variante1/2.png"));
        imageD.setImage(new Image("file:src/img/esCatta/facile/variante1/2.png"));
    }

     @FXML private void handleSalva() {
        String correctSequence = "ABCD";  // La sequenza corretta
        String userSequence = sequenceInput.getText().toUpperCase();

        if (userSequence.equals(correctSequence)) {
            showCorrectAnswerPage();
        } else {
            showWrongAnswerPage();
        }
    }

    @FXML private void handleEsci() {
// Torna alla pagina Front.fxml
        // Torna alla pagina Front.fxml
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) sequenceInput.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCorrectAnswerPage() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Esercizi/Catta/RispostaCorretta.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Risposta Corretta");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

            // Chiudi la finestra corrente
            Stage currentStage = (Stage) sequenceInput.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showWrongAnswerPage() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Esercizi/Catta/RispostaSbagliata.fxml"));
            javafx.scene.Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Risposta Sbagliata");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();

            // Chiudi la finestra corrente
            Stage currentStage = (Stage) sequenceInput.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setUtente(Utente utente) {
        this.utente = utente;
    }

   
}

