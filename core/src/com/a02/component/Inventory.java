package com.a02.component;

import com.a02.entity.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Inventory {
    private final int x = 256;
    private final int y = 0;
    protected ArrayList<GameObject> objects;
    private final String sprite = "inventory.png";
    private final Texture texture = new Texture(Gdx.files.internal(sprite));

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Texture getTexture() {
        return texture;
    }

    public Inventory(){
        this.objects = new ArrayList<GameObject>();
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    public String getSprite() {
        return sprite;
    }

    public void insert(GameObject gameObject ){
        this.objects.add(gameObject);
    }

    public void takeOut(GameObject gameObject ){
        this.objects.remove(gameObject);
    }
}
