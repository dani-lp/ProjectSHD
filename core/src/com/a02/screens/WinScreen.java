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
import static com.a02.game.Utils.createAnimation;

public class WinScreen implements Screen {

    private final MainGame game;

    private final int currentPoints; //Pasados entre GameScreen(s)
    private float animationTimer; //Usado para animaciones
    private final UIButton menuButton; //Para pasar a la siguiente frase
    private final UIButton quitButton; //Para saltarse las frases y pasar al jueg
    private final UIButton restartButton;
    private final SoundPlayer soundPlayer;
    private final BitmapFont font;
    private final Animation<TextureRegion> pizzAnimation;
    private float count1;
    private float count2;
    private float count3;
    private final Texture wallpaperTexture;

    public WinScreen(MainGame game,  int points) {
        this.game = game;
        this.currentPoints = points;

        this.animationTimer = 0;

        restartButton = new UIButton(123, 87, 74, 36,
                "Buttons/replayButtonIdle.png","Buttons/replayButtonPressed.png");
        menuButton = new UIButton(123, 46, 74, 36,
                "Buttons/menuButtonIdle.png","Buttons/menuButtonPressed.png");
        quitButton = new UIButton(123,5,74,36,"Buttons/quitButtonIdle.png","Buttons/quitButtonPressed.png");

        soundPlayer = new SoundPlayer();

        font = new BitmapFont(Gdx.files.internal("Fonts/gameFont.fnt"));
        pizzAnimation = createAnimation("pizza-sheet.png", 7, 2, 0.05f);
        count1 = 0;
        count2 = 0.2f;
        count3 = 0.5f;

        wallpaperTexture = new Texture("wallpaperTestOld.png");
    }

    @Override
    public void render(float delta) {
        count1 += delta;
        count2 += delta;
        count3 += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateButtonLogic();

        game.entityBatch.begin();
        game.entityBatch.draw(wallpaperTexture, 0, 0);
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.draw(restartButton.getCurrentTexture(), restartButton.getX(), restartButton.getY());

        font.draw(game.entityBatch, "Enhorabuenaaa", 100, 160);
        font.draw(game.entityBatch, "Gracias por salvarnos!!!", 90, 140);

        game.entityBatch.draw(pizzAnimation.getKeyFrame(count1,true),35, 42);
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count2,true),254, 101);
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count3,true),226, 16);

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
