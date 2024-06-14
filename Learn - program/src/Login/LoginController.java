package Login;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoginController {
   @FXML private TextField  UserName;
   @FXML private PasswordField PwField;
   
   public void checkLogin(ActionEvent event) throws FileNotFoundException{
     
      String user = UserName.getText();
      String pw = PwField.getText();
      if (checkContent(user, pw) == true) {
         File file = new File("users.csv");
         if (file.exists() && file.canRead()) {
            Scanner scan = new Scanner("users.csv");
            while(scan.hasNextLine()){
               String line = scan.nextLine();
               String[] token = line.split(",");
               if(token[0].contains(user)){
                  if (token[1].equals(pw)) {
                     changeview();
                  } else{
                     showAlert("Password errata");
                  }
               }
            }
            scan.close();
         } else {
            System.out.println("File not found");
         }
      }
   }


   private boolean checkContent(String user, String pw){
      if(user.isEmpty()){
         this.UserName.setPromptText("Username is required");
         return false;
      }else if(pw.isEmpty()){
         this.PwField.setPromptText("Password is required");
         return false;
      }
      else 
         return true;
   }


  @FXML private void changeview(){
      //TODO: Implementare il cambio di scena

   }

   private void showAlert(String message){
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Login Error");
      alert.setContentText(message);
      alert.showAndWait();
      //TODO: Implementare il cambio di scena dando la possibilità di andare alla schermata di registrazione

   }
}
