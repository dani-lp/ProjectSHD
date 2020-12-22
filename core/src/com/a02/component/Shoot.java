package com.a02.component;

import com.a02.entity.Attacker;
import com.a02.entity.Enemy;
import com.a02.entity.GameObject;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import sun.jvm.hotspot.gc.shared.Space;

import java.util.logging.Logger;

import static com.a02.game.Utils.getRelativeMousePos;

public class Shoot {

    private float x;
    private float y;
    private float speed;
    private float attackdamage;
    private int height;
    private int width;
    private String sprite;
    private int hp;
    State state;
    private static Logger logger = Logger.getLogger(Shoot.class.getName());

    public Shoot(float x, float y, int height, int width,int speed, float attackdamage,String sprite,int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.attackdamage = attackdamage;
        this.sprite = sprite;
        this.state = State.IDLE;
        this.height = height;
        this.width = width;
    }
    private enum State {
        ATTACKING,IMPACT,IDLE
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAttackdamage() { return attackdamage; }

    public void setAttackdamage(float attackdamage) { this.attackdamage = attackdamage; }

    public String getSprite() { return sprite; }

    public void setSprite(String sprite) { this.sprite = sprite; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "Shoot{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                '}';
    }

    Vector3 focus = new Vector3();

    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                    focus = getRelativeMousePos();
                    this.state = State.ATTACKING;

                }
                break;

            case ATTACKING:
                this.move(focus.x, focus.y);
                if (this.overlappedEnemy(gs) != null) {
                    this.state = State.IMPACT;
                }
                break;

            case IMPACT:
                if (this.overlappedEnemy(gs) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs);
                    tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackdamage));
                    this.setHp(0);
                    logger.info("Enemigo impactado por disparo");
                }
                break;
        }
    }

    public void move(float targx, float targy) {
            double angle = Math.toDegrees(-Math.atan((this.getY() - targy) / (this.getX() - targx)));
            this.setX((float) (this.getX() + Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed));
            this.setY((float) (this.getY() + Math.cos(angle) * Gdx.graphics.getDeltaTime() * this.speed));
    }

    protected Enemy overlappedEnemy(GameScreen gs) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : gs.enemies) {
            if (this.getX() < enemy.getX() + enemy.getWidth() && this.getX() + this.getWidth() > enemy.getX() &&
                    this.getY() < enemy.getY() + enemy.getHeight() && this.getY() + this.getHeight() > enemy.getY()) {
                return enemy;
            }
        }
        return null;
    }
}
