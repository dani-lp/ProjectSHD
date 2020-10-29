package com.a02.game;

import com.badlogic.gdx.math.Vector2;

public class Map {
    private boolean[][] occGrid; //Cuadrícula de ocupación (true si la casilla está ocupada)
    private GameObject[][] entityGrid; //Cuadrícula de ocupación por GameObject
    private final Vector2[][] coordGrid; //Cuadrícula de coordenadas
    private String sprite;

    protected final int GRID_WIDTH = 16;
    protected final int GRID_HEIGHT = 8;

    protected Map(String sprite) {
        occGrid = new boolean[10][16];
        entityGrid = new GameObject[10][16];
        this.sprite = sprite;

        //Creación de coordenadas
        coordGrid = new Vector2[10][16];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 16; j++) {
                coordGrid[i][j] = new Vector2(10*i, 16*j);
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
