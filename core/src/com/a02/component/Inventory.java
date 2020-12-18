package com.a02.component;

import com.a02.entity.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * El inventario base tiene 15 objetos, en una matriz de 5*3 elementos; el array que contiene sus posiciones es
 * unidimensional, y se lee/escribe de derecha a izquierda, en vez de de arriba a abajo.
 */
public class Inventory {
    private final int x = 256;
    private final int y = 0;
    protected ArrayList<GameObject> objects;
    protected Vector2[] positions;
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
        this.objects = new ArrayList<>();
        this.positions = new Vector2[15];
        final int currX = 261;
        int currY = 41;
        for (int i = 0; i < positions.length; i+=3) {
            for (int j = 0; j <= 2; j++) {
                positions[i+j] = new Vector2(currX + 19*j, 180 - currY);
            }
            currY += 29;
        }
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    public String getSprite() {
        return sprite;
    }

    public void insert(GameObject gameObject){
        this.objects.add(gameObject);
        gameObject.setX(positions[objects.size() - 1].x);
        gameObject.setY(positions[objects.size() - 1].y);
    }

    public Inventory sort(){
        Inventory order= new Inventory();
        for (GameObject object:this.getObjects()) {    //Objetos del inventario
            order.insert(object);
        }
        return order;
    }

    public boolean contains(GameObject obj) {
        return objects.contains(obj);
    }

    /*
    public void takeOut(GameObject gameObject ){
        this.objects.remove(gameObject);
    }

    */
}
