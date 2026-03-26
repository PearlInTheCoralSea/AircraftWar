package com.example.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioSystem {
    private static AudioSystem instance;
    private Context context;
    private MediaPlayer bgmPlayer;
    private SoundPool soundPool;
    
    // 音效ID
    private int bulletSoundId;
    private int bombSoundId;
    private int hitSoundId;
    private int gameOverSoundId;
    private int getPropSoundId;
    
    private AudioSystem(Context context) {
        this.context = context;
        init();
    }
    
    public static AudioSystem getInstance(Context context) {
        if (instance == null) {
            instance = new AudioSystem(context);
        }
        return instance;
    }
    
    private void init() {
        // 初始化SoundPool
        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();
        
        // 加载音效
        bulletSoundId = soundPool.load(context, R.raw.bullet, 1);
        bombSoundId = soundPool.load(context, R.raw.bomb, 1);
        hitSoundId = soundPool.load(context, R.raw.hit, 1);
        gameOverSoundId = soundPool.load(context, R.raw.gameover, 1);
        getPropSoundId = soundPool.load(context, R.raw.getprop, 1);
        
        // 初始化MediaPlayer
        bgmPlayer = MediaPlayer.create(context, R.raw.bgm);
        bgmPlayer.setLooping(true);
    }
    
    // 播放背景音乐
    public void playBGM() {
        if (bgmPlayer != null && !bgmPlayer.isPlaying()) {
            bgmPlayer.start();
        }
    }
    
    // 停止背景音乐
    public void stopBGM() {
        if (bgmPlayer != null && bgmPlayer.isPlaying()) {
            bgmPlayer.pause();
        }
    }
    
    // 播放子弹音效
    public void playBulletSound() {
        soundPool.play(bulletSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
    
    // 播放爆炸音效
    public void playBombSound() {
        soundPool.play(bombSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
    
    // 播放击中音效
    public void playHitSound() {
        soundPool.play(hitSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
    
    // 播放游戏结束音效
    public void playGameOverSound() {
        soundPool.play(gameOverSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
    
    // 播放获取道具音效
    public void playGetPropSound() {
        soundPool.play(getPropSoundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
    
    // 释放资源
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (bgmPlayer != null) {
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }
}