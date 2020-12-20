package com.a02.entity;

import com.a02.component.Map;
import com.a02.component.Shoot;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.List;
import java.util.logging.Logger;

import static com.a02.game.Utils.getRelativeMousePos;

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
        textures();
    }

    public void textures(){
        switch (this.getId()){
            case 0: //Electricidad
                this.setTexture(new Texture(Gdx.files.internal("electricity.png")));
                break;
            case 2:
                this.setTexture(new Texture(Gdx.files.internal("boredlion.png")));
        }
    }

    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
        this.state = State.IDLE;
        textures();
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
                if (this.getId()==2){
                    this.state = State.ATTACKING;
                }

                if (this.overlappedEnemy(gs) != null) {
                    this.state = State.ATTACKING;
                    logger.info("Enemigo atacando");
                }
                break;

            case ATTACKING:
                if (this.getId()!=2){
                    if (this.overlappedEnemy(gs) != null) {
                        Enemy tempEnemy = this.overlappedEnemy(gs);
                        if (tempEnemy.getHp() > 0 && gs.secTimer % 60 == 0) {
                            tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                        } else if (tempEnemy.getHp() <= 0) {
                            this.state = State.IDLE;
                        }
                    }
                } else{
                    if (!this.isInInventory(gs) && gs.secTimer % 60 == 0){
                        Shoot shoot= new Shoot(this.getX(),this.getY(),2,2,20,this.getAttackDamage(),"shoot.png",5);
                        GameScreen.shoots.add(shoot);
                    }

                }

                break;
        }
        this.hpBar.update(this, this.getHp());
    }

}


