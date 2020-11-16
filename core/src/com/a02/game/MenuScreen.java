package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

import static com.a02.game.MainGame.mainGameScreen;

public class MenuScreen implements Screen {

    final MainGame game;

    private OrthographicCamera camera;

    public MenuScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        game.entityBatch.begin();
        game.entityBatch.draw(new Texture(Gdx.files.internal("wallpaper.png")),0,0);
        game.entityBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }
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

    }
}
