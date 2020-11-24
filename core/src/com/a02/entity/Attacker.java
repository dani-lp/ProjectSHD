package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Attacker extends GameObject {
    private float attackDamage;
    private String attackType;

    private enum State {
        IDLE, ATTACKING
    }

    State state;

    public Attacker(int id, String type, int price, boolean unlocked, int hp, String attackType, float attackDamage) {
        super(id, type, price, unlocked, hp);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;

        switch (this.getId()){
            case 2: //Electricidad
                this.setTexture(new Texture(Gdx.files.internal("electricity.png")));
                break;
        }
    }

    public Attacker(int id, String type, int price, boolean unlocked, int hp, String attackType, float attackDamage,
                    float x, float y) {
        super(id, type, price, unlocked, hp, x, y);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;

        switch (this.getId()){
            case 2: //Electricidad
                this.setTexture(new Texture(Gdx.files.internal("electricity.png")));
                break;
        }
    }

    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
    }

    public Attacker(Attacker other) {
        super(other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setWidth(16);
        this.setHeight(18);
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
        this.setTexture(other.getTexture());
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

    public void update(List<GameObject> objects, List<Enemy> enemies, float secTimer) {
        switch (this.state) {
            case IDLE:
                if (this.overlappedEnemy(enemies) != null) {
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                if (this.overlappedEnemy(enemies) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(enemies);

                    if (tempEnemy.getHp() > 0 && secTimer % 60 == 0) {
                        tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                    } else if (tempEnemy.getHp() <= 0) {
                        this.state = State.IDLE;
                    }
                }
                break;
        }
        this.hpBar.update(this, this.getHp());
    }

}


