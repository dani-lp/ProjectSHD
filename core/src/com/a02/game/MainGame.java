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
	public static GameScreen mainGameScreen; //Sólo se utiliza para el método getRelativeMousePos(), es realmente necesario?
	static boolean tutorial;

	public static volatile boolean begin = false; //No cambiar "volatile", si no la CPU explota

	public MainGame() {
		tutorial = Settings.s.isTutorialCheck();
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
