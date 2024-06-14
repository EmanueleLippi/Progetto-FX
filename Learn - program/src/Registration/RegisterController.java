package Registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import Esercizi.Front.FrontController;
import Login.Utente;

public class RegisterController {
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    @FXML private TextField email;

  @FXML  public void registration(ActionEvent event){
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.confirmPassword.getText();
        String email = this.email.getText();

        if(doValidation(username, password, confirmPassword, email) ==true){
            try {
                //do registration on file csv
                File file = new File("users.csv");
                if (file.exists()) {
                    FileWriter fileWriter = new FileWriter(file, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    printWriter.println(username+","+password+","+email);
                    printWriter.close();
                    //todo send the user to start page
                    Utente utente = new Utente(username, email);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
  
                      Parent front = loader.load();
  
                      FrontController frontController = loader.getController();
  
                      Scene froScene = new Scene(front);
  
                      frontController.setUtente(utente);
  
                      Stage stage = (Stage) this.username.getScene().getWindow();
  
                      stage.setScene(froScene);
  
                      stage.show();
                } else {
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.println(username + ", "+password+", "+email);
                    printWriter.close();
                }

            } catch (Exception e) {
                System.out.println("Error: "+e.getMessage());
            }
        }
       


                    
    }

    private boolean doValidation(String username, String password, String confirmPassword, String email) {
        if (username.isEmpty()) {
            this.username.setPromptText("Username is required");
            return false;
        }
        if(password.isEmpty()){
            this.password.setPromptText("Password is required");
            return false;
        }
        if(confirmPassword.isEmpty()){
            this.confirmPassword.setPromptText("Confirm Password is required");
            return false;
        }           
        if(email.isEmpty()){
            this.email.setPromptText("Email is required");
            return false;
        }
        if(!password.equals(confirmPassword)){
            showAlert("Errore", "Passwords non coincidono", "Assicurarsi che le password siano corrette.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML protected void showLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/Login.fxml"));
                Parent Login = loader.load();
                Scene loginScene = new Scene(Login);
                Stage LoginWindow = (Stage) username.getScene().getWindow();
                LoginWindow.setScene(loginScene);
                LoginWindow.show();
            } catch(Exception e){
                System.out.println("Verificato un errore nel caricamento della finestra di login: --> "+e.getMessage());
            }
    }
}
