package com.a02.screens;

import com.a02.component.SoundPlayer;
import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WinScreen implements Screen {

    private final MainGame game;

    private final int  currentPoints; //Pasados entre GameScreen(s)
    private float animationTimer; //Usado para animaciones
    private final UIButton menuButton; //Para pasar a la siguiente frase
    private final UIButton quitButton; //Para saltarse las frases y pasar al jueg
    private final UIButton restartButton;
    private final SoundPlayer soundPlayer;
    private final BitmapFont font;

    public WinScreen(MainGame game,  int points) {
        this.game = game;
        this.currentPoints = points;

        this.animationTimer = 0;

        menuButton = new UIButton(123, 100, 74, 36,
                "Buttons/menuButtonIdle.png","Buttons/menuButtonPressed.png");
        quitButton = new UIButton(123, 50, 74, 36,
                "Buttons/quitButtonIdle.png","Buttons/quitButtonPressed.png");
        restartButton = new UIButton(123,15,18,18,"Buttons/nextButtonIdle.png","Buttons/nextButtonPressed.png");

        soundPlayer = new SoundPlayer();

        font = new BitmapFont(Gdx.files.internal("Fonts/gameFont.fnt"));

    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateButtonLogic();

        game.entityBatch.begin();
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.draw(restartButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());

        font.draw(game.entityBatch, "Enhorabuenaaa", 46, 30);
        font.draw(game.entityBatch, "Gracias por salvarnos!!!", 46, 20);



        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        menuButton.updateTouched();
        quitButton.updateTouched();
        restartButton.updateTouched();

        if (menuButton.isJustClicked()) {
            game.entityBatch.setColor(1,1,1, 1);
            game.setScreen(new MenuScreen(game));

        }
        if (quitButton.isJustClicked()) {
            Gdx.app.exit();
            System.exit(0);
        }
        if (restartButton.isJustClicked()) {
            game.entityBatch.setColor(1,1,1, 1);
            game.setScreen(new StoryScreen(game,1,0));
        }
    }

    @Override
    public void dispose() {
        menuButton.disposeButton();
        quitButton.disposeButton();
        restartButton.disposeButton();
        soundPlayer.dispose();
        font.dispose();
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
