package com.a02.game;

public abstract class Entity {
    private int x;
    private int y;
    private int width;
    private int height;
    private String sprite;

    public Entity(int x, int y, int width, int height, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.width =0;
        this.height = 0;
        this.sprite = "";
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

    public int getHeight() {
        return height;
    }

    public String getSprite() {
        return sprite;
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

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSprite(String sprite) {
        sprite = sprite;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", Sprite='" + sprite + '\'' +
                '}';
    }

    public boolean overlaps(Entity entity) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        if ((this.y + this.height < entity.y) || (this.y > entity.y + entity.height)) {
            return false;
        }
        else if ((this.x + this.width < entity.x) || (this.x > entity.x + entity.width)) {
            return false;
        }
        return true;
    }

    public boolean overlapsPoint (float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

}
