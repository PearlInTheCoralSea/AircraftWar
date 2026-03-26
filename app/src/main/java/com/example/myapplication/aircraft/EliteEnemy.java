package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.ImageManager;

import java.util.ArrayList;
import java.util.List;

public class EliteEnemy extends Aircraft {
    // 得分
    private int score;
    
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.score = score;
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
        int bulletX1 = aircraftX + aircraftWidth / 4 - bulletImage.getWidth() / 2;
        int bulletY1 = aircraftY + getImage().getHeight();
        
        int bulletX2 = aircraftX + 3 * aircraftWidth / 4 - bulletImage.getWidth() / 2;
        int bulletY2 = aircraftY + getImage().getHeight();
        
        // 创建子弹
        Bullet bullet1 = new Bullet(bulletX1, bulletY1, -1, 3, 15, bulletImage);
        bullets.add(bullet1);
        
        Bullet bullet2 = new Bullet(bulletX2, bulletY2, 1, 3, 15, bulletImage);
        bullets.add(bullet2);
        
        return bullets;
    }
    
    // Getter和Setter方法
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}