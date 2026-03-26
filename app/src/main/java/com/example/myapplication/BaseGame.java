package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;

import com.example.myapplication.aircraft.HeroAircraft;
import com.example.myapplication.aircraft.MobEnemy;
import com.example.myapplication.aircraft.EliteEnemy;
import com.example.myapplication.aircraft.BossEnemy;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.basic.Prop;
import com.example.myapplication.factory.EnemyFactory;
import com.example.myapplication.factory.PropFactory;
import com.example.myapplication.strategy.StraightShootStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseGame extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean mIsDrawing;
    private int screenWidth, screenHeight;
    
    // 游戏相关变量
    private HeroAircraft heroAircraft;
    private List<MobEnemy> mobEnemies;
    private List<EliteEnemy> eliteEnemies;
    private BossEnemy bossEnemy;
    private List<Bullet> heroBullets;
    private List<Bullet> enemyBullets;
    private List<Prop> props;
    
    // 游戏状态
    private int score;
    private int time;
    private boolean gameOver;
    
    // 工厂类
    private EnemyFactory enemyFactory;
    private PropFactory propFactory;
    
    // 随机数生成器
    private Random random;
    
    // 绘制工具
    private Paint paint;
    
    public BaseGame(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        
        // 初始化ImageManager
        ImageManager imageManager = ImageManager.getInstance(getContext());
        
        // 初始化工厂类
        enemyFactory = EnemyFactory.getInstance(getContext());
        propFactory = PropFactory.getInstance(getContext());
        
        // 初始化游戏变量
        heroAircraft = new HeroAircraft(200, 500, 0, 0, 100, imageManager.get("HeroAircraft"), new StraightShootStrategy());
        mobEnemies = new ArrayList<>();
        eliteEnemies = new ArrayList<>();
        bossEnemy = null;
        heroBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        props = new ArrayList<>();
        
        // 初始化游戏状态
        score = 0;
        time = 0;
        gameOver = false;
        
        // 初始化随机数生成器
        random = new Random();
        
        // 初始化绘制工具
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(24);
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        mIsDrawing = true;
        mThread = new Thread(this);
        mThread.start();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (mIsDrawing) {
            if (!gameOver) {
                update();
            }
            draw();
            try {
                Thread.sleep(16); // 约60fps
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void update() {
        time++;
        
        // 生成敌人
        generateEnemies();
        
        // 更新英雄飞机
        heroAircraft.forward();
        
        // 英雄飞机射击
        if (time % 20 == 0) {
            heroBullets.addAll(heroAircraft.shoot());
        }
        
        // 更新普通敌机
        for (MobEnemy enemy : mobEnemies) {
            enemy.forward();
            // 敌机射击
            if (random.nextInt(100) < 5) {
                enemyBullets.addAll(enemy.shoot());
            }
        }
        
        // 更新精英敌机
        for (EliteEnemy enemy : eliteEnemies) {
            enemy.forward();
            // 敌机射击
            if (random.nextInt(100) < 10) {
                enemyBullets.addAll(enemy.shoot());
            }
        }
        
        // 更新Boss敌机
        if (bossEnemy != null) {
            bossEnemy.forward();
            // Boss射击
            enemyBullets.addAll(bossEnemy.shoot());
            // 检查Boss是否超出边界
            if (bossEnemy.getLocationX() < 0 || bossEnemy.getLocationX() > screenWidth - bossEnemy.getImage().getWidth()) {
                bossEnemy.setSpeedX(-bossEnemy.getSpeedX());
            }
        }
        
        // 更新子弹
        updateBullets();
        
        // 更新道具
        updateProps();
        
        // 碰撞检测
        checkCollisions();
        
        // 移除超出边界的元素
        removeOutOfBoundsElements();
        
        // 检查游戏结束
        if (heroAircraft.isDestroyed()) {
            gameOver = true;
        }
    }
    
    private void generateEnemies() {
        // 生成普通敌机
        if (time % 10 == 0) {
            int x = random.nextInt(screenWidth - 50);
            MobEnemy enemy = enemyFactory.createMobEnemy(x, -50);
            if (enemy != null) {
                mobEnemies.add(enemy);
            }
        }
        
        // 生成精英敌机
        if (time % 50 == 0) {
            int x = random.nextInt(screenWidth - 100);
            EliteEnemy enemy = enemyFactory.createEliteEnemy(x, -100);
            if (enemy != null) {
                eliteEnemies.add(enemy);
            }
        }
        
        // 生成超级精英敌机
        if (time % 100 == 0) {
            int x = random.nextInt(screenWidth - 120);
            EliteEnemy enemy = enemyFactory.createSuperEliteEnemy(x, -120);
            if (enemy != null) {
                eliteEnemies.add(enemy);
            }
        }
        
        // 生成Boss敌机
        if (time % 300 == 0 && bossEnemy == null) {
            int x = screenWidth / 2 - 150;
            bossEnemy = enemyFactory.createBossEnemy(x, -300);
        }
    }
    
    private void updateBullets() {
        // 更新英雄子弹
        for (Bullet bullet : heroBullets) {
            bullet.forward();
        }
        
        // 更新敌人子弹
        for (Bullet bullet : enemyBullets) {
            bullet.forward();
        }
    }
    
    private void updateProps() {
        for (Prop prop : props) {
            prop.forward();
        }
    }
    
    private void checkCollisions() {
        // 英雄子弹与敌机碰撞
        for (int i = heroBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = heroBullets.get(i);
            
            // 与普通敌机碰撞
            for (int j = mobEnemies.size() - 1; j >= 0; j--) {
                MobEnemy enemy = mobEnemies.get(j);
                if (bullet.crash(enemy)) {
                    enemy.decreaseHp(bullet.getPower());
                    heroBullets.remove(i);
                    if (enemy.isDestroyed()) {
                        mobEnemies.remove(j);
                        score += 10;
                        // 生成道具
                        generateProp(enemy.getLocationX(), enemy.getLocationY());
                    }
                    break;
                }
            }
            
            if (i < heroBullets.size()) {
                // 与精英敌机碰撞
                for (int j = eliteEnemies.size() - 1; j >= 0; j--) {
                    EliteEnemy enemy = eliteEnemies.get(j);
                    if (bullet.crash(enemy)) {
                        enemy.decreaseHp(bullet.getPower());
                        heroBullets.remove(i);
                        if (enemy.isDestroyed()) {
                            eliteEnemies.remove(j);
                            score += enemy.getScore();
                            // 生成道具
                            generateProp(enemy.getLocationX(), enemy.getLocationY());
                        }
                        break;
                    }
                }
            }
            
            if (i < heroBullets.size() && bossEnemy != null) {
                // 与Boss敌机碰撞
                if (bullet.crash(bossEnemy)) {
                    bossEnemy.decreaseHp(bullet.getPower());
                    heroBullets.remove(i);
                    if (bossEnemy.isDestroyed()) {
                        score += bossEnemy.getScore();
                        bossEnemy = null;
                        // 生成道具
                        generateProp(screenWidth / 2, 100);
                    }
                }
            }
        }
        
        // 敌人子弹与英雄飞机碰撞
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            if (bullet.crash(heroAircraft)) {
                heroAircraft.decreaseHp(bullet.getPower());
                enemyBullets.remove(i);
            }
        }
        
        // 敌机与英雄飞机碰撞
        for (int i = mobEnemies.size() - 1; i >= 0; i--) {
            MobEnemy enemy = mobEnemies.get(i);
            if (enemy.crash(heroAircraft)) {
                heroAircraft.decreaseHp(50);
                mobEnemies.remove(i);
            }
        }
        
        for (int i = eliteEnemies.size() - 1; i >= 0; i--) {
            EliteEnemy enemy = eliteEnemies.get(i);
            if (enemy.crash(heroAircraft)) {
                heroAircraft.decreaseHp(100);
                eliteEnemies.remove(i);
            }
        }
        
        if (bossEnemy != null && bossEnemy.crash(heroAircraft)) {
            heroAircraft.decreaseHp(200);
        }
        
        // 英雄飞机与道具碰撞
        for (int i = props.size() - 1; i >= 0; i--) {
            Prop prop = props.get(i);
            if (prop.crash(heroAircraft)) {
                // 处理道具效果
                switch (prop.getType()) {
                    case BLOOD:
                        heroAircraft.setHp(heroAircraft.getMaxHp());
                        break;
                    case BOMB:
                        heroAircraft.addBomb();
                        break;
                    case BULLET:
                        // 切换到散射射击策略
                        break;
                    case SUPER_BULLET:
                        // 切换到圆形射击策略
                        break;
                }
                props.remove(i);
            }
        }
    }
    
    private void generateProp(int x, int y) {
        int rand = random.nextInt(100);
        if (rand < 20) {
            Prop prop = propFactory.createBloodProp(x, y);
            if (prop != null) {
                props.add(prop);
            }
        } else if (rand < 40) {
            Prop prop = propFactory.createBombProp(x, y);
            if (prop != null) {
                props.add(prop);
            }
        } else if (rand < 60) {
            Prop prop = propFactory.createBulletProp(x, y);
            if (prop != null) {
                props.add(prop);
            }
        } else if (rand < 80) {
            Prop prop = propFactory.createSuperBulletProp(x, y);
            if (prop != null) {
                props.add(prop);
            }
        }
    }
    
    private void removeOutOfBoundsElements() {
        // 移除超出边界的普通敌机
        for (int i = mobEnemies.size() - 1; i >= 0; i--) {
            MobEnemy enemy = mobEnemies.get(i);
            if (enemy.getLocationY() > screenHeight) {
                mobEnemies.remove(i);
            }
        }
        
        // 移除超出边界的精英敌机
        for (int i = eliteEnemies.size() - 1; i >= 0; i--) {
            EliteEnemy enemy = eliteEnemies.get(i);
            if (enemy.getLocationY() > screenHeight) {
                eliteEnemies.remove(i);
            }
        }
        
        // 移除超出边界的英雄子弹
        for (int i = heroBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = heroBullets.get(i);
            if (bullet.getLocationY() < 0) {
                heroBullets.remove(i);
            }
        }
        
        // 移除超出边界的敌人子弹
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = enemyBullets.get(i);
            if (bullet.getLocationY() > screenHeight) {
                enemyBullets.remove(i);
            }
        }
        
        // 移除超出边界的道具
        for (int i = props.size() - 1; i >= 0; i--) {
            Prop prop = props.get(i);
            if (prop.getLocationY() > screenHeight) {
                props.remove(i);
            }
        }
    }
    
    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                // 绘制背景
                mCanvas.drawColor(0xFF000000);
                
                // 绘制英雄飞机
                if (heroAircraft != null && heroAircraft.getImage() != null) {
                    mCanvas.drawBitmap(heroAircraft.getImage(), heroAircraft.getLocationX(), heroAircraft.getLocationY(), null);
                    // 绘制英雄飞机血条
                    drawHpBar(heroAircraft.getLocationX(), heroAircraft.getLocationY() - 10, heroAircraft.getImage().getWidth(), heroAircraft.getHp(), heroAircraft.getMaxHp());
                }
                
                // 绘制普通敌机
                for (MobEnemy enemy : mobEnemies) {
                    if (enemy.getImage() != null) {
                        mCanvas.drawBitmap(enemy.getImage(), enemy.getLocationX(), enemy.getLocationY(), null);
                        // 绘制敌机血条
                        drawHpBar(enemy.getLocationX(), enemy.getLocationY() - 10, enemy.getImage().getWidth(), enemy.getHp(), enemy.getMaxHp());
                    }
                }
                
                // 绘制精英敌机
                for (EliteEnemy enemy : eliteEnemies) {
                    if (enemy.getImage() != null) {
                        mCanvas.drawBitmap(enemy.getImage(), enemy.getLocationX(), enemy.getLocationY(), null);
                        // 绘制敌机血条
                        drawHpBar(enemy.getLocationX(), enemy.getLocationY() - 10, enemy.getImage().getWidth(), enemy.getHp(), enemy.getMaxHp());
                    }
                }
                
                // 绘制Boss敌机
                if (bossEnemy != null && bossEnemy.getImage() != null) {
                    mCanvas.drawBitmap(bossEnemy.getImage(), bossEnemy.getLocationX(), bossEnemy.getLocationY(), null);
                    // 绘制Boss血条
                    drawHpBar(bossEnemy.getLocationX(), bossEnemy.getLocationY() - 10, bossEnemy.getImage().getWidth(), bossEnemy.getHp(), bossEnemy.getMaxHp());
                }
                
                // 绘制英雄子弹
                for (Bullet bullet : heroBullets) {
                    if (bullet.getImage() != null) {
                        mCanvas.drawBitmap(bullet.getImage(), bullet.getLocationX(), bullet.getLocationY(), null);
                    }
                }
                
                // 绘制敌人子弹
                for (Bullet bullet : enemyBullets) {
                    if (bullet.getImage() != null) {
                        mCanvas.drawBitmap(bullet.getImage(), bullet.getLocationX(), bullet.getLocationY(), null);
                    }
                }
                
                // 绘制道具
                for (Prop prop : props) {
                    if (prop.getImage() != null) {
                        mCanvas.drawBitmap(prop.getImage(), prop.getLocationX(), prop.getLocationY(), null);
                    }
                }
                
                // 绘制游戏状态
                drawGameStatus();
                
                // 绘制游戏结束界面
                if (gameOver) {
                    drawGameOver();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
    
    private void drawHpBar(int x, int y, int width, int hp, int maxHp) {
        // 绘制血条背景
        paint.setColor(Color.GRAY);
        mCanvas.drawRect(x, y, x + width, y + 5, paint);
        // 绘制血条
        paint.setColor(Color.RED);
        int hpWidth = (int) ((float) hp / maxHp * width);
        mCanvas.drawRect(x, y, x + hpWidth, y + 5, paint);
    }
    
    private void drawGameStatus() {
        // 绘制得分
        paint.setColor(Color.WHITE);
        mCanvas.drawText("Score: " + score, 20, 30, paint);
        // 绘制时间
        mCanvas.drawText("Time: " + time / 60, 20, 60, paint);
        // 绘制炸弹数量
        if (heroAircraft != null) {
            mCanvas.drawText("Bomb: " + heroAircraft.getBombNum(), 20, 90, paint);
        }
    }
    
    private void drawGameOver() {
        // 绘制游戏结束文字
        paint.setColor(Color.RED);
        paint.setTextSize(48);
        mCanvas.drawText("Game Over", screenWidth / 2 - 100, screenHeight / 2, paint);
        // 绘制得分
        paint.setTextSize(24);
        mCanvas.drawText("Final Score: " + score, screenWidth / 2 - 70, screenHeight / 2 + 40, paint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) {
            // 游戏结束后点击屏幕重新开始
            init();
            gameOver = false;
            return true;
        }
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (heroAircraft != null) {
                    int x = (int) event.getX() - heroAircraft.getImage().getWidth() / 2;
                    int y = (int) event.getY() - heroAircraft.getImage().getHeight() / 2;
                    // 防止超出边界
                    if (x < 0) x = 0;
                    if (x > screenWidth - heroAircraft.getImage().getWidth()) x = screenWidth - heroAircraft.getImage().getWidth();
                    if (y < 0) y = 0;
                    if (y > screenHeight - heroAircraft.getImage().getHeight()) y = screenHeight - heroAircraft.getImage().getHeight();
                    heroAircraft.setLocationX(x);
                    heroAircraft.setLocationY(y);
                    heroAircraft.updateRectangle();
                }
                break;
        }
        return true;
    }
}