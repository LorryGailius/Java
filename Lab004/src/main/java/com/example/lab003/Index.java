package com.example.lab003;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Index extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Index.class.getResource("main_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        fxmlLoader.setController(new MainWindowController());
        stage.setTitle("Student management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}