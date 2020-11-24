/* Trampas de 5 tipos:
 *  1.- BURN: resta daño por segundo durante x tiempo
 *  2.- ROOT: paraliza durante unos segundos
 *  3.- TELEPORT: mueve al enemigo a una posición aleatoria; baratas, pero con riesgo
 *  4.- DAMAGE: daña instantaneamente al enemigo
 *  5.- CONFUSE: cambia la posición a la que se dirige el enemigo temporalmente
 */

package com.a02.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Trap extends GameObject {
    private int attackDamage;
    private enum Effect {
        BURN, FREEZE, TELEPORT, DAMAGE, CONFUSE
    }
    private enum State {
        IDLE, ATTACKING, DYING
    }
    private Effect effect;
    private State state;
    private Enemy focusedEnemy;

    public Trap(int id, String type, int price,
                boolean unlocked, int hp, String effect, int attackDamage) {
        super(id, type, price, unlocked, hp);
        this.attackDamage = attackDamage;

        switch (effect) {   //Ajuste de textura y efecto
            case "BURN" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/fire.png")));
                this.effect = Effect.BURN;
                break;
            case "FREEZE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/freeze.png")));
                this.effect = Effect.FREEZE;
                break;
            case "TELEPORT" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/teleport.png")));
                this.effect = Effect.TELEPORT;
                break;
            case "DAMAGE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/damage.png")));
                this.effect = Effect.DAMAGE;
                break;
            case "CONFUSE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/confuse.png")));
                this.effect = Effect.CONFUSE;
                break;
            default:
                this.setTexture(new Texture(Gdx.files.internal("Traps/freeze.png")));
                this.effect = Effect.FREEZE;
                break;
        }

        this.state = State.IDLE;
    }

    public Trap(int id, String type, int price,
                boolean unlocked, int hp, String effect, int attackDamage, float x, float y) {
        super(id, type, price, unlocked, hp, x, y);
        this.attackDamage = attackDamage;

        switch (effect) {   //Ajuste de textura y efecto
            case "BURN" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/fire.png")));
                this.effect = Effect.BURN;
                break;
            case "FREEZE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/freeze.png")));
                this.effect = Effect.FREEZE;
                break;
            case "TELEPORT" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/teleport.png")));
                this.effect = Effect.TELEPORT;
                break;
            case "DAMAGE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/damage.png")));
                this.effect = Effect.DAMAGE;
                break;
            case "CONFUSE" :
                this.setTexture(new Texture(Gdx.files.internal("Traps/confuse.png")));
                this.effect = Effect.CONFUSE;
                break;
            default:
                this.setTexture(new Texture(Gdx.files.internal("Traps/freeze.png")));
                this.effect = Effect.FREEZE;
                break;
        }

        this.state = State.IDLE;
    }

    public Trap(Trap other) {
        super(other.getId(), other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.setX(other.getX());
        this.setY(other.getY());
        this.setWidth(16);
        this.setHeight(18);
        this.effect = other.getEffect();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
        this.setTexture(other.getTexture());
    }

    public Trap() {
        super();
        this.effect = Effect.FREEZE;
        this.attackDamage = 0;
        this.state = State.IDLE;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    //TODO: trampas restantes
    public void update(List<GameObject> objects, List<Enemy> enemies, float secTimer) {
        switch (this.state) {
            case IDLE:
                if (this.overlappedEnemy(enemies) != null) {
                    this.focusedEnemy = this.overlappedEnemy(enemies);
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING: //Aplica su efecto y se autodestruye
                switch (this.effect) {
                    case BURN :

                        this.state = State.DYING;
                        break;
                    case FREEZE :

                        this.state = State.DYING;
                        break;
                    case TELEPORT :
                        this.focusedEnemy.setX((float) Math.random() * 320);
                        this.focusedEnemy.setY((float) Math.random() * 180);
                        this.state = State.DYING;
                        break;
                    case DAMAGE :
                        this.focusedEnemy.setHp(focusedEnemy.getHp() - this.attackDamage) ;
                        this.state = State.DYING;
                        break;
                    case CONFUSE :

                        this.state = State.DYING;
                        break;
                }
                break;

            case DYING:
                this.setHp(0);
                break;
        }
        this.hpBar.update(this, this.getHp());
    }
}
