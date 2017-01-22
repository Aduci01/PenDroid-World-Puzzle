package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;

public class Main extends Game {
	@Override
	public void create ()
	{
		com.adamhun11.wordpuzzle.Game.Levels.init();
		setScreen(new com.adamhun11.wordpuzzle.Screens.SplashScreen(this));
	}
}
