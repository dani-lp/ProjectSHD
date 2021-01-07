package com.a02.entity;

import com.a02.screens.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.a02.game.Utils.createAnimation;

public class FinalBoss extends Enemy{

    private int spawnTimer; //Usado para dejar espacio entre spawns
    private int spawningTimer; //Usado para tiempo DURANTE el spawn

    protected Animation<TextureRegion> spawnAnimation;

    public enum State {
        IDLE, WALKING, ATTACKING, SPAWNING, DYING
    }
    public State state;

    public FinalBoss() {
        this.state = State.IDLE;
    }

    @Override
    public void loadAnimations() {
        this.walkAnimation = createAnimation("Enemies/e8-walk.png",2,2,0.2f);
        this.attackAnimation = createAnimation("Enemies/e8-attack.png",2,2,0.25f);
        this.deathAnimation = createAnimation("Enemies/e8-death.png",3,1, 0.15f);
        this.spawnAnimation = createAnimation("Enemies/e8-spawn.png",2,2, 0.125f);
    }

    @Override
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:
                if (gs.secTimer >= this.getStartTime()){
                    if (this.trapEffect != TrapEffect.FREEZE) {
                        this.state = State.WALKING;
                        this.spawnTimer = 0;
                    }
                }
                break;

            case WALKING:
                this.updatePathfinding(gs);

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.setDeathTimer(gs.secTimer + 60);
                }
                else if (this.overlappedObject(gs) != null || this.overlappedObject(gs) instanceof Trap) {
                    if (!this.overlappedObject(gs).isGrabbed()) this.state = State.ATTACKING;
                }
                //Cada 10 segundos hay una probabilidad cada frame de spawnear minions
                if (this.spawnTimer > 480 && (int)(Math.random() * 100) == 2) { //TODO BALANCE
                    this.state = State.SPAWNING;
                    this.spawningTimer = 0;
                }

                this.spawnTimer++;
                break;

            case ATTACKING:
                if (this.overlappedObject(gs) != null) {
                    GameObject tempObj = this.overlappedObject(gs);    //Objeto siendo colisionado
                    if (tempObj.getHp() > 0 && !tempObj.isGrabbed() && gs.secTimer % 60 == 0) { //Pegar 1 vez por segundo
                        tempObj.setHp(tempObj.getHp() - this.getAttackDamage());
                    } else if (tempObj.getHp() <= 0) {
                        this.state = State.WALKING;
                    }
                }
                else {
                    this.state = State.WALKING;
                }

                if (this.getHp() <= 0) {
                    this.state = State.DYING;
                    this.setDeathTimer(gs.secTimer + 60);
                }
                //Cada 10 segundos hay una probabilidad cada frame de spawnear minions
                if (this.spawnTimer > 480 && (int)(Math.random() * 100) == 2) { //TODO BALANCE
                    this.state = State.SPAWNING;
                    this.spawningTimer = 0;
                }
                this.spawnTimer++;
                break;

            case SPAWNING:
                if (this.spawningTimer == 0) this.addMinions(gs);
                if (this.spawningTimer > 240) {//Después de 4 segundos de spawn vuelve a andar
                    this.state = State.WALKING;
                    this.spawnTimer = 0;
                }
                this.spawningTimer++;
                break;

            case DYING:
                this.hpBar = null;
                if (gs.secTimer > this.getDeathTimer()) {
                    gs.setGold(gs.getGold() + this.getGoldValue());
                    break;
                }
                break;
        }
        //this.updateEffect(gs); //TODO: al boss le afectan las trampas?
        if (this.hpBar != null) this.hpBar.update(this, this.getHp());
    }

    @Override
    public TextureRegion getCurrentAnimation(float animationTimer) {
        switch (this.state) {
            case IDLE:
                return new TextureRegion(this.idleTexture);
            case WALKING:
                return this.walkAnimation.getKeyFrame(animationTimer, true);
            case ATTACKING:
                return this.attackAnimation.getKeyFrame(animationTimer, true);
            case SPAWNING:
                return this.spawnAnimation.getKeyFrame(animationTimer, true);
            case DYING:
                return this.deathAnimation.getKeyFrame(animationTimer, true);
        }
        return null;
    }


    /**
     * Añade varios 'minions' a la plantilla de enemigos. Utilizado en el jefe final.
     */
    public void addMinions(GameScreen gs) {
        Enemy minion1 = new Enemy(this.getX() + 8,this.getY() - 8 ,16,16,6,450,50,
                30,gs.secTimer + 240, 50);
        Enemy minion2 = new Enemy(this.getX() + 8,this.getY() + 32,16,16,6,450,50,
                30,gs.secTimer + 240, 50);
        minion1.loadAnimations();
        minion2.loadAnimations();
        minion1.loadIdleTexture();
        minion2.loadIdleTexture();
        gs.enemies.add(minion1);
        gs.enemies.add(minion2);
    }
}
