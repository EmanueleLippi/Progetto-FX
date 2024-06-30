package Esercizi.Front;

import Login.Utente;
import profile.ProfiloController;
import Esercizi.Catta.RulesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FrontController {

    @FXML private Label nameUser;
    private Utente utente;

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void setUtente(Utente utente){
        this.utente = utente;
        nameUser.setText(utente.toString());
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void initData(Utente utente) {
        this.utente = utente;
        nameUser.setText(utente.toString());
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML private void goProfilo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile/Profilo.fxml"));
            Parent profilo = loader.load();
            ProfiloController profiloController = loader.getController();
            profiloController.setUtente(utente);
            Scene profiloScene = new Scene(profilo);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(profiloScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML private void goRegoleCosaStampa(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Lippi/Rules.fxml"));
            Parent cosaStampa = loader.load();
            Esercizi.Lippi.RulesController rulesLippi = loader.getController();
            rulesLippi.setUtente(this.utente);
            Scene cosaStampaScene = new Scene(cosaStampa);
            Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow(); //prova
            stage.setScene(cosaStampaScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Verificato un errore nel caricamento della finestra di cosaStampa: --> " + e.getMessage());
            e.printStackTrace();
        }
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

@FXML private void goRegoleOrdineCorretto(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Catta/Rules.fxml"));
        Parent root = loader.load();
        RulesController rulesController = loader.getController();
        rulesController.setUtente(utente);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Label) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @FXML private void move(MouseEvent event){
        Label source = (Label) event.getSource();
        source.setStyle("-fx-background-color: #f0f0f0; -fx-width: 200;");
    }

// -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML private void setInitial(MouseEvent event){
        Label source = (Label) event.getSource();
        source.setStyle("-fx-background-color: #ffffff; -fx-width: 133;");
    }

    //TODO: fare il metodo per mostrare a che punto è arrivato l'utente?
}


