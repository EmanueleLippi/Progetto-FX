import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HomeController {
    @FXML private Label target;

    @FXML public void GoToLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
            Parent login = loader.load();
            Scene loginScene = new Scene(login);
            Stage loginWindow = (Stage) target.getScene().getWindow();
            loginWindow.setScene(loginScene);
            loginWindow.show();
        } catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra di login: --> "+e.getMessage());
        }
    }

    @FXML public void GoToRegistration(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registration/Register.fxml"));
            Parent register = loader.load();
            Scene regScene = new Scene(register);
            Stage regWindow = (Stage) target.getScene().getWindow();
            regWindow.setScene(regScene);
            regWindow.show();
        } catch(Exception e){
            System.out.println("Verificato un errore nel caricamento della finestra di registrazione: --> "+e.getMessage());
        }
    }   
}
