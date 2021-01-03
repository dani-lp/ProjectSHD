package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

public class Shoot extends Entity {
    private double angle = 0;
    private int type; //0 para ballesta, 1 para waves
    private float speed;
    private float attackdamage;
    private Texture texture;
    private int hp;
    State state;
    private int att_id;
    private String dir;
    Vector3 focus = new Vector3();
    private static Logger logger = Logger.getLogger(Shoot.class.getName());

    public Shoot(float x, float y, int height, int width, int speed, float attackdamage, String sprite, int hp,
                 int att_id, String dir, int type, double angle) { //Disparo ballesta
        this.setX(x);
        this.setY(y);
        this.hp = hp;
        this.speed = speed;
        this.attackdamage = attackdamage;
        this.texture = new Texture(sprite);
        this.state = State.IDLE;
        this.setHeight(height);
        this.setWidth(width);
        this.att_id = att_id;
        this.dir = dir;
        this.type = type;
        this.angle = calcAngle(angle);
    }

    public Shoot(float x, float y, int height, int width, int speed, float attackdamage, String sprite, int hp,
                 int att_id, String dir, int type) { //Disparo waves
        this.setX(x);
        this.setY(y);
        this.hp = hp;
        this.speed = speed;
        this.attackdamage = attackdamage;
        this.texture = new Texture(sprite);
        this.state = State.IDLE;
        this.setHeight(height);
        this.setWidth(width);
        this.att_id = att_id;
        this.dir = dir;
        this.type = type;
        this.angle = calcAngle(0);
    }

    protected enum State {
        ATTACKING,IMPACT,IDLE
    }

    private double calcAngle(double angle) {
        if (this.type == 0) {
            return angle;
        }
        else if (this.type == 1) {
            switch (this.getDir()) {
                case "r":
                    return ((Math.atan2(0.0f, this.getX() - 350) * 180) / Math.PI + 90);
                case "l":
                    return ((Math.atan2(0.0f, this.getX() - -100) * 180) / Math.PI + 90);
                case "u":
                    return ((Math.atan2(this.getY() - 180, 0.0f) * 180) / Math.PI + 90);
                case "d":
                    return ((Math.atan2(this.getY() - -50, 0.0f) * 180) / Math.PI + 90);
            }
        }
        return 18;
    }

    /*
    public void update2(GameScreen gs) {
        label:
        switch (this.state) {
            case IDLE:
                if (this.att_id == 2){
                    Attacker boss = null;
                    for (GameObject att:gs.objects) {
                        if (att instanceof Attacker && att.isSelected()){
                            boss = (Attacker) att;
                        }
                    }
                    if ((gs.state == GameScreen.State.SELECTING) && boss.getX() == this.getX() && boss.getY() == this.getY()){
                        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                            this.focus = getRelativeMousePos();
                            angle = ((Math.atan2(this.getY() - focus.y, this.getX() - focus.x) * 180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break;
                        }
                    } else {
                        Enemy target = this.overlappedArea(gs);
                        if (target != null) {
                            angle = ((Math.atan2(this.getY() - overlappedArea(gs).getY(), this.getX() - overlappedArea(gs).getX())*180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break;
                        }
                        break;
                    }
                }
                if (this.att_id == 3){
                    switch (this.getDir()) {
                        case "r":
                            angle = ((Math.atan2(0.0f, this.getX() - 350) * 180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break label;
                        case "l":
                            angle = ((Math.atan2(0.0f, this.getX() + 100) * 180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break label;
                        case "u":
                            angle = ((Math.atan2(this.getY() - 180, 0.0f) * 180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break label;
                        case "d":
                            angle = ((Math.atan2(this.getY() + 50, 0.0f) * 180) / Math.PI + 90);
                            this.state = State.ATTACKING;
                            break label;
                    }
                }
                break;

            case ATTACKING:
                this.move();
                if (this.overlappedEnemy(gs) != null) {
                    this.state = State.IMPACT;
                }
                if (this.getX() < 0 || this.getY() < 0 || this.getX() > 320 || this.getY() > 180){
                    this.setHp(0);
                }
                break;

            case IMPACT:
                if (this.overlappedEnemy(gs) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs);
                    tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackdamage));
                    if (this.getDir().equals("n")){
                        this.setHp(0);
                    } else {
                        this.state = State.ATTACKING;
                    }
                    logger.info("Enemigo impactado por disparo");
                }
                break;
        }
    }

     */

    public void update(GameScreen gs) {
        //Movimiento
        this.move();

        //Ataque
        if (this.overlappedEnemy(gs) != null) {
            Enemy tempEnemy = this.overlappedEnemy(gs);
            tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackdamage));
            if (this.type == 0) this.setHp(0);
        }

        //Borrado
        if (this.getX() < 0 || this.getY() < 0 || this.getX() > 320 || this.getY() > 180){
            this.setHp(0);
        }
    }

    public void move() {
        this.setX((float) (this.getX() + Math.cos(Math.toRadians(angle + 90)) *  this.speed));
        this.setY((float) (this.getY() + Math.sin(Math.toRadians(angle + 90)) *  this.speed));
    }

    /**
     * Devuelve el enemigo colisionado por la bala.
     * @param gs GameScreen de juego
     * @return Enemigo colisionado
     */
    protected Enemy overlappedEnemy(GameScreen gs) {
        for (Enemy enemy : gs.enemies) {
            if (this.getX() < enemy.getX() + enemy.getWidth() && this.getX() + this.getWidth() > enemy.getX() &&
                    this.getY() < enemy.getY() + enemy.getHeight() && this.getY() + this.getHeight() > enemy.getY()) {
                return enemy;
            }
        }
        return null;
    }
/*
    protected Enemy overlappedArea(GameScreen gs) {
        for (Enemy enemy : gs.enemies) {
            if ((enemy.getX() < this.getX() + 50 && enemy.getX() > this.getX() - 50) && (enemy.getY() < this.getY() + 50
                    && enemy.getY() > this.getY() - 50)) {
                return enemy;
            }
        }
        return null;
    }

 */

    public float getSpeed() {
        return speed;
    }

    public float getAttackdamage() { return attackdamage; }

    public Texture getTexture() {
        return texture;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getDir() {
        return dir;
    }

    @Override
    public String toString() {
        return "Shoot{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", speed=" + speed +
                '}';
    }
}
