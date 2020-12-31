package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScreen implements Screen {

    final MainGame game;
    public boolean tutorial;
    private UIButton round1Button;
    private UIButton round2Button;
    private UIButton quitButton;

    private boolean introTimer; //Para poder hacer click en el botón de "Menu" en GameScreen sin tocar el de "Round 2"

    private OrthographicCamera camera;
    private static Logger logger = Logger.getLogger(MenuScreen.class.getName());

    public MenuScreen(MainGame game, boolean tutorial) {
        this.game = game;
        this.tutorial=tutorial;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);

        round1Button = new UIButton(123, 113, 74, 36, "Buttons/round1ButtonIdle.png");
        round2Button = new UIButton(123, 73, 74, 36, "Buttons/round2ButtonIdle.png");
        quitButton = new UIButton(123, 33, 74, 36, "Buttons/quitButtonIdle.png");

        introTimer = false;

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

        if (this.tutorial){
            game.setScreen(new GameScreen(game, 0));
        }

        game.entityBatch.begin();
        game.entityBatch.draw(new Texture(Gdx.files.internal("wallpaperTest.png")),0,0);
        game.entityBatch.draw(round1Button.getTexture(), round1Button.getX(),round1Button.getY());
        game.entityBatch.draw(round2Button.getTexture(), round2Button.getX(),round2Button.getY());
        game.entityBatch.draw(quitButton.getTexture(),quitButton.getX(),quitButton.getY());
        game.entityBatch.end();

        updateButtonLogic();
        introTimer = true;
    }

    private void updateButtonLogic() {
        //Aspecto
        if (round1Button.isTouched()) round1Button.setTexture(new Texture("Buttons/round1ButtonPressed.png"));
        else round1Button.setTexture(new Texture("Buttons/round1ButtonIdle.png"));

        if (round2Button.isTouched()) round2Button.setTexture(new Texture("Buttons/round2ButtonPressed.png"));
        else round2Button.setTexture(new Texture("Buttons/round2ButtonIdle.png"));

        if (quitButton.isTouched()) quitButton.setTexture(new Texture("Buttons/quitButtonPressed.png"));
        else quitButton.setTexture(new Texture("Buttons/quitButtonIdle.png"));

        //Lógica
        if (round1Button.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, 1));
        }
        else if (round2Button.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, 2));
        }
        else if (quitButton.isJustClicked() && introTimer) {
            Gdx.app.exit();
            System.exit(0);
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
