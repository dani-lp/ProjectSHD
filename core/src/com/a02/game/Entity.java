package com.a02.game;

import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    private float x;
    private float y;
    private int width;
    private int height;
    private String sprite;

    public Entity(float x, float y, int width, int height, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.sprite = "";
    }

    public float getX() {
        return x;
    }

    public float getY() {
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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
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

    protected boolean overlaps(Entity entity) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        if ((this.y + this.height < entity.y) || (this.y > entity.y + entity.height)) {
            return false;
        }
        else if ((this.x + this.width < entity.x) || (this.x > entity.x + entity.width)) {
            return false;
        }
        return true;
    }

    protected boolean overlaps(Vector2 vec2, int height, int width) { //Devuelve true si la Entity que llama colisiona con el Vector2 parámetro
        if ((this.y + this.height < vec2.y) || (this.y > vec2.y + height)) {
            return false;
        }
        else if ((this.x + this.width < vec2.x) || (this.x > vec2.x + width)) {
            return false;
        }
        return true;
    }

    protected boolean overlaps2(Vector2 vec, int height, int width) {
        return x < vec.x + width && x + width > vec.x && y < vec.y + height && y + height > vec.y;
    }

    protected boolean overlaps2(Entity entity) {
        return x < entity.x + entity.width && x + width > entity.x && y < entity.y + entity.height && y + height > entity.y;
    }

    protected boolean overlapsPoint (float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

}
