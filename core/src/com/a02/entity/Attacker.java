package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

import static com.a02.game.Utils.*;

public class Attacker extends GameObject {
    /**
     * Indica si el objeto concreto está seleccionado
     */
    private boolean isSelected;

    private float attackDamage;
    private String attackType;
    private double angle = 0;
    private final static Logger logger = Logger.getLogger(GameObject.class.getName());
    private enum State {
        IDLE, ATTACKING
    }
    private int timer = 60;

    State state;

    public Attacker(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp, String attackType, float attackDamage) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.attackType = attackType;
        this.attackDamage = attackDamage;
        this.state = State.IDLE;
        this.isSelected = false;
        loadTextures();
    }

    public void loadTextures(){
        switch (this.getId()){
            case 0: //Electricidad
                this.setTexture(new Texture(Gdx.files.internal("electricity.png")));
                this.setAnimation(createAnimation("electricity-Sheet.png", 2, 1, 0.1f));
                break;
            case 1: //Thwomp
                this.setTexture(new Texture(Gdx.files.internal("maquinaDeMatar.png")));
                this.setAnimation(createAnimation("willy-Sheet.png", 2, 2, 0.1f));
                break;
            case 2: //Ballesta/disparos
                this.setTexture(new Texture(Gdx.files.internal("crossbow.png")));
                break;
            case 3: //Waves
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
        this.isSelected = false;
        loadTextures();
    }

    public Attacker(Attacker other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.attackType = other.getAttackType();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
        this.isSelected = false;
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

    public double getAngle() {
        return angle;
    }

    public void update(GameScreen gs) {
        this.hpBar.update(this, this.getHp());
        if (this.isInInventory(gs)) return;
        switch (this.getId()) {
            case 0: //Electricidad
                if (this.overlappedEnemy(gs) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs);
                    if (tempEnemy.getHp() > 0 && gs.secTimer % 60 == 0) { //Golpea 1 vez/segundo
                        tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                    }
                }
                break;

            case 1: //Thwomp
                if (this.overlappedEnemy(gs) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs);
                    if (tempEnemy.getHp() > 0 && gs.secTimer % 15 == 0) { //Golpea 4 veces/segundo
                        tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackDamage));
                    }
                }
                break;

            case 2: //Ballesta
                //Rotación
                if (this.overlappedArea(gs) != null && !this.isInInventory(gs)) {
                    this.angle = ((Math.atan2(this.getY() - overlappedArea(gs).getY(),
                            this.getX() - overlappedArea(gs).getX()) * 180) / Math.PI + 90);
                }

                //Ataque
                if (gs.secTimer % 60 == 0 && !this.isSelected && this.overlappedArea(gs) != null && !this.isGrabbed()) { //Ataque cada segundo
                    gs.shots.add(new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 2,
                            this.getAttackDamage(), "shoot.png", 5,"n", 0, this.angle));
                    gs.soundPlayer.playArrow();
                }
                else if (this.isSelected) { //Ataque seleccionado
                    this.timer++;

                    Vector3 focus = getRelativeMousePos();
                    this.angle = ((Math.atan2(this.getY() - focus.y,
                            this.getX() - focus.x) * 180) / Math.PI + 90);;

                    if ((Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)) && this.timer > 60) {
                        double angle = ((Math.atan2(this.getY() - focus.y, this.getX() - focus.x) * 180) / Math.PI + 90);

                        gs.shots.add(new Shoot(this.getX() + 8, this.getY() + 9, 2, 2, 2,
                                this.getAttackDamage(), "shoot.png", 5,"n", 0, angle));
                        gs.soundPlayer.playArrow();
                        this.timer = 0;
                    }
                }

                //Selección
                Vector3 mousePos = getRelativeMousePos();
                if (this.overlapsPoint(mousePos.x, mousePos.y) && mouseJustClicked()) {
                    //Sólo se puede tomar control si no se está comprando ni eliminando
                    if (gs.state == GameScreen.State.PLAYING && !this.isSelected) {
                        gs.state = GameScreen.State.SELECTING;
                        this.isSelected = true;
                    }
                    else if (gs.state == GameScreen.State.SELECTING && this.isSelected) {
                        gs.state = GameScreen.State.PLAYING;
                        this.isSelected = false;
                    }
                }
                break;
            case 3: //Waves
                if (gs.secTimer % 120 == 0 && !this.isInInventory(gs)) { //Cada 2 segundos crea 4 disparos en cada una de las direcciones
                    gs.shots.add(new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5,
                            this.getAttackDamage(), "onda-export.png", 5,"r", 1));
                    gs.shots.add(new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5,
                            this.getAttackDamage(), "onda-export.png", 5,"l", 1));
                    gs.shots.add(new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5,
                            this.getAttackDamage(), "onda-export.png", 5,"u", 1));
                    gs.shots.add(new Shoot(this.getX()-8, this.getY()-9, 14, 14, 5,
                            this.getAttackDamage(), "onda-export.png", 5,"d", 1));
                }
                break;
        }
    }

    protected Enemy overlappedArea(GameScreen gs) { //TODO: no selecciona por proximidad, sino por primer enemigo encontrado
        for (Enemy enemy : gs.enemies) {
            if ((enemy.getX() < this.getX() + 50 && enemy.getX() > this.getX() - 50)
                    && (enemy.getY() < this.getY() + 50 && enemy.getY() > this.getY() - 50)) {
                return enemy;
            }
        }
        return null;
    }
}


