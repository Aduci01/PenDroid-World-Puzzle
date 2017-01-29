package com.adamhun11.wordpuzzle;

/**
 * Created by Adam on 2017. 01. 25..
 */

public interface PlayServices {
    public void signIn();
    public void signOut();
    public void rateGame();
    public void unlockAchievement(String s);
    public void quickGame();
    public void submitScore(int highScore);
    public void showAchievement();
    public void showScore();
    public boolean isSignedIn();
    public void setGame(Main game);
    public void sendResult(int steps,float time);
    public void leaveRoom();
    public void sendLevelNum(int n);
}
