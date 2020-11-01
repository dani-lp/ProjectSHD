package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Trap extends GameObject{
    private String effect;
    private float attackDamage;

    public Trap(float x, float y, int width, int height, String sprite, int id, String name, int price, boolean unlocked, int hp, boolean buyable, String effect, float attackDamage) {
        super(x, y, width, height, sprite, id, name, price, unlocked, hp, buyable);
        this.effect = effect;
        this.attackDamage = attackDamage;
    }

    public Trap(Trap other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(), other.getName(), other.getPrice(), other.isUnlocked(), other.getHp(), other.isBuyable());
        this.effect = other.getEffect();
        this.attackDamage = other.getAttackDamage();
    }

    public Trap() {
        super();
        this.effect = "";
        this.attackDamage = 0;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackDamage = attackDamage;
    }

    static boolean temp = false;
    public void buy(GameScreen game, ArrayList<GameObject> objects, ArrayList<Texture> textures, Inventory inventory, Map map){
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y)&& !temp && isBuyable()) {
            temp = true;
        }
        if (temp) { //El objeto ya ha sido "cogido"
            //Ajusta la posición del sprite a la del mouse
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));

            //Al "soltar" el objeto:
            if (!Gdx.input.isTouched()){
                Vector2 temp = this.mapGridCollisionMouse(map, touchPos.x, touchPos.y); //Pos. del mouse

                if (!map.getOccGrid()[(int)temp.x/16][(int)temp.y/18]) {    //Comprueba si la casilla está libre
                    Trap object = new Trap(this);   //Objeto que va a ser colocado
                    Texture textu = new Texture(Gdx.files.internal(object.getSprite())); //Textura del objeto copia

                    object.setX(temp.x);   //Fija la posición copia
                    object.setY(temp.y);
                    objects.add(object);
                    textures.add(textu);

                    map.getOccGrid()[(int)temp.x/16][(int)temp.y/18] = true;
                }
                this.setX(260); //Devuelve a su posición inicial al objeto original
                this.setY(115); //260 140
            }
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }

}
