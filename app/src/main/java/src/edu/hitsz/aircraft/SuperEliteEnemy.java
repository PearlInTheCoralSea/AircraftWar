package src.edu.hitsz.aircraft;

import edu.hitsz.prop.Observer;
import edu.hitsz.strategy.ScatterShootStrategy;

/**
 * 精英敌机
 * 可以射击
 *
 *
 */
public class SuperEliteEnemy extends AbstractEnemyAircraft implements Observer {

    public SuperEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int shootNum, int power) {
        super(locationX, locationY, speedX, speedY, hp, shootNum, power);
        this.setShootStrategy(new ScatterShootStrategy());
        this.scoreAward = 300;
    }

    @Override
    public void update(boolean flag) {
        if (flag) {
            this.vanish();
        }
    }

}
