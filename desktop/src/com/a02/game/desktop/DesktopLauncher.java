package com.a02.game.desktop;

import com.a02.users.windows.LoginWindow;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.a02.game.MainGame;

import javax.swing.*;

public class DesktopLauncher {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginWindow();
			}
		});
		while(!MainGame.begin); //Espera a que el flag 'begin' se active

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 180;
		config.fullscreen = true;
		config.foregroundFPS = 60;
		config.addIcon("boredlion.png", Files.FileType.Internal);
		new LwjglApplication(new MainGame(), config);
	}
}
