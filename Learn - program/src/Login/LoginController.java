package Login;

import Esercizi.Front.FrontController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.Scanner;

public class LoginController {
   @FXML private TextField  UserName;
   @FXML private PasswordField PwField;
   private Utente utente;

   @FXML protected void checkLogin(ActionEvent event) {
      if (checkContent()) {
          try {
              Scanner scan = new Scanner(new File("Learn - program/src/Data/users.csv"));
              while (scan.hasNextLine()) {
                  String line = scan.nextLine();
                  String[] data = line.split(",");
                  for (int i = 0; i < data.length; i++) 
                      data[i] = data[i].trim();
                  
                  if (data[0].equals(UserName.getText()) && data[1].equals(PwField.getText())) {
                      this.utente = new Utente(data[0], data[2]);
                      FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));
  
                      Parent front = loader.load();
  
                      FrontController frontController = loader.getController();
  
                      Scene froScene = new Scene(front);
  
                      frontController.setUtente(utente);
  
                      Stage stage = (Stage) UserName.getScene().getWindow();
  
                      stage.setScene(froScene);
  
                      stage.show();
                      scan.close();
                      return; // Exit the method if login is successful
                  }
              }
              scan.close();
              if (utente == null) {
                  showAlert("Username or password is incorrect, retry again.");
              }
          } catch (Exception e) {
              showAlert(e.getMessage());
          }
      }
  }


   private boolean checkContent(){
      if(this.UserName.getText().isEmpty()){
         this.UserName.setPromptText("Username is required");
         return false;
      }else if(this.PwField.getText().isEmpty()){
         this.PwField.setPromptText("Password is required");
         return false;
      }
      else 
         return true;
   }


   @FXML private void showAlert(String message){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Login Error");
      alert.setContentText(message);
      this.UserName.clear();
      this.PwField.clear();
      alert.showAndWait();

   }


   @FXML protected void showRegister(ActionEvent event){
        try{
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registration/Register.fxml"));
             Parent Register = loader.load();
             Scene RegScene = new Scene(Register);
             Stage RegWindow = (Stage) UserName.getScene().getWindow();
             RegWindow.setScene(RegScene);
             RegWindow.show();        
             } catch(Exception e){
        showAlert("Verificato un errore nel caricamento della finestra di registrazione: --> "+e.getMessage());
         }
    }

    @FXML private void goToHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene homeScene = new Scene(home);
            Stage primaryStage = (Stage) UserName.getScene().getWindow();
            primaryStage.setScene(homeScene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Errore nel caricamento della finestra Home: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

