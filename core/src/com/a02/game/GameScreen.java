package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    SpriteBatch batch;
    Texture imgL;
    Texture imgBox;
    Enemy larry;
    GameObject beacon;
    GameObject box;
    Inventory inventory;
    Texture imgB;
    Texture inventoryTexture;

    public GameScreen() {
        batch = new SpriteBatch();
        //TODO: Sprite debería ser un Texture o un Animation
        larry = new Enemy(20, 100, 16, 16, "Test2.png", 1, "Larry", 200, 1, 1);
        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        beacon= new GameObject(20,50,16,16,"beacon.png",0,"Beacon", 1000, true, 1000);
        box= new GameObject(260,140,12,12,"Test1.png",0,"Box", 1000, true, 1000);

        imgBox=new Texture(Gdx.files.internal(box.getSprite()));
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));

        inventory = new Inventory();
        inventoryTexture = new Texture(Gdx.files.internal(inventory.getSprite()));
        inventory.insert(box);
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
        batch.draw(imgBox, box.getX(), box.getY());
        batch.end();
        box.buy();
        larry.move(beacon.getX(), beacon.getY());
        larry.attack(beacon);
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
