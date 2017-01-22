package com.adamhun11.wordpuzzle.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Adam on 2017. 01. 01..
 */

public class Letter {
    String letter;
    float x, y, offX, offY;
    int c, r, tableCol;
    float size;
    Sprite sprite;

    float speedX, speedY;
    float fadeFloat;

    boolean endTransition = false;

    public Letter(String l, float s, float x, float y, int ox, int oy, float offsetsX, float offsetY, int table, float fadeFloat){
        letter = l;
        size = s;
        this.x = x;
        this.y = y;
        offX = offsetsX; offY = offsetY;

        tableCol = table;
        this.fadeFloat = fadeFloat;

        c = oy;
        r = ox;

        Texture texture = new Texture("letters/" + l.toUpperCase() + ".png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
    }

    public void move(int a){
        switch (a){
            case 0: speedX = -10f * size;
                break;
            case 1: speedX = 10f * size;
                break;
            case 2: speedY = 10f * size;
                break;
            case 3: speedY = -10f * size;
                break;
        }
    }

    public void update(float dt){
        if (fadeFloat > 0 && !endTransition) fadeFloat -= dt * size * 10;
        if (endTransition) fadeFloat += dt * size * 10;

        x += speedX * dt;
        y += speedY * dt;

        if (speedX < 0 && x < (r - 1) * size + offX) {
            x = (r - 1) * size + offX;
            speedX = 0;
        }

        if (speedX > 0 && x > (r - 1) * size + offX) {
            x = (r - 1) * size + offX;
            speedX = 0;
        }

        if (speedY > 0 && y > (tableCol - c) * size + offY) {
            y = (tableCol - c) * size + offY;
            speedY = 0;
        }

        if (speedY < 0 && y < (tableCol - c) * size + offY) {
            y = (tableCol - c) * size + offY;
            speedY = 0;
        }
    }

    public void render(SpriteBatch spriteBatch){
        sprite.setSize(size, size);
        sprite.setPosition(x, y - fadeFloat);
        sprite.draw(spriteBatch);
    }

}
