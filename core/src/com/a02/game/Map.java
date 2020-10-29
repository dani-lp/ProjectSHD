package com.a02.game;

public class Map {
    private boolean[][] occGrid; //Cuadricula de ocupación (true si la casilla está ocupada)
    private Entity[][] entityGrid; //Cuadricula de ocupación (true si la casilla está ocupada)

    protected Map() {
        occGrid = new boolean[10][16];
    }

}
