package com.adamhun11.wordpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.adamhun11.wordpuzzle.Game.Levels;
import com.adamhun11.wordpuzzle.Screens.GameScreen;
import com.adamhun11.wordpuzzle.Screens.MultiplayerGameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.example.games.basegameutils.GameHelper;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017. 01. 29..
 */

public class MultiPlayerServices extends GameHelper implements RoomUpdateListener, RealTimeMessageReceivedListener,RoomStatusUpdateListener {
    static final int RC_SELECT_PLAYERS = 454545;
    static final int RC_WAITING_ROOM = 423523;
    private Activity activity;
    private String mRoomID;
    private Room mRoom;
    private Main game;

    public MultiPlayerServices(Activity activity, int clientsToUse) {
        super(activity, clientsToUse);
        this.activity = activity;
        // TODO Auto-generated constructor stub
    }

    public void quickGame(){
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);
        RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();
        Games.RealTimeMultiplayer.create(getApiClient(), roomConfig);

        // prevent screen from sleeping during handshake
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public void initMatch(){
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(getApiClient(), 1, 1);
        this.activity.startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder((RoomUpdateListener) this)
                .setMessageReceivedListener((RealTimeMessageReceivedListener) this)
                .setRoomStatusUpdateListener((RoomStatusUpdateListener) this);
    }

    public void onActivityResult(int request,int response, Intent data){
        if (request == MultiPlayerServices.RC_WAITING_ROOM){
            if (response == Activity.RESULT_CANCELED || response == GamesActivityResultCodes.RESULT_LEFT_ROOM ){
                Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomID);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }else{
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run () {
                        game.setScreen(new MultiplayerGameScreen(game, 1));
                    }
                });
            }

        }
        else if (request == MultiPlayerServices.RC_SELECT_PLAYERS){
            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }

            // get the invitee list
            Bundle extras = data.getExtras();
            final ArrayList<String> invitees =
                    data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

            // get auto-match criteria
            Bundle autoMatchCriteria = null;
            int minAutoMatchPlayers =
                    data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers =
                    data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
            Gdx.app.log("J", "Jmin" + minAutoMatchPlayers + " Jmax:" + maxAutoMatchPlayers);
            for (String invitee : invitees){
                Gdx.app.log("L" , invitee);
            }
            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }

            // create the room and specify a variant if appropriate
            RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
            roomConfigBuilder.addPlayersToInvite(invitees);
            if (autoMatchCriteria != null) {
                roomConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
            }
            RoomConfig roomConfig = roomConfigBuilder.build();
            Games.RealTimeMultiplayer.create(getApiClient(), roomConfig);

            // prevent screen from sleeping during handshake
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }else{
            super.onActivityResult(request, response, data);
        }
    }

    @Override
    public void onJoinedRoom(int arg0, Room arg1) {
        if (arg0 != GamesStatusCodes.STATUS_OK) {
            Gdx.app.log("R", "Joined FAILED");
        }else{
            Gdx.app.log("R", "Joined Room");
        }


    }

    @Override
    public void onLeftRoom(int arg0, String arg1) {
        BaseGameUtils.makeSimpleDialog(activity, "Abandonado partida");
        Gdx.app.log("LEAVE", "Me fui de la Room");

    }

    @Override
    public void onRoomConnected(int arg0, Room arg1) {
        // TODO Auto-generated method stub

    }

    public void setGame(Main game){
        this.game = game;
    }

    public void leaveRoom(){
        Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomID);
    }

    @Override
    public void onRoomCreated(int arg0, Room arg1) {
        if (arg0 != GamesStatusCodes.STATUS_OK) {
            //BaseGameUtils.showAlert(activity, "Room creation error");
            BaseGameUtils.makeSimpleDialog(activity, "Error al crear la partida", "Room creation error").show();
            Gdx.app.log("R", "Room Created FAILED");
        }else{
            Gdx.app.log("R", "Room Created");
            mRoomID = arg1.getRoomId();
            mRoom = arg1;
            Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(getApiClient(), arg1, 2);
            this.activity.startActivityForResult(i, RC_WAITING_ROOM);
        }

    }

    public void sendResult(int steps,float time){
        try{
            byte[] mensaje;
            mensaje = ByteBuffer.allocate(8).putInt(steps).putFloat(time).array();
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(getApiClient(), mensaje, mRoomID);
        }catch(Exception e){

        }
    }

    public void sendLevelNum(int n){
        try{
            byte[] lvl;
            lvl = ByteBuffer.allocate(12).putInt(0).putFloat(0).putInt(n).array();
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(getApiClient(), lvl, mRoomID);
        }catch(Exception e){

        }
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        int steps;
        float time;
        byte[] b = rtm.getMessageData();
        BaseGameUtils.makeSimpleDialog(activity, Integer.toString(b.length));
        ByteBuffer bf = ByteBuffer.wrap(b);

            steps = bf.getInt();
            time = bf.getFloat();
            game.updateSteps(steps, time);
    }

    @Override
    public void onConnectedToRoom(Room arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnectedFromRoom(Room arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onP2PConnected(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onP2PDisconnected(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeerDeclined(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeerJoined(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeerLeft(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeersConnected(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPeersDisconnected(Room arg0, List<String> arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRoomAutoMatching(Room arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRoomConnecting(Room arg0) {
        // TODO Auto-generated method stub

    }

}