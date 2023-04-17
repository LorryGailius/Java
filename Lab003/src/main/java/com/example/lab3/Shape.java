package com.example.lab3;

import javafx.scene.image.Image;

public class Shape {

    public String image;
    double area;
    double perimeter;

    public Shape() {
        this.area = 0;
        this.perimeter = 0;
    }

    public Shape(double area, double perimeter) {
        this.area = area;
        this.perimeter = perimeter;
    }

    public void calculateArea() {
        this.area = 0;
    }

    public void calculatePerimeter() {
        this.perimeter = 0;
    }

}
