package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.dao.GameRecord;
import com.example.myapplication.dao.GameRecordDaoImpl;
import com.example.myapplication.factory.EnemyFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        gameView.setGameOverCallback(score -> showGameOverDialog(score));

        setContentView(gameView);

        // 开始播放BGM
        if (isMusic) {
            audioSystem.playBGM();
        }
    }

    private void showGameOverDialog(int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("游戏结束");
        builder.setMessage("你的得分为 " + score + " 分\n请输入用户名记录得分：");

        final EditText input = new EditText(this);
        input.setHint("User");
        input.setText("User");
        builder.setView(input);

        builder.setPositiveButton("确定", (dialog, which) -> {
            String userName = input.getText().toString().trim();
            if (userName.isEmpty()) {
                userName = "User";
            }

            // 保存记录到数据库
            GameRecordDaoImpl dao = new GameRecordDaoImpl(getApplicationContext());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
            String time = sdf.format(new Date());
            String diffKey = gameView.getDifficultyKey();
            dao.addRecord(new GameRecord(userName, time, score, diffKey));

            // 跳转排行榜
            Intent intent = new Intent(GameActivity.this, RankingActivity.class);
            intent.putExtra("difficulty", diffKey);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("取消", (dialog, which) -> {
            // 不保存，直接跳转排行榜
            Intent intent = new Intent(GameActivity.this, RankingActivity.class);
            intent.putExtra("difficulty", gameView.getDifficultyKey());
            startActivity(intent);
            finish();
        });

        builder.setCancelable(false);
        builder.show();
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
