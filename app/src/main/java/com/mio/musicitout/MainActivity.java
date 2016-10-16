package com.mio.musicitout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String CLIENT_ID = "8c7c22c572a9423e98a120eb84211952";
    private static final String REDIRECT_URI = "mio://callback";

    private static final int REQUEST_CODE = 1337;

    private boolean mIsBound;

    private Messenger mServiceMessenger = null;

    private final Messenger mMessenger = new Messenger(new IncomingMessageHandler());

    private ServiceConnection mConnection = this;

    /**
     * Handle incoming messages from MyService
     */
    private static class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PlayMusic.MSG_SET_INT_VALUE:
                    break;
                case PlayMusic.MSG_SET_STRING_VALUE:
                    String str1 = msg.getData().getString("str1");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        automaticBind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void automaticBind() {
        if (PlayMusic.isRunning()) {
            doBindService();
        }
    }

    private void sendMessageToService(int intvaluetosend) {
        if (mIsBound) {
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, PlayMusic.MSG_SET_INT_VALUE, intvaluetosend, 0);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                }
            }
        }
    }

    /**
     * Bind this Activity to MyService
     */
    private void doBindService() {
        bindService(new Intent(this, PlayMusic.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    /**
     * Un-bind this Activity to MyService
     */
    private void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, PlayMusic.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mServiceMessenger = new Messenger(service);
        try {
            Message msg = Message.obtain(null, PlayMusic.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            mServiceMessenger.send(msg);
        }
        catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
        mServiceMessenger = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        } catch (Throwable t) {
            Log.e("Main", "Failed to unbind from the service", t);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        IntentHelper.addObjectForKey(spotifyPlayer, "player");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });

                Intent TrainingIntent = new Intent(MainActivity.this, TrainingActivity.class);
                MainActivity.this.startActivity(TrainingIntent);
            }
        }

    }

    public void connectSpotify(View view) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI
        );
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
}