package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 2017. 01. 01..
 */

public class GameScreen implements Screen {
    Game game;
    private String[][] map;
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
    String puzzleWorld;
    Array<Letter> letters;

    ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;

    Stage stage;
    float wx = (float) Gdx.graphics.getWidth() / 399f;
    float hx = (float) Gdx.graphics.getHeight() / 666f;

    float offsetX, offsetY, offsetYUp;
    float tableX, tableY;
    float size;


    int col = 5, row = 5;

    boolean dragged = false;
    int touchCol, touchRow;


    public GameScreen(Game g){
        game = g;

        offsetX =  wx * 20;
        offsetY = hx * 100;
        tableX = wx * (399 - 2 * 20);
        size = tableX / row;
        offsetYUp = (666 - 100) * hx  - col * size;

        map = new String[col][row];
        puzzleWorld = "test";
        String a = "XOOOX" +
                "XsOXX" +
                "bOOmO" +
                "OOiOO" +
                "XOOiO"
                ;
        letters = new Array<Letter>();

        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                map[i][j] = String.valueOf(a.charAt(i * col + j));
                if (!map[i][j].equals("O") && !map[i][j].equals("X")){
                    letters.add(new Letter(map[i][j].toUpperCase(), size, j * size + offsetX, (col - i) * size - size + offsetY, j+1, i+1, offsetX, offsetY, col));
                }
            }
        }

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        stage = new Stage();

        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
    }

    private void inputHandler(){
        if (Gdx.input.isTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            if (!dragged) {
                if (y > offsetYUp) {
                    int c = (int) ((y - offsetYUp) / size) + 1;
                    int r = (int) ((x - offsetX) / size) + 1;
                    System.out.println(c);
                    System.out.println(r);
                    if (c > 0 && r > 0 && c < col + 1 && r < row + 1) {
                        if (!map[c - 1][r - 1].equals("O") && !map[c - 1][r - 1].equals("X")) {
                            dragged = true;
                            touchCol = c; touchRow = r;
                            System.out.println("Touched: " + map[c - 1][r - 1] + "    " + c + " " + r);
                        }
                    }
                }
            }
            else {
                float dc = (int) ((y - offsetYUp) / size) + 1;
                float dr = (int) ((x - offsetX) / size) + 1;
                int i;
                for (i = 0; i < letters.size; i++){
                    if (letters.get(i).c == touchCol && letters.get(i).r == touchRow) {
                        System.out.println(letters.get(i).letter + " dragged");
                        break;
                    }
                }
                if (dc == touchCol && dr < touchRow) {
                    moveLeft(i);
                }
                if (dc == touchCol && dr > touchRow) {
                    moveRight(i);
                }
                if (dc < touchCol && dr == touchRow) {
                    moveUp(i);
                }
                if (dc > touchCol && dr == touchRow) {
                    moveDown(i);
                }
            }
        } else {
            dragged = false;
        }
    }

    private void update(float dt){
        boolean transition = false;
        for (int i = 0; i < letters.size; i++){
            letters.get(i).update(dt);
            if (letters.get(i).speedX != 0 || letters.get(i).speedY != 0) transition = true;
        }
        if (!transition)
          inputHandler();

    }

    @Override
    public void show() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.3f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(Gdx.graphics.getDeltaTime());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CORAL);
        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++)
            {
                if (!map[i][j].equals("X")) {
                    boolean a, b, c, d;
                    a = b = c = d = false;
                    if (i == 0 && j == 0){
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                    } else if (i == 0 && j == row - 1){
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                    } else if (i == col - 1 && row == 0){
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                    } else if (i == col - 1 && j == row - 1){
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                    } else if (i == 0){
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                    } else if (i == col - 1){
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                    } else if (j == 0){
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                    } else if (j == row - 1){
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                    } else if (i > 0 && i < col - 1 && j > 0 && j < row - 1){
                        if (!map[i + 1][j].equals("X")) { a = true; b = true;}
                        if (!map[i][j + 1].equals("X")) { b = true; c = true;}
                        if (!map[i - 1][j].equals("X")) { d = true; c = true;}
                        if (!map[i][j - 1].equals("X")) { a = true; d = true;}
                    }
                    //shapeRenderer.rect(j * size + offsetX, (col - i) * size - size + offsetY, size, size);
                    roundedRect(j * size + offsetX, (col - i) * size - size + offsetY, size, size, size / 5, a, b, c, d);
                }
            }
        }
       // roundedRect(offsetX,4*size + offsetY,size,size,size/10, false, true, true, false);
        shapeRenderer.end();

        spriteBatch.begin();
        for (int i = 0; i < letters.size; i++){
            letters.get(i).render(spriteBatch);
        }


        spriteBatch.end();

        stage.act();
    }

    public void roundedRect(float x, float y, float width, float height, float radius, boolean a, boolean b, boolean c, boolean d){
        // Central rectangle
        shapeRenderer.rect(x + radius, y + radius, width - 2*radius, height - 2*radius);

        // Four side rectangles, in clockwise order
        shapeRenderer.rect(x + radius, y, width - 2*radius, radius);
        shapeRenderer.rect(x + width - radius, y + radius, radius, height - 2*radius);
        shapeRenderer.rect(x + radius, y + height - radius, width - 2*radius, radius);
        shapeRenderer.rect(x, y + radius, radius, height - 2*radius);

        // Four arches, clockwise too
        if (!a)
            shapeRenderer.arc(x + radius, y + radius, radius, 180f, 90f);
        else shapeRenderer.rect(x, y, radius, radius);

        if (!b)
            shapeRenderer.arc(x + width - radius, y + radius, radius, 270f, 90f);
        else shapeRenderer.rect(x + width - radius, y, radius, radius);

        if (!c)
            shapeRenderer.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
        else shapeRenderer.rect(x + width - radius, y + height - radius, radius, radius);

        if (!d)
            shapeRenderer.arc(x + radius, y + height - radius, radius, 90f, 90f);
        else shapeRenderer.rect(x, y + height - radius, radius, radius);
    }

    private void moveUp(int i){
        if (touchCol > 1) {
            int j = touchCol - 1;
            while (map[j - 1][touchRow - 1].equals("O") && j > 1){
                j--;
            }
            if (j == 1)
                if (map[j - 1][touchRow - 1].equals("O")) j--;
            if (j < touchCol - 1){
                letters.get(i).move(2);
                letters.get(i).c = j + 1;
                map[touchCol - 1][touchRow - 1] = "O";
                map[j][touchRow - 1] = letters.get(i).letter;
                dragged = false;

                for (int k = 0; k < col; k++) {
                    System.out.println();
                    for (int p = 0; p < row; p++)
                        System.out.print(map[k][p]);
                }
            }
        }
    }

    private void moveDown(int i){
        if (touchCol < col) {
            int j = touchCol - 1;
            while (map[j + 1][touchRow - 1].equals("O") && j < col - 2){
                j++;
            }
            if (j == col - 2)
                if (map[j + 1][touchRow - 1].equals("O")) j++;
            if (j > touchCol - 1){
                letters.get(i).move(3);
                letters.get(i).c = j + 1;
                map[touchCol - 1][touchRow - 1] = "O";
                map[j][touchRow - 1] = letters.get(i).letter;
                dragged = false;

                for (int k = 0; k < col; k++) {
                    System.out.println();
                    for (int p = 0; p < row; p++)
                        System.out.print(map[k][p]);
                }
            }
        }
    }

    private void moveRight(int i){
        if (touchRow < row) {
            int j = touchRow - 1;
            while (map[touchCol - 1][j + 1].equals("O") && j < row - 2){
                j++;
            }
            if (j == row - 2)
                if (map[touchCol - 1][j+1].equals("O")) j++;
            if (j > touchRow - 1){
                letters.get(i).move(1);
                letters.get(i).r = j + 1;
                map[touchCol - 1][touchRow - 1] = "O";
                map[touchCol - 1][j] = letters.get(i).letter;
                dragged = false;

                for (int k = 0; k < col; k++) {
                    System.out.println();
                    for (int p = 0; p < row; p++)
                        System.out.print(map[k][p]);
                }
            }
        }
    }

    private void moveLeft(int i){
        if (touchRow > 1) {
            int j = touchRow - 1;
            while (map[touchCol - 1][j-1].equals("O") && j > 1){
                j--;
            }
            if (j == 1)
                if (map[touchCol - 1][j-1].equals("O")) j--;
            if (j < touchRow - 1){
                letters.get(i).move(0);
                letters.get(i).r = j + 1;
                map[touchCol - 1][touchRow - 1] = "O";
                map[touchCol - 1][j] = letters.get(i).letter;
                dragged = false;

                for (int k = 0; k < col; k++) {
                    System.out.println();
                    for (int p = 0; p < row; p++)
                        System.out.print(map[k][p]);
                }
            }
        }
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
