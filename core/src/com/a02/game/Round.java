package com.a02.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 *En esta clase organizaremos las rondas
 *
 */
public class Round {
    protected int n;
    protected ArrayList<Enemy> enemies;
    protected ArrayList<GameObject> objects;

    public Round(int n, ArrayList<Enemy> enemies, ArrayList<GameObject> objects){
        this.n = n;
        this.enemies = null;
        this.objects = null;
    }

}
