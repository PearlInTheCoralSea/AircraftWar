package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.strategy.ShootStrategy;

import java.util.List;

public class HeroAircraft extends Aircraft {
    // 射击策略
    private ShootStrategy shootStrategy;
    // 炸弹数量
    private int bombNum;
    // 最大炸弹数量
    private static final int MAX_BOMB_NUM = 3;
    
    public HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.shootStrategy = shootStrategy;
        this.bombNum = MAX_BOMB_NUM;
    }
    
    /**
     * 英雄机射击
     * @return 子弹列表
     */
    public List<Bullet> shoot() {
        return shootStrategy.shoot(this);
    }
    
    /**
     * 增加炸弹数量
     */
    public void addBomb() {
        if (bombNum < MAX_BOMB_NUM) {
            bombNum++;
        }
    }
    
    /**
     * 使用炸弹
     * @return 是否成功使用
     */
    public boolean useBomb() {
        if (bombNum > 0) {
            bombNum--;
            return true;
        }
        return false;
    }
    
    // Getter和Setter方法
    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }
    
    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }
    
    public int getBombNum() {
        return bombNum;
    }
    
    public void setBombNum(int bombNum) {
        this.bombNum = bombNum;
        if (this.bombNum > MAX_BOMB_NUM) {
            this.bombNum = MAX_BOMB_NUM;
        }
        if (this.bombNum < 0) {
            this.bombNum = 0;
        }
    }
}