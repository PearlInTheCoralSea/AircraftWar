package src.edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.prop.SuperBulletProp;

/**
 *
 */
public class SuperBulletPropFactory implements PropFactory {

    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new SuperBulletProp(locationX, locationY, speedX, speedY);
    }

}
