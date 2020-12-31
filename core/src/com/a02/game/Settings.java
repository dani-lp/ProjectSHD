package com.a02.game;

public class Settings {
    private String gamemode, username;
    private double diff;
    private boolean musicCheck, soundCheck, tutorialCheck;

    public static final Settings s = new Settings();

    private Settings(){}

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

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
}
