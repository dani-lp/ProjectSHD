package com.a02.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Map {
    /**
     * Cuadrícula de ocupación (true si la casilla está ocupada)
     */
    private final boolean[][] occGrid;
    /**
     * Cuadrícula de coordenadas
     */
    private final Vector2[][] coordGrid;
    private final Texture texture;

    public Map(String sprite) {
        occGrid = new boolean[16][10];
        this.texture = new Texture(Gdx.files.internal(sprite));

        //Creación de coordenadas
        coordGrid = new Vector2[16][10];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                coordGrid[i][j] = new Vector2(16*i, 18*j);
            }
        }
    }

    public boolean[][] getOccGrid() {
        return occGrid;
    }

    public Vector2[][] getCoordGrid() {
        return coordGrid;
    }

    public Texture getTexture() {
        return texture;
    }
}
