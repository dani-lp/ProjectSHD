package com.a02.screens;

import com.a02.component.User;
import com.badlogic.gdx.Screen;

import java.io.IOException;
import java.util.HashMap;

import static com.a02.game.Utils.readSer;
import static com.a02.game.Utils.writeSer;

public class EndScreen implements Screen {

    public EndScreen(String username, int points) {
        saveMaxScore(username, points);
    }

    @Override
    public void render(float delta) {

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
