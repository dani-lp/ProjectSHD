package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameObject extends Entity{
    private int id;
    private String name;
    private int hp;

    public GameObject(float x, float y, int width, int height, String sprite, int id, String name,
                      int hp) {
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
    }


    public GameObject() {
        super();
        this.id = -1;
        this.name = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return "ASD [id=" + id + ", name=" + name + ", x=" + this.getX() + ", y=" + this.getY() + ", sprite=" + getSprite()
                 + "]";
    }

    static boolean temp = false;
    public void place(GameScreen game){
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        game.camera.unproject(touchPos);

        if (Gdx.input.isTouched() && this.overlapsPoint(touchPos.x, touchPos.y) && temp == false) {
            temp = true;
        }
        if (temp) {
            this.setX((int) (touchPos.x - 16 / 2));
            this.setY((int) (touchPos.y - 16 / 2));
        }
        if (!Gdx.input.isTouched()) {
            temp = false;
        }
    }

    //Devuelve el número de casilla con la que colisiona del mapa recibido como parámetro, null si no lo hace
    protected Vector2 mapGridCollision(Map map) {
        int x;
        int y;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 16; j++) {
                if (this.overlaps( map.getCoordGrid()[i][j] , 10, 16)) {
                    return new Vector2(map.getCoordGrid()[i][j].x, map.getCoordGrid()[i][j].y);
                }
            }
        }
        return null;
    }

}
