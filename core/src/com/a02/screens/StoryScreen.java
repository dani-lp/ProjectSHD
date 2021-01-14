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

import java.util.ArrayList;
import java.util.List;

import static com.a02.game.Utils.createAnimation;

public class StoryScreen implements Screen {
    private final MainGame game;

    private final int currentRound, currentPoints; //Pasados entre GameScreen(s)
    private float animationTimer; //Usado para animaciones
    private float baseX; //Posición inicial de los enemigos

    private List<Animation<TextureRegion>> enemyAnimations;

    private Texture characterIconTexture; //Textura de icono de personaje
    private Texture characterNameTexture; //Textura del nombre del personaje
    private final Texture frameTexture; //Textura del marco de texto
    private UIButton nextButton; //Para pasar a la siguiente frase
    private UIButton skipButton; //Para saltarse las frases y pasar al juego
    private int convCounter = 0; //Contador de línea
    private final SoundPlayer soundPlayer; //Reproductor de sonido

    private final BitmapFont font;

    public StoryScreen(MainGame game, int round, int points) {
        this.game = game;
        this.currentRound = round;
        this.currentPoints = points;

        this.animationTimer = 0;

        loadEnemyAnimations();

        characterIconTexture = new Texture(getIconRoute(this.currentRound));
        frameTexture = new Texture("storyFrame.png");
        nextButton = new UIButton(296,29,18,18,"Buttons/nextButtonIdle.png","Buttons/nextButtonPressed.png");
        skipButton = new UIButton(296,5,18,18,"Buttons/skipButtonIdle.png","Buttons/skipButtonPressed.png");
        soundPlayer = new SoundPlayer();

        font = new BitmapFont(Gdx.files.internal("Fonts/test.fnt"));

        baseX = -(20 * enemyAnimations.size()) - 16;
    }

    @Override
    public void render(float delta) {
        animationTimer += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateButtonLogic();

        game.entityBatch.begin();

        game.entityBatch.draw(frameTexture, 5, 5);
        game.entityBatch.draw(nextButton.getCurrentTexture(), nextButton.getX(), nextButton.getY());
        game.entityBatch.draw(skipButton.getCurrentTexture(), skipButton.getX(), skipButton.getY());
        game.entityBatch.draw(characterIconTexture, 10,10);

        font.draw(game.entityBatch, topTextLines[this.currentRound - 1][convCounter], 47, 35);
        font.draw(game.entityBatch, botTextLines[this.currentRound - 1][convCounter], 47, 25);

        int posCounter = 1;

        for (Animation<TextureRegion> animation:enemyAnimations) {
            game.entityBatch.draw(animation.getKeyFrame(animationTimer, true), baseX + posCounter*20,130);
            baseX += 0.1f;
            posCounter++;
        }

        game.entityBatch.end();
    }

    private void updateButtonLogic() {
        nextButton.isTouched();
        skipButton.isTouched();

        if (nextButton.isJustClicked()) {
            if (convCounter < topTextLines[this.currentRound - 1].length - 1) convCounter++; //Si hay más texto avanza
            else game.setScreen(new GameScreen(game, this.currentRound, this.currentPoints)); //Si no pasa al juego
        }
        if (skipButton.isJustClicked()) game.setScreen(new GameScreen(game, this.currentRound, this.currentPoints));
    }

    /**
     * Devuelve la ruta del icono solicitado.
     * @param round Ronda actual
     * @return Ruta del icono
     */
    private String getIconRoute(int round) {
        switch (round) {
            case 1:
                return "CharacterIcons/e6Icon.png"; //Caballero genérico
            case 2:
                return "CharacterIcons/e1Icon.png"; //Caballero rojo
            case 3:
                return "CharacterIcons/e5Icon.png"; //Árbol, está controlado por el E8
            case 4:
                return "CharacterIcons/e3Icon.png"; //Fantasma
            case 5:
                return "CharacterIcons/e8Icon.png"; //Boss
            default:
                return "empty.png";
        }
    }

    /**
     * Textos de la línea superior de diálogo.
     */
    private final String[][] topTextLines = {
            {"We're no strangers to love", "A full commitment's what I'm thinking of", "I just wanna tell you how I'm feeling", "Never gonna give you up", "Never gonna run around and desert you", "Never gonna say goodbye"},
            {"", "", ""},
            {"", "", ""},
            {"", "", ""},
            {"", "", ""}
    };

    /**
     * Textos de la línea inferior de diálogo.
     */
    private final String[][] botTextLines = {
            {"You know the rules and so do I", "You wouldn't get this from any other guy", "Gotta make you understand","Never gonna let you down", "Never gonna make you cry", "Never gonna tell a lie and hurt you"},
            {"", "", ""},
            {"", "", ""},
            {"", "", ""},
            {"", "", ""}
    };

    /**
     * Genera animaciones de cada enemigo que vaya a aparecer en la ronda. Éstas se mostrarán en la parte superior de la pantalla.
     */
    private void loadEnemyAnimations() {
        this.enemyAnimations = new ArrayList<>();
        switch (this.currentRound) {
            case 1: //3 5 6
                this.enemyAnimations.add(createAnimation("Enemies/e4-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e6-walk.png",2,2,0.5f));
                this.enemyAnimations.add(createAnimation("Enemies/e7-walk.png",2,2,0.2f));
                break;
            case 2: //0 1 5 6
                this.enemyAnimations.add(createAnimation("Enemies/e1-walk.png",3,1,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e2-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e6-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e7-walk.png",2,2,0.2f));
                break;
            case 3: //2 3 4 6
                this.enemyAnimations.add(createAnimation("Enemies/e3-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e4-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e5-walk.png",2,2,0.5f));
                this.enemyAnimations.add(createAnimation("Enemies/e7-walk.png",2,2,0.2f));
                break;
            case 4: //0 1 2 3 5 6
                this.enemyAnimations.add(createAnimation("Enemies/e1-walk.png",3,1,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e2-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e3-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e4-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e6-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e7-walk.png",2,2,0.2f));
                break;
            case 5: //0 2 7
                this.enemyAnimations.add(createAnimation("Enemies/e1-walk.png",3,1,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e3-walk.png",2,2,0.2f));
                this.enemyAnimations.add(createAnimation("Enemies/e8-walk.png",2,2,0.2f));
                break;
        }
    }

    @Override
    public void dispose() {
        characterIconTexture.dispose();
        frameTexture.dispose();
        characterNameTexture.dispose();
        nextButton.disposeButton();
        skipButton.disposeButton();
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
