package src.edu.hitsz.application;

import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.factory.SuperEliteEnemyFactory;

import java.io.IOException;

/**
 * 简单难度游戏
 */
public class EasyGame extends AbstractGame {

    public EasyGame(boolean isMusic) throws IOException {
        super(isMusic);
        this.difficulty = "EASY";
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE;
    }

    @Override
    protected void setEnemyParameters() {
        this.enemyMaxNumber = 3;
        this.rateBetweenMobAndElite = 0.7;
        this.rateBetweenEliteAndSuperElite = 0.9;
    }

    @Override
    protected void setBossParameters() {
        this.generateBossThreshold = 3000;
        BossEnemyFactory.initialHp = 200;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        // 简单模式不增加难度
    }

    @Override
    protected boolean needBossOrNot() {
        return false;
    }

    @Override
    protected boolean bossHpIncreaseOrNot() {
        return false;
    }

    @Override
    protected boolean difficultyIncreaseOrNot() {
        return false;
    }

    @Override
    protected void increaseDifficulty() {
        // 空实现
    }
}