package com.example.myapplication.factory;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.myapplication.aircraft.MobEnemy;
import com.example.myapplication.aircraft.EliteEnemy;
import com.example.myapplication.aircraft.BossEnemy;
import com.example.myapplication.ImageManager;

public class EnemyFactory {
    private static EnemyFactory instance;
    private ImageManager imageManager;
    
    private EnemyFactory(Context context) {
        imageManager = ImageManager.getInstance(context);
    }
    
    public static EnemyFactory getInstance(Context context) {
        if (instance == null) {
            instance = new EnemyFactory(context);
        }
        return instance;
    }
    
    /**
     * 创建普通敌机
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 普通敌机
     */
    public MobEnemy createMobEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("MobEnemy");
        if (image == null) {
            return null;
        }
        return new MobEnemy(locationX, locationY, 0, 2, 30, image);
    }
    
    /**
     * 创建精英敌机
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 精英敌机
     */
    public EliteEnemy createEliteEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("EliteEnemy");
        if (image == null) {
            return null;
        }
        return new EliteEnemy(locationX, locationY, 1, 1, 100, 50, image);
    }
    
    /**
     * 创建超级精英敌机
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 超级精英敌机
     */
    public EliteEnemy createSuperEliteEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("SuperEliteEnemy");
        if (image == null) {
            return null;
        }
        return new EliteEnemy(locationX, locationY, 1, 1, 150, 80, image);
    }
    
    /**
     * 创建Boss敌机
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return Boss敌机
     */
    public BossEnemy createBossEnemy(int locationX, int locationY) {
        Bitmap image = imageManager.get("BossEnemy");
        if (image == null) {
            return null;
        }
        return new BossEnemy(locationX, locationY, 2, 0, 1000, 500, image);
    }
}