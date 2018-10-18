package bin.Main.main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
    private static FXMLLoader mLoader;

    public static FXMLLoader getLoader() {
        return mLoader;
    }

    public static void setLoader(FXMLLoader tempLoader) {
        mLoader = tempLoader;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(this.getClass().getResource("../Style/MainSceneBuilder.fxml"));
        primaryStage.setTitle("DICTIONARY DEMO");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../Style/StyleBuilder.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setOnCloseRequest(event -> {
            // Ngăn cửa sổ đóng lại
            event.consume();

            // Tắt chương trình
            shutdown(primaryStage);
        });
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void shutdown(Stage mainWindow) {
        Alert alert = new Alert(Alert.AlertType.NONE, "Bạn có chắc là muốn thoát chương trình?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            mainWindow.close();
        }
    }
}
