package Esercizi.Front;

import Login.Utente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FrontController {
    @FXML private Label nameUser;
    
    public void setUtente(Utente utente){
        nameUser.setText(utente.toString());
    }


}
