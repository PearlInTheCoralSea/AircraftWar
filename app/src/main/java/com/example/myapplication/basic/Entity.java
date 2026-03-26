package com.example.myapplication.basic;

import android.graphics.Rect;

public abstract class Entity {
    // 位置
    protected int locationX;
    protected int locationY;
    // 速度向量
    protected int speedX;
    protected int speedY;
    // 碰撞框
    protected Rect rectangle;
    
    public Entity(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.rectangle = new Rect();
    }
    
    /**
     * 实体移动
     */
    public abstract void forward();
    
    /**
     * 检测碰撞
     * @param other 另一个实体
     * @return 是否碰撞
     */
    public boolean crash(Entity other) {
        return this.rectangle.intersect(other.rectangle);
    }
    
    /**
     * 更新碰撞框
     */
    public abstract void updateRectangle();
    
    // Getter和Setter方法
    public int getLocationX() {
        return locationX;
    }
    
    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }
    
    public int getLocationY() {
        return locationY;
    }
    
    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }
    
    public int getSpeedX() {
        return speedX;
    }
    
    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }
    
    public int getSpeedY() {
        return speedY;
    }
    
    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }
    
    public Rect getRectangle() {
        return rectangle;
    }
}