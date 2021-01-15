package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static com.a02.game.Utils.*;

public class Defender extends GameObject {

    private boolean isSelected;
    private ArrayList<GameObject> hurt = new ArrayList<>();

    public Defender(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.isSelected = false;
        loadTextures();
    }

    public void loadTextures() {
        switch (this.getId()) {
            case -1: //Beacon
                this.setTexture(new Texture(Gdx.files.internal("beacon.png")));
                break;
            case 0: //Health
                this.setTexture(new Texture(Gdx.files.internal("healer.png")));
                this.setAnimation(createAnimation("mercy-Sheet.png", 5, 1, 0.1f));
                break;
            case 1: //Wall
                this.setTexture(new Texture(Gdx.files.internal("wall.png")));
                break;
            case 2: //Valla
                this.setTexture(new Texture(Gdx.files.internal("valla.png")));
                break;
            case 3: //Repair
                this.setTexture(new Texture(Gdx.files.internal("martillo.png")));
                break;
        }
    }

    public Defender() {
        super();
        this.isSelected = false;
        loadTextures();
    }

    public Defender(Defender other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(),
                other.getPrice(), other.isUnlocked(), other.getHp());
        this.setAnimation(other.getAnimation());
        this.hurt = other.hurt;
        this.isSelected = false;
        this.setTexture(other.getTexture());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void update (GameScreen gs) {
        switch (this.getId()) {
            case 0: //Health: cura en área, perdiendo vida cuando lo hace
                if (gs.secTimer % 30 == 0 && !this.isInInventory(gs)) {
                    hurt = overlappedArea(gs);
                    int healedObjectCounter = 0;
                    for (GameObject obj : hurt) {
                        if (obj.getHp() < obj.getMaxHp()) {
                            obj.setHp(obj.getHp() + 20); //Cura 20 de vida a otros objetos
                            healedObjectCounter++;
                        }
                    }
                    this.setHp(this.getHp() - healedObjectCounter * 5); //Recibe 20 de daño por cada cura
                }
                break;
            case 3: //Repair: cura a un objeto en concreto
                Vector3 focus = getRelativeMousePos();

                if (this.isSelected && overlappedObject(gs,focus.x, focus.y) != null &&
                        (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE))) {
                    GameObject obj = overlappedObject(gs,focus.x, focus.y);
                    if (obj != null && obj.getHp() < obj.getMaxHp() && obj.getId() != -1) {
                        obj.setHp(obj.getMaxHp());
                        gs.soundPlayer.playHammer();
                        this.setHp(0);
                        gs.state = GameScreen.State.PLAYING;
                    }
                }

                //Selección
                if (this.overlapsPoint(focus.x, focus.y) && mouseJustClicked() && this.isUnlocked()) {
                    //Sólo se puede tomar control si no se está comprando ni eliminando
                    if (gs.state == GameScreen.State.PLAYING && !this.isSelected) {
                        gs.state = GameScreen.State.SELECTING;
                        this.isSelected = true;
                    }
                    else if (gs.state == GameScreen.State.SELECTING && this.isSelected) {
                        gs.state = GameScreen.State.PLAYING;
                        this.isSelected = false;
                    }
                }
                break;
            default: //Valla y muro (objetos estáticos, sólo bloquean)
                break;
        }
        this.hpBar.update(this, this.getHp());
    }

    /**
     * Devuelve el primer GameObject con el que se detecta colisión.
     * @param gs Screen de juego
     * @return GameObject colisionado
     */
    public GameObject overlappedObject(GameScreen gs,float x, float y) {
        for (GameObject obj : gs.objects) {
            if (x < obj.getX() + obj.getWidth() && x + this.getWidth() > obj.getX() &&
                    y < obj.getY() + obj.getHeight() && y > obj.getY()) {
                return obj;
            }
        }
        return null;
    }

    /**
     * Devuelve un ArrayList de los GameObject dentro de un área cuyo centro es el objeto que realiza la llamada.
     * @param gs Screen de juego
     * @return ArrayList de GameObject en un área
     */
    protected ArrayList<GameObject> overlappedArea(GameScreen gs) {
        ArrayList<GameObject> inArea = new ArrayList<>();
        for (GameObject obj : gs.objects) {
            if ((obj.getX() < this.getX() + 50 && obj.getX() > this.getX() - 50) && (obj.getY() < this.getY() + 50
                    && obj.getY() > this.getY() - 50) && !(obj.getX() == this.getX() && obj.getY() == this.getY())
                    && obj.getHp() < obj.getMaxHp()) {
                inArea.add(obj);
            }
        }
        return inArea;
    }
}
