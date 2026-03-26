package com.example.myapplication.basic;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Bullet extends Entity {
    // 伤害值
    private int power;
    // 图片
    private Bitmap image;
    
    public Bullet(int locationX, int locationY, int speedX, int speedY, int power, Bitmap image) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
        this.image = image;
        updateRectangle();
    }
    
    /**
     * 子弹移动
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
    public int getPower() {
        return power;
    }
    
    public void setPower(int power) {
        this.power = power;
    }
    
    public Bitmap getImage() {
        return image;
    }
    
    public void setImage(Bitmap image) {
        this.image = image;
        updateRectangle();
    }
}