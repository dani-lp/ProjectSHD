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

import javax.annotation.processing.SupportedSourceVersion;
import java.util.logging.Logger;

import static com.a02.game.Utils.getRelativeMousePos;

public class Shoot {
    private double angle=0;
    private float x;
    private float y;
    private float speed;
    private float attackdamage;
    private int height;
    private int width;
    private String sprite;
    private int hp;
    State state;
    private int att_id;
    private String dir;
    private static Logger logger = Logger.getLogger(Shoot.class.getName());

    public Shoot(float x, float y, int height, int width,int speed, float attackdamage,String sprite,int hp,int att_id,String dir) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.attackdamage = attackdamage;
        this.sprite = sprite;
        this.state = State.IDLE;
        this.height = height;
        this.width = width;
        this.att_id=att_id;
        this.dir=dir;
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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
                if (this.att_id == 2){
                    Attacker boss=null;
                    for (GameObject att:gs.objects) {
                        if (att instanceof Attacker && ((Attacker) att).isSelected()){
                            boss= (Attacker) att;
                        }
                    }
                    if (Attacker.selected && boss.getX() == this.getX() && boss.getY() == this.getY()){
                        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                            focus = getRelativeMousePos();
                            angle = ((Math.atan2(this.getY() - focus.y, this.getX() - focus.x)*180) / Math.PI + 90);
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
                    if (this.getDir().equals("r")){
                        angle = ((Math.atan2(this.getY() - this.getY(), this.getX() - 350)*180) / Math.PI + 90);
                        this.state = State.ATTACKING;
                        break;
                    }
                    else if (this.getDir().equals("l")) {
                        angle = ((Math.atan2(this.getY() - this.getY(), this.getX() - -100)*180) / Math.PI + 90);
                        this.state = State.ATTACKING;
                        break;
                    }
                    else if (this.getDir().equals("u")) {
                        angle = ((Math.atan2(this.getY() - 180, this.getX() - this.getX())*180) / Math.PI + 90);
                        this.state = State.ATTACKING;
                        break;
                    }
                    else if (this.getDir().equals("d")) {
                        angle = ((Math.atan2(this.getY() - -50, this.getX() - this.getX())*180) / Math.PI + 90);
                        this.state = State.ATTACKING;
                        break;
                    }
                }
                break;

            case ATTACKING:
                this.move();
                if (this.overlappedEnemy(gs) != null) {
                    this.state = State.IMPACT;
                }
                if (this.getX()<0 || this.getY()<0 || this.getX()>300 || this.getY()>180){
                    this.setHp(0);
                }
                break;

            case IMPACT:
                if (this.overlappedEnemy(gs) != null) {
                    Enemy tempEnemy = this.overlappedEnemy(gs);
                    tempEnemy.setHp((int) (tempEnemy.getHp() - this.attackdamage));
                    if (this.getDir().equals("n")){
                        this.setHp(0);
                    } else{
                        this.state=State.ATTACKING;
                    }
                    logger.info("Enemigo impactado por disparo");
                }
                break;
        }
    }

    public void move() {
        this.setX((float) (this.getX() + Math.cos(Math.toRadians(angle + 90)) *  this.speed));
        this.setY((float) (this.getY() + Math.sin(Math.toRadians(angle + 90)) *  this.speed));
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

    protected Enemy overlappedArea(GameScreen gs) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        for (Enemy enemy : gs.enemies) {
            if ((enemy.getX() < this.getX() + 50 && enemy.getX() > this.getX() - 50) && (enemy.getY() < this.getY() + 50 && enemy.getY() > this.getY() - 50)) {
                return enemy;
            }
        }
        return null;
    }
}
