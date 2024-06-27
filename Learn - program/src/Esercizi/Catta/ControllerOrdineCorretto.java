package Esercizi.Catta;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import Login.Utente;

public class ControllerOrdineCorretto {

    private Utente utente;
    @FXML private ListView<String> listView;
    @FXML private Label messageLabel;
    @FXML private Button tornaDashboard;
    @FXML private Label usernameLabel;

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

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
    }

    @FXML public void verificaOrdine() {
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
}
