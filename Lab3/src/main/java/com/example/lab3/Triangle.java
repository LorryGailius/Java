package com.example.lab3;

import javafx.scene.image.Image;

public class Triangle extends Shape{

    double side1, side2, side3;

    public Triangle() {
        super();
        this.image = "triangle.png";
        this.side1 = 0;
        this.side2 = 0;
        this.side3 = 0;
    }

    public Triangle(double side1, double side2, double side3) {
        super();
        this.image = "triangle.png";
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
        calculateArea();
        calculatePerimeter();
    }

    @Override
    public void calculateArea() {
        double s = (side1 + side2 + side3) / 2;
        this.area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
    }

    @Override
    public void calculatePerimeter() {
        this.perimeter = side1 + side2 + side3;
    }

    @Override
    public String toString() {
        return "Triangle : " +
                "side1 = " + side1 +
                ", side2 = " + side2 +
                ", side3 = " + side3;
    }
}
