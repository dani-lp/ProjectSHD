package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.logging.Logger;

public class Attacker extends GameObject {
    private float attackDamage;
    private String attackType;
    private static Logger logger = Logger.getLogger(GameObject.class.getName());

    private enum State {
        IDLE, ATTACKING
    }

    State state;

    public Attacker(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp, String attackType, float attackDamage) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;

        switch (this.getId()){
            case 0: //Electricidad
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
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
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

    /**
     * Actualiza el estado de los objetos de ataque.
     * @param gs GameScreen utilizada
     */
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (this.overlappedEnemy(gs.enemies) != null) {
                    this.state = State.ATTACKING;
                    logger.info("Enemigo atacando");
                }
                break;

            case ATTACKING:
                if (this.overlappedEnemy(gs.enemies) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs.enemies);

                    if (tempEnemy.getHp() > 0 && gs.secTimer % 60 == 0) {
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


