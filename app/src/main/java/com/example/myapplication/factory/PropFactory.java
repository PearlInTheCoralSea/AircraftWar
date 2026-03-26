package com.example.myapplication.factory;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.myapplication.basic.Prop;
import com.example.myapplication.ImageManager;

public class PropFactory {
    private static PropFactory instance;
    private ImageManager imageManager;
    
    private PropFactory(Context context) {
        imageManager = ImageManager.getInstance(context);
    }
    
    public static PropFactory getInstance(Context context) {
        if (instance == null) {
            instance = new PropFactory(context);
        }
        return instance;
    }
    
    /**
     * 创建血包道具
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 血包道具
     */
    public Prop createBloodProp(int locationX, int locationY) {
        Bitmap image = imageManager.get("BloodProp");
        if (image == null) {
            return null;
        }
        return new Prop(locationX, locationY, 0, 2, Prop.PropType.BLOOD, image);
    }
    
    /**
     * 创建炸弹道具
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 炸弹道具
     */
    public Prop createBombProp(int locationX, int locationY) {
        Bitmap image = imageManager.get("BombProp");
        if (image == null) {
            return null;
        }
        return new Prop(locationX, locationY, 0, 2, Prop.PropType.BOMB, image);
    }
    
    /**
     * 创建子弹道具
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 子弹道具
     */
    public Prop createBulletProp(int locationX, int locationY) {
        Bitmap image = imageManager.get("BulletProp");
        if (image == null) {
            return null;
        }
        return new Prop(locationX, locationY, 0, 2, Prop.PropType.BULLET, image);
    }
    
    /**
     * 创建超级子弹道具
     * @param locationX X坐标
     * @param locationY Y坐标
     * @return 超级子弹道具
     */
    public Prop createSuperBulletProp(int locationX, int locationY) {
        Bitmap image = imageManager.get("SuperBulletProp");
        if (image == null) {
            return null;
        }
        return new Prop(locationX, locationY, 0, 2, Prop.PropType.SUPER_BULLET, image);
    }
}