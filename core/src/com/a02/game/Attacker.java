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
                    boolean unlocked, int hp, boolean buyable, boolean selected, String attackType, float attackDamage) {
        super(x, y, width, height, sprite, id, name, type, price, unlocked, hp, buyable, selected);
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
                other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp(), other.isBuyable(), other.isSelected());
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
                if (this.overlapsArrayEnemies(enemies)) {
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                if (this.overlapsArrayEnemies(enemies)) {
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

    protected boolean overlapsArrayEnemies(List<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : enemies) {
            if ((this.getY() + this.getHeight() < enemy.getY()) || (this.getY() > enemy.getY() + enemy.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < enemy.getX()) || (this.getX() > enemy.getX() + enemy.getWidth())) {
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    protected Enemy overlappedEnemy(List<Enemy> enemies) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : enemies) {
            if ((this.getY() + this.getHeight() < enemy.getY()) || (this.getY() > enemy.getY() + enemy.getHeight())) {
                continue;
            } else if ((this.getX() + this.getWidth() < enemy.getX()) || (this.getX() > enemy.getX() + enemy.getWidth())) {
                continue;
            } else {
                return enemy;
            }
        }
        return null;
    }

    public void attack(Enemy thing) {
        if (this.overlaps(thing)) {      //Si estan en contacto empieza a restarle vida
            thing.setHp((int) (thing.getHp() - this.getAttackDamage()));
            System.out.println(thing.getHp());
        }
    }

}


