package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.observer.Observer;
import com.example.myapplication.strategy.ScatterShootStrategy;

public class SuperEliteEnemy extends Aircraft implements Observer {
    private int score;

    public SuperEliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.score = score;
        this.scoreAward = score;
        this.direction = 1;
        this.isHero = false;
        this.shootNum = 3;
        this.power = 20;
        this.shootStrategy = new ScatterShootStrategy();
    }

    @Override
    public void update(boolean flag) {
        if (flag) {
            // SuperEliteEnemy 受炸弹影响只减半血量
            decreaseHp(getHp() / 2);
        }
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; this.scoreAward = score; }
}
