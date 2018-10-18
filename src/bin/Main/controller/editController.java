package bin.Main.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import bin.Main.main.Main;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class editController implements Initializable {

    @FXML
    TextField word;

    @FXML
    TextField editWord;

    @FXML
    HTMLEditor def;


    @FXML
    public void editWord(ActionEvent event) throws SQLException {
        String wordd = word.getText();
        String editWordd = editWord.getText();
        String deff = def.getHtmlText();
        Controller c = Main.getLoader().getController();
        c.editWord(wordd, editWordd, deff);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
