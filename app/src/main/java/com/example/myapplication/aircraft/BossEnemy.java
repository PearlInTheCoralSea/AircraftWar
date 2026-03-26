package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class BossEnemy extends Aircraft {
    // 得分
    private int score;
    // 射击间隔
    private int shootInterval;
    // 射击计数器
    private int shootCount;
    
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.score = score;
        this.shootInterval = 50;
        this.shootCount = 0;
    }
    
    /**
     * 敌机射击
     * @return 子弹列表
     */
    public List<Bullet> shoot() {
        List<Bullet> bullets = new ArrayList<>();
        shootCount++;
        
        if (shootCount % shootInterval != 0) {
            return bullets;
        }
        
        int aircraftX = getLocationX();
        int aircraftY = getLocationY();
        int aircraftWidth = getImage().getWidth();
        int aircraftHeight = getImage().getHeight();
        
        // 加载子弹图片
        Bitmap bulletImage = ImageManager.getInstance(null).get("EnemyBullet");
        if (bulletImage == null) {
            return bullets;
        }
        
        // 计算子弹初始位置
        int bulletWidth = bulletImage.getWidth();
        int bulletHeight = bulletImage.getHeight();
        
        // 创建子弹（圆形射击）
        int bulletCount = 8;
        double angleStep = 2 * Math.PI / bulletCount;
        
        for (int i = 0; i < bulletCount; i++) {
            double angle = i * angleStep;
            int speedX = (int) (3 * Math.cos(angle));
            int speedY = (int) (3 * Math.sin(angle));
            int bulletX = aircraftX + aircraftWidth / 2 - bulletWidth / 2;
            int bulletY = aircraftY + aircraftHeight / 2 - bulletHeight / 2;
            
            Bullet bullet = new Bullet(bulletX, bulletY, speedX, speedY, 20, bulletImage);
            bullets.add(bullet);
        }
        
        return bullets;
    }
    
    // Getter和Setter方法
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getShootInterval() {
        return shootInterval;
    }
    
    public void setShootInterval(int shootInterval) {
        this.shootInterval = shootInterval;
    }
    
    public int getShootCount() {
        return shootCount;
    }
    
    public void setShootCount(int shootCount) {
        this.shootCount = shootCount;
    }
}