package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import static com.a02.game.Utils.getRelativeMousePos;

/**
 * Botones de la UI interna del juego
 */
public class UIButton extends Entity {

    private final Texture idleTexture; //Aspecto del botón
    private final Texture pressedTexture;

    private boolean touched = false;
    private boolean pressed;

    private boolean active; //Si el botón está inactivo (active = false) no se realiza ninguna acción con él.

    public UIButton(float x, float y, int width, int height, String idleTexturePath, String pressedTexturePath) {
        super(x, y, width, height);
        this.idleTexture = new Texture(idleTexturePath);
        this.pressedTexture = new Texture(pressedTexturePath);
        this.active = true;
    }

    /**
     * Comprueba si el botón está tocado pero no pulsado.
     * @return True si se cumple la condición
     */
    public boolean isTouched() {
        Vector3 mousePos = getRelativeMousePos();
        if (!this.active) return false;
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
        if (!this.active) return false;
        Vector3 mousePos = getRelativeMousePos();
        return this.overlapsPoint(mousePos.x, mousePos.y) && Gdx.input.isTouched();
    }

    /**
     * Comprueba si el botón ha sido pulsado una vez.
     * @return true si acaba de ser pulsado, false en cualquier otro caso.
     */
    public boolean isJustClicked() {
        if (!this.active) return false;
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

    /**
     * Devuelve la textura actual.
     * @return Texture actual
     */
    public Texture getCurrentTexture() {
        if (this.touched) return this.getPressedTexture();
        else return this.getIdleTexture();
    }

    /**
     * Elimina las texturas del botón.
     */
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

    public Texture getPressedTexture() {
        return pressedTexture;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
