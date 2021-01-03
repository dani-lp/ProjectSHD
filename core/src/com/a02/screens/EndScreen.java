package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.users.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;
import java.util.HashMap;

import static com.a02.game.Utils.readSer;
import static com.a02.game.Utils.writeSer;

public class EndScreen implements Screen {
    MainGame game;

    private final UIButton menuButton;
    private final UIButton quitButton;
    private final Texture endScreenTexture;

    public EndScreen(final String username, final int points, MainGame game) {
        //Guarda los datos desde un hilo
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                saveMaxScore(username, points);

            }
        });
        t.start();

        this.game = game;

        Pixmap pm = new Pixmap(Gdx.files.internal("cursor-export.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        menuButton = new UIButton(73,-150,74,36,
                "Buttons/menuButtonIdle.png","Buttons/menuButtonPressed.png");
        quitButton = new UIButton(173,-220,74,36,
                "Buttons/quitButtonIdle.png","Buttons/quitButtonPressed.png");
        endScreenTexture = new Texture("endScreen.png");
    }

    @Override
    public void render(float delta) {

        updateButtonLogic();

        menuButton.setY((float) (menuButton.getY() + (70 - menuButton.getY())*0.04));
        quitButton.setY((float) (quitButton.getY() + (70 - quitButton.getY())*0.04));

        game.entityBatch.begin();
        game.entityBatch.draw(endScreenTexture,0,0);
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        menuButton.isTouched();
        quitButton.isTouched();

        if (menuButton.isJustClicked()) {
            game.setScreen(new MenuScreen(game));
        }
        else if (quitButton.isJustClicked()) {
            Gdx.app.exit();
            System.exit(0);
        }
    }

    private void saveMaxScore(String username, int points) {
        HashMap<String, User> map = null;
        try {
            map = readSer("users.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert map != null;
        User tempUser = map.get(username);

        if (tempUser.getScoreRecord() < points) tempUser.setScoreRecord(points);
        map.put(tempUser.getUsername(), tempUser);

        try {
            writeSer("users.ser", map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
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
