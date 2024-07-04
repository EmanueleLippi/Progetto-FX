package Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login/login.fxml"));  // Assicurati che il percorso sia corretto
        Scene scene = new Scene(root);

        primaryStage.setTitle("Login Application");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);  // Imposta la finestra a tutto schermo
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

