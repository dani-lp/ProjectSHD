package com.a02.component;

import com.a02.game.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundPlayer {
    private Sound arrow, coins, github, hammer, horn, roar, roundEnd;
    private final float SOUND_VOLUME = 0.7f;

    public SoundPlayer() {
        this.arrow = Gdx.audio.newSound(Gdx.files.internal("Sounds/arrow.mp3"));
        this.coins = Gdx.audio.newSound(Gdx.files.internal("Sounds/coins.mp3"));
        this.github = Gdx.audio.newSound(Gdx.files.internal("Sounds/github.mp3"));
        this.hammer = Gdx.audio.newSound(Gdx.files.internal("Sounds/hammer.mp3"));
        this.horn = Gdx.audio.newSound(Gdx.files.internal("Sounds/horn.mp3"));
        this.roar = Gdx.audio.newSound(Gdx.files.internal("Sounds/roar.mp3"));
        this.roundEnd = Gdx.audio.newSound(Gdx.files.internal("Sounds/round_end.mp3"));
    }

    public void playArrow() {
        if (Settings.s.isSoundCheck()) this.arrow.play(SOUND_VOLUME);
    }
    public void playCoins() {
        if (Settings.s.isSoundCheck()) this.coins.play(SOUND_VOLUME);
    }
    public void playGithub() {
        if (Settings.s.isSoundCheck()) this.github.play(SOUND_VOLUME);
    }
    public void playHammer() {
        if (Settings.s.isSoundCheck()) this.hammer.play(SOUND_VOLUME);
    }
    public void playHorn() {
        if (Settings.s.isSoundCheck()) this.horn.play(SOUND_VOLUME);
    }
    public void playRoar() {
        if (Settings.s.isSoundCheck()) this.roar.play(SOUND_VOLUME);
    }
    public void playRoundEnd() {
        if (Settings.s.isSoundCheck()) this.roundEnd.play(SOUND_VOLUME);
    }

    public void dispose() {
        this.arrow.dispose();
        this.coins.dispose();
        this.github.dispose();
        this.hammer.dispose();
        this.horn.dispose();
        this.roar.dispose();
        this.roundEnd.dispose();
    }
}