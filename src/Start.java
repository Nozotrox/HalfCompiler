import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Ui/Main.fxml"));
        primaryStage.setTitle("Metade de Compilador");
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.show();
    }

    @FXML
    void onLoadFile(ActionEvent event) {

    }
}
