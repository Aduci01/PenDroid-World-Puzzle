package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;

public class Main extends Game {
	
	@Override
	public void create ()
	{
		Levels.init();
		setScreen(new GameScreen(this, 3));
	}
}
