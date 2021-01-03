package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import static com.a02.game.Utils.getRelativeMousePos;

/**
 * Botones de la UI interna del juego
 */
public class UIButton extends Entity {

    private Texture idleTexture; //Aspecto del botón
    private Texture pressedTexture;

    private boolean touched = false;
    private boolean pressed;

    public UIButton(float x, float y, int width, int height, String idleTexturePath, String pressedTexturePath) {
        super(x, y, width, height);
        this.idleTexture = new Texture(idleTexturePath);
        this.pressedTexture = new Texture(pressedTexturePath);
    }

    public UIButton() {
        super();
        this.idleTexture = null;
    }

    /**
     * Comprueba si el botón está tocado pero no pulsado.
     * @return True si se cumple la condición
     */
    public boolean isTouched() {
        Vector3 mousePos = getRelativeMousePos();
        if (!(this.overlapsPoint(mousePos.x, mousePos.y) && !Gdx.input.isTouched())) {
            this.touched = false;
        }
        else {
            this.touched = true;
            return true;
        }
        return false;
    }

    /**
     * Comprueba si el botón está tocado y pulsado.
     * @return True si se cumple la condición
     */
    public boolean isBeingClicked() {
        Vector3 mousePos = getRelativeMousePos();
        return this.overlapsPoint(mousePos.x, mousePos.y) && Gdx.input.isTouched();
    }

    /**
     * Comprueba si el botón ha sido pulsado una vez.
     * @return true si acaba de ser pulsado, false en cualquier otro caso.
     */
    public boolean isJustClicked() {
        Vector3 mousePos = getRelativeMousePos();
        if (this.overlapsPoint(mousePos.x, mousePos.y) && Gdx.input.isTouched() && !this.isPressed()) {
            this.pressed = true;
            return true;
        }
        else if (this.overlapsPoint(mousePos.x, mousePos.y) && !Gdx.input.isTouched() && this.isPressed()) {
            this.pressed = false;
            return false;
        }
        else {
            return false;
        }
    }

    public Texture getCurrentTexture() {
        if (this.touched) return this.getPressedTexture();
        else return this.getIdleTexture();
    }

    public void disposeButton () {
        this.idleTexture.dispose();
        this.pressedTexture.dispose();
    }

    public boolean isPressed() {
        return pressed;
    }

    public Texture getIdleTexture() {
        return idleTexture;
    }

    public void setIdleTexture(Texture idleTexture) {
        this.idleTexture = idleTexture;
    }

    public Texture getPressedTexture() {
        return pressedTexture;
    }

    public void setPressedTexture(Texture pressedTexture) {
        this.pressedTexture = pressedTexture;
    }
}
