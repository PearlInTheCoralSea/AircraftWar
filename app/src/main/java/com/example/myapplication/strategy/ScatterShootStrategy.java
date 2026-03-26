package com.example.myapplication.strategy;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.aircraft.HeroAircraft;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class ScatterShootStrategy implements ShootStrategy {
    @Override
    public List<Bullet> shoot(HeroAircraft aircraft) {
        List<Bullet> bullets = new ArrayList<>();
        int aircraftX = aircraft.getLocationX();
        int aircraftY = aircraft.getLocationY();
        int aircraftWidth = aircraft.getImage().getWidth();
        
        // 加载子弹图片
        Bitmap bulletImage = ImageManager.getInstance(null).get("HeroBullet");
        if (bulletImage == null) {
            return bullets;
        }
        
        // 计算子弹初始位置和速度
        int bulletWidth = bulletImage.getWidth();
        int bulletHeight = bulletImage.getHeight();
        
        // 中子弹
        int bulletX1 = aircraftX + aircraftWidth / 2 - bulletWidth / 2;
        int bulletY1 = aircraftY - bulletHeight;
        Bullet bullet1 = new Bullet(bulletX1, bulletY1, 0, -5, 20, bulletImage);
        bullets.add(bullet1);
        
        // 左子弹
        int bulletX2 = aircraftX + aircraftWidth / 4 - bulletWidth / 2;
        int bulletY2 = aircraftY - bulletHeight;
        Bullet bullet2 = new Bullet(bulletX2, bulletY2, -2, -4, 20, bulletImage);
        bullets.add(bullet2);
        
        // 右子弹
        int bulletX3 = aircraftX + 3 * aircraftWidth / 4 - bulletWidth / 2;
        int bulletY3 = aircraftY - bulletHeight;
        Bullet bullet3 = new Bullet(bulletX3, bulletY3, 2, -4, 20, bulletImage);
        bullets.add(bullet3);
        
        return bullets;
    }
}