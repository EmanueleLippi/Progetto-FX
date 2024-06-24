package Esercizi.Catta;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.util.List;

public class ControllerOrdineCorretto {

    @FXML
    private ListView<String> listView;

    @FXML
    private Label messageLabel;

    @FXML
    private Button backButton;

    private final ObservableList<String> segments = FXCollections.observableArrayList(
            "public class HelloWorld {",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, World!\");",
            "    }",
            "}"
    );

    @FXML
    public void initialize() {
        listView.setItems(FXCollections.observableArrayList(segments));
        listView.setEditable(true);
        backButton.setOnAction(event -> backToHome());
    }

    @FXML
    public void verificaOrdine() {
        List<String> orderedSegments = listView.getItems();

        if (isCorrectOrder(orderedSegments)) {
            messageLabel.setText("L'ordine è corretto!");
        } else {
            messageLabel.setText("L'ordine non è corretto. Riprova.");
        }
    }

    private boolean isCorrectOrder(List<String> orderedSegments) {
        List<String> correctOrder = List.of(
                "public class HelloWorld {",
                "    public static void main(String[] args) {",
                "        System.out.println(\"Hello, World!\");",
                "    }",
                "}"
        );

        return orderedSegments.equals(correctOrder);
    }

    private void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
