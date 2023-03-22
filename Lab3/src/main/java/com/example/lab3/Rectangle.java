package com.example.lab3;

import javafx.scene.image.Image;

public class Rectangle extends Shape{

    double length, width;

    public Rectangle() {
        super();
        this.image = "rectangle.png";
        this.length = 0;
        this.width = 0;
    }

    public Rectangle(double length, double width) {
        super();
        this.length = length;
        this.width = width;
        this.image = "rectangle.png";
        calculateArea();
        calculatePerimeter();
    }

    @Override
    public void calculateArea() {
        this.area = length * width;
    }

    @Override
    public void calculatePerimeter() {
        this.perimeter = 2 * (length + width);
    }

    @Override
    public String toString() {
        return "Rectangle: " +
                "length = " + length +
                ", width = " + width;
    }
}
