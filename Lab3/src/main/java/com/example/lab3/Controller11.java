package com.example.lab3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller11 implements Initializable {

    Square square = new Square();
    Triangle triangle = new Triangle();

    Rectangle rectangle = new Rectangle();

    Stage stage12 = new Stage();

    @FXML
    private ImageView tr_img;

    @FXML
    private TextField tr_A;

    @FXML
    private TextField tr_B;

    @FXML
    private TextField tr_C;

    @FXML
    private ImageView sq_img;

    @FXML
    private TextField sq_A;

    @FXML
    private ImageView rec_img;

    @FXML
    private TextField rec_A;

    @FXML
    private TextField rec_B;

    @FXML
    private Button calc_tr;

    @FXML
    private Button calc_sq;

    @FXML
    private Button calc_rec;

    @FXML
    public void onCalcTr() {
        double a = Double.parseDouble(tr_A.getText());
        double b = Double.parseDouble(tr_B.getText());
        double c = Double.parseDouble(tr_C.getText());
        triangle = new Triangle(a, b, c);
        stage12.setUserData(triangle);
        try {
            startStage12();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCalcSq() {
        double a = Double.parseDouble(sq_A.getText());
        square = new Square(a);
        stage12.setUserData(square);
        try {
            startStage12();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCalcRec() {
        double a = Double.parseDouble(rec_A.getText());
        double b = Double.parseDouble(rec_B.getText());
        rectangle = new Rectangle(a, b);
        stage12.setUserData(rectangle);
        try {
            startStage12();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void startStage12() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Application1.class.getResource("second-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage12.setTitle("Stage12");
        stage12.setScene(scene);
        Shape shape = (Shape) stage12.getUserData();
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
        stage12.show();
    }
}
