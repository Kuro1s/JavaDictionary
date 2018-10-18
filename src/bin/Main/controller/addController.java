package bin.Main.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import bin.Main.main.Main;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addController implements Initializable {

    @FXML
    TextField word;

    @FXML
    HTMLEditor def;

    @FXML
    public void addWord(ActionEvent event) throws SQLException {

        String wordd = word.getText();
        String deff = def.getHtmlText();

        if (wordd.equals("")) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Bạn điền thiếu rồi!", ButtonType.OK);
            alert.setTitle("Add");
            alert.show();
        } else {
            Controller c = Main.getLoader().getController();
            c.addword(wordd, deff);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
