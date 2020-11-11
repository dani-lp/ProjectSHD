package com.a02.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.a02.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 180;
		config.fullscreen = !true;
		config.foregroundFPS = 60;
		config.addIcon("boredlion.png", Files.FileType.Internal);
		new LwjglApplication(new MainGame(), config);
	}
}
