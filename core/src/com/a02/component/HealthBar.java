package com.a02.component;

import com.a02.entity.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Barra de vida asociada a las entidades. 2 rectángulos, uno de ellos varía de tamaño.
 */
public class HealthBar {
    private float x;
    private float y;
    private int maxHP;
    private int currentHP;
    private final Texture foreground = new Texture(Gdx.files.internal("hpBarFront.png"));
    private final Texture background = new Texture(Gdx.files.internal("hpBarBack.png"));

    public HealthBar(Entity entity, int maxHP) {
        this.x = entity.getX() + 1;
        this.y = entity.getY() + entity.getHeight() + 1; //Posición encima de la entidad
        this.maxHP = maxHP;
    }

    public void update(Entity entity, int hp) {
        this.x = entity.getX() + 1;
        this.y = entity.getY() + entity.getHeight() + 1;
        this.currentHP = hp;
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

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public int getCurrentWidth() {
        return (int) ((this.currentHP * 14) / this.maxHP);
    }

    public Texture getForeground() {
        return foreground;
    }

    public Texture getBackground() {
        return background;
    }
}
