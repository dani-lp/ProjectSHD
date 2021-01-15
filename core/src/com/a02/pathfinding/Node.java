package com.a02.pathfinding;

import com.a02.entity.Enemy;

public class Node {

    private Node nextNode; //Siguiente nodo al que dirigirse.
    private final float x, y; //Coordenadas 'x', 'y' del nodo.

    public Node(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "nextNode=" + nextNode +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Devuelve la distancia de las coordenadas dadas a este nodo.
     * @param x Coordenada x
     * @param y Coordenada y
     * @return Distancia al nodo
     */
    public double distanceToNode(float x, float y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
    }

    /**
     * Devuelve la distancia del enemigo dado a este nodo.
     * @param enemy Enemigo objetivo
     * @return Distancia al enemigo
     */
    public double distanceToNode(Enemy enemy) {
        return Math.sqrt(Math.pow(this.x - enemy.getX(), 2) + Math.pow(this.y - enemy.getY(), 2));
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
