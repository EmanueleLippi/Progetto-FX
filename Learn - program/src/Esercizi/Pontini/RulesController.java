package Esercizi.Pontini;

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

    private Utente utente; // Variabile per memorizzare l'utente loggato

    // Metodo per impostare l'utente
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    // Metodo chiamato quando l'utente clicca sul pulsante per iniziare l'esercizio
    @FXML
    private void IniziaEsercizio(ActionEvent event) {
        try {
            // Carica il file FXML dell'interfaccia ConfrontaCodice
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Pontini/ConfrontaCodice.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della scena ConfrontaCodice e imposta l'utente
            ConfrontaCodiceController confronta = loader.getController();
            confronta.setUtente(utente);

            // Imposta la nuova scena e mostra la finestra
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Stampa l'eccezione in caso di errore
        }
    }

    // Metodo chiamato quando l'utente clicca sul pulsante per tornare alla dashboard
    @FXML private void tornaDashboard(ActionEvent event) {
        try {
            // Carica il file FXML dell'interfaccia front
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/front.fxml"));
            Parent root = loader.load();

            // Ottiene il controller della scena front e imposta l'utente
            FrontController frontController = loader.getController();
            frontController.setUtente(utente);

            // Imposta la nuova scena e mostra la finestra
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Stampa l'eccezione in caso di errore
        }
    }
}
