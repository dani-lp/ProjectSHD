package com.a02.screens;

import com.a02.game.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.a02.game.MainGame.mainGameScreen;
import static com.a02.game.Utils.getRelativeMousePos;

public class MenuScreen implements Screen {

    public static int ronda;
    final MainGame game;

    private OrthographicCamera camera;
    private static Logger logger = Logger.getLogger(MenuScreen.class.getName());
    public MenuScreen(MainGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del MenuScreen");
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
        game.entityBatch.draw(new Texture(Gdx.files.internal("wallpaperTest.png")),0,0);
        game.entityBatch.draw(new Texture(Gdx.files.internal("Boton1.png")),120,110);
        game.entityBatch.draw(new Texture(Gdx.files.internal("Boton2.png")),120,50);
        game.entityBatch.end();

        Vector3 mousePos = getRelativeMousePos();
        if (mousePos.x <=210 & mousePos.x >= 120 ) {
            if (110 <= mousePos.y && 137 >= mousePos.y && Gdx.input.isTouched()){
                ronda=1;
                game.setScreen(new GameScreen(game));
            } else if (51 <= mousePos.y && 77 >= mousePos.y && Gdx.input.isTouched()){
                ronda=2;
                game.setScreen(new GameScreen(game));
            }
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
