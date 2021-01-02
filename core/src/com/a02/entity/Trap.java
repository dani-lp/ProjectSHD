/* Trampas de 5 tipos:
 *  1.- BURN: resta daño por segundo durante x tiempo
 *  2.- ROOT: paraliza durante unos segundos
 *  3.- TELEPORT: mueve al enemigo a una posición aleatoria; baratas, pero con riesgo
 *  4.- DAMAGE: daña instantaneamente al enemigo
 *  5.- CONFUSE: cambia la posición a la que se dirige el enemigo temporalmente
 */

package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.logging.Logger;

public class Trap extends GameObject {
    private final static Logger logger = Logger.getLogger(GameObject.class.getName());
    private int attackDamage;
    private String effect;
    public enum Effect {
        BURN, FREEZE, TELEPORT, DAMAGE, CONFUSE
    }
    private enum State {
        IDLE, ATTACKING, DYING
    }
    private Effect effects;
    private State state;
    private Enemy focusedEnemy;

    public Trap(float x, float y, int width, int height, int id, String type, int price,
                boolean unlocked, int hp, String effect, int attackDamage) {
        super(x, y, width, height, id, type, price, unlocked, hp);
        this.attackDamage = attackDamage;
        this.effect=effect;

        loadTextures();

        this.state = State.IDLE;
    }

    public void loadTextures(){
        switch (this.getId()) {   //Ajuste de textura y efecto
            case 0 :
                this.setTexture(new Texture(Gdx.files.internal("Traps/freeze.png")));
                this.effects = Effect.FREEZE;
                break;
            case 1 :
                this.setTexture(new Texture(Gdx.files.internal("Traps/fire.png")));
                this.effects = Effect.BURN;
                break;
            case 2 :
                this.setTexture(new Texture(Gdx.files.internal("Traps/damage.png")));
                this.effects = Effect.DAMAGE;
                break;
            case 3 :
                this.setTexture(new Texture(Gdx.files.internal("Traps/confuse.png")));
                this.effects = Effect.CONFUSE;
                break;
            case 4 :
                this.setTexture(new Texture(Gdx.files.internal("Traps/teleport.png")));
                this.effects = Effect.TELEPORT;
                break;
        }
    }

    public Trap(Trap other) {
        super(other.getX(), other.getY(), other.getWidth(), other.getHeight(), other.getId(),
               other.getType(), other.getPrice(), other.isUnlocked(), other.getHp());
        this.effects = other.getEffect();
        this.attackDamage = other.getAttackDamage();
        this.state = other.state;
        this.effect = other.effect;
        this.focusedEnemy = other.focusedEnemy;
        this.setAnimation(other.getAnimation());
        this.setTexture(other.getTexture());
    }

    public Trap() {
        super();
        this.effects = Effect.FREEZE;
        this.attackDamage = 0;
        this.state = State.IDLE;
        loadTextures();
    }

    public Effect getEffect() {
        return effects;
    }

    public void setEffect(Effect effect) {
        this.effects = effect;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (this.overlappedEnemy(gs) != null) {
                    this.focusedEnemy = this.overlappedEnemy(gs);
                    this.state = State.ATTACKING;
                }
                break;

            case ATTACKING: //Aplica su efecto y se autodestruye
                Enemy tempEnemy = this.overlappedEnemy(gs);    //Referencia temporal del enemigo
                switch (this.effects) {
                    case BURN :
                        tempEnemy.trapEffect = Enemy.TrapEffect.BURNING;
                        tempEnemy.setEffectTimer(gs.secTimer);
                        this.state = State.DYING;
                        logger.info("Quemado por trampa");
                        break;
                    case FREEZE :
                        tempEnemy.trapEffect = Enemy.TrapEffect.FREEZE;
                        tempEnemy.setEffectTimer(gs.secTimer);
                        this.state = State.DYING;
                        logger.info("Congelado por trampa");
                        break;
                    case TELEPORT :
                        this.focusedEnemy.setX((float) Math.random() * 320);
                        this.focusedEnemy.setY((float) Math.random() * 180);
                        this.state = State.DYING;
                        logger.info("Teleportado por trampa");
                        break;
                    case DAMAGE :
                        this.focusedEnemy.setHp(focusedEnemy.getHp() - this.attackDamage) ;
                        this.state = State.DYING;
                        logger.info("Dañado por trampa");
                        break;
                    case CONFUSE :
                        tempEnemy.trapEffect = Enemy.TrapEffect.CONFUSED;
                        tempEnemy.setEffectTimer(gs.secTimer);
                        this.state = State.DYING;
                        logger.info("Confundido por trampa");
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
