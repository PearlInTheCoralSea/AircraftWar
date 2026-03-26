package src.edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemyAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.SuperEliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 *
 */
public class SuperEliteEnemyFactory implements EnemyAircraftFactory {

    public static int initialHp = 100;

    public static int initialShootNum = 3;

    public static int initialPower = 20;

    @Override
    public AbstractEnemyAircraft createEnemyAircraft() {
        return new SuperEliteEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.SUPER_ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                Math.random() > 0.5 ? 5 : -5,
                10,
                initialHp,
                initialShootNum,
                initialPower);
    }

}
