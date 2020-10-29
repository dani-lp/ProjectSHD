package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class InventoryObject extends Entity {
    protected String name;
    protected int price;
    protected boolean unlocked;
    protected GameObject object;

    public InventoryObject(float x, float y, int width, int height, String sprite, String name, int price, boolean unlocked) {
        super(x, y, width, height, sprite);
        this.name = name;
        this.price = price;
        this.unlocked = unlocked;
    }

    public InventoryObject(String name, int price, boolean unlocked) {
        this.name = name;
        this.price = price;
        this.unlocked = unlocked;
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

    @Override
    public String toString() {
        return "InventoryObject{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", unlocked=" + unlocked +
                '}';
    }
    static boolean temp = false;
    public void buy(GameScreen game) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && temp == false) {
            new GameObject().place(game);
            System.out.println("AA");
        }
    }
}
