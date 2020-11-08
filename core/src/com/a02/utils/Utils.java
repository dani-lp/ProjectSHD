package com.a02.utils;

import com.a02.game.MainGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Utils {
    /**
     * Devuelve la posición del mouse en un Vector3.
     */
    public static Vector3 getMousePos() {
        return new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    }

    public static Vector3 getRelativeMousePos() {
        return MainGame.mainGameScreen.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
    }

    /**
     * Crea una animación a partir de una sprite sheet.
     * @param path Ruta del archivo.
     * @param frameCols Columnas de la sprite sheet.
     * @param frameRows Filas de la sprite sheet.
     * @param frameDuration Duración de cada fotograma de la animación.
     * @return Objeto Animation con duración concreta.
     */
    public static Animation<TextureRegion> createAnimation(String path, int frameCols, int frameRows, float frameDuration) {
        //Cargar el sprite sheet
        Texture tempTexture = new Texture(Gdx.files.internal(path));

        //Divide el sprite sheet en una TextureRegion[][] bidimensional
        TextureRegion[][] tempTR = TextureRegion.split(tempTexture,
                tempTexture.getWidth() / frameCols,
                tempTexture.getHeight() / frameRows);

        //Coloca los frames de la animación en un array 1D de TextureRegion
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tempTR[i][j];
            }
        }

        //Crea y devuelve la animación
        return new Animation<TextureRegion>(frameDuration, animationFrames);
    }
}
