package com.a02.screens;

import com.a02.component.SoundPlayer;
import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import static com.a02.game.Utils.createAnimation;

public class WinScreen implements Screen {

    private final MainGame game;

    private final int currentPoints; //Pasados entre GameScreen(s)
    private final UIButton menuButton; //Para salir al menu
    private final UIButton quitButton; //Para cerrar el juego
    private final UIButton restartButton; //Volver a empezar las rondas normales desde la ronda 1
    private final SoundPlayer soundPlayer; //Para reproducir sonidos de victoria
    private final BitmapFont font; //Fuente para las letras
    private final Animation<TextureRegion> pizzAnimation; //Animacion de celebrando
    private float count1, count2, count3;//Contadores para las animaciones para que vayan no sincronizadas
    private double heartXCounter;
    private final Texture wallpaperTexture, heartTexture, frameTexture;//Texturas del fondo, corazones y marco de puntos
    private final Pixmap pm3;

    public WinScreen(final String username, MainGame game, final int points) { //game y points se pasan desde GameScreen
        //Guarda los datos desde un hilo
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Utils.saveMaxScore(username, points);
            }
        });
        t.start();

        this.game = game;
        this.currentPoints = points;

        //Asignacion de valores por defecto
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
        heartXCounter = 0;

        wallpaperTexture = new Texture("wallpaperTestOld.png");
        heartTexture = new Texture("pizzaHeart.png");
        frameTexture = new Texture("winFrame.png");

        pm3 = new Pixmap(Gdx.files.internal("defaultCursor.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm3, 0, 0));

        count1 += delta; //Actualizar los contadores
        count2 += delta;
        count3 += delta;
        heartXCounter += 0.2f;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateButtonLogic(); //Cambiar el estado de los botones

        game.entityBatch.begin();//Dibujado de los botones y el fondo
        game.entityBatch.draw(wallpaperTexture, 0, 0);
        game.entityBatch.draw(frameTexture, 90, 128);
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.draw(restartButton.getCurrentTexture(), restartButton.getX(), restartButton.getY());

        font.draw(game.entityBatch, "¡VICTORIA!", 136, 152); //Dibujado de los dialogos
        font.draw(game.entityBatch, "Puntuación: " + currentPoints, 117, 138);

        game.entityBatch.draw(pizzAnimation.getKeyFrame(count1,true),35, 42);   //Dibujado de las animaciones de las pizzas
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count2,true),254, 101);
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count3,true),226, 16);
        game.entityBatch.draw(heartTexture, 44, (float) Math.sin(heartXCounter) + 77);
        game.entityBatch.draw(heartTexture, 263, (float) Math.sin(heartXCounter) + 136);
        game.entityBatch.draw(heartTexture, 235, (float) Math.sin(heartXCounter) + 51);

        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        menuButton.updateTouched(); //Cambiar el sprite
        quitButton.updateTouched();
        restartButton.updateTouched();

        if (menuButton.isJustClicked()) { //Devolverte a MenuScreen
            game.entityBatch.setColor(1,1,1, 1);
            game.setScreen(new MenuScreen(game));

        }
        if (quitButton.isJustClicked()) {   //Hacer que te salgas
            Gdx.app.exit();
            System.exit(0);
        }
        if (restartButton.isJustClicked()) { //Empezar de nuevo desde la ronda 1
            game.entityBatch.setColor(1,1,1, 1);
            game.setScreen(new StoryScreen(game,1,0));
        }
    }

    @Override
    public void dispose() {     //Limpiado de lo que hay en la screen
        wallpaperTexture.dispose();
        heartTexture.dispose();
        frameTexture.dispose();
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
