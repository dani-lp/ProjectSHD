package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Defender extends GameObject {
    public Defender(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp) {
        super(x, y, width, height, id, type, price, unlocked, hp);

        switch (this.getId()){
            case 0: //Beacon
                this.setTexture(new Texture(Gdx.files.internal("beacon.png")));
                break;
            case 1: //Wall
                this.setTexture(new Texture(Gdx.files.internal("wall.png")));
        }
    }

    public Defender() {
        super();
    }

    public Defender(Defender other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(),
                other.getPrice(), other.isUnlocked(), other.getHp());
        this.setTexture(other.getTexture());
    }

    public void update(GameScreen gs) {
        this.hpBar.update(this, this.getHp());
    };
}
