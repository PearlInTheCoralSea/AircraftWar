package com.example.myapplication;

import android.content.Context;

public class MediumGame extends AbstractGame {

    public MediumGame(Context context, boolean isMusic) {
        super(context, isMusic);
    }

    @Override
    protected void setEnemyParameters() {
        enemyMaxNumber = 5;
        rateBetweenMobAndElite = 0.5;
        rateBetweenEliteAndSuperElite = 0.75;
        difficulty = "普通";
        difficultyKey = "medium";
    }

    @Override
    protected void setBossParameters() {
        generateBossThreshold = 2000;
        bossHp = 300;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        difficultyIncreaseCycleFrames = 1875; // ~30秒
    }

    @Override
    protected void setBackgroundImage() {
        backgroundImage = ImageManager.getInstance().getBackgroundImage(2);
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
        return false;
    }
}
