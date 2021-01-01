package com.a02.screens;

import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.users.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

import java.io.IOException;
import java.util.HashMap;

import static com.a02.game.Utils.readSer;
import static com.a02.game.Utils.writeSer;

public class EndScreen implements Screen {
    MainGame game;

    private UIButton menuButton;
    private UIButton quitButton;
    private Texture endScreenTexture;

    public EndScreen(String username, int points, MainGame game) {
        saveMaxScore(username, points);
        this.game = game;

        menuButton = new UIButton(73,70,74,36,"Buttons/menuButtonIdle.png");
        quitButton = new UIButton(173,70,74,36,"Buttons/quitButtonIdle.png");
        endScreenTexture = new Texture("endScreen.png");
    }

    @Override
    public void render(float delta) {

        updateButtonLogic();

        game.entityBatch.begin();
        game.entityBatch.draw(endScreenTexture,0,0);
        game.entityBatch.draw(menuButton.getTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        if (menuButton.isTouched()) menuButton.setTexture(new Texture("Buttons/menuButtonPressed.png"));
        else menuButton.setTexture(new Texture("Buttons/menuButtonIdle.png"));

        if (quitButton.isTouched()) quitButton.setTexture(new Texture("Buttons/quitButtonPressed.png"));
        else quitButton.setTexture(new Texture("Buttons/quitButtonIdle.png"));

        if (menuButton.isJustClicked()) {
            game.setScreen(new MenuScreen(game,false));
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
        menuButton.getTexture().dispose();
        quitButton.getTexture().dispose();
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
