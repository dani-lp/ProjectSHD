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
