package com.a02.entity;

public class Obstacle extends Entity{
    public Obstacle(float x, float y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
    }

    public Obstacle() {
        this.setX(0);
        this.setY(0);
        this.setWidth(0);
        this.setHeight(0);
    }
}
