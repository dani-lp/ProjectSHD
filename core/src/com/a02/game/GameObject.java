package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class GameObject extends Entity{
    private int id;
    private String name;
    private int price;
    private boolean unlocked;
    private int hp;

    public GameObject(float x, float y, int width, int height, String sprite, int id, String name,
                      int price, boolean unlocked, int hp) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
    }


    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", x=" + this.getX() + ", y=" + this.getY() + ", sprite=" + getSprite() + ", price=" + price
                + ", unlock=" + unlocked + "]";
    }

    static boolean temp = false;
    public void buy(){
        if (Gdx.input.isTouched() && this.overlapsPoint(Gdx.input.getX(), 180-Gdx.input.getY()) && temp == false) {
            temp = true;
        }
        if (temp) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            GameScreen.camera.unproject(touchPos);
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }
}
