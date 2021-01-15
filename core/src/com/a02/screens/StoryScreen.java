package com.a02.screens;

import com.a02.component.SoundPlayer;
import com.a02.entity.UIButton;
import com.a02.game.MainGame;
import com.a02.game.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private float alphaTimer; //Para el 'fade in'

    private List<Animation<TextureRegion>> enemyAnimations;

    private final Texture characterIconTexture; //Textura de icono de personaje
    private final Texture characterNameTexture; //Textura del nombre del personaje
    private final Texture frameTexture; //Textura del marco de texto
    private final UIButton nextButton; //Para pasar a la siguiente frase
    private final UIButton skipButton; //Para saltarse las frases y pasar al juego
    private int convCounter = 0; //Contador de línea
    private final SoundPlayer soundPlayer; //Reproductor de sonido

    private final BitmapFont font;

    private Music taikosMusic = null;

    public StoryScreen(MainGame game, int round, int points) {
        this.game = game;
        this.currentRound = round;
        this.currentPoints = points;

        this.animationTimer = 0;
        this.alphaTimer = 0;

        loadEnemyAnimations();

        characterIconTexture = new Texture(getIconRoute(this.currentRound));
        characterNameTexture = new Texture(getNameRoute(this.currentRound));
        frameTexture = new Texture("storyFrame.png");
        nextButton = new UIButton(296,29,18,18,"Buttons/nextButtonIdle.png","Buttons/nextButtonPressed.png");
        skipButton = new UIButton(296,5,18,18,"Buttons/skipButtonIdle.png","Buttons/skipButtonPressed.png");
        soundPlayer = new SoundPlayer();

        font = new BitmapFont(Gdx.files.internal("Fonts/gameFont.fnt"));

        baseX = -(20 * enemyAnimations.size()) - 16;

        if (Settings.s.isMusicCheck()) {
            this.taikosMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/SHDtaikos.mp3"));
            this.taikosMusic.setLooping(true);
            this.taikosMusic.setVolume(Settings.s.getVolume());
            this.taikosMusic.play();
        }
    }

    @Override
    public void render(float delta) {
        if (animationTimer == 0 && this.currentRound == 5) soundPlayer.playRoar();
        animationTimer += Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (alphaTimer < 1.0f) alphaTimer += 0.02f;
        game.entityBatch.setColor(1,1,1,alphaTimer);

        updateButtonLogic();

        game.entityBatch.begin();

        game.entityBatch.draw(frameTexture, 5, 5);
        game.entityBatch.draw(nextButton.getCurrentTexture(), nextButton.getX(), nextButton.getY());
        game.entityBatch.draw(skipButton.getCurrentTexture(), skipButton.getX(), skipButton.getY());
        game.entityBatch.draw(characterIconTexture, 10,10);
        game.entityBatch.draw(characterNameTexture, 14,48);

        font.draw(game.entityBatch, topTextLines[this.currentRound - 1][convCounter], 46, 30);
        font.draw(game.entityBatch, botTextLines[this.currentRound - 1][convCounter], 46, 20);

        int posCounter = 1;

        for (Animation<TextureRegion> animation:enemyAnimations) {
            game.entityBatch.draw(animation.getKeyFrame(animationTimer, true), baseX + posCounter * 20,130);
            baseX += 0.1f; //Movimiento lateral
            posCounter++;
        }

        game.entityBatch.end();

        this.taikosMusic.setVolume(Settings.s.getVolume());
    }

    private void updateButtonLogic() {
        nextButton.updateTouched();
        skipButton.updateTouched();

        if (nextButton.isJustClicked()) {
            if (convCounter < topTextLines[this.currentRound - 1].length - 1) convCounter++; //Si hay más texto avanza
            else {
                game.entityBatch.setColor(1,1,1, 1);
                game.setScreen(new GameScreen(game, this.currentRound, this.currentPoints)); //Si no pasa al juego
                if (Settings.s.isMusicCheck()) taikosMusic.dispose();
            }
        }
        if (skipButton.isJustClicked()) {
            game.entityBatch.setColor(1,1,1, 1);
            game.setScreen(new GameScreen(game, this.currentRound, this.currentPoints));
            if (Settings.s.isMusicCheck()) taikosMusic.dispose();
        }
    }

    /**
     * Devuelve la ruta del icono solicitado.
     * @param round Ronda actual
     * @return Ruta del icono
     */
    private String getIconRoute(int round) {
        switch (round) {
            case 1:
                return "CharacterIcons/e6Icon.png"; //Gregor
            case 2:
                return "CharacterIcons/e1Icon.png"; //Larry
            case 3:
                return "CharacterIcons/e5Icon.png"; //Vmenta
            case 4:
                return "CharacterIcons/e3Icon.png"; //Kelsier
            case 5:
                return "CharacterIcons/e8Icon.png"; //Abzul
            default:
                return "empty.png";
        }
    }

    /**
     * Devuelve la ruta del nombre del enemigo necesario.
     * @param round Ronda actual
     * @return Ruta de la imagen
     */
    private String getNameRoute(int round) {
        switch (round) {
            case 1:
                return "EnemyNames/Gregor.png"; //Gregor
            case 2:
                return "EnemyNames/Larry.png"; //Larry
            case 3:
                return "EnemyNames/Vmenta.png"; //Vmenta
            case 4:
                return "EnemyNames/Kelsier.png"; //Kelsier
            case 5:
                return "EnemyNames/Abzul.png"; //Abzul
            default:
                return "empty.png";
        }
    }

    /**
     * Textos de la línea superior de diálogo.
     */
    private final String[][] topTextLines = {
            {"Bro, estoy harto del infierno.", "Hace eones que no pruebo una,", "tropecé con esa BROcha..."},
            {"BROS DEL INFRAMUNDO!", "para evitar que la humanidad ", "Así podremos conquistar la Tierra,"},
            {"Lo siento, no quiero hacer esto...", "Está obligando a mi gente a", "Aunque algunos de nosotros se han", "de un manjar divino que llaman 'pizza'."},
            {"No te haces a la idea de lo que me costó", "No vamos a dejar que todo nuestro", "El 'Proyecto SHD' era mucho más", "Intentar obtener energía..."},
            {"Pizzaaaa..."}
    };

    /**
     * Textos de la línea inferior de diálogo.
     */
    private final String[][] botTextLines = {
            {"Quiero un poco de pizza, bro...", "desde que me morí cuando me ", "Llevo mucho esperando..."},
            {"Debemos destruir el cuaternizador,", "cierre el portal.", "y poder tener pizza infinita!!"},
            {"El Rey Demonio Abzul...", "obedecerle! Somos un pueblo pacífico...", "pasado a su bando, por las promesas", "El Rey Demonio no deja de repetirlo..."},
            {"poseer a aquel científico...", "trabajo sea en vano.", "peligroso de lo que nunca pensasteis.", "del infierno? No me hagas reir."},
            {"PIZZAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"}
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
        enemyAnimations.clear();
        if (Settings.s.isMusicCheck()) taikosMusic.dispose();
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
