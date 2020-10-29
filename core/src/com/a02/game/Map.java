package com.a02.game;

public class Map {
    private boolean[][] occGrid; //Cuadricula de ocupación (true si la casilla está ocupada)
    private GameObject[][] entityGrid; //Cuadricula de ocupación (true si la casilla está ocupada)
    private String sprite;

    protected Map(String sprite) {
        occGrid = new boolean[10][16];
        entityGrid = new GameObject[10][16];
        this.sprite = sprite;
    }

    public boolean[][] getOccGrid() {
        return occGrid;
    }

    public void setOccGrid(boolean[][] occGrid) {
        this.occGrid = occGrid;
    }

    public GameObject[][] getEntityGrid() {
        return entityGrid;
    }

    public void setEntityGrid(GameObject[][] entityGrid) {
        this.entityGrid = entityGrid;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
