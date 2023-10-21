package com.example.documentarian;

public class Point {
    public  double z = 0;

    private double w = 0;
    private static volatile transient double x = 0;
    private double y = 0;

    public double getX() {
        return x;
    }
    private double getY() {
        return y;
    }

    public void setW(double w) {
        this.w = w;
    }
}
