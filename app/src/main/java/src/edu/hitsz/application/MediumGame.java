package src.edu.hitsz.application;

import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.factory.SuperEliteEnemyFactory;

import java.io.IOException;

/**
 * 中等难度游戏
 *
 */
public class MediumGame extends AbstractGame {

    private int difficultyIncreaseCount = 0;

    public MediumGame(boolean isMusic) throws IOException {
        super(isMusic);
        this.difficulty = "MEDIUM";
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE2;
    }

    @Override
    protected void setEnemyParameters() {
        this.enemyMaxNumber = 5;
        this.rateBetweenMobAndElite = 0.5;
        this.rateBetweenEliteAndSuperElite = 0.75;
    }

    @Override
    protected void setBossParameters() {
        this.generateBossThreshold = 2000;
        BossEnemyFactory.initialHp = 300;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        this.increaseDifficultyCycleDuration = 30000; // 30秒提升一次难度
    }

    @Override
    protected boolean needBossOrNot() {
        return true;
    }

    @Override
    protected boolean bossHpIncreaseOrNot() {
        return false;
    }

    @Override
    protected boolean difficultyIncreaseOrNot() {
        return true;
    }

    @Override
    protected void increaseDifficulty() {
        difficultyIncreaseCount++;
        System.out.println("中等难度提升第" + difficultyIncreaseCount + "次");

        switch (difficultyIncreaseCount) {
            case 1:
                increaseEnemyHp();
                break;
            case 2:
                decreaseEnemyCycle();
                break;
            case 3:
                increaseEnemySpeed();
                break;
            default:
                // 后续每次提升综合难度
                increaseEnemyHp();
                decreaseEnemyCycle();
                break;
        }
    }
}