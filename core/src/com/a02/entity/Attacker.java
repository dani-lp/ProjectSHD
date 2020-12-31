package com.a02.entity;

import com.a02.component.Shoot;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

import static com.a02.game.Utils.*;

public class Attacker extends GameObject {
    public static boolean selected;
    private float attackDamage;
    private String attackType;
    private static Logger logger = Logger.getLogger(GameObject.class.getName());
    private boolean isSelected;
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
        this.isSelected=false;
        loadTextures();
    }

    public void loadTextures(){
        switch (this.getId()){
            case 0: //Electricidad
                this.setTexture(new Texture(Gdx.files.internal("electricity.png")));
                break;
            case 1:
                this.setTexture(new Texture(Gdx.files.internal("maquinaDeMatar.png")));
                this.setAnimation(createAnimation("willy-Sheet.png", 2, 2, 0.1f));
                break;
            case 2:
                this.setTexture(new Texture(Gdx.files.internal("boredlion.png")));
                break;
            case 3:
                this.setTexture(new Texture(Gdx.files.internal("waves.png")));
                this.setAnimation(createAnimation("wifi-Sheet.png", 3, 1, 0.2f));
                break;
        }
    }

    public Attacker() {
        super();
        this.attackType = "";
        this.attackDamage = 0;
        this.state = State.IDLE;
        this.isSelected=false;
        loadTextures();
    }

    public Attacker(Attacker other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
        this.isSelected=false;
        this.setAnimation(other.getAnimation());
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
                if (this.getId() == 0){
                    if (this.overlappedEnemy(gs) != null) {
                        Enemy tempEnemy = this.overlappedEnemy(gs);
                        if (tempEnemy.getHp() > 0 && gs.secTimer % 60 == 0) {
                            tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                        } else if (tempEnemy.getHp() <= 0) {
                            this.state = State.IDLE;
                        }
                    }
                }

                else if (this.getId() == 1){
                    if (this.overlappedEnemy(gs) != null) {
                        Enemy tempEnemy = this.overlappedEnemy(gs);
                        if (tempEnemy.getHp() > 0 && gs.secTimer % 15 == 0) {
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
                            this.isSelected = true;
                            selected=true;
                            Pixmap pm = new Pixmap(Gdx.files.internal("mira-export.png"));
                            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
                            pm.dispose();
                        }
                        Shoot shoot = new Shoot(this.getX(), this.getY(), 2, 2, 2, this.getAttackDamage(), "shoot.png", 5, this.getId(),"n");

                        GameScreen.shoots.add(shoot);

                    }
                }
                else if (this.getId() == 3) {
                    if (!this.isInInventory(gs) && gs.secTimer % 120 == 0) {
                        Shoot right = new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5, this.getAttackDamage(), "onda-export.png", 5, this.getId(),"r");
                        Shoot left = new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5, this.getAttackDamage(), "onda-export.png", 5, this.getId(),"l");
                        Shoot up = new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5, this.getAttackDamage(), "onda-export.png", 5, this.getId(),"u");
                        Shoot down = new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5, this.getAttackDamage(), "onda-export.png", 5, this.getId(),"d");

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


