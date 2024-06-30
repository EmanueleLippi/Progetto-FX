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
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.util.Scanner; 

public class LoginController {

    @FXML private TextField userName; 
    @FXML private PasswordField pwField; 
    private Utente utente; // Oggetto Utente per memorizzare le informazioni dell'utente loggato

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @FXML protected void checkLogin(ActionEvent event) {
        if (userName == null || pwField == null) { // Verifica che i campi siano stati inizializzati correttamente
            showAlert("Errore nell'inizializzazione degli elementi UI.");
            return;
        }
    
        String usernameText = userName.getText(); 
        String passwordText = pwField.getText();
    
        if (usernameText.isEmpty() || passwordText.isEmpty()) { // Controlla se l'username o la password sono vuoti
            showAlert("Username e password sono obbligatori.");
            return;
        }
    
        try {
            Scanner scan = new Scanner(new File("Learn - program/src/Data/users.csv")); // Apre il file users.csv per la lettura
            while (scan.hasNextLine()) { 
                String line = scan.nextLine(); 
                String[] data = line.split(","); // Divide la riga usando la virgola come delimitatore e rimuove gli spazi iniziali e finali da ogni elemento
                for (int i = 0; i < data.length; i++) 
                    data[i] = data[i].trim();
                
                if (data.length >= 3 && data[0].equals(usernameText) && data[1].equals(passwordText)) {
                    this.utente = new Utente(data[0], data[2], data[1]); 
                    this.utente.loadFile(data[0], data[2], data[1]);//caricare da file 
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Esercizi/Front/Front.fxml")); 
    
                    Parent front = loader.load(); 
    
                    FrontController frontController = loader.getController(); 
                    frontController.setUtente(utente); 
    
                    Scene froScene = new Scene(front); 
    
                    Stage stage = (Stage) userName.getScene().getWindow(); 
    
                    stage.setScene(froScene);
    
                    stage.show(); 
                    scan.close();
                    return; 
                }
            }
            scan.close(); 
            showAlert("Username o password non corretti, riprova."); 
        } catch (FileNotFoundException e) {
            showAlert("File 'users.csv' non trovato."); 
        } catch (IOException e) {
            showAlert("Errore durante la lettura di 'users.csv': " + e.getMessage()); 
        } catch (Exception e) {
            showAlert("Errore: " + e.getMessage()); 
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------

    
    // Metodo per controllare se i campi di input sono vuoti
    private boolean checkContent() {
        if (userName.getText().isEmpty()) { 
            userName.setPromptText("Username richiesto"); 
            return false; 
        } else if (pwField.getText().isEmpty()) { 
            pwField.setPromptText("Password richiesta");
            return false; 
        } else {
            return true; // Restituisce true se entrambi i campi sono pieni
        }
    }


    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------


    // Metodo per mostrare un alert con un messaggio specificato
    @FXML private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle("Errore di Login"); 
        alert.setContentText(message); 
        userName.clear(); 
        pwField.clear(); 
        alert.showAndWait(); 
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------


    // Metodo per passare alla schermata di registrazione
    @FXML protected void showRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Registration/Register.fxml"));
            Parent register = loader.load(); 
            Scene regScene = new Scene(register); 
            Stage regWindow = (Stage) userName.getScene().getWindow(); 
            regWindow.setScene(regScene);
            regWindow.show(); 
        } catch (Exception e) {
            showAlert("Errore durante il caricamento della schermata di registrazione: " + e.getMessage()); // Mostra un messaggio di errore se il caricamento fallisce
        }
    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------


    // Metodo per tornare alla schermata home
    @FXML private void goToHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene homeScene = new Scene(home); 
            Stage primaryStage = (Stage) userName.getScene().getWindow(); 
            primaryStage.setScene(homeScene); 
            primaryStage.show(); 
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento della schermata home: " + e.getMessage()); // Mostra un messaggio di errore se il caricamento fallisce
            e.printStackTrace(); 
        }
    }
    
}
