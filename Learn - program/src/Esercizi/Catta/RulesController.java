package Esercizi.Catta;

import Esercizi.Front.FrontController;
import Login.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RulesController {

    private Utente utente;

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    @FXML
    private void IniziaEsercizio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrdineCorretto.fxml"));
            Parent root = loader.load();
            ControllerOrdineCorretto controllerOrdineCorretto = loader.getController();
            controllerOrdineCorretto.setUtente(utente);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void tornaDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/front.fxml"));
            Parent root = loader.load();
            FrontController frontController = loader.getController();
            frontController.setUtente(utente);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
