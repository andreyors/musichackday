package com.mio.musicitout;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class Notification {
    public static final int START = 0;
    public static final int NEXT = 1;


    public static void play(final Context context, final int type) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                MediaPlayer mediaPlayer = new MediaPlayer();

                int resId = -1;

                switch (type) {
                    case Notification.NEXT:
                        resId = R.raw.next;
                        break;

                    case Notification.START:
                        resId = R.raw.start;
                        break;
                }

                if (resId != -1) {
                    mediaPlayer = MediaPlayer.create(context, resId);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();

                    while (mediaPlayer.isPlaying() == true) {

                    }
                }
            }
        }).start();

    }
}
