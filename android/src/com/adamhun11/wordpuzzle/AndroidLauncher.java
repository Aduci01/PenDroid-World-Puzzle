package com.adamhun11.wordpuzzle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.adamhun11.wordpuzzle.Main;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class AndroidLauncher extends AndroidApplication implements PlayServices {
	private MultiPlayerServices multiPlayerServices;
	private final static int requestCode = 1;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		multiPlayerServices = new MultiPlayerServices(this, GameHelper.CLIENT_GAMES);
		multiPlayerServices.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		multiPlayerServices.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Main(this), config);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		multiPlayerServices.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		multiPlayerServices.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		multiPlayerServices.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void signIn() {
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					multiPlayerServices.beginUserInitiatedSignIn();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut() {
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					multiPlayerServices.signOut();
				}
			});
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame() {
		String str = "https://play.google.com/store/apps/details?id=com.adamhun11.wordpuzzle";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement(String s) {
		Games.Achievements.unlock(multiPlayerServices.getApiClient(), s);
	}

	@Override
	public void quickGame() {
		try{
			runOnUiThread(new Runnable(){
				public void run(){
					multiPlayerServices.quickGame();
				}
			});
		}
		catch (Exception e){
			Gdx.app.log("CIRUS", "Google Services Logout Failed " + e.getMessage());
		}
	}

	@Override
	public void submitScore(int highScore) {

	}

	@Override
	public void showAchievement() {
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(multiPlayerServices.getApiClient()), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public void showScore() {
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(multiPlayerServices.getApiClient(),
					getString(R.string.m_leaderboard)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public boolean isSignedIn() {
		return multiPlayerServices.isSignedIn();

	}

	@Override
	public void setGame(Main game) {
		multiPlayerServices.setGame(game);
	}

	public void sendResult(int steps, float time) {
		multiPlayerServices.sendResult(steps, time);
	}

	@Override
	public void leaveRoom() {
		multiPlayerServices.leaveRoom();
	}

	@Override
	public void sendLevelNum(int n) {
		multiPlayerServices.sendLevelNum(n);
	}
}
