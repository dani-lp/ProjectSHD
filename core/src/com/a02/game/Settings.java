package com.a02.game;

public class Settings {
    private String username;
    private double diff;
    private boolean musicCheck, soundCheck, tutorialCheck;
    private float volume = 0.5f;

    public static final Settings s = new Settings();

    private Settings(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public boolean isMusicCheck() {
        return musicCheck;
    }

    public void setMusicCheck(boolean musicCheck) {
        this.musicCheck = musicCheck;
    }

    public boolean isSoundCheck() {
        return soundCheck;
    }

    public void setSoundCheck(boolean soundCheck) {
        this.soundCheck = soundCheck;
    }

    public boolean isTutorialCheck() {
        return tutorialCheck;
    }

    public void setTutorialCheck(boolean tutorialCheck) {
        this.tutorialCheck = tutorialCheck;
    }

    public float getVolume() {
        return volume;
    }

    public void incVolume() {
        if (this.volume < 1.0f) volume += 0.1f;
    }

    public void decVolume() {
        if (this.volume > 0.1f) volume -= 0.1f;
    }
}
