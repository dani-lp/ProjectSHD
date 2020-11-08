package com.a02.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

import static com.a02.utils.Utils.*;

public class Enemy extends Entity{
    private int id;
    private String name;
    private int hp;
    private int attackDamage;
    private float speed;
    private int value;

    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> deathAnimation;

    enum State {
        WALKING, ATTACKING, DYING, DEAD
    }

    State state;

    public Enemy(float x, float y, int width, int height, String sprite, int id, String name, int hp, int attackDamage, float speed) {  //Constructor de enemigos
        super(x, y, width, height, sprite);
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.WALKING;
        this.walkAnimation = createAnimation("larry-walk.png", 3, 1, 0.2f);
        this.deathAnimation = createAnimation("larry-death.png", 2, 2, 0.25f);

        this.value = 50;
    }

    public Enemy() {        //Constructor vacio de enemigos
        super();
        this.id = -1;
        this.name = "";
        this.hp = 0;
        this.attackDamage = 0;
        this.speed = 0;

        this.value = 50;
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

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Actualiza la posición y estado de un enemigo.
     * @param beaconX Coordenada X del beacon
     * @param beaconY Coordenada Y del beacon
     * @param objects Arraylist de objetos defensivos
     * @param secTimer Reloj de juego
     */
    protected void update(float beaconX, float beaconY, List<GameObject> objects, List<Enemy> enemies, float secTimer) {
        switch (this.state) {
            case WALKING:
                this.move(beaconX, beaconY);    //Movimiento a beacon

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                }

                else if (this.overlapsArray(objects)) { //TODO: es posible que se pueda resumir en un sólo método
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                if (this.overlapsArray(objects)) {
                    GameObject tempObj = this.overlappedObject(objects);    //Objeto siendo colisionado

                    if (tempObj.getHp() > 0 && secTimer % 60 == 0) {    //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.attackDamage);
                    } else if (tempObj.getHp() <= 0) {
                        //target.delete()?
                        this.state = State.WALKING;
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                }

                break;

            case DYING:
                //playAnimation();
                try {
                    enemies.remove(this);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                this.state = State.DEAD;
                GameScreen.setGold(GameScreen.getGold() + this.value);
                break;

            case DEAD:  //Igual hay que eliminar manualmente el objeto, si es que es posible
                break;

        }
    }

    protected void move(float beaconX, float beaconY) {             //Dependiendo de donde se encuentre el beacon respecto al objeto se aumenta o decrementan la X y la Y hasta que sean iguales
        if (this.getX() < beaconX && this.getY() < beaconY) {                  //El beacon esta por encima y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() > beaconY) {            //El beacon esta por debajo y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() < beaconX && this.getY() == beaconY) {           //El beacon esta a la misma altura y a la derecha
            this.setX(this.getX() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() == beaconY) {           //El beacon esta a la misma altura y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() > beaconY) {            //El beacon esta por debajo y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() > beaconX && this.getY() < beaconY) {            //El beacon esta por encima y a la izquierda
            this.setX(this.getX() - this.speed * Gdx.graphics.getDeltaTime());
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() < beaconY) {           //El beacon esta por debajo y en la misma posicion
            this.setY(this.getY() + this.speed * Gdx.graphics.getDeltaTime());
        } else if (this.getX() == beaconX && this.getY() > beaconY) {           //El beacon esta por arriba y en la misma posicion
            this.setY(this.getY() - this.speed * Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Comprueba si existe colisión con algún objeto dentro de una Lista.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return True si hay colisión, false si no la hay.
     */
    protected boolean overlapsArray(List<GameObject> objects) {
        for (GameObject object: objects) {
            if (object instanceof Trap || object.isGrabbed()) {
                continue;
            }
            else if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Halla que objeto está colisionando con el enemigo.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return Objecto colisionado.
     */
    protected GameObject overlappedObject(List<GameObject> objects) {
        for (GameObject object: objects) {
            if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY()) {
                return object;
            }
        }
        return null;
    }
}
