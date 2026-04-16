package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioSystem {
    private static AudioSystem instance;
    private Context context;
    private MediaPlayer bgmPlayer;
    private MediaPlayer bossBgmPlayer;
    private SoundPool soundPool;
    private boolean enabled = true;

    // 音效ID
    private int bulletSoundId;
    private int bombSoundId;
    private int hitSoundId;
    private int gameOverSoundId;
    private int getPropSoundId;

    private AudioSystem(Context context) {
        this.context = context.getApplicationContext();
        init();
    }

    public static AudioSystem getInstance(Context context) {
        if (instance == null) {
            instance = new AudioSystem(context);
        }
        return instance;
    }

    public static AudioSystem getInstance() {
        return instance;
    }

    private void init() {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        bulletSoundId = soundPool.load(context, R.raw.bullet, 1);
        bombSoundId = soundPool.load(context, R.raw.bomb_explosion, 1);
        hitSoundId = soundPool.load(context, R.raw.bullet_hit, 1);
        gameOverSoundId = soundPool.load(context, R.raw.game_over, 1);
        getPropSoundId = soundPool.load(context, R.raw.get_supply, 1);

        bgmPlayer = MediaPlayer.create(context, R.raw.bgm);
        if (bgmPlayer != null) {
            bgmPlayer.setLooping(true);
        }

        bossBgmPlayer = MediaPlayer.create(context, R.raw.bgm_boss);
        if (bossBgmPlayer != null) {
            bossBgmPlayer.setLooping(true);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stopBGM();
            stopBossBGM();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void playBGM() {
        if (!enabled) return;
        if (bgmPlayer != null && !bgmPlayer.isPlaying()) {
            bgmPlayer.start();
        }
    }

    public void stopBGM() {
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }

    public void playBossBGM() {
        if (!enabled) return;
        stopBGM();
        if (bossBgmPlayer != null && !bossBgmPlayer.isPlaying()) {
            bossBgmPlayer.start();
        }
    }

    public void stopBossBGM() {
        if (bossBgmPlayer != null && bossBgmPlayer.isPlaying()) {
            bossBgmPlayer.pause();
        }
    }

    public void switchToNormalBGM() {
        stopBossBGM();
        playBGM();
    }

    public void playBulletSound() {
        if (!enabled) return;
        soundPool.play(bulletSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playBombSound() {
        if (!enabled) return;
        soundPool.play(bombSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playHitSound() {
        if (!enabled) return;
        soundPool.play(hitSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playGameOverSound() {
        if (!enabled) return;
        soundPool.play(gameOverSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playGetPropSound() {
        if (!enabled) return;
        soundPool.play(getPropSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (bgmPlayer != null) {
            bgmPlayer.release();
            bgmPlayer = null;
        }
        if (bossBgmPlayer != null) {
            bossBgmPlayer.release();
            bossBgmPlayer = null;
        }
        instance = null;
    }
}
