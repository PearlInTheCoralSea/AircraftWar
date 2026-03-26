package src.edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.aircraft.SuperEliteEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 炸弹道具 - 使用观察者模式
 *
 */
public class BombProp extends AbstractProp {

    private final List<Observer> observers = new ArrayList<>();
    private final List<AbstractProp> propsGenerated = new ArrayList<>();
    private int scoresAward;

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 添加观察者
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * 移除所有观察者
     */
    public void removeAllObservers() {
        observers.clear();
    }

    /**
     * 通知所有观察者
     */
    public void notifyAllObservers(boolean flag) {
        for (Observer observer : observers) {
            observer.update(flag);
        }
    }

    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullets) {
        this.scoresAward = 0;
        this.propsGenerated.clear();

        // 注册观察者 - 所有敌机和敌机子弹
        for (AbstractFlyingObject enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            if (enemyAircraft instanceof Observer) {
                addObserver((Observer) enemyAircraft);
            }
        }

        for (AbstractFlyingObject enemyBullet : enemyBullets) {
            if (enemyBullet.notValid()) {
                continue;
            }
            if (enemyBullet instanceof Observer) {
                addObserver((Observer) enemyBullet);
            }
        }

        // 通知观察者 - 炸弹生效
        notifyAllObservers(true);

        // 计算分数和生成道具
        for (Observer observer : observers) {
            if (observer instanceof MobEnemy) {
                scoresAward += ((MobEnemy) observer).getScoreAward();
            } else if (observer instanceof EliteEnemy) {
                scoresAward += ((EliteEnemy) observer).getScoreAward();
                ((EliteEnemy) observer).generateProp(propsGenerated);
            } else if (observer instanceof SuperEliteEnemy) {
                // 超级精英敌机血量减少但不销毁
                SuperEliteEnemy superElite = (SuperEliteEnemy) observer;
                superElite.decreaseHp(superElite.getHp() / 2); // 减少一半血量
                scoresAward += superElite.getScoreAward();
                superElite.generateProp(propsGenerated);
            }
            // Boss敌机不受影响，不处理
        }

        removeAllObservers();
        System.out.println("BombSupply active! Scores awarded: " + scoresAward);
    }

    public int getScoresAward() {
        return scoresAward;
    }

    public List<AbstractProp> getPropsGenerated() {
        return propsGenerated;
    }
}