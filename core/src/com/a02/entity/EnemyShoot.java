package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.logging.Logger;

public class EnemyShoot extends Entity{
    private double angle = 0;
    private float speed;
    private float attackdamage;
    private Texture texture;
    private int hp;
    Shoot.State state;
    private String dir;
    Vector3 focus = new Vector3();
    private static Logger logger = Logger.getLogger(Shoot.class.getName());

    public EnemyShoot(float x, float y, int height, int width, int speed, float attackdamage, String sprite, int hp,
                  double angle) {
        this.setX(x);
        this.setY(y);
        this.hp = hp;
        this.speed = speed;
        this.attackdamage = attackdamage;
        this.texture = new Texture(sprite);
        this.state = Shoot.State.IDLE;
        this.setHeight(height);
        this.setWidth(width);
        this.dir = dir;
        this.angle = angle;
    }

    public void update(GameScreen gs) {
        //Movimiento
        this.move();

        //Ataque
        if (this.overlappedObject(gs) != null) {
            GameObject tempObj = this.overlappedObject(gs);
            tempObj.setHp((int) (tempObj.getHp() - this.attackdamage));
            this.setHp(0);
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
    protected GameObject overlappedObject(GameScreen gs) {
        for (GameObject object: gs.objects) {
            if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY() &&
                    !object.isInInventory(gs)) {
                return object;
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
