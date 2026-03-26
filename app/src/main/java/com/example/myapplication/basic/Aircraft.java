package com.example.myapplication.basic;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class Aircraft extends Entity {
    // 生命值
    protected int maxHp;
    protected int hp;
    // 图片
    protected Bitmap image;
    
    public Aircraft(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image) {
        super(locationX, locationY, speedX, speedY);
        this.maxHp = hp;
        this.hp = hp;
        this.image = image;
        updateRectangle();
    }
    
    /**
     * 飞机受到伤害
     * @param damage 伤害值
     */
    public void decreaseHp(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }
    
    /**
     * 飞机是否被摧毁
     * @return 是否被摧毁
     */
    public boolean isDestroyed() {
        return hp <= 0;
    }
    
    /**
     * 飞机移动
     */
    @Override
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        updateRectangle();
    }
    
    /**
     * 更新碰撞框
     */
    @Override
    public void updateRectangle() {
        if (image != null) {
            rectangle.left = locationX;
            rectangle.top = locationY;
            rectangle.right = locationX + image.getWidth();
            rectangle.bottom = locationY + image.getHeight();
        }
    }
    
    // Getter和Setter方法
    public int getMaxHp() {
        return maxHp;
    }
    
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
    
    public int getHp() {
        return hp;
    }
    
    public void setHp(int hp) {
        this.hp = hp;
    }
    
    public Bitmap getImage() {
        return image;
    }
    
    public void setImage(Bitmap image) {
        this.image = image;
        updateRectangle();
    }
}