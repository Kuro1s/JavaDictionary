package bin.Main.controller;

import Dictionary.Dic;
import GoogleAPI.Audio;
import GoogleAPI.Language;
import bin.Dictionary.DatabaseConnection;
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
import javafx.concurrent.Worker;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Worker.State;

public class Controller implements Initializable {
    private static String typeOfDictionary = "av";

    public static void setTypeOfDictionary(String typeOfDictionary) {
        Controller.typeOfDictionary = typeOfDictionary;
    }

    private Connection connection = DatabaseConnection.getConnection();
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private ObservableList<String> listWord = FXCollections.observableArrayList();
    private FilteredList<String> filteredData = new FilteredList<>(listWord, e -> true);
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
    private ListView listView;
    @FXML
    private WebView webView;
    @FXML
    private TextField textField;
    @FXML
    private Button AudioButton;
    public boolean checkTypeDictionary;

    //hàm hiện từ lên listview và gợi ý từ tìm kiếm
    @FXML
    public void searchWord() {
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

    /**
     * Chương trình sử lý sự kiên cho các Button
     *
     * @param event
     */
    public void SearchButtonEvent(ActionEvent event) throws SQLException {
        String query = "Select * from " + typeOfDictionary + " WHERE word=?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, textField.getText());
        rs = preparedStatement.executeQuery();
        if (event.getSource() == searchButton) {
            String text = textField.getText();
            if ("".equals(text)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("                      TỪ CHƯA ĐƯỢC NHẬP!");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            } else if (listView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("THÔNG BÁO");
                alert.setHeaderText("                      TỪ KHÔNG HỢP LỆ!");
                alert.setContentText("*WARNING: FBI");
                alert.show();
            } else {
                while (rs.next()) {
                    webView.getEngine().loadContent(rs.getString("html"));
                    System.out.println(rs.getString("html"));
                }
                preparedStatement.close();
                rs.close();
            }
        }
    }

    //  gọi cửa sổ thêm từ
    public void addwordscene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Style/addword.fxml"));
        loader.load();

        Parent p = loader.getRoot();
        Scene scene = new Scene(p);

        Stage stage = new Stage();
        stage.setResizable(false);
        Stage primary = (Stage) textField.getScene().getWindow();
        stage.initOwner(primary);
        stage.setTitle("Add Word");
        stage.setScene(scene);
        stage.show();
    }

    //  thêm từ vào database
    public void addword(String word, String html) throws SQLException {
        String query = "INSERT INTO " + typeOfDictionary + " (word, html) VALUES(?,?)";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, word);
        preparedStatement.setString(2, html);
        preparedStatement.execute();
        preparedStatement.close();

        listWord.clear();
        showlistview();
    }

    public void editwordscene() throws IOException, SQLException {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Style/editword.fxml"));

            loader.load();

            Parent p = loader.getRoot();
            Scene scene = new Scene(p);

            //truyền dữ liệu qua scene edit
            editController editController = loader.getController();

            editController.word.setText((String) listView.getSelectionModel().getSelectedItem());//set text cho textfield word bên scene edit


            String query = "Select * from " + typeOfDictionary + " WHERE word=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, (String) listView.getSelectionModel().getSelectedItem());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                editController.def.setHtmlText(rs.getString("html"));//set text cho phần sửa từ
            }
            preparedStatement.close();
            rs.close();


            Stage stage = new Stage();
            stage.setResizable(false);
            Stage primary = (Stage) textField.getScene().getWindow();
            stage.initOwner(primary);
            stage.setTitle("Edit Word");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Bạn chưa chọn từ nào để sửa!", ButtonType.OK);
            alert.setTitle("Edit");
            alert.show();
        }

    }

    public void editWord(String word, String editWord, String newDef) throws SQLException {
        //chỉ sửa nghĩa
        if (editWord.equals("")) {
            //xóa từ cũ
            String query = "delete from " + typeOfDictionary + "  where word=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, word);
            preparedStatement.execute();
            preparedStatement.close();
            //thêm từ word với nghĩa mới
            addword(word, newDef);
        }
        // chỉ sửa nghĩa của từ
        else if (newDef.equals("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>")) {
            String def = new String();

            String query = "Select * from " + typeOfDictionary + " WHERE word=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, word);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                def = rs.getString("html");//lấy nghĩa cũ
            }
            preparedStatement.close();
            rs.close();


            //xóa từ cũ
            query = "delete from " + typeOfDictionary + "  where word=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, word);
            preparedStatement.execute();
            preparedStatement.close();


            //thêm từ mới với nghĩa cũ
            addword(editWord, def);
        }
        //sửa cả nghĩa và từ
        else {
            //xóa từ cũ
            String query = "delete from " + typeOfDictionary + "  where word=?";
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, word);
                preparedStatement.execute();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //thêm từ mới với nghĩa mới
            addword(editWord, newDef);
        }

        listWord.clear();
        showlistview();
        webView.getEngine().loadContent("");
    }

    public void removeWord() throws SQLException {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Bạn có chắc là xóa từ này?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                String word = (String) listView.getSelectionModel().getSelectedItem();
                String query = "delete from " + typeOfDictionary + "  where word=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, word);
                preparedStatement.execute();
                preparedStatement.close();
                listWord.clear();
                showlistview();
                webView.getEngine().loadContent("");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE, "Bạn chưa chọn từ nào để xóa!", ButtonType.OK);
            alert.setTitle("Remove");
            alert.show();
        }
    }

    public void changeAv(ActionEvent event) {
        setTypeOfDictionary("av");
        listWord.clear();
        showlistview();
        webView.getEngine().loadContent("");
        checkTypeDictionary = true;
    }

    public void changeVa(ActionEvent event) {
        setTypeOfDictionary("va");
        listWord.clear();
        showlistview();
        webView.getEngine().loadContent("");
        checkTypeDictionary = false;
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
                        } else {
                            try {
                                String query = "Select * from " + typeOfDictionary + " WHERE word=?";
                                preparedStatement = connection.prepareStatement(query);
                                preparedStatement.setString(1, textField.getText());
                                rs = preparedStatement.executeQuery();
                                while (rs.next()) {
                                    webView.getEngine().loadContent(rs.getString("html"));
                                }
                                preparedStatement.close();
                                rs.close();
                            } catch (SQLException ee) {
                                System.out.println(ee.getMessage());
                            }
                        }
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

    public void showlistview() {
        try {
            String query = "select word from " + typeOfDictionary;
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {

                listWord.add(rs.getString(1));
            }
            preparedStatement.close();
            rs.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // Hàm xử lý cho list View
    public void chooseitem() {
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                try {
                    String query = "Select * from " + typeOfDictionary + " WHERE word=?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, newValue);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        webView.getEngine().loadContent(rs.getString("html"));
                    }
                    preparedStatement.close();
                    rs.close();
                } catch (SQLException ee) {
                    System.out.println(ee.getMessage());
                }
            }
        });
    }

    /**
     * phương thức sử lý Button Audio bằng Google API
     * các TH có thể xảy ra:
     * TH1 : str1.equals("") -> ưu tiên đọc str
     * TH2 : str1 != "" -> ưu tiên đọc str1,không đọc str
     * @param event
     * @param boolean checkTypeDictionary : trả về true khi click vào button A-V, false khi click vào button V-A
     * @param str = (String) listView.getSelectionModel().getSelectedItem(); một string lấy từ listView khi click Mouse hoặc thao tác ENTER
     * @param str1 = textField.getText(); string lấy từ textfield khi người dùng nhập vào
     * @Note str luôn luôn không rỗng,trừ khi str1 không hợp lệ khi search(có thông báo Alert)
     * @Note str1 có thể rỗng, hoặc xuất hiện trong từ điển hoặc không
     * @throws IOException
     * @throws InterruptedException
     */
    public void speak(ActionEvent event) throws IOException, InterruptedException {
        if (event.getSource() == AudioButton) {
            String str = (String) listView.getSelectionModel().getSelectedItem();
            String str1 = textField.getText();
            if (InternetConnected.IsConnecting() == true) {
                if ("".equals(str1)) {
                    if (checkTypeDictionary == true) {
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
                    } else {
                        try {
                            InputStream sound = null;
                            Audio audio = Audio.getInstance();
                            sound = audio.getAudio(str, Language.VIETNAMESE);
                            audio.play(sound);
                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    if (checkTypeDictionary == true) {
                        try {
                            InputStream sound = null;
                            Audio audio = Audio.getInstance();
                            sound = audio.getAudio(str1, Language.ENGLISH);
                            audio.play(sound);
                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            InputStream sound = null;
                            Audio audio = Audio.getInstance();
                            sound = audio.getAudio(str1, Language.VIETNAMESE);
                            audio.play(sound);
                        } catch (IOException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (JavaLayerException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
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

    /**
     *  Load cửa sổ để thao tác vời google dịch
     *  stage.initStyle(StageStyle.UNDECORATED) : xóa các nút Minimize, Maximize, Close
     *  stage.initModality(Modality.APPLICATION_MODAL) : không thẻ tắt cửa số Main khi đang thao tác với cửa sổ Google
     * @param event
     * @throws InterruptedException
     * @throws IOException
     */
    public void loadGoogle(ActionEvent event) throws InterruptedException, IOException {
        if (InternetConnected.IsConnecting() == true) {
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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("THÔNG BÁO:");
            alert.setHeaderText("KHÔNG CÓ KẾT NỐI INTERNET");
            alert.setContentText("*WARNING: FBI");
            alert.showAndWait();
        }
    }

    /**
     *  phương thhuwcs xử lý cho Button WIkisearch*
     * @param event
     * @throws InterruptedException
     * @throws IOException
     */
    public void setWikiButton(ActionEvent event) throws InterruptedException, IOException {
        if (InternetConnected.IsConnecting() == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("THÔNG BÁO:");
            alert.setHeaderText("KHÔNG CÓ KẾT NỐI INTERNET");
            alert.setContentText("*WARNING: FBI");
            alert.showAndWait();
        } else {
            WebEngine engine = webView.getEngine();
            String str = (String) listView.getSelectionModel().getSelectedItem();
            String str1 = textField.getText();
            if ("".equals(str1)) {
                if (checkTypeDictionary == true) {
                    engine.load("https://en.wiktionary.org/wiki/" + str);
                } else {
                    engine.load("https://vi.wiktionary.org/wiki/" + str);
                }
            } else {
                if (checkTypeDictionary == true) {
                    engine.load("https://en.wiktionary.org/wiki/" + str);
                } else {
                    engine.load("https://vi.wiktionary.org/wiki/" + str);
                }
            }
        }
    }

    @Override
    /**
     * Chương trình khởi tạo môi trường cho từ điển
     */
    public void initialize(URL location, ResourceBundle resources) {
        //hàm hiện từ lên listview
        showlistview();
        SearchTextFieldEvent();
        //gợi ý từ tìm kiếm
        searchWord();
        //bắt sự kiện cho listview khi item đc chọn
        chooseitem();
        // bắt sự kiện cho textfield SEARCH
        setCalendarDisplay();
        setClockDisplay();
    }
}
