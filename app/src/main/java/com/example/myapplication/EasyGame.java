package com.example.myapplication;

import android.content.Context;

public class EasyGame extends AbstractGame {

    public EasyGame(Context context, boolean isMusic) {
        super(context, isMusic);
    }

    @Override
    protected void setEnemyParameters() {
        enemyMaxNumber = 3;
        rateBetweenMobAndElite = 0.7;
        rateBetweenEliteAndSuperElite = 0.9;
        difficulty = "简单";
        difficultyKey = "easy";
    }

    @Override
    protected void setBossParameters() {
        generateBossThreshold = 3000;
        bossHp = 200;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        difficultyIncreaseCycleFrames = 0; // 不递增
    }

    @Override
    protected void setBackgroundImage() {
        backgroundImage = ImageManager.getInstance().getBackgroundImage(1);
    }

    @Override
    protected boolean needBossOrNot() {
        return false;
    }

    @Override
    protected boolean difficultyIncreaseOrNot() {
        return false;
    }

    @Override
    protected void increaseDifficulty(int phase) {
        // 简单模式不递增
    }

    @Override
    protected boolean bossHpIncreaseOrNot() {
        return false;
    }
}
