package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private static ImageManager instance;
    private Context context;
    private Map<String, Bitmap> imageMap;
    
    // 图片资源ID映射
    private static final Map<String, Integer> RESOURCE_MAP = new HashMap<>();
    static {
        RESOURCE_MAP.put("HeroAircraft", R.drawable.hero);
        RESOURCE_MAP.put("MobEnemy", R.drawable.mob);
        RESOURCE_MAP.put("EliteEnemy", R.drawable.elite);
        RESOURCE_MAP.put("SuperEliteEnemy", R.drawable.elite_plus);
        RESOURCE_MAP.put("BossEnemy", R.drawable.boss);
        RESOURCE_MAP.put("HeroBullet", R.drawable.bullet_hero);
        RESOURCE_MAP.put("EnemyBullet", R.drawable.bullet_enemy);
        RESOURCE_MAP.put("BloodProp", R.drawable.prop_blood);
        RESOURCE_MAP.put("BombProp", R.drawable.prop_bomb);
        RESOURCE_MAP.put("BulletProp", R.drawable.prop_bullet);
        RESOURCE_MAP.put("SuperBulletProp", R.drawable.prop_bullet_plus);
    }
    
    private ImageManager(Context context) {
        this.context = context;
        this.imageMap = new HashMap<>();
        preloadImages();
    }
    
    public static ImageManager getInstance(Context context) {
        if (instance == null) {
            instance = new ImageManager(context);
        }
        return instance;
    }

    public static ImageManager getInstance() {
        return instance;
    }
    
    // 预加载所有游戏图片
    private void preloadImages() {
        for (Map.Entry<String, Integer> entry : RESOURCE_MAP.entrySet()) {
            String key = entry.getKey();
            int resId = entry.getValue();
            imageMap.put(key, BitmapFactory.decodeResource(context.getResources(), resId));
        }
    }
    
    // 根据类名获取图片
    public Bitmap get(String className) {
        // 提取简单类名
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        return imageMap.get(simpleClassName);
    }
    
    // 根据对象获取图片
    public Bitmap get(Object obj) {
        if (obj == null) {
            return null;
        }
        return get(obj.getClass().getName());
    }
    
    // 获取背景图片
    public Bitmap getBackgroundImage(int index) {
        switch (index) {
            case 1:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
            case 2:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
            case 3:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
            case 4:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg4);
            case 5:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg5);
            default:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        }
    }
    
    // 释放资源
    public void release() {
        for (Bitmap bitmap : imageMap.values()) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        imageMap.clear();
    }
}