package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import static com.a02.game.Utils.getRelativeMousePos;

/**
 * Botones de la UI interna del juego
 */
public class UIButton extends Entity {

    private Texture texture; //Aspecto del botón
    private boolean pressed;

    public UIButton(float x, float y, int width, int height, String texturePath) {
        super(x, y, width, height);
        this.texture = new Texture(texturePath);
    }

    public UIButton() {
        super();
        this.texture = null;
    }

    /**
     * Comprueba si el botón está tocado pero no pulsado.
     * @return
     */
    public boolean isTouched() {
        Vector3 mousePos = getRelativeMousePos();
        return this.overlapsPoint(mousePos.x, mousePos.y) && !Gdx.input.isTouched();
    }

    /**
     * Comprueba si el botón está tocado y pulsado.
     * @return
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

    public Texture getTexture() {
        return texture;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
