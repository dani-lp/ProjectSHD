package com.a02.component;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Countdown {
    private final float x, y; //Posiciones
    private int timer, counter; //Contador por frame y por número
    private final BitmapFont bigFont;
    private boolean finished;

    public Countdown(float x, float y, int counter){
        this.x = x;
        this.y = y;
        this.timer = 0;
        this.counter = counter;

        this.bigFont = new BitmapFont(Gdx.files.internal("Fonts/gameFontBig.fnt"));
    }

    /**
     * Actualiza el estado del contador.
     * @param gs Screen de juego
     */
    public void update(GameScreen gs) {
        if (!gs.isPaused() && !this.isFinished()) timer++;

        if (timer == 60) {
            counter--;
            timer = 0;
        }

        if (this.counter < 0) this.finished = true;
    }

    /**
     * Dibuja la cuenta atrás.
     * @param gs Screen de juego
     */
    public void draw(GameScreen gs) {
        if (!this.isFinished()) this.bigFont.draw(gs.getGame().entityBatch, Integer.toString(this.counter) , this.getX(), this.getY());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isFinished() {
        return finished;
    }
}
