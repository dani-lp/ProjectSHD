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
    protected ArrayList<GameObject> objects;
    protected Vector2[] positions;
    private final String sprite;
    private final Texture texture;
    public int getX() {
        return 256;
    }
    public int getY() {
        return 0;
    }
    public Texture getTexture() {
        return texture;
    }

    /**
     * Constructor de Inventory. Genera las posiciones de forma automática.
     */
    public Inventory(String sprite){
        this.sprite = sprite;
        texture = new Texture(Gdx.files.internal(sprite));
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

    /**
     * Introduce en el inventario un objeto, posicionándolo en unas coordenadas correspondientes al orden de entrada.
     * @param gameObject Objeto a introducir
     */
    public void insert(GameObject gameObject){
        this.objects.add(gameObject);
        gameObject.setX(positions[objects.size() - 1].x);
        gameObject.setY(positions[objects.size() - 1].y);
    }

    /**
     * Devuelve un inventario copiado del que realiza la llamada.
     * @return Inventario copiado
     */
    public Inventory sortInventory(){
        Inventory order = new Inventory(this.sprite);
        for (GameObject object:this.getObjects()) {    //Objetos del inventario
            order.insert(object);
        }
        return order;
    }

    public boolean contains(GameObject obj) {
        return objects.contains(obj);
    }
}
