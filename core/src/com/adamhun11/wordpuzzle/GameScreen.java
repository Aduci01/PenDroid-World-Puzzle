package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 2017. 01. 01..
 */

public class GameScreen implements Screen {
    Game game;
    GameLogic gameLogic;

    Stage stage;
    Table pauseTable;
    Skin skin;

    float wx = (float) Gdx.graphics.getWidth() / 399f;
    float hx = (float) Gdx.graphics.getHeight() / 666f;


    boolean paused = false;

    public GameScreen(Game g, int lvlNum) {
        stage = new Stage();
        skin = new Skin();
        pauseTable = new Table();
        pauseTable.setPosition(-500f, -500f);
        pauseTable.setSize(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 2);


        skin.add("pause", new Texture("GUI/buttons/pause.png"));
        skin.add("exit", new Texture("GUI/buttons/exit.png"));
        skin.add("resume", new Texture("GUI/buttons/resume.png"));
        skin.add("restart", new Texture("GUI/buttons/restart.png"));
        skin.add("bg", new Texture("GUI/menus/pause_menu.png"));
        BitmapFont bfont = new BitmapFont();
        skin.add("default",bfont);

        initButtons();
        stage.addActor(pauseTable);

        game = g;
        gameLogic = new GameLogic(stage, lvlNum);
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
                pauseTable.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.moveTo(-500f, -500f, 0.4f)));
                paused = false;
            }
        });
        pauseTable.add(resume).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);
        pauseTable.row();

        TextButton restart = createTextButton("restart");
        restart.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                pauseTable.addAction(Actions.sequence(Actions.fadeOut(0.3f),Actions.moveTo(-500f, -500f, 0.4f)));
                gameLogic.solved = true;
                gameLogic.endTransition = true;
                paused = false;
            }
        });
        pauseTable.add(restart).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);
        pauseTable.row();

        TextButton exit = createTextButton("exit");
        exit.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
            }
        });
        pauseTable.add(exit).width(Gdx.graphics.getWidth() / 3f).height( Gdx.graphics.getWidth() / 4f / resume.getWidth() * resume.getHeight()).padBottom(20);


        pauseTable.setSkin(skin);
        pauseTable.setBackground("bg");

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

    @Override
    public void show() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (!paused)
         gameLogic.update(delta);
        gameLogic.render(delta);
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
