package com.a02.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity {
    private float x;
    private float y;
    private int width;
    private int height;
    private String sprite;

    public Entity(float x, float y, int width, int height, String sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public Entity() {
        this.x = 0;
        this.y = 0;
        this.width =0;
        this.height = 0;
        this.sprite = "";
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSprite() {
        return sprite;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSprite(String sprite) {
        sprite = sprite;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", Sprite='" + sprite + '\'' +
                '}';
    }

    protected boolean overlaps(Entity entity) { //Devuelve true si la Entity que llama colisiona con la Entity parámetro
        if ((this.y + this.height < entity.y) || (this.y > entity.y + entity.height)) {
            return false;
        }
        else if ((this.x + this.width < entity.x) || (this.x > entity.x + entity.width)) {
            return false;
        }
        return true;
    }

    protected boolean overlapsPoint (float x, float y) {
        return this.x <= x && this.x + this.width >= x && this.y <= y && this.y + this.height >= y;
    }

    protected static Animation<TextureRegion> createAnimation(String path, int frameCols, int frameRows, float frameDuration) {
        //Cargar el sprite sheet
        Texture tempTexture = new Texture(Gdx.files.internal(path));

        //Divide el sprite sheet en una TextureRegion[][] bidimensional
        TextureRegion[][] tempTR = TextureRegion.split(tempTexture,
                tempTexture.getWidth() / frameCols,
                tempTexture.getHeight() / frameRows);

        //Coloca los frames de la animación en un array 1D de TextureRegion
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tempTR[i][j];
            }
        }

        //Crea y devuelve la animación
        return new Animation<TextureRegion>(frameDuration, animationFrames);
    }

}
