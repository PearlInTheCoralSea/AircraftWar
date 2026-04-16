package com.example.myapplication.strategy;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class StraightShootStrategy implements ShootStrategy {
    @Override
    public List<Bullet> shoot(Aircraft aircraft) {
        List<Bullet> bullets = new ArrayList<>();
        int shootNum = aircraft.getShootNum();
        int aircraftX = aircraft.getLocationX();
        int aircraftY = aircraft.getLocationY();
        int aircraftWidth = aircraft.getImage().getWidth();
        int aircraftHeight = aircraft.getImage().getHeight();
        int direction = aircraft.getDirection();
        int power = aircraft.getPower();

        String bulletKey = aircraft.isHero() ? "HeroBullet" : "EnemyBullet";
        Bitmap bulletImage = ImageManager.getInstance().get(bulletKey);
        if (bulletImage == null) {
            return bullets;
        }

        for (int i = 0; i < shootNum; i++) {
            int bulletX = aircraftX + (i * 2 - shootNum + 1) * 10 + aircraftWidth / 2 - bulletImage.getWidth() / 2;
            int bulletY;
            if (direction < 0) {
                bulletY = aircraftY - bulletImage.getHeight();
            } else {
                bulletY = aircraftY + aircraftHeight;
            }
            int speedY = direction * 5;
            Bullet bullet = new Bullet(bulletX, bulletY, 0, speedY, power, bulletImage, aircraft.isHero());
            bullets.add(bullet);
        }

        return bullets;
    }
}
