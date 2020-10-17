package com.a02.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	GameObject smile;
	@Override
	public void create () {
		batch = new SpriteBatch();
		smile= new GameObject(1,"smile",0,0,"badlogic.jpg",0,0);
		img= new Texture(smile.getSprite());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, smile.getX(), smile.getY());
		batch.end();



	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
