package com.example.lab3;

import javafx.scene.image.Image;

public class Square extends Shape{

    double side;

    public Square() {
        super();
        this.image = "square.png";
        this.side = 0;
    }

    public Square(double side) {
        super();
        this.side = side;
        this.image = "square.png";
        calculateArea();
        calculatePerimeter();
    }

    @Override
    public void calculateArea() {
        this.area = side * side;
    }

    @Override
    public void calculatePerimeter() {
        this.perimeter = 4 * side;
    }

    @Override
    public String toString() {
        return "Square: " +
                "side = " + side;
    }
}
