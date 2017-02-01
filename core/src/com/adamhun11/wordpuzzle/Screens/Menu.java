package com.adamhun11.wordpuzzle.Screens;

import com.adamhun11.wordpuzzle.Facebook;
import com.adamhun11.wordpuzzle.Main;
import com.adamhun11.wordpuzzle.SmartFontGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import sun.rmi.runtime.Log;

/**
 * Created by Adam on 2017. 01. 13..
 */

public class Menu implements Screen {
    Main game;
    Stage stage;
    Skin skin;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    BitmapFont lvlFont;

    Table levelTable;
    ScrollPane scrollPane;

    Sprite circle;

    float wx = (float) Gdx.graphics.getWidth() / 399f;
    float hx = (float) Gdx.graphics.getHeight() / 666f;

    Facebook facebook;

    int unlockedLevel;

    boolean inMulti = false;

    float timer = 0;


    public Menu(Main g){
        game = g;
        facebook = new Facebook();
        facebook.init();

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initFonts();

        Preferences prefs = Gdx.app.getPreferences("datas");
        unlockedLevel = prefs.getInteger("unlockedLevel", 1);
        initStage();
        stage.addAction(Actions.sequence(Actions.fadeOut(0), Actions.fadeIn(1f)));
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
    }

    private void initFonts(){
        lvlFont = game.font;
    }

    private void initStage(){
        circle = new Sprite(game.assets.get("GUI/circle.png", Texture.class));

        stage = new Stage();
        skin = new Skin();
        skin.add("a", game.assets.get("GUI/transparent.png", Texture.class));
        skin.add("fb", game.assets.get("GUI/buttons/fb.png", Texture.class));
        skin.add("achievements", game.assets.get("GUI/buttons/achievements.png"), Texture.class);
        skin.add("leaderboard", new Texture("GUI/buttons/leaderboard.png"));
        skin.add("up", new Texture("GUI/buttons/up.png"));
        skin.add("down", new Texture("GUI/buttons/down.png"));
        skin.add("multiplayer", new Texture("GUI/buttons/multiplayer.png"));

        Image image = new Image(circle);
        image.setSize(200 * wx, 200 * wx);
        image.setPosition(Gdx.graphics.getWidth() / 2 - 100 * wx, Gdx.graphics.getHeight() / 2 - 100 * hx);
        stage.addActor(image);

        Image image2 = new Image(new Texture("GUI/bg/lettersbg.png"));
        image2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(image2);

        BitmapFont bfont = new BitmapFont();
        skin.add("default",bfont);

        levelTable = new Table();
        levelTable.setPosition(0,0);

        for (int i = 1; i <= com.adamhun11.wordpuzzle.Game.Levels.levels.size; i++){
            levelTable.add(createTextButton("a", i)).padBottom(50 * hx).padTop(50 * hx);
            levelTable.row();
        }

        scrollPane = new ScrollPane(levelTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setPosition(Gdx.graphics.getWidth() / 2 - 55 * wx, Gdx.graphics.getHeight() / 2 - 95 * hx);
        scrollPane.setSize(120 * wx, 190 * wx);
        stage.addActor(scrollPane);


        //Facbook button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("fb", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("fb", Color.BLUE);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton button = new TextButton("",textButtonStyle);
        button.setSize(wx * 55, hx * 55);
        button.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                facebook.logIn();
                facebook.share();
            }
        });
        stage.addActor(button);

        //Achievement button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("achievements", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("achievements", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("achievements", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("achievements", Color.LIME);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton achievementButton = new TextButton("",textButtonStyle);
        achievementButton.setSize(wx * 55, hx * 55);
        achievementButton.setPosition((achievementButton.getWidth() * 1f + 2), 0);
        achievementButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.playServices.showAchievement();
            }
        });
        stage.addActor(achievementButton);

        //LEADERBOARD button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("leaderboard", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("leaderboard", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("leaderboard", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("leaderboard", Color.LIME);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton leaderboardButton = new TextButton("",textButtonStyle);
        leaderboardButton.setSize(wx * 55, hx * 55);
        leaderboardButton.setPosition((achievementButton.getWidth() * 2f + 4), 0);
        leaderboardButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.playServices.showScore();
            }
        });
        stage.addActor(leaderboardButton);

        //UP selector button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("up", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("up", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("up", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("up", Color.LIME);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton upButton = new TextButton("",textButtonStyle);
        upButton.setSize(wx * 75, hx * 75);
        upButton.setPosition(200 * wx - upButton.getWidth() * 2, 320 * hx);
        upButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                scrollPane.layout();
                scrollPane.setScrollPercentY(((int)((scrollPane.getScrollY()) / ((96 + 100) * wx)) - 1) * (96 + 100) * wx / (2162 * wx));
            }
        });
        stage.addActor(upButton);

        //DOWN selector button
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("down", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("down", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("down", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("down", Color.LIME);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton downButton = new TextButton("",textButtonStyle);
        downButton.setSize(wx * 75, hx * 75);
        downButton.setPosition(200 * wx + downButton.getWidth(), 280 * hx);
        downButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                scrollPane.layout();
                scrollPane.setScrollPercentY(((int)((scrollPane.getScrollY()) / ((96 + 100) * wx)) + 1) * (96 + 100) * wx / (2162 * wx));
            }
        });
        stage.addActor(downButton);

        //MULTIPLAYER buttons
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("multiplayer", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("multiplayer", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("multiplayer", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("multiplayer", Color.LIME);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton multiplayerButton = new TextButton("",textButtonStyle);
        multiplayerButton.setSize(wx * 75, hx * 75);
        multiplayerButton.setPosition(200 * wx - multiplayerButton.getWidth() / 2, 200 * hx - multiplayerButton.getHeight());
        multiplayerButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                if (game.playServices.isSignedIn() && !inMulti){
                    game.playServices.quickGame();
                    inMulti = true;
                } else game.playServices.signIn();
            }
        });
        stage.addActor(multiplayerButton);
    }

    private TextButton createTextButton(String name, int num){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        final TextButton button;
        textButtonStyle.up = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.down = skin.newDrawable(name, Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.over = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.font = lvlFont;

        skin.add("default", textButtonStyle);

        button = new TextButton("",textButtonStyle);
        button.setSize(wx * button.getWidth(), hx * button.getHeight());
        button.setText(Integer.toString(num));
        final int lvl = num;
        if (unlockedLevel >= num) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, lvl));
                }
            });
        } else button.getLabel().setColor(Color.DARK_GRAY);
        return button;
    }




    @Override
    public void show() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (inMulti) {
            timer += delta;
            if (timer > 10f) {
                inMulti = false;
                timer = 0;
            }
        }
        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
