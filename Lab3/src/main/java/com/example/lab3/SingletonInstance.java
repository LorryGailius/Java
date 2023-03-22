package com.example.lab3;

public class SingletonInstance {

    private static SingletonInstance instance = null;

    private Shape shape;

    private SingletonInstance() {
    }

    public static SingletonInstance getInstance() {
        if (instance == null) {
            instance = new SingletonInstance();
        }
        return instance;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
