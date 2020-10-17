package com.a02.game;

import com.badlogic.gdx.utils.Null;

public class GameObject {
    protected int id;
    protected String name;
    protected int x;
    protected int y;
    protected String sprite;
    protected int price;
    protected  int unlock;


    public GameObject(int id, String name, int x, int y, String sprite, int price, int unlock) {
        super();
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.price = price;
        this.unlock = unlock;
    }
    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
        this.x = 0;
        this.y = 0;
        this.sprite = "";
        this.price = 0;
        this.unlock = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnlock() {
        return unlock;
    }

    public void setUnlock(int unlock) {
        this.unlock = unlock;
    }
    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", x=" + x + ", y=" + y + ", sprite=" + sprite + ", price=" + price
                + ", unlock=" + unlock + "]";
    }
}
