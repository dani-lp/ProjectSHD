package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Attacker extends GameObject{
    private float attackDamage;
    private String attackType;

    public Attacker(float x, float y, int width, int height, String sprite, int id, String name, String type, int price, boolean unlocked, int hp, boolean buyable, boolean selected, String attackType, float attackDamage) {
        super(x, y, width, height, sprite, id, name, type, price, unlocked, hp, buyable,selected);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
    }
    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
    }
    public Attacker(Attacker other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(), other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp(), other.isBuyable(),other.isSelected());
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    public String getAttackType() {
        return attackType;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    float x= this.getX();
    float y = this.getY();
    static boolean temp = false;
    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory inventory, Map map){
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);
        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && !temp  && isBuyable() && objects.get(0).isSelected()) {
            temp = true;
            objects.get(0).setSelected(false);
        }
        if (temp) { //El objeto ya ha sido "cogido"
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));
            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()){
                Vector2 temp = this.mapGridCollisionMouse(map, touchPos.x, touchPos.y); //Pos. del mouse
                if (!map.getOccGrid()[(int)temp.x/16][(int)temp.y/18]) {    //Comprueba si la casilla está libre
                    Attacker object = new Attacker(this);   //Objeto que va a ser colocado
                    Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia
                    object.setX(temp.x);   //Fija la posición copia
                    object.setY(temp.y);

                    objects.add(object);
                    textures.add(textu);

                    map.getOccGrid()[(int)temp.x/16][(int)temp.y/18] = true;
                }
                this.setX(x); //Devuelve a su posición inicial al objeto original
                this.setY(y); //280 135
                objects.get(0).setSelected(true);
            }
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }
}
