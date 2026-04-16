package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.observer.Observer;

public class MobEnemy extends Aircraft implements Observer {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.direction = 1;
        this.isHero = false;
        this.shootNum = 0;
        this.scoreAward = 10;
    }

    @Override
    public void update(boolean flag) {
        if (flag) {
            vanish();
        }
    }
}
