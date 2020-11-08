package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Inventory {
    private final int x = 256;
    private final int y = 0;
    protected ArrayList<GameObject> objects;
    private final String sprite = "inventory.png";
    private final Texture texture = new Texture(Gdx.files.internal(sprite));

    protected int getX() {
        return x;
    }
    protected int getY() {
        return y;
    }
    public Texture getTexture() {
        return texture;
    }

    protected Inventory(){
        this.objects = new ArrayList<GameObject>();
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

    public void takeOut(GameObject gameObject ){
        this.objects.remove(gameObject);
    }
}
