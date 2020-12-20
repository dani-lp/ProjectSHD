package com.a02.game;

public class Settings {
    private String gamemode;
    private double diff;
    private boolean musicCheck, soundCheck, tutorialCheck;

    public static final Settings settings = new Settings();

    private Settings(){}

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
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
}
