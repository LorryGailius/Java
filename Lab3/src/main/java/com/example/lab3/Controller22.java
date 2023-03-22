package com.example.lab3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller22 implements Initializable {

    @FXML
    private VBox shape_field;

    private Shape shape;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setShape(Shape shape){
        this.shape = shape;
    }

    public void updateField() throws IOException {
        Stage stage22 = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application1.class.getResource("second-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage22.setTitle("Stage22");
        stage22.setScene(scene);
        //Change shape_data label in stage12
        VBox shape_field = (VBox) scene.lookup("#shape_field");
        Text shape_data = new Text(shape.toString());
        Text shape_area = new Text("Area: " + String.format("%.2f", shape.area));
        Text shape_perimeter = new Text("Perimeter: " + String.format("%.2f", shape.perimeter));
        Image image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(shape.image)));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        shape_field.getChildren().add(imageView);
        shape_field.getChildren().add(shape_data);
        shape_field.getChildren().add(shape_area);
        shape_field.getChildren().add(shape_perimeter);
        stage22.show();
    }

}
