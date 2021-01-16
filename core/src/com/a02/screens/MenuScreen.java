package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuScreen implements Screen {
    final MainGame game;

    private boolean introTimer; //Para poder hacer click en el botón de "Menu" en GameScreen sin tocar el de "Round 2"

    private final UIButton playButton; //Para iniciar el modo de rondas normal
    private final UIButton infiniteButton; //Para iniciar rondas infinitas
    private final UIButton testingButton; //Para iniciar el modo practica
    private final UIButton quitButton;  //Para salir

    private final Texture backgroundTexture; //Textura del fondo del menu

    private final OrthographicCamera camera; //Para determinar el tamaño base
    private final static Logger logger = Logger.getLogger(MenuScreen.class.getName()); //Para informar de como va progresando

    public MenuScreen(MainGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 180); //Crear tamanaño del juego

        playButton = new UIButton(160, 194, 74, 36,                                         //Asigancion de valores por defecto
                "Buttons/playButtonIdle.png", "Buttons/playButtonPressed.png");
        infiniteButton = new UIButton(240, 194, 74, 36,
                "Buttons/infiniteButtonIdle.png", "Buttons/infiniteButtonPressed.png");
        testingButton = new UIButton(160, -52, 74, 36,
                "Buttons/testingButtonIdle.png", "Buttons/testingButtonPressed.png");
        quitButton = new UIButton(240, -52, 74, 36,
                "Buttons/quitButtonIdle.png", "Buttons/quitButtonPressed.png");

        backgroundTexture = new Texture(Gdx.files.internal("wallpaperTest.png"));

        introTimer = false;

        Pixmap pm = new Pixmap(Gdx.files.internal("defaultCursor.png"));        //Asignar el cursor por defecto
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        Logger.getLogger("").setLevel(Level.INFO);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.INFO); //Informe de estado de menu del logger
        logger.info("Inicio del MenuScreen");
    }

    @Override
    public void render(float delta) {
        if (Settings.s.isTutorialCheck()) game.setScreen(new GameScreen(game, 0, 0)); //Ronda 0 si se pide ejecutar tutorial

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playButton.setY((float) (playButton.getY() + (94 - playButton.getY()) * 0.08));
        infiniteButton.setY((float) (infiniteButton.getY() + (94 - infiniteButton.getY()) * 0.08));
        testingButton.setY((float) (testingButton.getY() + (52 - testingButton.getY()) * 0.08));
        quitButton.setY((float) (quitButton.getY() + (52 - quitButton.getY()) * 0.08));

        camera.update();
        game.entityBatch.setProjectionMatrix(camera.combined);

        game.entityBatch.begin();   //Dibujado de botones y texturas
        game.entityBatch.draw(backgroundTexture,0,0);
        game.entityBatch.draw(playButton.getCurrentTexture(),playButton.getX(),playButton.getY());
        game.entityBatch.draw(infiniteButton.getCurrentTexture(),infiniteButton.getX(),infiniteButton.getY());
        game.entityBatch.draw(testingButton.getCurrentTexture(),testingButton.getX(),testingButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(),quitButton.getX(),quitButton.getY());
        game.entityBatch.end();

        updateButtonLogic(); //Cambiar el estado de los botones
        introTimer = true;
    }

    private void updateButtonLogic() {
        //Aspecto, cambia de un sprite a otro
        playButton.updateTouched();
        infiniteButton.updateTouched();
        testingButton.updateTouched();
        quitButton.updateTouched();

        //Lógica
        if (playButton.isJustClicked() && introTimer) {
            game.setScreen(new StoryScreen(game, 1, 0)); //Modo de rondas normal comienza en ronda 1 y va avanzando al superarlas
        }
        if (infiniteButton.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, -1, 0)); //Modo infinito (ronda -1)
        }
        if (testingButton.isJustClicked() && introTimer) {
            game.setScreen(new GameScreen(game, -2, 0)); //Modo testing (ronda -2)
        }
        else if (quitButton.isJustClicked() && introTimer) { //Cerrar la aplicacion
            Gdx.app.exit();
            System.exit(0);
        }
    }

    @Override
    public void dispose() {         //Limpiado de lo de la screen
        playButton.disposeButton();
        infiniteButton.disposeButton();
        testingButton.disposeButton();
        quitButton.disposeButton();
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
