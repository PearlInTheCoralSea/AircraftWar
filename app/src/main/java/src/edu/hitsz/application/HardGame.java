package src.edu.hitsz.application;

import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.factory.SuperEliteEnemyFactory;

import java.io.IOException;

/**
 * 困难难度游戏
 */
public class HardGame extends AbstractGame {

    private int difficultyIncreaseCount = 0;
    private int bossGenerationCount = 0;

    public HardGame(boolean isMusic) throws IOException {
        super(isMusic);
        this.difficulty = "HARD";
        this.backgroundImage = ImageManager.BACKGROUND_IMAGE3;
    }

    @Override
    protected void setEnemyParameters() {
        this.enemyMaxNumber = 8;
        this.rateBetweenMobAndElite = 0.3;
        this.rateBetweenEliteAndSuperElite = 0.6;
    }

    @Override
    protected void setBossParameters() {
        this.generateBossThreshold = 1500;
        BossEnemyFactory.initialHp = 500;
    }

    @Override
    protected void setDifficultyIncreaseSettings() {
        this.increaseDifficultyCycleDuration = 20000; // 20秒提升一次难度
    }

    @Override
    protected boolean needBossOrNot() {
        return true;
    }

    @Override
    protected boolean bossHpIncreaseOrNot() {
        return true;
    }

    @Override
    protected boolean difficultyIncreaseOrNot() {
        return true;
    }

    @Override
    protected void increaseDifficulty() {
        difficultyIncreaseCount++;
        System.out.println("困难难度提升第" + difficultyIncreaseCount + "次");

        // 每次Boss出现后提升难度
        bossGenerationCount++;
        increaseBossHp();

        switch (difficultyIncreaseCount % 4) {
            case 1:
                increaseEnemyHp();
                System.out.println("敌机血量提升");
                break;
            case 2:
                decreaseEnemyCycle();
                System.out.println("敌机生成加速");
                break;
            case 3:
                increaseEnemySpeed();
                System.out.println("敌机速度提升");
                break;
            case 0:
                // 综合提升
                increaseEnemyHp();
                decreaseEnemyCycle();
                increaseEnemySpeed();
                System.out.println("综合难度提升");
                break;
        }
    }
}