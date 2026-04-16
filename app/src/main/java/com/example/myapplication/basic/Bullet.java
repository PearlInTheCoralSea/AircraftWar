package com.example.myapplication.basic;

import android.graphics.Bitmap;
import com.example.myapplication.observer.Observer;

public class Bullet extends Entity implements Observer {
    private int power;
    private Bitmap image;
    private boolean isHero;

    public Bullet(int locationX, int locationY, int speedX, int speedY, int power, Bitmap image) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
        this.image = image;
        this.isHero = false;
        updateRectangle();
    }

    public Bullet(int locationX, int locationY, int speedX, int speedY, int power, Bitmap image, boolean isHero) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
        this.image = image;
        this.isHero = isHero;
        updateRectangle();
    }

    @Override
    public void update(boolean flag) {
        if (flag) {
            vanish();
        }
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

    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    public Bitmap getImage() { return image; }
    public void setImage(Bitmap image) { this.image = image; updateRectangle(); }
    public boolean isHero() { return isHero; }
    public void setHero(boolean hero) { isHero = hero; }
}
