package com.example.myapplication.aircraft;

import android.graphics.Bitmap;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.observer.Observer;
import com.example.myapplication.strategy.CircleShootStrategy;

public class BossEnemy extends Aircraft implements Observer {
    private int score;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, Bitmap image) {
        super(locationX, locationY, speedX, speedY, hp, image);
        this.score = score;
        this.scoreAward = score;
        this.direction = 1;
        this.isHero = false;
        this.shootNum = 20;
        this.power = 20;
        this.shootStrategy = new CircleShootStrategy();
    }

    @Override
    public void update(boolean flag) {
        // Boss 不受炸弹影响
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; this.scoreAward = score; }
}
