package com.a02.game;

public class GameObject {
    private int id;
    private String name;
    private int x;
    private int y;
    private String sprite;
    private int price;
    private boolean unlocked;


    public GameObject(int id, String name, int x, int y, String sprite, int price, boolean unlocked) {
        super();
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.price = price;
        this.unlocked = unlocked;
    }
    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
        this.x = 0;
        this.y = 0;
        this.sprite = "";
        this.price = 0;
        this.unlocked = false;
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

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", x=" + x + ", y=" + y + ", sprite=" + sprite + ", price=" + price
                + ", unlock=" + unlocked + "]";
    }
}
