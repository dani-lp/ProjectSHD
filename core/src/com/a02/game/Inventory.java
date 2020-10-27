package com.a02.game;

import java.util.ArrayList;

public class Inventory {
    private final int x = 256;
    private final int y = 0;
    protected ArrayList<GameObject> objects;
    private final String sprite = "inventory.png";

    protected int getX() {
        return x;
    }
    protected int getY() {
        return y;
    }

    protected Inventory(){
        this.objects=new ArrayList<GameObject>();
    }

    protected ArrayList<GameObject> getObjects() {
        return objects;
    }
    protected String getSprite() {
        return sprite;
    }

    public void insert(GameObject gameObject ){
        this.objects.add(gameObject);
    }
}
