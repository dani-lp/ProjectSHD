package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Enemy extends Entity{
    private int id;
    private String name;
    private int hp;
    private int attackDamage;
    private float speed;

    private enum State {
        WALKING, ATTACKING, DYING
    }

    State state;

    public Enemy(float x, float y, int width, int height, String sprite, int id, String name, int hp, int attackDamage, float speed) {  //Constructor de enemigos
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.WALKING;
    }

    public Enemy() {        //Constructor vacio de enemigos
        super();
        this.id = -1;
        this.name = "";
        this.hp = 0;
        this.attackDamage = 0;
        this.speed = 0;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Actualiza la posición y estado de un enemigo.
     * @param beaconX Coordenada X del beacon
     * @param beaconY Coordenada Y del beacon
     * @param objects Arraylist de objetos defensivos
     * @param secTimer Reloj de juego
     */
    protected void update(float beaconX, float beaconY, ArrayList<GameObject> objects, float secTimer) {
        switch (this.state) {
            case WALKING:
                this.move(beaconX, beaconY);    //Movimiento a beacon

                if (this.getHp() == 0) {
                    this.state = State.DYING;
                }

                else if (this.overlapsArray(objects)) {
                    this.state = State.ATTACKING;
                }

            case ATTACKING:
                if (this.overlapsArray(objects)) {
                    GameObject tempObj = this.overlappedObject(objects);    //Objeto siendo colisionado

                    if (tempObj.getHp() > 0 && secTimer % 60 == 0) {    //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.attackDamage);
                    } else if (tempObj.getHp() <= 0) {
                        //target.delete()?
                        this.state = State.WALKING;
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() == 0) {
                    this.state = State.DYING;
                }

            case DYING:
                //Muere;

        }
    }

    protected void move(float beaconX, float beaconY) {             //Dependiendo de donde se encuentre el beacon respecto al objeto se aumenta o decrementan la X y la Y hasta que sean iguales
        if (this.getX() < beaconX && this.getY() < beaconY) {                  //El beacon esta por encima y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() > beaconY) {            //El beacon esta por debajo y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() == beaconY) {           //El beacon esta a la misma altura y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() == beaconY) {           //El beacon esta a la misma altura y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() > beaconY) {            //El beacon esta por debajo y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() < beaconY) {            //El beacon esta por encima y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() < beaconY) {           //El beacon esta por debajo y en la misma posicion
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() > beaconY) {           //El beacon esta por arriba y en la misma posicion
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        }
    }

    protected boolean overlapsArray(ArrayList<GameObject> objects) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (GameObject object: objects) {
            if ((this.getY() + this.getHeight() < object.getY()) || (this.getY() > object.getY() + object.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < object.getX()) || (this.getX() > object.getX() + object.getWidth())) {
                continue;
            }else if(object.getType()=="Trap"){
                continue;
            }
            else {
                return true;
            }
        }
        return false;
    }

    protected GameObject overlappedObject(ArrayList<GameObject> objects) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (GameObject object: objects) {
            if ((this.getY() + this.getHeight() < object.getY()) || (this.getY() > object.getY() + object.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < object.getX()) || (this.getX() > object.getX() + object.getWidth())) {
                continue;
            }
            else {
                return object;
            }
        }
        return null;
    }

    public void attack(GameObject thing){
        if (this.overlaps(thing)){      //Si estan en contacto empieza a restarle vida
            thing.setHp(thing.getHp()- this.getAttackDamage());
            System.out.println(thing.getHp());
        }
    }
}
