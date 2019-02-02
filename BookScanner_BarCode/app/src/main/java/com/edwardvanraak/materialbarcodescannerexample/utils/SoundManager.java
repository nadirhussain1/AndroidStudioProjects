package com.edwardvanraak.materialbarcodescannerexample.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.edwardvanraak.materialbarcodescannerexample.R;

import java.util.HashMap;

/**
 * Created by nadirhussain on 03/04/2017.
 */

public class SoundManager {
    public static final String BUY_SOUND = "BUY_SOUND";
    public static final String REJECT_SOUND = "REJECT_SOUND";
    public static final String BLANK_SOUND = "BLANK_SOUND";

    private static boolean loaded = false;
    private static SoundPool soundPool;
    private static HashMap<String, Integer> soundPoolMap = new HashMap();

    public static void initSounds(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        soundPoolMap.put(BUY_SOUND, soundPool.load(context, R.raw.buy_sound, 1));
        soundPoolMap.put(REJECT_SOUND, soundPool.load(context, R.raw.reject_sound, 1));
        soundPoolMap.put(BLANK_SOUND, soundPool.load(context, R.raw.blank_sound, 1));
    }

    public static void playSound(Context context, String soundKey) {
        if (soundPool == null || soundPoolMap == null) {
            initSounds(context);
        }
        float volume = 1.0f;
        soundPool.play(soundPoolMap.get(soundKey), volume, volume, 1, 0, 1f);
    }
}
