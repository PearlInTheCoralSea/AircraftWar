package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.dao.GameRecord;
import com.example.myapplication.dao.GameRecordDaoImpl;

import java.util.ArrayList;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    private GameRecordDaoImpl dao;
    private RankingAdapter adapter;
    private String currentDifficulty;
    private ListView listView;
    private TextView tvEmpty;
    private Button btnFilterEasy, btnFilterMedium, btnFilterHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        currentDifficulty = getIntent().getStringExtra("difficulty");
        if (currentDifficulty == null) {
            currentDifficulty = "easy";
        }

        dao = new GameRecordDaoImpl(getApplicationContext());
        adapter = new RankingAdapter(this, new ArrayList<>());

        listView = findViewById(R.id.lv_ranking);
        tvEmpty = findViewById(R.id.tv_empty);
        btnFilterEasy = findViewById(R.id.btn_filter_easy);
        btnFilterMedium = findViewById(R.id.btn_filter_medium);
        btnFilterHard = findViewById(R.id.btn_filter_hard);
        Button btnDelete = findViewById(R.id.btn_delete);
        Button btnBack = findViewById(R.id.btn_back);

        listView.setAdapter(adapter);

        // 列表项点击选中
        listView.setOnItemClickListener((parent, view, position, id) -> {
            adapter.setSelectedPosition(position);
        });

        // 难度筛选按钮
        btnFilterEasy.setOnClickListener(v -> loadRecords("easy"));
        btnFilterMedium.setOnClickListener(v -> loadRecords("medium"));
        btnFilterHard.setOnClickListener(v -> loadRecords("hard"));

        // 删除按钮
        btnDelete.setOnClickListener(v -> deleteSelectedRecord());

        // 返回按钮
        btnBack.setOnClickListener(v -> finish());

        // 加载初始数据
        loadRecords(currentDifficulty);
    }

    private void loadRecords(String difficulty) {
        currentDifficulty = difficulty;
        List<GameRecord> records = dao.getRecordsByDifficulty(difficulty);
        adapter.updateData(records);

        // 更新空状态
        if (records.isEmpty()) {
            listView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }

        // 更新按钮高亮状态
        updateFilterButtons(difficulty);
    }

    private void updateFilterButtons(String difficulty) {
        float normalAlpha = 0.6f;
        float activeAlpha = 1.0f;

        btnFilterEasy.setAlpha(normalAlpha);
        btnFilterMedium.setAlpha(normalAlpha);
        btnFilterHard.setAlpha(normalAlpha);

        switch (difficulty) {
            case "easy":
                btnFilterEasy.setAlpha(activeAlpha);
                break;
            case "medium":
                btnFilterMedium.setAlpha(activeAlpha);
                break;
            case "hard":
                btnFilterHard.setAlpha(activeAlpha);
                break;
        }
    }

    private void deleteSelectedRecord() {
        int selectedPosition = adapter.getSelectedPosition();
        if (selectedPosition == -1) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请先选择要删除的记录！")
                    .setPositiveButton("确定", null)
                    .show();
            return;
        }

        GameRecord record = adapter.getItem(selectedPosition);

        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("是否删除选中的记录？")
                .setPositiveButton("删除", (dialog, which) -> {
                    dao.deleteRecord(record);
                    loadRecords(currentDifficulty);
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
