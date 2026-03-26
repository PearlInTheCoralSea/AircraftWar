package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class MobEnemy extends Aircraft {
    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
    }
    
    /**
     * 敌机射击
     * @return 子弹列表
     */
    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        int aircraftX = getLocationX();
        int aircraftY = getLocationY();
        int aircraftWidth = getImage().getWidth();
        
        // 加载子弹图片
        Bitmap bulletImage = ImageManager.getInstance(null).get("EnemyBullet");
        if (bulletImage == null) {
            return bullets;
        }
        
        // 计算子弹初始位置
        int bulletX = aircraftX + aircraftWidth / 2 - bulletImage.getWidth() / 2;
        int bulletY = aircraftY + getImage().getHeight();
        
        // 创建子弹
        Bullet bullet = new Bullet(bulletX, bulletY, 0, 3, 10, bulletImage);
        bullets.add(bullet);
        
        return bullets;
    }
}