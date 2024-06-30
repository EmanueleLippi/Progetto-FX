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
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FrontController {

    @FXML private Label nameUser;
    @FXML private ProgressBar cosaStampaBar;
    @FXML private ProgressBar OrdinaCodiceBar;
    @FXML private ProgressBar ConfrontaCodiceBar;
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
    @FXML private void showProgress(){
        //progress CosaStampa
        for(int i = 0; i < 3; i++){
            if(utente.getScore()[0] == 1){
                cosaStampaBar.setProgress(0.33);
            }
            if(utente.getScore()[1] == 1){
                cosaStampaBar.setProgress(0.66);
            }
            if(utente.getScore()[2] == 1){
                cosaStampaBar.setProgress(1);
            }
        }

        //progress OrdinaCodice
        for(int i = 3; i < 6; i++){
            if(utente.getScore()[3] == 1){
                OrdinaCodiceBar.setProgress(0.33);
            }
            if(utente.getScore()[4] == 1){
                OrdinaCodiceBar.setProgress(0.66);
            }
            if(utente.getScore()[5] == 1){
                OrdinaCodiceBar.setProgress(1);
            }
        }

        //progress ConfrontaCodice
        for(int i = 6; i < 9; i++){
            if(utente.getScore()[6] == 1){
                ConfrontaCodiceBar.setProgress(0.33);
            }
            if(utente.getScore()[7] == 1){
                ConfrontaCodiceBar.setProgress(0.66);
            }
            if(utente.getScore()[8] == 1){
                ConfrontaCodiceBar.setProgress(1);
            }
        }

    }
}


