package bin.Main.controller;
import Dictionary.Dic;
import GoogleAPI.Audio;
import GoogleAPI.Language;
import bin.Internet.InternetConnected;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javazoom.jl.decoder.JavaLayerException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable {
    private String URL1 = "C:/Users/Asus/Desktop/JavaDictionary/src/Data/V_E.txt";
    private String URL2 = "C:/Users/Asus/Desktop/JavaDictionary/src/Data/E_V.txt";
    Dic dictionary = new Dic();
    private int hour;
    private int minute;
    private int second;
    private int year;
    private int month;
    private int day;
    static int index = 0;//biến lưu vị trí listview
    @FXML
    private Label ClockDisplay;
    @FXML
    private Label CalendarDisplay;
    @FXML
    private Button searchButton;
    @FXML
    private Button engvietButton;
    @FXML
    private Button vietengButton;
    @FXML
    private ListView listView;
    @FXML
    private WebView webView;
    @FXML
    private TextField textField;
    @FXML
    private Button AudioButton;
    //hàm hiện từ lên listview và gợi ý từ tìm kiếm
    @FXML
    public void searchWord() {
        ObservableList<String> listWord = FXCollections.observableArrayList(dictionary.Word);
        FilteredList<String> filteredData = new FilteredList<>(listWord, s -> true);
        listView.setItems(filteredData);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(s -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String tolower = newValue.toLowerCase();
                if (s.toLowerCase().startsWith(tolower)) {
                    return true;
                }
                return false;
            });
            listView.setItems(filteredData);
        });
    }

    public void setKeyPressed() {
        //TODO : bắt Mouse Event khi click vào listView
        listView.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                String text = (String) listView.getSelectionModel().getSelectedItem();
                textField.setText(text);
                webView.getEngine().loadContent(dictionary.Data.get(text));
            }
        });
    }
    /**
     * Chương trình sử lý sự kiên cho các Button
     *
     * @param event
     */
    public void SearchButtonEvent(ActionEvent event) {
        if (event.getSource() == searchButton) {
            //TODO : Xử lý search Button
            String text = textField.getText();
            text = text.toLowerCase();
            if ("".equals(text)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("                       TỪ CHƯA ĐƯỢC NHẬP!");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            } else if (dictionary.Data.get(text) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("                TỪ VỪA NHẬP KHÔNG HỢP LỆ!");
                alert.setContentText("*ERROR: 404");
                alert.show();
            } else webView.getEngine().loadContent(dictionary.Data.get(text));
        }
        if (event.getSource() == engvietButton) {
            dictionary.readData(URL2);
            setKeyPressed();
            searchWord();
        } else if (event.getSource() == vietengButton) {
            dictionary.readData(URL1);
            searchWord();
            setKeyPressed();
        }
    }

    //  Kết thúc bắt sự kiện cho các Button
    private void setCalendarDisplay() {
        //TODO : Hàm thực hiện sự kiên cho Label CalenDarDisplay
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        CalendarDisplay.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year));
    }

    private void setClockDisplay() {
        //TODO : Hàm thực hiện animation cho Label ClockDisplay
        Timeline clock;
        clock = new Timeline(new KeyFrame(Duration.ZERO, (ActionEvent e) -> {
            second = LocalDateTime.now().getSecond();
            minute = LocalDateTime.now().getMinute();
            hour = LocalDateTime.now().getHour();
            if (hour >= 0 && hour <= 12) {
                ClockDisplay.setText(String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + " AM");
            } else {
                ClockDisplay.setText(String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(second) + " PM");
            }

        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void SearchTextFieldEvent() {
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {

                if (e.getCode() == KeyCode.ENTER) { //nhấn enter để tìm kiếm
                    if (index == 0) {
                        String text = textField.getText();
                        if ("".equals(text)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("THÔNG BÁO");
                            alert.setHeaderText("                       TỪ CHƯA ĐƯỢC NHẬP!");
                            alert.setContentText("*WARNING: FBI");
                            alert.show();
                        } else if (dictionary.Data.get(text) == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("THÔNG BÁO");
                            alert.setHeaderText("                TỪ VỪA NHẬP KHÔNG HỢP LỆ!");
                            alert.setContentText("*ERROR: 404");
                            alert.show();
                        } else webView.getEngine().loadContent(dictionary.Data.get(text));
                    } else {
                        if (listView.getSelectionModel().getSelectedItem() != null) {
                            textField.setText((String) listView.getSelectionModel().getSelectedItem());
                        }
                    }
                } else if (e.getCode() == KeyCode.DOWN) {
                    listView.getSelectionModel().select(index); //chọn item vị trí trong listview
                    listView.getFocusModel().focus(index);
                    listView.scrollTo(index); //cuộn list theo vị trí
                    index++;
                } else if (e.getCode() == KeyCode.UP) {
                    index--;
                    listView.getSelectionModel().select(index); //chọn item vị trí trong listview
                    listView.getFocusModel().focus(index);
                    listView.scrollTo(index); //cuộn list theo vị trí
                    if (index < 0) index = 0;
                } else index = 0;

            }
        });
    }

    // Hàm xử lý cho list View
    public void chooseitem() {
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                webView.getEngine().loadContent(dictionary.Data.get(newValue));
            }
        });
    }

    /**
     * hàm sử lý Button Audio bằng Google API
     *
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void speak(ActionEvent event) throws IOException, InterruptedException {
        if (event.getSource() == AudioButton) {
            String str = textField.getText();
            if (InternetConnected.IsConnecting() == true) {
                if(!"".equals(str))
                {
                    try {
                        InputStream sound = null;
                        Audio audio = Audio.getInstance();
                        sound = audio.getAudio(str, Language.ENGLISH);
                        audio.play(sound);
                    } catch (IOException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("THÔNG BÁO");
                    alert.setHeaderText("KHÔNG PHÁT HIỆN TỪ");
                    alert.setContentText("*ERROR : 404");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("KHÔNG CÓ KẾT NỐI INTERNET");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            }
        }
    }

    public void loadGoogle(ActionEvent event) throws InterruptedException, IOException{
        if(InternetConnected.IsConnecting() == true) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Style/GoogleLoader.fxml"));
                Parent root1 = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Hello World");
                Scene scene = new Scene(root1);
                scene.getStylesheets().add(getClass().getResource("../Style/StyleBuilder.css").toExternalForm());
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                Platform.setImplicitExit(false);
                stage.show();
            } catch (Exception e) {
                System.out.println("cant load new window");
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("THÔNG BÁO:");
            alert.setHeaderText("KHÔNG CÓ KẾT NỐI INTERNET");
            alert.setContentText("*WARNING: FBI");
            alert.showAndWait();
        }
    }

    @Override
    /**
     * Chương trình khởi tạo môi trường cho từ điển
     */
    public void initialize(URL location, ResourceBundle resources) {
        setCalendarDisplay();
        chooseitem();
        SearchTextFieldEvent();
        setClockDisplay();
    }
}
