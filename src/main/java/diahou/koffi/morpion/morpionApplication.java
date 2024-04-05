package diahou.koffi.morpion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class morpionApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(morpionApplication.class.getResource("morpion-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(" DIAHOU-EMMANUEL && KOFFI-DANIEL");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
