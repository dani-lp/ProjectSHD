package com.a02.screens;

import com.a02.component.SoundPlayer;
import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Utils;
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
    private final UIButton menuButton; //Para salir al menu
    private final UIButton quitButton; //Para cerrar el juego
    private final UIButton restartButton; //Volver a empezar las rondas normales desde la ronda 1
    private final SoundPlayer soundPlayer; //Para reproducir sonidos de victoria
    private final BitmapFont font; //Fuente para las letras
    private final Animation<TextureRegion> pizzAnimation; //Animacion de celebrando
    private float count1;//Contadores para las animaciones para que vayan no sincronizadas
    private float count2;
    private float count3;
    private final Texture wallpaperTexture; //Textura del fondo

    public WinScreen(final String username,MainGame game,  final int points) { //game y points se pasan desde GameScreen
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

        this.animationTimer = 0;
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

        wallpaperTexture = new Texture("wallpaperTestOld.png");
    }

    @Override
    public void render(float delta) {
        count1 += delta;    //Actualizar los contadores
        count2 += delta;
        count3 += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateButtonLogic(); //Cambiar el estado de los botones

        game.entityBatch.begin();//Dibujado de los botones y el fondo
        game.entityBatch.draw(wallpaperTexture, 0, 0);
        game.entityBatch.draw(menuButton.getCurrentTexture(), menuButton.getX(), menuButton.getY());
        game.entityBatch.draw(quitButton.getCurrentTexture(), quitButton.getX(), quitButton.getY());
        game.entityBatch.draw(restartButton.getCurrentTexture(), restartButton.getX(), restartButton.getY());

        font.draw(game.entityBatch, "Enhorabuenaaa", 100, 160); //Dibujado de los dialogos
        font.draw(game.entityBatch, "Gracias por salvarnos!!!", 90, 140);

        game.entityBatch.draw(pizzAnimation.getKeyFrame(count1,true),35, 42);   //Dibujado de las animaciones de las pizzas
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count2,true),254, 101);
        game.entityBatch.draw(pizzAnimation.getKeyFrame(count3,true),226, 16);

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
