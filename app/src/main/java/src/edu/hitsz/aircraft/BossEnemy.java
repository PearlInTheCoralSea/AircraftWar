package src.edu.hitsz.aircraft;


import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Observer;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.List;

/**
 * BOSS敌机
 * 可以射击
 *
 *
 */
public class BossEnemy extends AbstractEnemyAircraft implements Observer {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int shootNum, int power) {
        super(locationX, locationY, speedX, speedY, hp, shootNum, power);
        this.setShootStrategy(new RingShootStrategy());
        this.scoreAward = 400;
    }


    public void generateProp(List<AbstractProp> props) {
        PropFactory propFactory;
        int i = 0 ;
        for (;i<=2;i++) {
            if (Math.random() < 0.9) {
                double randomNumber = Math.random() * 4;
                if (randomNumber < 1) {
                    propFactory = new BloodPropFactory();
                } else if (randomNumber < 2) {
                    propFactory = new BombPropFactory();
                }
                else if (randomNumber < 3) {
                    propFactory = new SuperBulletPropFactory();
                }else {
                    propFactory = new BulletPropFactory();
                }
                props.add(propFactory.createProp(locationX, locationY, 0, 10));
            }
        }
    }

    @Override
    public void update(boolean flag) {
        if (flag) {
            this.vanish();
        }
    }

}
