package com.adamhun11.wordpuzzle.Screens;

import com.adamhun11.wordpuzzle.Facebook;
import com.adamhun11.wordpuzzle.SmartFontGenerator;
import com.badlogic.gdx.Game;
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

/**
 * Created by Adam on 2017. 01. 13..
 */

public class Menu implements Screen {
    Game game;
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


    public Menu(Game g){
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
        SmartFontGenerator fontGen = new SmartFontGenerator();
        FileHandle exoFile = Gdx.files.internal("GUI/font.ttf");
        lvlFont = fontGen.createFont(exoFile, "font", (int)(120 * wx));
    }

    private void initStage(){
        circle = new Sprite(new Texture("GUI/circle.png"));

        stage = new Stage();
        skin = new Skin();
        skin.add("a", new Texture("GUI/transparent.png"));
        skin.add("fb", new Texture("GUI/buttons/fb.png"));

        Image image = new Image(circle);
        image.setSize(200 * wx, 200 * wx);
        image.setPosition(Gdx.graphics.getWidth() / 2 - 100 * wx, Gdx.graphics.getHeight() / 2 - 100 * hx);
        stage.addActor(image);

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
        //FACEBOOK button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.down = skin.newDrawable("fb", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("fb", Color.BLUE);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);
        final TextButton button = new TextButton("",textButtonStyle);
        button.setSize(wx * button.getWidth() / 4, hx * button.getHeight() / 4);
        button.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                facebook.logIn();
                facebook.share();
            }
        });
        stage.addActor(button);

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
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();

        //TODO Scroll to the next level number
        /*if (Gdx.input.justTouched()) {
            scrollPane.layout();
scrollPane.setScrollPercentY(scrollPane.getScrollPercentY() + 1f/39);
        }*/
        /*if (scrollPane.getVelocityY() == 0 && scrollPane.getScrollPercentY() / 1/40 > 0.05){
            scrollPane.setVelocityY(500);
        }*/

        //System.out.println (scrollPane.getScrollPercentY() * 100 % (100/40));


       /* spriteBatch.begin();
        circle.setSize(200 * wx, 200 * wx);
        circle.setPosition(Gdx.graphics.getWidth() / 2 - 100 * wx, Gdx.graphics.getHeight() / 2 - 100 * hx);
        circle.draw(spriteBatch);
        spriteBatch.end();*/



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