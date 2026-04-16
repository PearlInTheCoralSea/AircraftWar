package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.strategy.ShootStrategy;

import java.util.List;

public class HeroAircraft extends Aircraft {
    private int bombNum;
    private static final int MAX_BOMB_NUM = 3;

    public HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp, Bitmap image, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.shootStrategy = shootStrategy;
        this.bombNum = MAX_BOMB_NUM;
        this.direction = -1;
        this.isHero = true;
        this.shootNum = 1;
        this.power = 30;
    }

    @Override
    public List<Bullet> shoot() {
        return super.shoot();
    }

    public void addBomb() {
        if (bombNum < MAX_BOMB_NUM) {
            bombNum++;
        }
    }

    public boolean useBomb() {
        if (bombNum > 0) {
            bombNum--;
            return true;
        }
        return false;
    }

    public int getBombNum() { return bombNum; }
    public void setBombNum(int bombNum) {
        this.bombNum = Math.max(0, Math.min(bombNum, MAX_BOMB_NUM));
    }
}
