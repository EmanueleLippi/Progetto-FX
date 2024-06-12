import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HomeController {
    @FXML private Label target;
    public void GoToLogin(ActionEvent event){
        try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent Login = loader.load();
            Scene loginScene = new Scene(Login);
            Stage LoginWindow = (Stage) target.getScene().getWindow();
            LoginWindow.setScene(loginScene);
            LoginWindow.show();
        } catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra di login: --> "+e.getMessage());
        }
    }

    public void GoToRegistration(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent Register = loader.load();
            Scene RegScene = new Scene(Register);
            Stage RegWindow = (Stage) target.getScene().getWindow();
            RegWindow.setScene(RegScene);
            RegWindow.show();        
        } catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra di registrazione: --> "+e.getMessage());
        }
    }
}
