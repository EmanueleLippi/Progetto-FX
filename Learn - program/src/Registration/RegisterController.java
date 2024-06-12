package Registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

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
}
