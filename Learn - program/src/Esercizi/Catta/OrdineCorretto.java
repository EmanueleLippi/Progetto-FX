package Esercizi.Catta;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OrdineCorretto extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
    try{
        Parent root = FXMLLoader.load(getClass().getResource("OrdineCorretto.fxml"));
        
        primaryStage.setTitle("Ordina i Segmenti di Codice");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();

        } catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra principale: --> "+e.getMessage());
        }
    }
}
