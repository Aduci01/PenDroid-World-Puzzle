package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Main extends Game {
	public AssetManager assets;
	final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
	private boolean lettersLoaded = false;
	public BitmapFont font;
	private Texture bg;
	private SpriteBatch spriteBatch;
	private boolean loaded = false;

	public static PlayServices playServices;

	public Main(PlayServices playServices)
	{
		this.playServices = playServices;
	}
	public Main(){}

	@Override
	public void create ()
	{
		com.adamhun11.wordpuzzle.Game.Levels.init();
		bg = new Texture("GUI/bg/background.png");
		spriteBatch = new SpriteBatch();
		loadAssets();
		//assets.finishLoading(); //TODO load asynchronously

	}

	private void loadAssets()
	{
		assets = new AssetManager();

		assets.load("GUI/circle.png", Texture.class);
		assets.load("GUI/transparent.png", Texture.class);
		assets.load("GUI/buttons/fb.png", Texture.class);
		assets.load("GUI/bg/background.png", Texture.class);


		/*FileHandleResolver resolver = assets.getFileHandleResolver();
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		font.fontFileName = "GUI/font.ttf";
		font.fontParameters.size = (int)(120 * ((float)Gdx.graphics.getWidth()) / 399) / 2;
		font.fontParameters.color = new Color(Color.WHITE);
		assets.load("GUI/font-white.ttf", BitmapFont.class, font);*/


		SmartFontGenerator fontGen = new SmartFontGenerator();
		FileHandle exoFile = Gdx.files.internal("GUI/font.ttf");
		font = fontGen.createFont(exoFile, "font",
				(int)(120 * ((float)Gdx.graphics.getWidth()) / 399));
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


		if (!assets.update()) {
			spriteBatch.begin();
			spriteBatch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			spriteBatch.end();

		}else if (!loaded){
			setScreen(new com.adamhun11.wordpuzzle.Screens.Menu(this));
			loaded = true;
		}


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
