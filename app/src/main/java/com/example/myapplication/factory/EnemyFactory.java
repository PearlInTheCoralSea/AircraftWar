package com.example.myapplication.factory;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.myapplication.aircraft.MobEnemy;
import com.example.myapplication.aircraft.EliteEnemy;
import com.example.myapplication.aircraft.SuperEliteEnemy;
import com.example.myapplication.aircraft.BossEnemy;
import com.example.myapplication.ImageManager;

public class EnemyFactory {
    private static EnemyFactory instance;
    private ImageManager imageManager;

    // 可变 HP 静态字段，用于难度递增
    public static int mobInitialHp = 30;
    public static int eliteInitialHp = 60;
    public static int superEliteInitialHp = 100;
    public static int bossInitialHp = 240;

    private EnemyFactory(Context context) {
        imageManager = ImageManager.getInstance(context);
    }

    public static EnemyFactory getInstance(Context context) {
        if (instance == null) {
            instance = new EnemyFactory(context);
        }
        return instance;
    }

    public static void resetDefaults() {
        mobInitialHp = 30;
        eliteInitialHp = 60;
        superEliteInitialHp = 100;
        bossInitialHp = 240;
    }

    public MobEnemy createMobEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("MobEnemy");
        if (image == null) return null;
        return new MobEnemy(locationX, locationY, 0, 2, mobInitialHp, image);
    }

    public EliteEnemy createEliteEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("EliteEnemy");
        if (image == null) return null;
        int speedX = (Math.random() > 0.5) ? 1 : -1;
        return new EliteEnemy(locationX, locationY, speedX, 1, eliteInitialHp, 30, image);
    }

    public SuperEliteEnemy createSuperEliteEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("SuperEliteEnemy");
        if (image == null) return null;
        int speedX = (Math.random() > 0.5) ? 1 : -1;
        return new SuperEliteEnemy(locationX, locationY, speedX, 1, superEliteInitialHp, 50, image);
    }

    public BossEnemy createBossEnemy(int locationX, int locationY, int hp) {
        Bitmap image = imageManager.get("BossEnemy");
        if (image == null) return null;
        return new BossEnemy(locationX, locationY, 2, 0, hp, 100, image);
    }

    public BossEnemy createBossEnemy(int locationX, int locationY) {
        return createBossEnemy(locationX, locationY, bossInitialHp);
    }
}
