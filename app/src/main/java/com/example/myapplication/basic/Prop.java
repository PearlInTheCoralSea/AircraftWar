package com.example.myapplication.basic;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Prop extends Entity {
    // 道具类型
    public enum PropType {
        BLOOD, BOMB, BULLET, SUPER_BULLET
    }
    
    private PropType type;
    private Bitmap image;
    
    public Prop(int locationX, int locationY, int speedX, int speedY, PropType type, Bitmap image) {
        super(locationX, locationY, speedX, speedY);
        this.type = type;
        this.image = image;
        updateRectangle();
    }
    
    /**
     * 道具移动
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
    public PropType getType() {
        return type;
    }
    
    public void setType(PropType type) {
        this.type = type;
    }
    
    public Bitmap getImage() {
        return image;
    }
    
    public void setImage(Bitmap image) {
        this.image = image;
        updateRectangle();
    }
}