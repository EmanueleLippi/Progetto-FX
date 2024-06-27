package Registration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.Arrays;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import Esercizi.Front.FrontController;
import Login.Utente;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private TextField emailField;

    @FXML public void registration(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();

        if (doValidation(username, password, confirmPassword, email)) {
            try {
                // Effettua la registrazione su file CSV
                File file = new File("Learn - program/src/Data/users.csv");
                if (file.exists()) {
                    FileWriter fileWriter = new FileWriter(file, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    int[] score = {0,0,0,0,0,0,0,0,0};
                    printWriter.println(username + "," + password + "," + email + "," + Arrays.toString(score));
                    printWriter.close();

                    Utente utente = new Utente(username, email, password);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));

                    Parent front = loader.load();

                    FrontController frontController = loader.getController();

                    Scene froScene = new Scene(front);

                    frontController.setUtente(utente);

                    Stage stage = (Stage) usernameField.getScene().getWindow();

                    stage.setScene(froScene);

                    stage.show();
                } else {
                    PrintWriter printWriter = new PrintWriter(file);
                    printWriter.println(username + ", " + password + ", " + email);
                    printWriter.close();
                }

            } catch (Exception e) {
                showAlert("Error", "An error occurred during registration", e.getMessage());
            }
        }
    }

    private boolean doValidation(String username, String password, String confirmPassword, String email) {
        if (username.isEmpty()) {
            usernameField.setPromptText("Username is required");
            return false;
        }
        if (password.isEmpty()) {
            passwordField.setPromptText("Password is required");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordField.setPromptText("Confirm Password is required");
            return false;
        }
        if (email.isEmpty()) {
            emailField.setPromptText("Email is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match", "Make sure passwords are correct.");
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
            Parent login = loader.load();
            Scene loginScene = new Scene(login);
            Stage loginWindow = (Stage) usernameField.getScene().getWindow();
            loginWindow.setScene(loginScene);
            loginWindow.show();
        } catch(Exception e){
            showAlert("Error", "Error loading login window", e.getMessage());
        }
    }

    @FXML private void goToHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene homeScene = new Scene(home);
            Stage primaryStage = (Stage) usernameField.getScene().getWindow();
            primaryStage.setScene(homeScene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento della schermata home: " + e.getMessage()); // Mostra un messaggio di errore se il caricamento fallisce
            e.printStackTrace(); 
        }
    }
}
