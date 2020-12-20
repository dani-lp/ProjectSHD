/*
 * Esta clase es llamada desde el método main. Gestiona la creación de
 * distintas Screens, y posee el SpriteBatch que se encarga del dibujado.
 */

package com.a02.game;

import com.a02.screens.GameScreen;
import com.a02.screens.MenuScreen;
import com.a02.screens.PauseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	public SpriteBatch entityBatch;
	public static GameScreen mainGameScreen;

	public MainGame(String gamemode, double diff, boolean musicCheck, boolean soundCheck, boolean tutorialCheck) {
		Settings.settings.setGamemode(gamemode);
		Settings.settings.setDiff(diff);
		Settings.settings.setMusicCheck(musicCheck);
		Settings.settings.setSoundCheck(soundCheck);
		Settings.settings.setTutorialCheck(tutorialCheck);
	}

	@Override
	public void create () {
		entityBatch = new SpriteBatch();
		mainGameScreen = new GameScreen(this);
		this.setScreen(new MenuScreen(this));
	}

	public void render() {
		super.render();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
			System.exit(0);
		}
		/*
		else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			this.setScreen(new PauseScreen(this));
		}

		 */
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
