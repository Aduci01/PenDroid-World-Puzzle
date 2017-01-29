package com.adamhun11.wordpuzzle.Game;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Adam on 2017. 01. 05..
 */

public class Levels {
    int lvlNum;
    public String map;
    public String word;
    public String letterTexture;

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


        Levels drive = new Levels();
        drive.word = "drive";
        drive.letterTexture = "normal";
        drive.map =
                "d.X.r" +
                        "....." +
                        "X.e.X" +
                        "....." +
                        "v.X.i";
        levels.add(drive);


        Levels miles = new Levels();
        miles.word = "miles";
        miles.letterTexture = "normal";
        miles.map =
                "l.X.m" +
                        "....." +
                        "sX.Xi" +
                        "XXeXX"+
        "XXXXX";
        levels.add(miles);


        Levels road = new Levels();
        road.word = "road";
        road.letterTexture = "normal";
        road.map =
                "r.dX" +
                        "...o" +
                        ".aXX"+
        "XXXX";
        levels.add(road);


        Levels banana = new Levels();
        banana.word = "banana";
        banana.letterTexture = "normal";
        banana.map =
                "n.XXnb" +
                        "......" +
                        "..XXXX" +
                        "..XXXX" +
                        "......" +
                        "a.XXaa";
        levels.add(banana);


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


        Levels plane = new Levels();
        plane.lvlNum = 2;
        plane.word = "plane";
        plane.letterTexture = "normal";
        plane.map =
                "n...a" +
                        ".X.X." +
                        "e.l.p" +
                        "XX.XX" +
                        "..X..";
        levels.add(plane);


        Levels forever = new Levels();
        forever.word = "forever";
        forever.letterTexture = "normal";
        forever.map =
                "XXX.o.XXX" +
                "XX.....XX" +
                "Xf.....rX" +
                "........." +
                "......v.." +
                "....e...." +
                ".......X." +
                ".eXX....." +
                "........r";
        levels.add(forever);


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


        Levels elephant = new Levels();
        elephant.lvlNum = 4;
        elephant.word = "elephant";
        elephant.letterTexture = "normal";
        elephant.map =
                "Xl....XX" +
                "XX....Xn" +
                ".e....h." +
                "..XXXX.." +
                "..XXXX.." +
                ".t....e." +
                "pX....XX" +
                "XX....aX";

        levels.add(elephant);

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