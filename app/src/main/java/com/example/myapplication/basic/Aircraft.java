package com.example.myapplication.basic;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.example.myapplication.strategy.ShootStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class Aircraft extends Entity {
    protected int maxHp;
    protected int hp;
    protected Bitmap image;
    protected int shootNum = 1;
    protected int power = 30;
    protected int direction = -1; // -1=向上(英雄), 1=向下(敌机)
    protected boolean isHero = false;
    protected ShootStrategy shootStrategy;
    protected int scoreAward = 0;

    public Aircraft(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image) {
        super(locationX, locationY, speedX, speedY);
        this.maxHp = hp;
        this.hp = hp;
        this.image = image;
        updateRectangle();
    }

    public void decreaseHp(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            vanish();
        }
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public boolean isDestroyed() {
        return hp <= 0 || notValid();
    }

    public List<Bullet> shoot() {
        if (shootStrategy != null) {
            return shootStrategy.shoot(this);
        }
        return new ArrayList<>();
    }

    @Override
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        updateRectangle();
    }

    @Override
    public void updateRectangle() {
        if (image != null) {
            rectangle.left = locationX;
            rectangle.top = locationY;
            rectangle.right = locationX + image.getWidth();
            rectangle.bottom = locationY + image.getHeight();
        }
    }

    // Getter和Setter
    public int getMaxHp() { return maxHp; }
    public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; updateRectangle(); }
    public int getShootNum() { return shootNum; }
    public void setShootNum(int shootNum) { this.shootNum = shootNum; }
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }
    public boolean isHero() { return isHero; }
    public ShootStrategy getShootStrategy() { return shootStrategy; }
    public void setShootStrategy(ShootStrategy s) { this.shootStrategy = s; }
    public int getScoreAward() { return scoreAward; }
    public void setScoreAward(int scoreAward) { this.scoreAward = scoreAward; }
}
