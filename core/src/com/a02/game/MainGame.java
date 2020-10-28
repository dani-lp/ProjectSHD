/**
 * Esta clase es llamada desde el método main. Gestiona la creación de
 * distintas Screens, y posee el SpriteBatch que se encarga del dibujado.
 */

package com.a02.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class MainGame extends Game {
	public SpriteBatch entityBatch;

	@Override
	public void create () {
		entityBatch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
