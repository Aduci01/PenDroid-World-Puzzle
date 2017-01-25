package com.adamhun11.wordpuzzle.Game;

import com.adamhun11.wordpuzzle.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 2017. 01. 14..
 */

public class GameLogic {
    Main game;
    private String[][] map;
    String word;
    Array<Letter> letters;
    Array<Letter> solvedLetters;
    Array<Coin> coins;

    ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;

    Stage stage;
    float wx = (float) Gdx.graphics.getWidth() / 399f;
    float hx = (float) Gdx.graphics.getHeight() / 666f;

    float offsetX, offsetY, offsetYUp;
    float tableX, tableY;
    float size;

    //Default values
    Color bgColor = new Color(0.3f, 0.2f, 0.2f, 1f), mapColor = Color.CORAL;

    int col, row;

    int lvlNum;

    public int addCoin;

    boolean dragged = false;
    int touchCol, touchRow;


    //Shaders to make the textures "colorless" for a specific level
    final String VERT =
            "attribute vec4 "+ ShaderProgram.POSITION_ATTRIBUTE+";\n" +
                    "attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "       vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
                    "       vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
                    "       gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";

    final String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "varying LOWP vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform float grayscale;\n" +
                    "void main() {\n" +
                    "       vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
                    "       \n" +
                    "       float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
                    "       texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" +
                    "       \n" +
                    "       gl_FragColor = texColor * vColor;\n" +
                    "}";

    ShaderProgram shader;
    Preferences prefs;

    public boolean solved, endTransition, transition;
    float fadeFloat;

    public GameLogic(Main g, Stage s, int lvlNum){
        game = g;
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        stage = s;
        this.lvlNum = lvlNum;
        init(lvlNum);

        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);

        prefs = Gdx.app.getPreferences("datas");
    }

    public void init(int lvlNum){
        solved = false; endTransition = false;
        addCoin = 0;

        //Clearing the shader and background
        spriteBatch.setShader(null);
        bgColor = new Color(0.3f, 0.2f, 0.2f, 1f);
        mapColor = Color.CORAL;

        if (Levels.levels.get(lvlNum - 1).letterTexture.equals("white")){
            ShaderProgram.pedantic = false;
            shader = new ShaderProgram(VERT, FRAG);
            spriteBatch.setShader(shader);

            bgColor = Color.DARK_GRAY;
            mapColor = Color.WHITE;
        }


        row = col = (int) Math.sqrt(Levels.levels.get(lvlNum - 1).map.length());
        map = new String[col][row];
        word = Levels.levels.get(lvlNum - 1).word;
        String a = Levels.levels.get(lvlNum - 1).map;
        letters = new Array<Letter>();

        offsetX =  wx * 20;
        offsetY = hx * 150;
        tableX = wx * (399 - 2 * 20);
        size = tableX / row;
        offsetYUp = (666 - 150) * hx  - col * size;

        fadeFloat = offsetYUp + row * size - 50 * hx;


        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                map[i][j] = String.valueOf(a.charAt(i * col + j));
                if (!map[i][j].equals(".") && !map[i][j].equals("X")){
                    letters.add(new Letter(game, map[i][j].toUpperCase(), size, j * size + offsetX, (col - i) * size - size + offsetY, j+1, i+1, offsetX, offsetY, col, fadeFloat));
                }
            }
        }
        solvedLetters();

        coins = new Array<Coin>();
        Preferences prefs = Gdx.app.getPreferences("datas");
        System.out.println(prefs.getInteger("unlockedLevel", 1));
        System.out.println(lvlNum + 1);
        if (prefs.getInteger("unlockedLevel", 1) == lvlNum) {
            for (int i = 0; i < MathUtils.random(2) + 1; i++) {
                int x;
                int y;
                do {
                    x = MathUtils.random(row - 1);
                    y = MathUtils.random(col - 1);
                } while (!map[x][y].equals("."));
                coins.add(new Coin(y * size + offsetX, (col - x) * size - size + offsetY, size, fadeFloat, y + 1, x + 1, row));
            }
        }
    }

    private void solvedLetters(){
        solvedLetters = new Array<Letter>();
        float sizeX = (399f / 10 * 4.5f) / Levels.levels.get(lvlNum - 1).word.length() * wx;
        for (int i = 0; i < Levels.levels.get(lvlNum - 1).word.length(); i++){
            solvedLetters.add(new Letter(game, Character.toString(Levels.levels.get(lvlNum - 1).word.charAt(i)), sizeX, i * sizeX, Gdx.graphics.getHeight() - sizeX, 0,0,0,0,0,0));
        }
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
                        if (!map[c - 1][r - 1].equals(".") && !map[c - 1][r - 1].equals("X")) {
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
                for (Coin star : coins){
                    if (star.c == letters.get(i).c &&
                            star.r == letters.get(i).r)
                        star.collect(letters.get(i));
                }


                if (isSolved()){
                    System.out.println("Solved");
                    solved = true;
                    endTransition = true;
                    if (prefs.getInteger("unlockedLevel", 0) < lvlNum + 1)
                    prefs.putInteger("unlockedLevel", lvlNum + 1);
                    prefs.putInteger("coins", prefs.getInteger("coins", 0) + addCoin);
                    prefs.flush();
                    lvlNum++;
                }

            }
        } else {
            dragged = false;
        }
    }

    public void update(float dt){
        transition = false;
        for (int i = 0; i < letters.size; i++){
            letters.get(i).update(dt);
            if (letters.get(i).speedX != 0 || letters.get(i).speedY != 0) transition = true;
        }
        for (Coin star : coins) star.update(dt);
        if (!transition) {
            //start transition
            if (fadeFloat > 0 && !solved) fadeFloat -= size * 10 * dt;
            if (endTransition){
                for (Letter l : letters) l.endTransition = true;
                for (Coin star : coins) star.endTransition = true;
                fadeFloat += size * 10 * dt;
                if (offsetY - fadeFloat + col * size < 0){
                    init(lvlNum);
                }
            } else {
                inputHandler();
            }
        }
    }

    public boolean increaseCoins(){
        for (Coin c : coins){
            if (c.increaseCoins) {
                addCoin++;
                return true;
            }
        }
        return false;
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(mapColor);
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
                    } else if (i == col - 1 && j == 0){
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
                    roundedRect(j * size + offsetX, (col - i) * size - size + offsetY - fadeFloat, size, size, size / 6, a, b, c, d);
                }
            }
        }
        // roundedRect(offsetX,4*size + offsetY,size,size,size/10, false, true, true, false);
        shapeRenderer.end();

        spriteBatch.begin();
        for (int i = 0; i < letters.size; i++){
            letters.get(i).render(spriteBatch);
            solvedLetters.get(i).render(spriteBatch);
        }
        for (Coin s : coins) s.render(spriteBatch);

        spriteBatch.end();
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
            while (map[j - 1][touchRow - 1].equals(".") && j > 1){
                j--;
            }
            if (j == 1)
                if (map[j - 1][touchRow - 1].equals(".")) j--;
            if (j < touchCol - 1){
                letters.get(i).move(2);
                letters.get(i).c = j + 1;
                map[touchCol - 1][touchRow - 1] = ".";
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
            while (map[j + 1][touchRow - 1].equals(".") && j < col - 2){
                j++;
            }
            if (j == col - 2)
                if (map[j + 1][touchRow - 1].equals(".")) j++;
            if (j > touchCol - 1){
                letters.get(i).move(3);
                letters.get(i).c = j + 1;
                map[touchCol - 1][touchRow - 1] = ".";
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
            while (map[touchCol - 1][j + 1].equals(".") && j < row - 2){
                j++;
            }
            if (j == row - 2)
                if (map[touchCol - 1][j+1].equals(".")) j++;
            if (j > touchRow - 1){
                letters.get(i).move(1);
                letters.get(i).r = j + 1;
                map[touchCol - 1][touchRow - 1] = ".";
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
            while (map[touchCol - 1][j-1].equals(".") && j > 1){
                j--;
            }
            if (j == 1)
                if (map[touchCol - 1][j-1].equals(".")) j--;
            if (j < touchRow - 1){
                letters.get(i).move(0);
                letters.get(i).r = j + 1;
                map[touchCol - 1][touchRow - 1] = ".";
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

    //TODO show a level finished screen if solved() && !transition
    //Tells whether the level is solved.
    public boolean isSolved()
    {
        String firstLetter = word.substring(0, 1);

        for(int y = 0; y < row; y++)
            for (int x = 0; x < col; x++) {
                if (map[y][x].equalsIgnoreCase(firstLetter)) {
                    if(solve(x, y - 1, 0, 1)) return true;
                    if(solve(x + 1, y, 1, 1)) return true;
                    if(solve(x, y + 1, 2, 1)) return true;
                }
            }

        return false;
    }

    //Recursive function for checking solution (direction: 0-up, 1-left, 2-down)
    private boolean solve(int x, int y, int direction, int letterIndex)
    {
        if(letterIndex >= word.length()) return true;
        if(x < 0 || x >= col || y < 0 || y >= row) return false;
        if(!word.substring(letterIndex, letterIndex + 1).equalsIgnoreCase(map[y][x])) return false;

        int nextX = x + (direction == 1 ? 1 : 0), nextY = y;
        if(direction == 0) nextY--;
        else if(direction == 2) nextY++;

        return solve(nextX, nextY, direction, letterIndex + 1);
    }

    public void increaseLevelNum(int n){lvlNum += n;
    }

}