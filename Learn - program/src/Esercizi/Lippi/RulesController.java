package Esercizi.Lippi;

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

    public void setUtente(Utente utente){
        this.utente = utente;
    }

    @FXML private void goCosaStampa(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CosaStampaBeta.fxml"));
            Parent cosaStampa = loader.load();
            CosaStampaController cosaStampaController = loader.getController();
            cosaStampaController.setUtente(this.utente);
            Scene cosaStampaScene = new Scene(cosaStampa);
            Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow(); //prova
            stage.setScene(cosaStampaScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
            e.printStackTrace();
        }
    }
}
