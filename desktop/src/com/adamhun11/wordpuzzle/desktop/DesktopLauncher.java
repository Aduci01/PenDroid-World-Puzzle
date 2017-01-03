package com.adamhun11.wordpuzzle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.adamhun11.wordpuzzle.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int)(480 / 1.2f);
		config.height = (int)(800 / 1.2f);
		new LwjglApplication(new Main(), config);
	}
}
