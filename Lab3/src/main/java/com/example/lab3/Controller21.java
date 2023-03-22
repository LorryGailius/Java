package com.example.lab3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller21 implements Initializable {

    Square square = new Square();
    Triangle triangle = new Triangle();

    Rectangle rectangle = new Rectangle();

    Controller22 controller22 = new Controller22();

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
    public void onCalcTr() throws IOException {
        double a = Double.parseDouble(tr_A.getText());
        double b = Double.parseDouble(tr_B.getText());
        double c = Double.parseDouble(tr_C.getText());
        triangle = new Triangle(a, b, c);
        changeController((Shape) triangle);
    }

    @FXML
    public void onCalcSq() throws IOException {
        double a = Double.parseDouble(sq_A.getText());
        square = new Square(a);
        changeController((Shape) square);
    }

    @FXML
    public void onCalcRec() throws IOException {
        double a = Double.parseDouble(rec_A.getText());
        double b = Double.parseDouble(rec_B.getText());
        rectangle = new Rectangle(a, b);
        changeController((Shape) rectangle);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    private void changeController(Shape shape) throws IOException {
        controller22.setShape(shape);
        controller22.updateField();
    }

}
