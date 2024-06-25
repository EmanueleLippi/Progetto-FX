package Esercizi.Lippi;

import Login.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CosaStampaController {
    @FXML private Label nameUser;

    public void setUtente(Utente utente){
        nameUser.setText(utente.toString());
    }


    //TODO: fare il metodo per il load casuale di una domanda

    @FXML private void checkAnswer(ActionEvent event){
        //TODO: fare il metodo per controllare la risposta

    }

    @FXML private void save(ActionEvent event){
        //TODO: fare il metodo per salvare il punteggio

    }
    
}
