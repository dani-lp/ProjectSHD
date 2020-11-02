package com.a02.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import javax.rmi.ssl.SslRMIClientSocketFactory;
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

    public Enemy(float x, float y, int width, int height, String sprite, int id, String name, int hp, int attackDamage, float speed) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.WALKING;
    }

    public Enemy() {
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

    protected void update(float beaconX, float beaconY, ArrayList<GameObject> objects, float secTimer) {
        switch (this.state) {
            case WALKING:
                this.move(beaconX, beaconY);

                if (this.getHp() == 0) {
                    this.state = State.DYING;
                }

                else if (this.overlapsArray(objects)) {
                    this.state = State.ATTACKING;
                }

            case ATTACKING:
                if (this.overlapsArray(objects)) {
                    int tempIndex = objects.indexOf(this.overlappedObject(objects));
                    System.out.println(objects.get(tempIndex).getHp());
                    if (objects.get(tempIndex).getHp() > 0 || secTimer % 60 == 0) {
                        objects.get(tempIndex).setHp(objects.get(tempIndex).getHp() - this.attackDamage);
                    } else if (this.overlappedObject(objects).getHp() > 0) {
                        //target.delete() ?
                        this.state = State.WALKING;
                    }
                }

            case DYING:
                //Muere;

        }
    }

    protected void move(float beaconX, float beaconY) {
        if (this.getX() < beaconX && this.getY() < beaconY) {
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() > beaconY) {
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() == beaconY) {
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() == beaconY) {
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() > beaconY) {
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() < beaconY) {
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() < beaconY) {
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() > beaconY) {
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        }
    }

    protected boolean overlapsArray(ArrayList<GameObject> objects) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (GameObject object: objects) {
            if ((this.getY() + this.getHeight() < object.getY()) || (this.getY() > object.getY() + object.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < object.getX()) || (this.getX() > object.getX() + object.getWidth())) {
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
        if (this.overlaps(thing)){
            thing.setHp(thing.getHp()- this.getAttackDamage());
            System.out.println(thing.getHp());
        }
    }
}
