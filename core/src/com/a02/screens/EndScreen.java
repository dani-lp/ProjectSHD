package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;


public class EndScreen implements Screen {
    MainGame game;
    private final UIButton menuButton; //Boton de menu
    private final UIButton quitButton; //Boton de salir
    private final Texture endScreenTexture; //Textura del fondo

    public EndScreen(final String username, final int points, MainGame game) {
        //Guarda los datos desde un hilo
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.saveMaxScore(username, points);
            }
        });
        t.start();

        this.game = game;

        Pixmap pm = new Pixmap(Gdx.files.internal("defaultCursor.png")); //Cambia el cursor al default
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        menuButton = new UIButton(73,-150,74,36, //Creacion de boton menu
                "Buttons/menuButtonIdle.png","Buttons/menuButtonPressed.png");
        quitButton = new UIButton(173,-220,74,36, //Creacion de boton salir
                "Buttons/quitButtonIdle.png","Buttons/quitButtonPressed.png");
        endScreenTexture = new Texture("endScreen.png");
    }

    @Override
    public void render(float delta) {

        updateButtonLogic();

        menuButton.setY((float) (menuButton.getY() + (70 - menuButton.getY())*0.04));   //posicionar
        quitButton.setY((float) (quitButton.getY() + (70 - quitButton.getY())*0.04));

        game.entityBatch.begin();                               //Dibujado de botones y fondo
        game.entityBatch.draw(endScreenTexture,0,0);
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        menuButton.updateTouched();
        quitButton.updateTouched();

        if (menuButton.isJustClicked()) {   //Cambiar de screen
            game.setScreen(new MenuScreen(game));
        }
        else if (quitButton.isJustClicked()) { //cerrar
            Gdx.app.exit();
            System.exit(0);
        }
    }


    @Override
    public void dispose() {         //limpiar lo de la screen
        menuButton.disposeButton();
        quitButton.disposeButton();
        endScreenTexture.dispose();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

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
}
