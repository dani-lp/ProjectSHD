package com.a02.game;

import java.util.ArrayList;
import java.util.List;

public class Attacker extends GameObject {
    private float attackDamage;
    private String attackType;

    private enum State {
        IDLE, ATTACKING
    }

    State state;

    public Attacker(float x, float y, int width, int height, String sprite, int id, String name, String type, int price,
                    boolean unlocked, int hp, String attackType, float attackDamage) {
        super(x, y, width, height, sprite, id, name, type, price, unlocked, hp);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;
    }

    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
    }

    public Attacker(Attacker other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(),
                other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
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
    }

}


