package com.example.myapplication.strategy;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class CircleShootStrategy implements ShootStrategy {
    @Override
    public List<Bullet> shoot(Aircraft aircraft) {
        List<Bullet> bullets = new ArrayList<>();
        int shootNum = aircraft.getShootNum();
        int aircraftX = aircraft.getLocationX();
        int aircraftY = aircraft.getLocationY();
        int aircraftWidth = aircraft.getImage().getWidth();
        int aircraftHeight = aircraft.getImage().getHeight();
        int power = aircraft.getPower();

        String bulletKey = aircraft.isHero() ? "HeroBullet" : "EnemyBullet";
        Bitmap bulletImage = ImageManager.getInstance().get(bulletKey);
        if (bulletImage == null) {
            return bullets;
        }

        int centerX = aircraftX + aircraftWidth / 2;
        int centerY = aircraftY + aircraftHeight / 2;

        for (int i = 0; i < shootNum; i++) {
            double angle = 2 * Math.PI * i / shootNum;
            int speedX = (int) (5 * Math.cos(angle));
            int speedY = (int) (5 * Math.sin(angle));
            int bulletX = centerX - bulletImage.getWidth() / 2;
            int bulletY = centerY - bulletImage.getHeight() / 2;

            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, power, bulletImage, aircraft.isHero());
            bullets.add(bullet);
        }

        return bullets;
    }
}
