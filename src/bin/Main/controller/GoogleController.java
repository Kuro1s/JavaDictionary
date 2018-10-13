package bin.Main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class GoogleController implements Initializable {
    @FXML
    Button add;

    @FXML
    public void addWord(ActionEvent event)
    {
        Stage stage = (Stage) add.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
