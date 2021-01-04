package com.a02.entity;

import com.a02.screens.GameScreen;
import com.a02.component.HealthBar;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static com.a02.game.Utils.*;

public class Enemy extends Entity {
    private int id;
    private int hp;
    private int attackDamage;
    private float speed;
    private int goldValue;
    private int startTime;  //Tiempo de inicio de movimiento
    private int effectTimer;    //Timer de efectos de las trampas
    private int deathTimer; //Timer de animación de muerte
    private Vector2 focus;
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> deathAnimation;

    public enum State {
        IDLE, WALKING, ATTACKING, DYING
    }
    public enum TrapEffect {
        BURNING, FREEZE, CONFUSED, NEUTRAL
    }

    public State state;
    public TrapEffect trapEffect;
    public boolean routing;

    public Enemy(float x, float y, int width, int height, int id, int hp, int attackDamage, float speed, int startTime, int goldValue) {  //Constructor de enemigos
        super(x, y, width, height);
        this.id = id;
        this.hp = hp;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.state = State.IDLE;
        this.trapEffect = TrapEffect.NEUTRAL;
        this.loadAnimations();
        this.startTime = startTime;
        this.goldValue = goldValue;
        this.hpBar = new HealthBar(this, hp);
        this.focus = new Vector2(0,0);
        this.routing = false;

    }

    public Enemy() {    //Constructor vacio de enemigos
        super();
        this.id = -1;
        this.hp = 0;
        this.attackDamage = 0;
        this.speed = 0;
        this.state = State.IDLE;
        this.trapEffect = TrapEffect.NEUTRAL;
        this.hpBar = new HealthBar(this, 0);
        this.goldValue = 50;
        this.focus = new Vector2(0,0);
        this.routing = false;
        this.loadAnimations();
    }

    public void loadAnimations() {
        switch (this.getId()){
            case 0:
                this.walkAnimation = createAnimation("Enemies/e1-walk.png",3,1,0.2f);
                this.attackAnimation = createAnimation("Enemies/e1-attack.png",2,2,0.2f);
                this.deathAnimation = createAnimation("Enemies/e1-death.png",2,1, 0.2f);
                break;
            case 1:
                this.walkAnimation = createAnimation("Enemies/e2-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e2-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e2-death.png",2,1, 0.2f);
                break;
            case 2:
                this.walkAnimation = createAnimation("Enemies/e3-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e3-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e3-death.png",2,1, 0.2f);
                break;
            case 3:
                this.walkAnimation = createAnimation("Enemies/e4-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e4-attack.png",2,2,0.125f);
                this.deathAnimation = createAnimation("Enemies/e4-death.png",2,1, 0.2f);
                break;
            case 4:
                this.walkAnimation = createAnimation("Enemies/e5-walk.png",2,2,0.5f); //MUY LENTO (el movimiento)
                this.attackAnimation = createAnimation("Enemies/e5-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e5-death.png",2,1, 0.2f);
                break;
            case 5:
                this.walkAnimation = createAnimation("Enemies/e6-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e6-attack.png",5,1,0.2f);
                this.deathAnimation = createAnimation("Enemies/e6-death.png",2,1, 0.2f);
                break;
            case 6:
                this.walkAnimation = createAnimation("Enemies/e7-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e7-attack.png",2,2,0.125f);
                this.deathAnimation = createAnimation("Enemies/e7-death.png",2,1, 0.2f);
                break;
            case 7: //BOSS
                this.walkAnimation = createAnimation("Enemies/e8-walk.png",2,2,0.2f);
                this.attackAnimation = createAnimation("Enemies/e8-attack.png",2,2,0.25f);
                this.deathAnimation = createAnimation("Enemies/e8-death.png",3,1, 0.15f);
                break;
        }
    }

    /**
     * Devuelve la textura Idle de un enemigo, usada en congelación.
     * @return Texture idle
     */
    private Texture getIdleTexture() {
        if (this.getId() >= 0 && this.getId() <= 7) return new Texture("Enemies/e" + (this.getId()+1) + "-idle.png");
        else return new Texture(Gdx.files.internal("empty.png"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setEffectTimer(int effectTimer) {
        this.effectTimer = effectTimer;
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

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setFocus(float x, float y) {
        this.focus.x = x;
        this.focus.y = y;
    }

    public float getFocusX() {
        return this.focus.x;
    }

    public float getFocusY() {
        return this.focus.y;
    }

    public boolean isRouting() {
        return routing;
    }

    public void setRouting(boolean routing) {
        this.routing = routing;
    }

    public int getDeathTimer() {
        return deathTimer;
    }

    /**
     * Actualiza la posición, estado y efectos de un enemigo.
     * @param gs GameScreen utilizada
     */
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (gs.secTimer >= this.startTime){
                    if (this.trapEffect != TrapEffect.FREEZE) {
                        this.state = State.WALKING;
                    }
                }
                break;

            case WALKING: //Movimiento a beacon

                if (this.overlappedObstacle(gs) != null && !this.isRouting()){
                    this.route(gs);
                }
                else {
                    this.move();
                }
                if (routing && this.getY() < this.getFocusY() + 5 && this.getY() > this.getFocusY() - 5){
                    this.setFocus(125,this.getFocusY());
                }
                if (routing && this.getX() > 110){
                    this.setFocus(gs.beacon.getX(),gs.beacon.getY());
                }
                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.deathTimer = gs.secTimer + 60;
                }
                else if (this.overlappedObject(gs) != null || this.overlappedObject(gs) instanceof Trap) {
                    if (!this.overlappedObject(gs).isGrabbed()) this.state = State.ATTACKING;
                }
                break;

            case ATTACKING:
                System.out.println("Ey");
                if (this.overlappedObject(gs) != null) {
                    GameObject tempObj = this.overlappedObject(gs);    //Objeto siendo colisionado
                    if (tempObj.getHp() > 0 && !tempObj.isGrabbed() && gs.secTimer % 60 == 0) { //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.attackDamage);
                    } else if (tempObj.getHp() <= 0) {
                        this.state = State.WALKING;
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.deathTimer = gs.secTimer + 60;
                }

                break;

            case DYING:
                this.hpBar = null;
                if (gs.secTimer > this.deathTimer) {
                    GameScreen.setGold(GameScreen.getGold() + this.goldValue);
                    break;
                }
                break;
        }

        switch (this.trapEffect) {
            case BURNING:
                if ((gs.secTimer % 30 == 0) && (gs.secTimer < this.effectTimer + 180)){
                    this.hp -= 30;
                    this.trapEffect = TrapEffect.NEUTRAL;
                }
                break;
            case FREEZE:
                this.state = State.IDLE;
                if (gs.secTimer > this.effectTimer + 300) {
                    this.trapEffect = TrapEffect.NEUTRAL;
                    this.state = State.WALKING;
                }
                break;
            case CONFUSED:
                if (gs.secTimer == this.effectTimer) {
                    this.focus.x = (float)(Math.random() * 3200 - 1600); //Posición X máxima
                    this.focus.y = (float)(Math.random() * 1800 - 900); //Posición Y máxima
                }
                if (gs.secTimer > this.effectTimer + 280) {
                    this.trapEffect = TrapEffect.NEUTRAL;
                    this.focus.x = gs.beacon.getX();
                    this.focus.y = gs.beacon.getY();
                }
            case NEUTRAL:
                break;
        }
        if (this.hpBar != null) this.hpBar.update(this, this.getHp());
    }

    public boolean flipped = false; //TODO temporal?

    protected void move() {
        double angle = Math.toDegrees(-Math.atan((this.getY() - this.focus.y) / (this.getX() - this.focus.x)));
        this.flipped = Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed < 0;
        this.setX((float) (this.getX() + Math.sin(angle) * Gdx.graphics.getDeltaTime() * this.speed));
        this.setY((float) (this.getY() + Math.cos(angle) * Gdx.graphics.getDeltaTime() * this.speed));
    }

    /**
     * Comprueba si existe colisión con algún objeto dentro de una Lista.
     * @param objects List de objetos con los que es posible hallar colisión.
     * @return True si hay colisión, false si no la hay.
     */
    protected boolean overlapsArray(List<GameObject> objects) {
        for (GameObject object: objects) {
            if (!(object instanceof Trap) && !object.isGrabbed()) {
                if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                        this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Halla qué objeto está colisionando con el enemigo, excluyendo los objetos del inventario.
     * @param gs GameScreen utilizada
     * @return Objecto colisionado.
     */
    protected GameObject overlappedObject(GameScreen gs) {
        for (GameObject object: gs.objects) {
            if (this.getX() < object.getX() + object.getWidth() && this.getX() + this.getWidth() > object.getX() &&
                    this.getY() < object.getY() + object.getHeight() && this.getY() + this.getHeight() > object.getY() &&
                    !object.isInInventory(gs)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Devuelve un TextureRegion con la animación correspondiente al estado
     * @param animationTimer Reloj de la animación
     * @return El TextureRegion de animación
     */
    public TextureRegion getCurrentAnimation(float animationTimer) {
        switch (this.state) {
            case IDLE:
                return new TextureRegion(this.getIdleTexture());
            case WALKING:
                return this.walkAnimation.getKeyFrame(animationTimer, true);
            case ATTACKING:
                return this.attackAnimation.getKeyFrame(animationTimer, true);
            case DYING:
                return this.deathAnimation.getKeyFrame(animationTimer, true);
        }
        return null;
    }

    protected Obstacle overlappedObstacle(GameScreen gs) {
        for (Obstacle obstacle: gs.obstacles) {
            if (this.getX() < obstacle.getX() + obstacle.getWidth() && this.getX() + this.getWidth() > obstacle.getX() &&
                    this.getY() < obstacle.getY() + obstacle.getHeight() && this.getY() + this.getHeight() > obstacle.getY() ) {
                return obstacle;
            }
        }
        return null;
    }

    protected void route(GameScreen gs){
        this.setRouting(true);
        switch (gs.map.getSprite()){ //TODO: cambiar por currentRound una vez que estén creadas
            case "riverMap.png":
                if (this.getY() >= 0 && this.getY() <= 54){
                    this.setFocus(30,25);
                    this.move();

                } else if (this.getY() >= 55 && this.getY() <= 125){
                    this.setFocus(30,90);
                    this.move();

                }  else {
                    this.setFocus(30,135);
                    this.move();
                }
        }

    }
}
