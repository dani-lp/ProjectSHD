package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import static com.a02.game.Utils.createAnimation;
import static com.a02.game.Utils.getRelativeMousePos;

public class Defender extends GameObject {

    private enum State {
        IDLE, HEALING
    }
    public static boolean selected;
    private boolean isSelected;
    State state;

    public void states(int i){
        if (i==0){
            this.state = State.IDLE;
        }
    }

    public Defender(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.state = State.IDLE;
        this.isSelected=false;
        loadTextures();
    }

    private ArrayList<GameObject> hurt = new ArrayList<>();

    public void loadTextures() {
        switch (this.getId()) {
            case 0: //Beacon
                this.setTexture(new Texture(Gdx.files.internal("beacon.png")));
                break;
            case 1: //Wall
                this.setTexture(new Texture(Gdx.files.internal("wall.png")));
                break;
            case 2: //Valla
                this.setTexture(new Texture(Gdx.files.internal("valla.png")));
                break;
            case 3:
                this.setTexture(new Texture(Gdx.files.internal("martillo.png")));
                break;
            case 4:
                this.setTexture(new Texture(Gdx.files.internal("healer.png")));
                this.setAnimation(createAnimation("mercy-Sheet.png", 5, 1, 0.1f));
                break;
        }
    }

    public Defender() {
        super();
        this.state = State.IDLE;
        this.isSelected=false;
        loadTextures();
    }

    public Defender(Defender other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(),
                other.getPrice(), other.isUnlocked(), other.getHp());
        this.state = other.state;
        this.setAnimation(other.getAnimation());
        this.hurt = other.hurt;
        this.isSelected=false;
        this.setTexture(other.getTexture());
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (this.getId() == 4 && !this.isInInventory(gs)) {
                    hurt = overlappedArea(gs);
                    if (hurt.size() > 0) {
                        this.state = State.HEALING;
                    }
                } else if (this.getId() == 3 && !this.isInInventory(gs)){
                    Vector3 mousePos = getRelativeMousePos();
                    if (this.overlapsPoint(mousePos.x, mousePos.y) && Gdx.input.isTouched()) {
                        Pixmap pm = new Pixmap(Gdx.files.internal("healcross.png"));
                        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
                        pm.dispose();
                        this.isSelected = true;
                        selected=true;
                        this.state = State.HEALING;
                    }
                }
                break;
            case HEALING:
                boolean healed=false;
                if (this.getId() == 4){
                    if (gs.secTimer % 120 == 0) {
                        for (GameObject obj : hurt) {
                            if (obj.getHp() < obj.getMaxHp()) {
                                obj.setHp(obj.getHp() + 75);
                                healed=true;
                            }
                        }
                        if (healed){
                            this.setHp(this.getHp() - 75);
                        }
                    }
                    break;
                } else if (this.getId() == 3){
                    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                        Vector3 focus = getRelativeMousePos();
                        GameObject obj = overlappedObject(gs,focus.x, focus.y);
                        if (obj != null && obj.getHp() < obj.getMaxHp()){
                            obj.setHp(obj.getMaxHp());
                            this.setHp(0);
                            Pixmap pm = new Pixmap(Gdx.files.internal("cursor-export.png"));
                            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
                            pm.dispose();
                            this.state = State.IDLE;
                        }
                        break;
                    }
                }
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
            if ((obj.getX() < this.getX() + 50 && obj.getX() > this.getX() - 50) && (obj.getY() < this.getY() + 50 && obj.getY() > this.getY() - 50) && !(obj.getX() == this.getX() && obj.getY() == this.getY()) && obj.getHp() < obj.getMaxHp()) {
                inArea.add(obj);
            }
        }
        return inArea;
    }
}
