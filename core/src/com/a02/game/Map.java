package com.a02.game;

import com.badlogic.gdx.math.Vector2;

public class Map {
    private boolean[][] occGrid; //Cuadrícula de ocupación (true si la casilla está ocupada)
    private GameObject[][] entityGrid; //Cuadrícula de ocupación por GameObject
    private final Vector2[][] coordGrid; //Cuadrícula de coordenadas
    private String sprite;

    protected final int GRID_WIDTH = 16;
    protected final int GRID_HEIGHT = 18;

    protected Map(String sprite) {
        occGrid = new boolean[16][10];
        entityGrid = new GameObject[16][10];
        this.sprite = sprite;

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

    protected Vector2[][] getCoordGrid() {
        return coordGrid;
    }
}
