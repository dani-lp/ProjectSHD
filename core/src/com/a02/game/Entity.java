package com.a02.game;

public class Entity {
    private int x;
    private int y;
    private int width;
    private int length;
    private String Sprite;

    public Entity(int x, int y, int width, int length, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = length;
        Sprite = sprite;
    }

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.width =0;
        this.length = 0;
        Sprite = "";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public String getSprite() {
        return Sprite;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setSprite(String sprite) {
        Sprite = sprite;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", length=" + length +
                ", Sprite='" + Sprite + '\'' +
                '}';
    }

    public boolean intersect(Entity entity) {
        //Devuelve true si existe colisión entre el jugador y el sprite
        if ((this.topRightY < entity.botLeftY)  (this.botLeftY > entity.topRightY)) {
            return false;
        }
        if ((this.topRightX < entity.botLeftX)  (this.botLeftX > entity.topRightX)) {
            return false;
        }
        return true;
    }
}
