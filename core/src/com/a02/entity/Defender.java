package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Defender extends GameObject {
    public Defender(int id, String type, int price,
                    boolean unlocked, int hp) {
        super(id, type, price, unlocked, hp);

        switch (this.getId()){
            case 0: //Beacon
                this.setTexture(new Texture(Gdx.files.internal("beacon.png")));
                break;
            case 1: //Wall
                this.setTexture(new Texture(Gdx.files.internal("wall.png")));
        }
    }

    public Defender(int id, String type, int price, boolean unlocked, int hp, float x, float y) {
        super(id, type, price, unlocked, hp, x, y);

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
        super(other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setWidth(16);
        this.setHeight(18);
        this.setTexture(other.getTexture());
    }

    public void update(List<GameObject> objects, List<Enemy> enemies, float secTimer) {
        this.hpBar.update(this, this.getHp());
    };
}
