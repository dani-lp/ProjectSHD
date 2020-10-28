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

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<GameObject> objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {
        return "Round{" +
                "n=" + n +
                ", enemies=" + enemies +
                ", objects=" + objects +
                '}';
    }
}
