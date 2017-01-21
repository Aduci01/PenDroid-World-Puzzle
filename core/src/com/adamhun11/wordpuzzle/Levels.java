package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 2017. 01. 05..
 */

public class Levels {
    int lvlNum;
    String map;
    String word;
    String letterTexture;

    public static Array<Levels> levels;
    public static void init(){
        levels = new Array<Levels>();

        Levels one = new Levels();
        one.lvlNum = 1;
        one.word = "first";
        one.letterTexture = "normal";
        one.map = "f.X.." +
                ".r..." +
                "..t.." +
                "...i." +
                "..X.s";
        levels.add(one);

        Levels two = new Levels();
        two.lvlNum = 2;
        two.word = "second";
        two.letterTexture = "normal";
        two.map =
                "Xdo..X" +
                ".s...." +
                "..e..." +
                "......" +
                "c....n" +
                "X....X";
        levels.add(two);


        Levels three = new Levels();
        three.lvlNum = 3;
        three.word = "temple";
        three.letterTexture = "normal";
        three.map =
                ".....p." +
                "...X..." +
                ".eX.X.." +
                ".X.X.X." +
                "..X.Xm." +
                "X.lXe.X" +
                "X..Xt.X";
        levels.add(three);

        Levels four = new Levels();
        four.lvlNum = 4;
        four.word = "colorless";
        four.letterTexture = "white";
        four.map =
                "........o" +
                "X...X...X" +
                ".X..X..X." +
                ".XX...XX." +
                "........." +
                ".X..l..X." +
                ".XXX.XXX." +
                ".ls...sr." +
                "e..oXc...";
        levels.add(four);

        Levels five = new Levels();
        five.lvlNum = 5;
        five.word = "abcdefgh";
        five.letterTexture = "normal";
        five.map =
                        "aX...b.." +
                        "......gX" +
                        "...f...." +
                        ".....d.." +
                        "........" +
                        "........" +
                        "X......e" +
                        "...c..Xh";
        levels.add(five);
    }
}