package com.a02.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
		super.render(); //important!
	}

	public void dispose() {
		entityBatch.dispose();
	}
}
