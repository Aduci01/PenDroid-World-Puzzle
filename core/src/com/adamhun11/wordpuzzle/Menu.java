package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
        facebook.logIn();

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();


        Preferences prefs = Gdx.app.getPreferences("datas");
        unlockedLevel = prefs.getInteger("unlockedLevel", 1);
        initStage();
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);

    }

    private void initStage(){
        circle = new Sprite(new Texture("GUI/circle.png"));
        stage = new Stage();
        skin = new Skin();
        skin.add("a", new Texture("GUI/transparent.png"));
        skin.add("fb", new Texture("GUI/buttons/fb.png"));
        BitmapFont bfont = new BitmapFont();
        skin.add("default",bfont);

        levelTable = new Table();
        levelTable.setPosition(0,0);

        for (int i = 1; i <= Levels.levels.size; i++){
            levelTable.add(createTextButton("a", i)).padBottom(50 * hx).padTop(50 * hx);
            levelTable.row();
        }

        scrollPane = new ScrollPane(levelTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setPosition(Gdx.graphics.getWidth() / 2 - 55 * wx, Gdx.graphics.getHeight() / 2 - 95 * hx);
        scrollPane.setSize(120 * wx, 190 * hx);
        stage.addActor(scrollPane);
        //FACEBOOK button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("fb", Color.BLUE);
        textButtonStyle.down = skin.newDrawable("fb", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.over = skin.newDrawable("fb", Color.GREEN);
        textButtonStyle.font = new BitmapFont();
        skin.add("default", textButtonStyle);

        final TextButton button = new TextButton("",textButtonStyle);
        button.setSize(wx * button.getWidth() / 4, hx * button.getHeight() / 4);
        button.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                facebook.share();
            }
        });
        stage.addActor(button);

    }

    private TextButton createTextButton(String name, int num){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GUI/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)(120 * wx);
        if (unlockedLevel < num)
            parameter.color = new Color(0.3f,0.3f,0.3f,1);
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.down = skin.newDrawable(name, Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.over = skin.newDrawable(name, Color.GREEN);
        textButtonStyle.font = font;

        skin.add("default", textButtonStyle);

        final TextButton button = new TextButton("",textButtonStyle);
        button.setSize(wx * button.getWidth(), hx * button.getHeight());
        button.setText(Integer.toString(num));

        final int lvl = num;
        if (unlockedLevel >= num) {
            button.addListener(new ChangeListener() {
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    game.setScreen(new GameScreen(game, lvl));
                }
            });
        }
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


        spriteBatch.begin();
        circle.setSize(200 * wx, 200 * wx);
        circle.setPosition(Gdx.graphics.getWidth() / 2 - 100 * wx, Gdx.graphics.getHeight() / 2 - 100 * hx);
        circle.draw(spriteBatch);
        spriteBatch.end();



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
