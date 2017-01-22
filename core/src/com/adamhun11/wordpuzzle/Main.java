package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class Main extends Game {
	public AssetManager assets;
	final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
	private boolean lettersLoaded = false;

	@Override
	public void create ()
	{
		com.adamhun11.wordpuzzle.Game.Levels.init();

		loadAssets();
		assets.finishLoading(); //TODO load asynchronously

		setScreen(new com.adamhun11.wordpuzzle.Screens.SplashScreen(this));
	}

	private void loadAssets()
	{
		assets = new AssetManager();

		assets.load("GUI/circle.png", Texture.class);
		assets.load("GUI/transparent.png", Texture.class);
		assets.load("GUI/buttons/fb.png", Texture.class);

		/*
		FileHandleResolver resolver = assets.getFileHandleResolver();
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreeTypeFontLoaderParameter font = new FreeTypeFontLoaderParameter();
		font.fontFileName = "GUI/font.ttf";
		font.fontParameters.size = Gdx.graphics.getWidth() / 399;
		assets.load("GUI/font-black.ttf", BitmapFont.class, font);
		font.fontParameters.color = new Color(0.3f,0.3f,0.3f,1);
		assets.load("GUI/font-grey.ttf", BitmapFont.class, font);
		*/

		for(int i = 0; i < alphabet.length(); i++)
			assets.load("letters/" + alphabet.charAt(i) + ".png", Texture.class);

		assets.load("GUI/buttons/pause.png", Texture.class);
		assets.load("GUI/buttons/exit.png", Texture.class);
		assets.load("GUI/buttons/resume.png", Texture.class);
		assets.load("GUI/buttons/restart.png", Texture.class);
		assets.load("GUI/menus/pause_menu.png", Texture.class);
	}

	@Override
	public void render() {
		super.render();

		assets.update();

		if(!lettersLoaded)
		{
			for(int i = 0; i < alphabet.length(); i++)
				if(assets.isLoaded("letters/" + alphabet.charAt(i) + ".png"))
				{
					assets.get("letters/" + alphabet.charAt(i) + ".png", Texture.class)
							.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				}
		}
	}
}
