package com.example.myapplication.strategy;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.aircraft.HeroAircraft;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class CircleShootStrategy implements ShootStrategy {
    @Override
    public List<Bullet> shoot(HeroAircraft aircraft) {
        List<Bullet> bullets = new ArrayList<>();
        int aircraftX = aircraft.getLocationX();
        int aircraftY = aircraft.getLocationY();
        int aircraftWidth = aircraft.getImage().getWidth();
        int aircraftHeight = aircraft.getImage().getHeight();
        
        // 加载子弹图片
        Bitmap bulletImage = ImageManager.getInstance(null).get("HeroBullet");
        if (bulletImage == null) {
            return bullets;
        }
        
        // 计算子弹初始位置
        int centerX = aircraftX + aircraftWidth / 2;
        int centerY = aircraftY + aircraftHeight / 2;
        int bulletWidth = bulletImage.getWidth();
        int bulletHeight = bulletImage.getHeight();
        
        // 创建8个方向的子弹
        int bulletCount = 8;
        double angleStep = 2 * Math.PI / bulletCount;
        
        for (int i = 0; i < bulletCount; i++) {
            double angle = i * angleStep;
            int speedX = (int) (5 * Math.cos(angle));
            int speedY = (int) (5 * Math.sin(angle));
            int bulletX = centerX - bulletWidth / 2;
            int bulletY = centerY - bulletHeight / 2;
            
            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, 15, bulletImage);
            bullets.add(bullet);
        }
        
        return bullets;
    }
}