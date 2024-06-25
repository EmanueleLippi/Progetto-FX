package Esercizi.Front;

import Esercizi.Lippi.CosaStampaController;
import Login.Utente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FrontController {
    @FXML private Label nameUser;
    private Utente utente;

    public void setUtente(Utente utente){
        this.utente = utente;
        nameUser.setText(utente.toString());
    }

    @FXML private void goCosaStampa(MouseEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Lippi/CosaStampaBeta.fxml"));
            Parent cosaStampa = loader.load();
            CosaStampaController cosaStampaController = loader.getController();
            Scene cosaStampaScene = new Scene(cosaStampa);
            cosaStampaController.setUtente(utente);
            Stage stage = (Stage) this.nameUser.getScene().getWindow();
            stage.setScene(cosaStampaScene);
            stage.show();
        }catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
        }
    }

    @FXML private void move(MouseEvent event){
        Label source = (Label) event.getSource();
        source.setStyle("-fx-background-color: #f0f0f0; -fx-width: 200;");
    }

    @FXML private void setInitial(MouseEvent event){
        Label source = (Label) event.getSource();
        source.setStyle("-fx-background-color: #ffffff; -fx-width: 133;");
    }

    //TODO: fare il metodo per mostrare a che punto è arrivato l'utente?
}

