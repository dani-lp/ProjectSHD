package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class GameObject extends Entity{
    private int id;
    private String name;
    private int price;
    private boolean unlocked;
    private boolean buyable=true;
    private int hp;

    public GameObject(float x, float y, int width, int height, String sprite, int id, String name,
                      int price, boolean unlocked, int hp, boolean buyable) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.price = price;
        this.unlocked = unlocked;
        this.buyable = buyable;
        this.hp = hp;
    }

    public GameObject(GameObject other){
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite());
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        this.unlocked = other.unlocked;
        this.buyable = other.buyable;
        this.hp = other.hp;
    }

    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
        this.price = 0;
        this.unlocked = false;
        this.unlocked = true;
        this.hp = 0;
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

    public boolean isBuyable() {
        return buyable;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
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
                + ", unlock=" + unlocked + ", buyable=" + buyable + ", hp=" + hp+"]";
    }

    static boolean temp = false;
    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory inventory){
        System.out.println(this);
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && temp == false && buyable) {
            temp = true;
        }
        if (temp) { //Es objeto ya ha sido "cogido"
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));

            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()){
                GameObject object = new GameObject(this);   //Objeto que va a ser colocado
                Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia
                object.setX(this.getX());   //Fija la posición copia
                object.setY(this.getY());
                objects.add(object);
                textures.add(textu);
                this.setX(260); //Devuelve a su posición inicial al objeto original
                this.setY(140);
            }
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }

    //Devuelve el número de casilla con la que colisiona del mapa recibido como parámetro, null si no lo hace
    protected Vector2 mapGridCollision(Map map) {
        int x;
        int y;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 16; j++) {
                if (this.overlaps( map.getCoordGrid()[i][j] , 10, 16)) {
                    return new Vector2(map.getCoordGrid()[i][j].x, map.getCoordGrid()[i][j].y);
                }
            }
        }
        return null;
    }

}
