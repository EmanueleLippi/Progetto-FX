package Esercizi.Finale;
import Login.Utente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.ArrayList;
import java.util.List;

public class testFinaleController implements Initializable {
    @FXML private Label nameUser;
    @FXML private TextArea codeTextArea;
    @FXML private AnchorPane root;

    private Utente utente;
    private List<String> esercizi = new ArrayList<>(); //lista di esercizi si può fare? o meglio map?

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

                    }
                });
            }
        });
    }

    //TODO: metodo checkanswer --> creare corrispondenza tra risposta e esercizio

}
