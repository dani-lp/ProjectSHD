package com.a02.entity;

import com.a02.screens.GameScreen;

import static com.a02.game.Utils.createAnimation;

public class FinalBoss extends Enemy{
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
    }

    @Override
    public void update(GameScreen gs) {
        switch (this.state) {
            case IDLE:

                break;
            case WALKING:

                break;
            case ATTACKING:

                break;
            case SPAWNING:

                break;
            case DYING:

                break;
        }
    }
}
