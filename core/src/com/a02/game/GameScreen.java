package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    SpriteBatch batch;
    Texture imgL;
    Enemy larry;
    GameObject beacon;
    Inventory inventory;
    Texture imgB;
    Texture inventoryTexture;

    public GameScreen() {
        batch = new SpriteBatch();
        //TODO: Sprite debería ser un Texture o un Animation
        larry = new Enemy(200, 100, 16, 16, "Test2.png", 1, "Larry", 200, 1, 1);
        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        beacon= new GameObject(20,50,16,16,"beacon.png",0,"Beacon", 1000, true, 1000);
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));
        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(imgB, beacon.getX(), beacon.getY());
        batch.draw(inventoryTexture, inventory.getX(), inventory.getY());
        batch.end();

        larry.move(beacon.getX(), beacon.getY());
        larry.attack(beacon);
        System.out.println(beacon.getHp());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        imgL.dispose();
        imgB.dispose();
    }
}
