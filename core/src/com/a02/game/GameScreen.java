package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    SpriteBatch batch;
    Texture imgL;
    TestEnemy larry;
    GameObject beacon;
    Texture imgB;


    public GameScreen() {
        batch = new SpriteBatch();
        //TODO: Sprite debería ser un Texture o un Animation
        larry = new TestEnemy(1, "Larry", 200, 100, "Test2.png", 100, 5, 2);
        imgL = new Texture(Gdx.files.internal(larry.getSprite()));
        beacon= new GameObject(0,"BEACON", 20,50,"beacon.png",0,true, 10);
        imgB= new Texture(Gdx.files.internal(beacon.getSprite()));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(imgL, larry.getX(), larry.getY());
        batch.draw(imgB, beacon.getX(), beacon.getY());
        larry.move(beacon.getX(), beacon.getY());
        batch.end();

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
