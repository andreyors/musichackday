package com.mio.musicitout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class TrainingDetailActivity extends AppCompatActivity implements ConnectionStateCallback, Player.NotificationCallback {

    public static final String EXTRA_PARAM_ID = "training_id";

    private ExerciseListAdapter mAdapter;
    private ListView mRecyclerView;

    private  boolean isStarted = false;

    private SpotifyPlayer mPlayer;

    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("Notification", "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d("Notification", "ERROR" + error);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_detail);

        mAdapter = new ExerciseListAdapter(this);

        mAdapter.addSectionHeaderItem("Round #1 of 5");

        mAdapter.addItem("Jumping jacks (x5)");
        mAdapter.addItem("Wall sit (x10)");
        mAdapter.addItem("Push-ups (x5)");
        mAdapter.addItem("Abdominal crunch (x5)");
        mAdapter.addItem("Step-up onto chair (x5)");

        mAdapter.addSectionHeaderItem("Round #2 of 5");

        mAdapter.addItem("Squat (x10)");
        mAdapter.addItem("Triceps dip on chair (x10)");
        mAdapter.addItem("Plank (x5)");
        mAdapter.addItem("High knees running in place (x10)");
        mAdapter.addItem("Lunge (x10)");

        mRecyclerView = (ListView) findViewById(R.id.exercises);
        mRecyclerView.setAdapter(mAdapter);

        mPlayer = (SpotifyPlayer) IntentHelper.getObjectForKey("player");
        mPlayer.addConnectionStateCallback(TrainingDetailActivity.this);
        mPlayer.addNotificationCallback(TrainingDetailActivity.this);
    }

    public void toggleExercise(View view) {

        if (!isStarted) {
            isStarted = true;

            final LinearLayout layout = (LinearLayout) (findViewById(R.id.viewA));
            layout.setClickable(false);

            mPlayer.playUri(null, "spotify:track:6qHR3naun0EMcfVtOTkro0", 0, 0);

            Toast.makeText(this, "HELLO", Toast.LENGTH_LONG).show();

            //Notification.play(this, Notification.NEXT);

            isStarted = false;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layout.setClickable(true);
                    mPlayer.pause(mOperationCallback);
                }
            }, 6000);
        }
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(int i) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}
