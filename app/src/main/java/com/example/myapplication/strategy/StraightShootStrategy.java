package com.example.myapplication.strategy;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.aircraft.HeroAircraft;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class StraightShootStrategy implements ShootStrategy {
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
        
        // 计算子弹初始位置
        int bulletX = aircraftX + aircraftWidth / 2 - bulletImage.getWidth() / 2;
        int bulletY = aircraftY - bulletImage.getHeight();
        
        // 创建子弹
        Bullet bullet = new Bullet(bulletX, bulletY, 0, -5, 30, bulletImage);
        bullets.add(bullet);
        
        return bullets;
    }
}