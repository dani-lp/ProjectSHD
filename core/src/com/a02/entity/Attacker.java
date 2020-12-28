package com.a02.entity;

import com.a02.component.Map;
import com.a02.component.Shoot;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.a02.game.Utils.getRelativeMousePos;

public class Attacker extends GameObject {
    private float attackDamage;
    private String attackType;
    private static Logger logger = Logger.getLogger(GameObject.class.getName());
    public static boolean selected;
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
                break;
            case 3:
                this.setTexture(new Texture(Gdx.files.internal("waves.png")));
                break;
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
                if (this.getId() == 2 || this.getId() == 3){
                    this.state = State.ATTACKING;
                }

                if (this.overlappedEnemy(gs) != null) {
                    this.state = State.ATTACKING;
                    logger.info("Enemigo atacando");
                }
                break;

            case ATTACKING:
                if (this.getId() == 1){
                    if (this.overlappedEnemy(gs) != null) {
                        Enemy tempEnemy = this.overlappedEnemy(gs);
                        if (tempEnemy.getHp() > 0 && gs.secTimer % 60 == 0) {
                            tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                        } else if (tempEnemy.getHp() <= 0) {
                            this.state = State.IDLE;
                        }
                    }
                }
                else if (this.getId() == 2) {
                    if (!this.isInInventory(gs) && gs.secTimer % 60 == 0) {
                        Vector3 mousePos = getRelativeMousePos();
                        if (this.overlapsPoint(mousePos.x, mousePos.y) && Gdx.input.isTouched()) {
                            selected = true;
                            Pixmap pm = new Pixmap(Gdx.files.internal("mira-export.png"));
                            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
                            pm.dispose();
                        }
                        if (selected) {
                            Shoot shoot = new Shoot(this.getX(), this.getY(), 2, 2, 2, this.getAttackDamage(), "shoot.png", 5, this.getId(),"n");
                            GameScreen.shoots.add(shoot);
                        }
                    }
                }
                else if (this.getId() == 3) {
                    if (!this.isInInventory(gs) && gs.secTimer % 60 == 0) {
                        Shoot right = new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 5, this.getAttackDamage(), "shoot.png", 5, this.getId(),"r");
                        Shoot left = new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 5, this.getAttackDamage(), "shoot.png", 5, this.getId(),"l");
                        Shoot up = new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 5, this.getAttackDamage(), "shoot.png", 5, this.getId(),"u");
                        Shoot down = new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 5, this.getAttackDamage(), "shoot.png", 5, this.getId(),"d");

                        GameScreen.shoots.add(right);
                        GameScreen.shoots.add(left);
                        GameScreen.shoots.add(up);
                        GameScreen.shoots.add(down);
                    }
                }
                break;
        }
        this.hpBar.update(this, this.getHp());
    }

}


