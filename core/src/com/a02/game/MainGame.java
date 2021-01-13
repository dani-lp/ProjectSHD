/*
 * Esta clase es llamada desde el método main. Gestiona la creación de
 * distintas Screens, y posee el SpriteBatch que se encarga del dibujado.
 */

package com.a02.game;

import com.a02.screens.MenuScreen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	public SpriteBatch entityBatch;
	public static OrthographicCamera cursorCamera;

	public static volatile boolean begin = false; //No cambiar "volatile", si no la CPU explota

	@Override
	public void create () {
		entityBatch = new SpriteBatch();
		cursorCamera = new OrthographicCamera();
		cursorCamera.setToOrtho(false, 320, 180);
		this.setScreen(new MenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
