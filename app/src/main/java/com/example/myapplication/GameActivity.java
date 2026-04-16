package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.factory.EnemyFactory;

public class GameActivity extends AppCompatActivity {

    private AbstractGame gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String difficulty = getIntent().getStringExtra("difficulty");
        boolean isMusic = getIntent().getBooleanExtra("isMusic", true);

        // 重置工厂默认值
        EnemyFactory.resetDefaults();

        // 初始化音频系统
        AudioSystem audioSystem = AudioSystem.getInstance(this);
        audioSystem.setEnabled(isMusic);

        // 根据难度创建对应的游戏
        switch (difficulty != null ? difficulty : "easy") {
            case "easy":
                gameView = new EasyGame(this, isMusic);
                break;
            case "medium":
                gameView = new MediumGame(this, isMusic);
                break;
            case "hard":
                gameView = new HardGame(this, isMusic);
                break;
            default:
                gameView = new EasyGame(this, isMusic);
                break;
        }

        // 设置游戏结束回调
        gameView.setGameOverCallback(score -> {
            // 返回菜单
            finish();
        });

        setContentView(gameView);

        // 开始播放BGM
        if (isMusic) {
            audioSystem.playBGM();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioSystem audio = AudioSystem.getInstance();
        if (audio != null) {
            audio.stopBGM();
            audio.stopBossBGM();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AudioSystem audio = AudioSystem.getInstance();
        if (audio != null && audio.isEnabled()) {
            audio.playBGM();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioSystem audio = AudioSystem.getInstance();
        if (audio != null) {
            audio.release();
        }
    }
}
