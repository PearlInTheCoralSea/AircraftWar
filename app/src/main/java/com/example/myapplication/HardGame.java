package com.example.myapplication;

import android.content.Context;

public class HardGame extends AbstractGame {

    public HardGame(Context context, boolean isMusic) {
        super(context, isMusic);
    }

    @Override
    protected void setEnemyParameters() {
        enemyMaxNumber = 8;
        rateBetweenMobAndElite = 0.3;
        rateBetweenEliteAndSuperElite = 0.6;
        difficulty = "困难";
        difficultyKey = "hard";
    }

    @Override
    protected void setBossParameters() {
        generateBossThreshold = 1500;
        bossHp = 500;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        difficultyIncreaseCycleFrames = 1250; // ~20秒
    }

    @Override
    protected void setBackgroundImage() {
        backgroundImage = ImageManager.getInstance().getBackgroundImage(3);
    }

    @Override
    protected boolean needBossOrNot() {
        return true;
    }

    @Override
    protected boolean difficultyIncreaseOrNot() {
        return true;
    }

    @Override
    protected void increaseDifficulty(int phase) {
        switch (phase) {
            case 0: increaseEnemyHp(); break;
            case 1: decreaseEnemyCycle(); break;
            case 2: increaseEnemySpeed(); break;
            case 3:
                increaseEnemyHp();
                decreaseEnemyCycle();
                increaseEnemySpeed();
                break;
        }
    }

    @Override
    protected boolean bossHpIncreaseOrNot() {
        return true;
    }
}
