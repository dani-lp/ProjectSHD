package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

import static com.a02.game.Utils.createAnimation;

public class Defender extends GameObject {

    private enum State {
        IDLE, HEALING
    }

    State state;

    public Defender(float x, float y, int width, int height, int id, String type, int price,
                    boolean unlocked, int hp) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.state = State.IDLE;
        loadTextures();
    }

    private ArrayList<GameObject> hurt = new ArrayList<GameObject>();

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
        loadTextures();
    }

    public Defender(Defender other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(), other.getType(),
                other.getPrice(), other.isUnlocked(), other.getHp());
        this.state = other.state;
        this.setAnimation(other.getAnimation());
        this.hurt = other.hurt;
        this.setTexture(other.getTexture());
    }

    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (this.getId() == 4 && !this.isInInventory(gs)) {
                    hurt = overlappedArea(gs);
                    if (hurt.size() > 0) {
                        this.state = State.HEALING;
                    }
                }
                break;
            case HEALING:
                if (!this.isInInventory(gs)){
                    System.out.println(this.getHp());
                }

                boolean healed=false;
                if (gs.secTimer % 120 == 0) {
                    for (GameObject obj : hurt) {
                        if (obj.getHp() < obj.getMaxHp()) {
                            obj.setHp(obj.getHp() + 75);
                            healed=true;
                        }
                    }
                    if (this.getX() == 261){
                        this.getHp();
                    }
                    if (healed){
                        this.setHp(this.getHp() - 75);
                    }
                }
                break;
        }
        this.hpBar.update(this, this.getHp());
    }

    /**
     * Devuelve el primer GameObject con el que se detecta colisión.
     * @param gs Screen de juego
     * @return GameObject colisionado
     */
    public GameObject overlappedObject(GameScreen gs) {
        for (GameObject obj : gs.objects) {
            if (this.getX() < obj.getX() + obj.getWidth() && this.getX() + this.getWidth() > obj.getX() &&
                    this.getY() < obj.getY() + obj.getHeight() && this.getY() + this.getHeight() > obj.getY()) {
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
        ArrayList<GameObject> inArea = new ArrayList<GameObject>();
        for (GameObject obj : gs.objects) {
            if ((obj.getX() < this.getX() + 50 && obj.getX() > this.getX() - 50) && (obj.getY() < this.getY() + 50 && obj.getY() > this.getY() - 50) && !(obj.getX() == this.getX() && obj.getY() == this.getY()) && obj.getHp() < obj.getMaxHp()) {
                inArea.add(obj);
            }
        }
        return inArea;
    }
}
