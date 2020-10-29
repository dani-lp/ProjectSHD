package com.a02.game;

public class Shoot {
    private int x;
    private int y;
    private int speed;

    public Shoot(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Shoot{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                '}';
    }
}
