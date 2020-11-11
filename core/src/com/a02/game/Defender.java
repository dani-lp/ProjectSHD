package com.a02.game;

import java.util.List;

public class Defender extends GameObject {
    public Defender(float x, float y, int width, int height, String sprite, int id, String name, String type, int price,
                    boolean unlocked, int hp) {
        super(x, y, width, height, sprite, id, name, type, price, unlocked, hp);
    }

    public Defender() {
        super();
    }

    public Defender(Defender other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getSprite(), other.getId(),
                other.getName(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
    }

    public void update(List<GameObject> objects, List<Enemy> enemies, float secTimer) {

    };
}
