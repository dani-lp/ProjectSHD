package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScreen implements Screen {
    final MainGame game;

    private boolean introTimer; //Para poder hacer click en el botón de "Menu" en GameScreen sin tocar el de "Round 2"

    private final UIButton playButton;
    private final UIButton infiniteButton;
    private final UIButton testingButton;
    private final UIButton quitButton;

    private final UIButton githubButton;

    private final Texture backgroundTexture;

    private final OrthographicCamera camera;
    private final static Logger logger = Logger.getLogger(MenuScreen.class.getName());

    public MenuScreen(MainGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180);

        playButton = new UIButton(160, 194, 74, 36, "Buttons/playButtonIdle.png");
        infiniteButton = new UIButton(340, 94, 74, 36, "Buttons/infiniteButtonIdle.png");
        testingButton = new UIButton(160, -52, 74, 36, "Buttons/testingButtonIdle.png");
        quitButton = new UIButton(340, 52, 74, 36, "Buttons/quitButtonIdle.png");

        githubButton = new UIButton(1, 1, 26, 29, "Buttons/github.png");

        backgroundTexture = new Texture(Gdx.files.internal("wallpaperTest.png"));

        introTimer = false;

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO);
        logger.info("Inicio del MenuScreen");
    }

    @Override
    public void render(float delta) {
        if (Settings.s.isTutorialCheck()) game.setScreen(new GameScreen(game, 0));

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playButton.setY((float) (playButton.getY() + (94 - playButton.getY()) * 0.08));
        infiniteButton.setX((float) (infiniteButton.getX() + (240 - infiniteButton.getX()) * 0.08));
        testingButton.setY((float) (testingButton.getY() + (52 - testingButton.getY()) * 0.08));
        quitButton.setX((float) (quitButton.getX() + (240 - quitButton.getX()) * 0.08));

        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        game.entityBatch.begin();
        game.entityBatch.draw(backgroundTexture,0,0);
        game.entityBatch.draw(playButton.getTexture(),playButton.getX(),playButton.getY());
        game.entityBatch.draw(infiniteButton.getTexture(),infiniteButton.getX(),infiniteButton.getY());
        game.entityBatch.draw(testingButton.getTexture(),testingButton.getX(),testingButton.getY());
        game.entityBatch.draw(quitButton.getTexture(),quitButton.getX(),quitButton.getY());
        game.entityBatch.draw(githubButton.getTexture(), githubButton.getX(), githubButton.getY());
        game.entityBatch.end();

        updateButtonLogic();
        introTimer = true;
    }

    private void updateButtonLogic() {
        //Aspecto
        if (playButton.isTouched()) playButton.setTexture(new Texture("Buttons/playButtonPressed.png"));
        else playButton.setTexture(new Texture("Buttons/playButtonIdle.png"));

        if (infiniteButton.isTouched()) infiniteButton.setTexture(new Texture("Buttons/infiniteButtonPressed.png"));
        else infiniteButton.setTexture(new Texture("Buttons/infiniteButtonIdle.png"));

        if (testingButton.isTouched()) testingButton.setTexture(new Texture("Buttons/testingButtonPressed.png"));
        else testingButton.setTexture(new Texture("Buttons/testingButtonIdle.png"));

        if (quitButton.isTouched()) quitButton.setTexture(new Texture("Buttons/quitButtonPressed.png"));
        else quitButton.setTexture(new Texture("Buttons/quitButtonIdle.png"));

        if (githubButton.isTouched()) githubButton.setTexture(new Texture("Buttons/githubTouched.png"));
        else githubButton.setTexture(new Texture("Buttons/github.png"));

        //Lógica
        if (playButton.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, 1));
        }
        if (infiniteButton.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, -1)); //Modo infinito (ronda -1)
        }
        if (testingButton.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, -2)); //Modo testing (ronda -2)
        }
        else if (quitButton.isJustClicked() && introTimer) {
            Gdx.app.exit();
            System.exit(0);
        }
        else if (githubButton.isJustClicked() && introTimer) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("https://github.com/Dzl17/ProjectSHD.git"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dispose() {
        playButton.getTexture().dispose();
        infiniteButton.getTexture().dispose();
        testingButton.getTexture().dispose();
        quitButton.getTexture().dispose();
        githubButton.getTexture().dispose();
        backgroundTexture.dispose();
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
    public void show() {

    }
}
