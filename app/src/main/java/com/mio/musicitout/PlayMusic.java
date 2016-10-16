package com.mio.musicitout;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayMusic extends Service {
    private static boolean isRunning = false;

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SET_INT_VALUE = 3;
    public static final int MSG_SET_STRING_VALUE = 4;

    private Timer mTimer = new Timer();

    private SpotifyPlayer mPlayer;

    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler()); // Target we publish for clients to send messages to IncomingHandler.
    private List<Messenger> mClients = new ArrayList<>(); // Keeps track of all current registered clients.

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer.scheduleAtFixedRate(new MyTask(), 0, 100L);
        isRunning = true;
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            try {
                sendMessageToUI(1);
            } catch (Throwable t) {
                Log.e("TimerTick", "Timer Tick Failed.", t);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
        isRunning = false;
    }

    /**
     * Handle incoming messages from MainActivity
     */
    private class IncomingMessageHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_INT_VALUE:

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void sendMessageToUI(int intvaluetosend) {
        Iterator<Messenger> messengerIterator = mClients.iterator();
        while (messengerIterator.hasNext()) {
            Messenger messenger = messengerIterator.next();
            try {
                // Send data as an Integer
                messenger.send(Message.obtain(null, MSG_SET_INT_VALUE, intvaluetosend, 0));

                // Send data as a String
                Bundle bundle = new Bundle();
                bundle.putString("str1", "ab" + intvaluetosend + "cd");
                Message msg = Message.obtain(null, MSG_SET_STRING_VALUE);
                msg.setData(bundle);
                messenger.send(msg);

            } catch (RemoteException e) {
                // The client is dead. Remove it from the list.
                mClients.remove(messenger);
            }
        }
    }

}