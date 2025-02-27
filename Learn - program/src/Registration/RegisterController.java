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
import java.util.Scanner;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private TextField emailField;

    @FXML private boolean check(String usernameString){ //controlla se l'username è già presente nel file users.csv
        try {
            File file = new File("Learn - program/src/Data/users.csv");
            if (file.exists()) {
                @SuppressWarnings("resource") //solo per evitare il warning
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(",");//divide la linea estrapolata in base alla virgola
                    if (fields[0].equals(usernameString)) { //controlla se l'username è già presente
                        showAlert("Errore", "Username già esistente", "Cambiare la username.");
                        usernameField.clear();
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            showAlert("Errore", "Errore durante la registrazione", e.getMessage());
        }
        return true;
    }


    // Registra un nuovo utente
    @FXML public void registration(ActionEvent event){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();

        //if che controlla se i campi sono vuoti
        if (doValidation(username, password, confirmPassword, email)) {
            Utente utente = new Utente(username, email, password); //crea un nuovo utente
            try {
                // Effettua la registrazione su file CSV
                File file = new File("Learn - program/src/Data/users.csv");
                //if che controlla se il file esiste e se l'username è già presente
                if (file.exists() && check(username) == true) {
                    FileWriter fileWriter = new FileWriter(file, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    printWriter.println(utente.onFile()); //scrive i dati dell'utente nel file in modalità append
                    printWriter.close();

                    // Carica la schermata front
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));

                    Parent front = loader.load();

                    FrontController frontController = loader.getController();

                    Scene froScene = new Scene(front);

                    frontController.setUtente(utente);

                    Stage stage = (Stage) usernameField.getScene().getWindow();

                    stage.setScene(froScene);

                    stage.show();
                } else {
                    PrintWriter printWriter = new PrintWriter(file); //crea un nuovo file
                    printWriter.println(utente.onFile()); //scrive i dati dell'utente nel file creato
                    printWriter.close();

                    // Carica la schermata front
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml"));

                    Parent front = loader.load();

                    FrontController frontController = loader.getController();

                    Scene froScene = new Scene(front);

                    frontController.setUtente(utente);

                    Stage stage = (Stage) usernameField.getScene().getWindow();

                    stage.setScene(froScene);

                    stage.show();
                }

            } catch (Exception e) {
                showAlert("Error", "An error occurred during registration", e.getMessage());
            }
        }
    }

    // Metodo che controlla se i campi sono vuoti
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

    // Metodo che mostra un alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Metodo che mostra la schermata di login al click del pulsante
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

    // Metodo che mostra la schermata home al click del pulsante
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
