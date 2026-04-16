package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartMenuActivity extends AppCompatActivity {

    private boolean isMusic = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button btnEasy = findViewById(R.id.btn_easy);
        Button btnMedium = findViewById(R.id.btn_medium);
        Button btnHard = findViewById(R.id.btn_hard);
        Switch switchMusic = findViewById(R.id.switch_music);
        TextView tvMusicStatus = findViewById(R.id.tv_music_status);

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isMusic = isChecked;
            tvMusicStatus.setText(isChecked ? "开" : "关");
            tvMusicStatus.setTextColor(isChecked ? 0xFF4CAF50 : 0xFFAAAAAA);
        });

        btnEasy.setOnClickListener(v -> startGame("easy"));
        btnMedium.setOnClickListener(v -> startGame("medium"));
        btnHard.setOnClickListener(v -> startGame("hard"));
    }

    private void startGame(String difficulty) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("isMusic", isMusic);
        startActivity(intent);
    }
}
