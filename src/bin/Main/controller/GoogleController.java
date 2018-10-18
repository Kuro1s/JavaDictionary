package bin.Main.controller;

import GoogleAPI.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GoogleController implements Initializable {
    private Map<String, String> map = new HashMap<String, String>();
    @FXML
    private Button Home;
    @FXML
    private Button GooglespeakButton;
    @FXML
    private Button GooglespeakButton2;
    @FXML
    private Button GooglesearchButton;
    @FXML
    private TextArea GoogletextArea;
    @FXML
    private TextArea GoogleTranslateTextArea;
    @FXML
    private ChoiceBox<String> Box1;
    @FXML
    private ChoiceBox<String> Box2;

    /**
     * phương thức xử lý cho button return stage home
     * khi click vào nút home sẽ tắt cửa sổ googe dịch và trở về cửa sổ Main
     * @param event
     */
    public void ReturnHome(ActionEvent event) {
        Stage stage = (Stage) Home.getScene().getWindow();
        stage.close();
    }

    /**
     *  Xử lý cho phím ENTER khi nhập văn bản
     *  TODO : sau khi nhập văn bản từ TextArea1 nhấn Enter sẽ đc dịch luôn sang TextArea2 không cầm bấm button search cho màu mè
     *
     */
    public void setENTERKeyPressed() {
        GoogletextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String text = GoogletextArea.getText();
                    if ("".equals(text)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("THÔNG BÁO");
                        alert.setHeaderText("                       VĂN BẢN CHƯA ĐƯỢC NHẬP!");
                        alert.setContentText("*WARNING: FBI");
                        alert.show();
                    } else {
                        try {
                            String rescb1 = map.get(Box1.getValue());
                            String rescb2 = map.get(Box2.getValue());
                            String showtext = GoogleTranslate.translate(rescb1, rescb2, text);
                            GoogleTranslateTextArea.setText(showtext);
                        } catch (IOException e) {
                            System.out.println("Lỗi");
                        }
                    }
                }
            }
        });
    }

    /**
     * Hàm xử lý cho Button search
     * @param event
     * @throws IOException
     */
    public void setGooglesearchButton(ActionEvent event) throws IOException {
        if (event.getSource() == GooglesearchButton) {
            String text = GoogletextArea.getText();
            if ("".equals(text)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("                       VĂN BẢN CHƯA ĐƯỢC NHẬP!");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            } else {
                String rescb1 = map.get(Box1.getValue());
                String rescb2 = map.get(Box2.getValue());
                String showtext = GoogleTranslate.translate(rescb1, rescb2, text);
                GoogleTranslateTextArea.setText(showtext);
            }
        }
    }
    /**
     * Hàm xử lý cho Button speak
     * @param event
     * @throws IOException
     */
    public void setGooglespeakButton(ActionEvent event) throws IOException, JavaLayerException {
        if (event.getSource() == GooglespeakButton) {
            String rescb1 = map.get(Box1.getValue());
            String text = GoogletextArea.getText();
            if ("".equals(text)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("KHÔNG PHÁT HIỆN VĂN BẢN");
                alert.setContentText("*ERROR: 404");
                alert.showAndWait();
            } else {
                Audio audio = Audio.getInstance();
                InputStream sound = audio.getAudio(text.trim(), rescb1);
                audio.play(sound);
            }
        }
        if (event.getSource() == GooglespeakButton2) {
            String rescb2 = map.get(Box2.getValue());
            String text1 = GoogleTranslateTextArea.getText();

            if ("".equals(text1)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("KHÔNG PHÁT HIỆN VĂN BẢN");
                alert.setContentText("*ERROR: 404");
                alert.showAndWait();
            } else {
                Audio audio1 = Audio.getInstance();
                InputStream sound1 = audio1.getAudio(text1, rescb2);
                audio1.play(sound1);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setENTERKeyPressed();
        map.put("Vietnamese", "vi");
        map.put("English", "en");
        map.put("Korean", "ko");
        map.put("Japanese", "ja");
        map.put("Chinese", "zh");
        map.put("Thailand", "th");
        map.put("French", "fr");
        map.put("German", "de");
        map.put("Spanish", "es");
        map.put("Russian", "ru");
        ObservableList<String> languages = FXCollections.observableArrayList("Russian", "Spanish", "German", "French", "Vietnamese", "English", "Korean", "Chinese", "Thailand", "Japanese");
        Box1.setItems(languages);
        Box2.setItems(languages);
        Box2.setValue("Vietnamese");
        Box1.setValue("English");
    }
}
