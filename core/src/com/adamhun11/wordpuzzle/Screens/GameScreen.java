package com.adamhun11.wordpuzzle.Screens;

import com.adamhun11.wordpuzzle.Game.Levels;
import com.adamhun11.wordpuzzle.Main;
import com.adamhun11.wordpuzzle.SmartFontGenerator;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Adam on 2017. 01. 01..
 */

public class GameScreen implements Screen {
    Main game;
    com.adamhun11.wordpuzzle.Game.GameLogic gameLogic;

    Stage stage;
    Table pauseTable, skipLevelTable;
    Skin skin;
    Skin coinSkin;

    float wx = (float) Gdx.graphics.getWidth() / 399f;
    float hx = (float) Gdx.graphics.getHeight() / 666f;


    boolean paused = false;
    Preferences prefs;

    TextButton coinLabel;

    public GameScreen(Main g, int lvlNum) {
        stage = new Stage();
        skin = new Skin();
        game = g;
        pauseTable = new Table();
        pauseTable.setPosition(-5000f, -5000f);
        pauseTable.addAction(Actions.fadeOut(0));
        pauseTable.setSize(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 2);

        skipLevelTable = new Table();
        skipLevelTable.setSize(Gdx.graphics.getWidth() / 4f, 178 / 5 * hx);
        skipLevelTable.setPosition(Gdx.graphics.getWidth() - 178 / 4 * wx - skipLevelTable.getWidth() - 10 * wx, Gdx.graphics.getHeight() - skipLevelTable.getHeight() - 5 * hx);


        skin.add("pause", new Texture("GUI/buttons/pause.png"));
        skin.add("exit", new Texture("GUI/buttons/exit.png"));
        skin.add("resume", new Texture("GUI/buttons/resume.png"));
        skin.add("restart", new Texture("GUI/buttons/restart.png"));
        skin.add("bg", new Texture("GUI/menus/pause_menu.png"));
        skin.add("transparent", new Texture("GUI/transparent.png"));
        skin.add("tablebg", new Texture("GUI/menus/table.png"));
        skin.add("skip", new Texture("GUI/buttons/skip.png"));

        coinSkin = new Skin();
        coinSkin.add("coinbg", new Texture("GUI/coins.png"));

        BitmapFont defaultFont = new BitmapFont();
        skin.add("default", defaultFont);

        BitmapFont bfont;
        /*SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("GUI/font.ttf");
        bfont = fontGen.createFont(exoFile, "font", (int)(120 * wx));*/
        bfont = game.font;
        coinSkin.add("default", bfont);



        /*if (Levels.levels.get(lvlNum - 1).letterTexture.equals("normal")) {
            Image image = new Image(new Texture("GUI/bg/background.png"));
            image.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            image.setPosition(0, 0);
            stage.addActor(image);
        }*/

        Table coinTable = new Table();
        Image coins = new Image(new Texture("GUI/coins.png"));
        coinTable.setSize(coins.getWidth() * ((399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() / coins.getHeight()) * wx, (399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() * wx);
        coins.setSize(coins.getWidth() * ((399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() / coins.getHeight()) * wx, (399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() * wx);
        coins.setPosition(0, Gdx.graphics.getHeight() - (399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() * wx - coins.getHeight());
        coinTable.setPosition(0, Gdx.graphics.getHeight() - (399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() * wx - coins.getHeight());

        prefs = Gdx.app.getPreferences("datas");
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = coinSkin.getFont("default");
        coinLabel = createCoinButton("transparent");
        coinLabel.getLabel().setText(Integer.toString(prefs.getInteger("coins", 0)));
        coinLabel.getLabel().setFontScale(0.28f - 0.02f * Integer.toString(prefs.getInteger("coins", 0)).length());
        coinLabel.setName("coinLabel");

        coinTable.add(coinLabel).padLeft(25 * wx);

        coinTable.setSkin(coinSkin);
        coinTable.setBackground("coinbg");

        stage.addActor(coinTable);


        initButtons();
        stage.addActor(pauseTable);
        stage.addActor(skipLevelTable);

        gameLogic = new com.adamhun11.wordpuzzle.Game.GameLogic(game, stage, lvlNum);
        stage.addAction(Actions.sequence(Actions.fadeOut(0f), Actions.fadeIn(0.5f)));
    }

    private void initButtons(){
        //PAUSE button
        final TextButton pauseButton = createTextButton("pause");
        pauseButton.setSize(pauseButton.getWidth() * wx / 4, pauseButton.getHeight() * hx / 4);

        pauseButton.setPosition(Gdx.graphics.getWidth() - pauseButton.getWidth(), Gdx.graphics.getHeight() - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                pauseTable.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 4),Actions.fadeIn(0.3f)));
                paused = true;
            }
        });
        stage.addActor(pauseButton);

        //FILL PAUSETABLE table
        TextButton resume = createTextButton("resume");
        resume.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                pauseTable.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.moveTo(-5000f, -5000f, 0.4f)));
                paused = false;
            }
        });
        pauseTable.add(resume).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);
        pauseTable.row();

        TextButton restart = createTextButton("restart");
        restart.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                pauseTable.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.moveTo(-5000f, -5000f, 0.4f)));
                gameLogic.solved = true;
                coinLabel.setText(Integer.toString(Integer.valueOf(coinLabel.getLabel().getText().toString()) - gameLogic.addCoin));
                gameLogic.endTransition = true;
                paused = false;
            }
        });
        pauseTable.add(restart).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);
        pauseTable.row();

        TextButton exit = createTextButton("exit");
        exit.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                game.setScreen(new Menu(game));
            }
        });
        pauseTable.add(exit).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);
        pauseTable.setSkin(skin);
        pauseTable.setBackground("bg");

        //SKIP LEVEL
        skipLevelTable.setSkin(skin);
        skipLevelTable.setBackground("tablebg");

        TextButton skipButton = createTextButton("skip");
        skipButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                if (Integer.valueOf(coinLabel.getLabel().getText().toString()) >= 15){
                    if (!gameLogic.endTransition && !gameLogic.transition){
                        prefs.putInteger("coins", prefs.getInteger("coins", 0) - 15);
                    coinLabel.getLabel().setText(Integer.toString(Integer.valueOf(coinLabel.getLabel().getText().toString()) - 15));
                    gameLogic.solved = true;
                    gameLogic.endTransition = true;
                    gameLogic.increaseLevelNum(1);
                        prefs.flush();
                    }
                }
            }
        });
        skipLevelTable.add(skipButton).width(skipButton.getWidth() * wx / 5).height(skipButton.getHeight() * hx / 6).padLeft(10 * wx);

        TextButton costLabel = createCoinButton("transparent");
        costLabel.getLabel().setText("15");
        costLabel.getLabel().setFontScale(0.28f);
        costLabel.setSize(costLabel.getWidth() * wx / 4, costLabel.getHeight() * hx / 4);
        skipLevelTable.add(costLabel);


    }
    private TextButton createTextButton(String name){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.down = skin.newDrawable(name, Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.over = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.font = skin.getFont("default");

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        final TextButton button = new TextButton("",textButtonStyle);
        return button;
    }

    private TextButton createCoinButton(String name){
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.down = skin.newDrawable(name, Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.over = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.font = coinSkin.getFont("default");

        coinSkin.add("default", textButtonStyle);

        final TextButton button = new TextButton("",textButtonStyle);
        return button;
    }

    @Override
    public void show() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            gameLogic.update(delta);
            if (gameLogic.increaseCoins()) coinLabel.getLabel().setText(
                    Integer.toString(Integer.valueOf(coinLabel.getLabel().getText().toString()) + 1));
        }


        gameLogic.render(delta);
        stage.draw();
        stage.act();
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
