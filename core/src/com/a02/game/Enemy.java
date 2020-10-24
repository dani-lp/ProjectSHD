package com.a02.game;

public abstract class Enemy {
    private int id;
    private String name;
    private int x;
    private int y;
    private String sprite;
    private int hp;
    private int attackDamage;
    private int speed;

    public Enemy(int id, String name, int x, int y, String sprite, int hp, int attackDamage, int speed) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
    }

    public Enemy() {
        this.id = -1;
        this.name = "";
        this.x = 0;
        this.y = 0;
        this.sprite = "";
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void move(float beaconX, float beaconY){
        if (this.getX()<beaconX){
            this.setX(this.getX()+this.speed);
        }   else if (this.getX()> beaconX){
            this.setX(this.getX()-this.speed);
        }
        if (this.getY()<beaconY){
            this.setY(this.getY()+this.speed);
        }   else if (this.getY()> beaconY){
            this.setY(this.getY()-this.speed);
        }
    }
}
