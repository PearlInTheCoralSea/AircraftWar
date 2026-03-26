package src.edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.DirectShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.List;

/**
 *
 */
public class SuperBulletProp extends AbstractProp {
    public SuperBulletProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemyAircraft> enemyAircrafts, List<BaseBullet> enemyBullets) {

        Runnable r1 = () -> {
            heroAircraft.changeShootNum(heroAircraft.getShootNum() + 7);
            heroAircraft.setShootStrategy(new RingShootStrategy());
        };

        Thread thread1 = new Thread(r1);
        thread1.start();

        Runnable r2 = () -> {
            try {
                thread1.join();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heroAircraft.changeShootNum(1);
            heroAircraft.setShootStrategy(new DirectShootStrategy());
        };

        Thread thread2 = new Thread(r2);
        thread2.start();

        System.out.println("FireSupply active!");
    }
}
