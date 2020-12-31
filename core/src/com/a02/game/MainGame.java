/*
 * Esta clase es llamada desde el método main. Gestiona la creación de
 * distintas Screens, y posee el SpriteBatch que se encarga del dibujado.
 */

package com.a02.game;

import com.a02.screens.GameScreen;
import com.a02.screens.MenuScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	public SpriteBatch entityBatch;
	public static GameScreen mainGameScreen;
	static boolean tutorial;

	public MainGame(String gamemode, double diff, boolean musicCheck, boolean soundCheck, boolean tutorialCheck, String username) {
		Settings.s.setGamemode(gamemode);
		Settings.s.setDiff(diff);
		Settings.s.setMusicCheck(musicCheck);
		Settings.s.setSoundCheck(soundCheck);
		Settings.s.setTutorialCheck(tutorialCheck);
		tutorial=tutorialCheck;
		Settings.s.setUsername(username);
	}
	public static boolean tutorial(){
		return tutorial;
	}
	@Override
	public void create () {
		entityBatch = new SpriteBatch();
		mainGameScreen = new GameScreen(this, 0);
		this.setScreen(new MenuScreen(this,tutorial()));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
