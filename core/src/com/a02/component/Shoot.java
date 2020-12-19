package com.a02.component;

import com.a02.entity.Attacker;
import com.a02.entity.Enemy;
import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static com.a02.game.Utils.getRelativeMousePos;

public class Shoot {

    private float x;
    private float y;
    private float speed;
    private float attackdamage;
    private String sprite;
    State state;

    public Shoot(float x, float y, int speed, float attackdamage,String sprite) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.attackdamage=attackdamage;
        this.sprite=sprite;
        this.state = State.IDLE;
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

    @Override
    public String toString() {
        return "Shoot{" +
                "x=" + x +
                ", y=" + y +
                ", speed=" + speed +
                '}';
    }

    Vector3 focus=new Vector3();
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (Gdx.input.isTouched()){
                    focus=getRelativeMousePos();
                    this.state = State.ATTACKING;

                }
                break;

            case ATTACKING:
                this.move(focus.x, focus.y);
                break;
        }
    }

    public void move(float targx, float targy) {
            double angle = Math.toDegrees(-Math.atan((this.getY() - targy) / (this.getX() - targx)));
            this.setX((float) (this.getX() + Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed));
            this.setY((float) (this.getY() + Math.cos(angle) * Gdx.graphics.getDeltaTime() * this.speed));
    }
}
