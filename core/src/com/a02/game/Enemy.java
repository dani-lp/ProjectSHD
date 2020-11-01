package com.a02.game;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Enemy extends Entity{
    private int id;
    private String name;
    private int hp;
    private int attackDamage;
    private float speed;

    public Enemy(float x, float y, int width, int height, String sprite, int id, String name, int hp, int attackDamage, float speed) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
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

    public void move(float beaconX, float beaconY,ArrayList<GameObject> objects){     //Va comparando para intentar encontrar la ruta mas rapida
        for (GameObject object: objects) {
            if (!this.overlaps(object)) {
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
            }else{
                while (object.getHp()!=0){
                    this.attack(object);
                    for (int i = 0; i < 10000000; i++){
                    }
                }
            }
        }

    }

    public void attack(GameObject thing){
            if (this.overlaps(thing)){
                thing.setHp(thing.getHp()- this.getAttackDamage());
            }
    }
}
