package com.a02.game;

import java.util.ArrayList;

public class Inventory {
    private final int x = 256;
    private final int y = 0;
    private ArrayList<GameObject> objects;
    private final String sprite = "inventory.png";

    protected int getX() {
        return x;
    }
    protected int getY() {
        return y;
    }
    protected ArrayList<GameObject> getObjects() {
        return objects;
    }
    protected String getSprite() {
        return sprite;
    }
}
