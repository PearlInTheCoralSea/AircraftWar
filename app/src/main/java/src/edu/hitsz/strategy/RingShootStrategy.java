package src.edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class RingShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> doShoot(int shootNum, int aircraftLocationX, int aircraftLocationY, int aircraftSpeedY, int direction, int power, boolean isHero) {

        List<BaseBullet> res = new LinkedList<>();
        //int num = 20;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            double angle = 360.0 * i / shootNum;
            double radian = Math.toRadians(angle);
            int speedX = (int) (5 * Math.cos(radian));
            int speedY = (int) (5 * Math.sin(radian)) ;
            if (isHero) {
                baseBullet = new HeroBullet(aircraftLocationX, aircraftLocationY, speedX, speedY, power);
            } else {
                baseBullet = new EnemyBullet(aircraftLocationX, aircraftLocationY, speedX, speedY, power);
            }
            res.add(baseBullet);
        }
        return res;

    }

}
