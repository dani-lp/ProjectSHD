package com.a02.component;

import com.a02.entity.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Map {
    /**
     * Cuadrícula de ocupación (true si la casilla está ocupada)
     */
    private boolean[][] occGrid;
    /**
     * Cuadrícula de ocupación por GameObject
     */
    private GameObject[][] entityGrid;
    /**
     * Cuadrícula de coordenadas
     */
    private final Vector2[][] coordGrid;
    private String sprite;
    private Texture texture;

    protected final int GRID_WIDTH = 16;
    protected final int GRID_HEIGHT = 18;

    public Map(String sprite) {
        occGrid = new boolean[16][10];
        entityGrid = new GameObject[16][10];
        this.sprite = sprite;
        this.texture = new Texture(Gdx.files.internal(this.sprite));

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

    public GameObject[][] getEntityGrid() {
        return entityGrid;
    }

    public String getSprite() {
        return sprite;
    }

    public Vector2[][] getCoordGrid() {
        return coordGrid;
    }

    public Texture getTexture() {
        return texture;
    }
}
