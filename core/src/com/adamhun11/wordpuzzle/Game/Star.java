package com.adamhun11.wordpuzzle.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Adam on 2017. 01. 22..
 */

public class Star {
    float posX, posY;
    float speedX, speedY;
    float size;
    Sprite sprite;
    boolean collected = false;
    float fadeFloat;
    boolean endTransition = false;
    int r, c;
    float timer = 0;
    float deltaSize;
    int table;
    float wx = (float) Gdx.graphics.getWidth() / 399f;


    public Star(float x, float y, float size, float fade, int r, int c, int table){
        posX = x; posY = y;
        this.size = size;
        sprite = new Sprite(new Texture("star.png"));
        sprite.setSize(size, size);
        fadeFloat = fade;
        this.c = c; this.r = r;
        this.table = table;
    }

    public void collect(Letter l){
        collected = true;
        speedX = (-posX) + 10 * wx;
        speedY = Gdx.graphics.getHeight() - 2 * (399f / 10 * 4.5f) / table * wx - posY;
        deltaSize = (size - (399f / 10 * 3.5f) / table * wx);
    }

    public void update(float dt){
        if (fadeFloat > 0 && !endTransition) fadeFloat -= dt * size * 10;
        if (endTransition && timer == 0) fadeFloat += dt * size * 10;

        if (collected && timer <= 1f){
            timer += dt;
                posX += speedX * dt;
                posY += speedY * dt;
            size -= deltaSize * dt;
        }
        if (timer >= 1) {
            posY = Gdx.graphics.getHeight() - 2 * (399f / 10 * 4.5f) / table * wx; posX = 10 * wx;
        }
            sprite.setSize(size, size);
            sprite.setPosition(posX, posY - fadeFloat);
    }

    public void render(SpriteBatch spriteBatch){
        sprite.draw(spriteBatch);
    }
}
