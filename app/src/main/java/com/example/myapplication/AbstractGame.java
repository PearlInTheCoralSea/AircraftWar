package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.MotionEvent;

import com.example.myapplication.aircraft.HeroAircraft;
import com.example.myapplication.aircraft.MobEnemy;
import com.example.myapplication.aircraft.EliteEnemy;
import com.example.myapplication.aircraft.SuperEliteEnemy;
import com.example.myapplication.aircraft.BossEnemy;
import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;
import com.example.myapplication.basic.Prop;
import com.example.myapplication.factory.EnemyFactory;
import com.example.myapplication.factory.PropFactory;
import com.example.myapplication.observer.Observer;
import com.example.myapplication.strategy.StraightShootStrategy;
import com.example.myapplication.strategy.ScatterShootStrategy;
import com.example.myapplication.strategy.CircleShootStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class AbstractGame extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean mIsDrawing;
    protected int screenWidth, screenHeight;

    // 游戏对象
    protected HeroAircraft heroAircraft;
    protected List<Aircraft> enemyAircrafts;
    protected List<Bullet> heroBullets;
    protected List<Bullet> enemyBullets;
    protected List<Prop> props;

    // 游戏状态
    protected int score;
    protected int time;
    protected boolean gameOver;
    protected boolean isMusic;

    // Boss 相关
    protected int generateBossScore = 0;
    protected boolean isBossAliveFlag = false;

    // 工厂
    protected EnemyFactory enemyFactory;
    protected PropFactory propFactory;
    protected Random random;
    protected Paint paint;
    private final Object gameLock = new Object();

    // 背景滚动
    protected Bitmap backgroundImage;
    private Bitmap scaledBackground;
    private int backGroundTop = 0;

    // 周期计时（帧数）
    protected int enemyCycleFrames = 38;       // ~600ms
    protected int heroBulletCycleFrames = 8;   // ~120ms
    protected int enemyBulletCycleFrames = 38; // ~600ms
    private int enemyCycleCount = 0;
    private int heroBulletCycleCount = 0;
    private int enemyBulletCycleCount = 0;
    private int difficultyCycleCount = 0;
    protected int difficultyIncreaseCycleFrames = 0;
    private int difficultyPhase = 0;

    // 难度参数（子类设置）
    protected int enemyMaxNumber;
    protected double rateBetweenMobAndElite;
    protected double rateBetweenEliteAndSuperElite;
    protected int generateBossThreshold;
    protected int bossHp;
    protected String difficulty;

    // 子弹道具计时
    private int bulletPropFrameCount = 0;
    private boolean bulletPropActive = false;

    // 音频
    protected AudioSystem audioSystem;

    // Handler for UI thread operations
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // 游戏结束回调
    private GameOverCallback gameOverCallback;

    public interface GameOverCallback {
        void onGameOver(int score);
    }

    public void setGameOverCallback(GameOverCallback callback) {
        this.gameOverCallback = callback;
    }

    public AbstractGame(Context context, boolean isMusic) {
        super(context);
        this.isMusic = isMusic;
        initBase();
        setupGameDifficulty();
    }

    private void initBase() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);

        ImageManager.getInstance(getContext());
        enemyFactory = EnemyFactory.getInstance(getContext());
        propFactory = PropFactory.getInstance(getContext());
        audioSystem = AudioSystem.getInstance(getContext());

        enemyAircrafts = new ArrayList<>();
        heroBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        props = new ArrayList<>();

        score = 0;
        time = 0;
        gameOver = false;
        generateBossScore = 0;
        isBossAliveFlag = false;
        bulletPropActive = false;
        bulletPropFrameCount = 0;
        difficultyPhase = 0;
        difficultyCycleCount = 0;

        random = new Random();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        paint.setAntiAlias(true);
    }

    // 模板方法
    public final void setupGameDifficulty() {
        setEnemyParameters();
        setBossParameters();
        setDifficultyIncreaseSettings();
        setBackgroundImage();
    }

    protected abstract void setEnemyParameters();
    protected abstract void setBossParameters();
    protected abstract void setDifficultyIncreaseSettings();
    protected abstract void setBackgroundImage();
    protected abstract boolean needBossOrNot();
    protected abstract boolean difficultyIncreaseOrNot();
    protected abstract void increaseDifficulty(int phase);
    protected abstract boolean bossHpIncreaseOrNot();

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();

        // 初始化英雄飞机
        ImageManager im = ImageManager.getInstance();
        Bitmap heroImage = im.get("HeroAircraft");
        if (heroImage != null) {
            int heroX = screenWidth / 2 - heroImage.getWidth() / 2;
            int heroY = screenHeight - heroImage.getHeight() - 50;
            heroAircraft = new HeroAircraft(heroX, heroY, 0, 0, 1000, heroImage, new StraightShootStrategy());
        }

        // 缩放背景图
        if (backgroundImage != null) {
            scaledBackground = Bitmap.createScaledBitmap(backgroundImage, screenWidth, screenHeight, true);
        }

        mIsDrawing = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        if (backgroundImage != null) {
            scaledBackground = Bitmap.createScaledBitmap(backgroundImage, screenWidth, screenHeight, true);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
        try {
            if (mThread != null) mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            synchronized (gameLock) {
                if (!gameOver) {
                    update();
                }
                draw();
            }
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (heroAircraft == null) return;
        time++;

        // 英雄子弹周期
        heroBulletCycleCount++;
        if (heroBulletCycleCount >= heroBulletCycleFrames) {
            heroBulletCycleCount = 0;
            List<Bullet> newBullets = heroAircraft.shoot();
            heroBullets.addAll(newBullets);
            if (!newBullets.isEmpty()) {
                audioSystem.playBulletSound();
            }
        }

        // 子弹道具计时
        if (bulletPropActive) {
            bulletPropFrameCount++;
            if (bulletPropFrameCount >= 312) { // ~5秒
                heroAircraft.setShootStrategy(new StraightShootStrategy());
                heroAircraft.setShootNum(1);
                bulletPropActive = false;
                bulletPropFrameCount = 0;
            }
        }

        // 敌机生成周期
        enemyCycleCount++;
        if (enemyCycleCount >= enemyCycleFrames) {
            enemyCycleCount = 0;
            generateEnemies();
        }

        // 敌机子弹周期
        enemyBulletCycleCount++;
        if (enemyBulletCycleCount >= enemyBulletCycleFrames) {
            enemyBulletCycleCount = 0;
            shootEnemyBullets();
        }

        // 难度递增
        if (difficultyIncreaseOrNot() && difficultyIncreaseCycleFrames > 0) {
            difficultyCycleCount++;
            if (difficultyCycleCount >= difficultyIncreaseCycleFrames) {
                difficultyCycleCount = 0;
                increaseDifficulty(difficultyPhase);
                difficultyPhase = (difficultyPhase + 1) % 4;
            }
        }

        // 移动所有对象
        moveAll();

        // 碰撞检测
        checkCollisions();

        // 移除无效对象
        postProcess();

        // 检查游戏结束
        if (heroAircraft.isDestroyed()) {
            gameOver = true;
            audioSystem.stopBGM();
            audioSystem.stopBossBGM();
            audioSystem.playGameOverSound();
            if (gameOverCallback != null) {
                mainHandler.post(() -> gameOverCallback.onGameOver(score));
            }
        }
    }

    private void generateEnemies() {
        if (screenWidth <= 0 || screenHeight <= 0) return;

        // 普通敌机生成
        int currentEnemyCount = enemyAircrafts.size();
        if (currentEnemyCount < enemyMaxNumber) {
            double rand = Math.random();
            if (rand < rateBetweenMobAndElite) {
                int x = random.nextInt(Math.max(1, screenWidth - 60));
                MobEnemy enemy = enemyFactory.createMobEnemy(x, -60);
                if (enemy != null) enemyAircrafts.add(enemy);
            } else if (rand < rateBetweenEliteAndSuperElite) {
                int x = random.nextInt(Math.max(1, screenWidth - 80));
                EliteEnemy enemy = enemyFactory.createEliteEnemy(x, -80);
                if (enemy != null) enemyAircrafts.add(enemy);
            } else {
                int x = random.nextInt(Math.max(1, screenWidth - 80));
                SuperEliteEnemy enemy = enemyFactory.createSuperEliteEnemy(x, -80);
                if (enemy != null) enemyAircrafts.add(enemy);
            }
        }

        // Boss 生成
        if (needBossOrNot() && generateBossScore >= generateBossThreshold && !isBossAliveFlag) {
            int x = screenWidth / 2 - 75;
            BossEnemy boss = enemyFactory.createBossEnemy(x, -150, bossHp);
            if (boss != null) {
                enemyAircrafts.add(boss);
                isBossAliveFlag = true;
                generateBossScore = 0;
                audioSystem.playBossBGM();
                if (bossHpIncreaseOrNot()) {
                    bossHp += 100;
                }
            }
        }
    }

    private void shootEnemyBullets() {
        for (Aircraft enemy : enemyAircrafts) {
            if (enemy instanceof BossEnemy || enemy instanceof EliteEnemy || enemy instanceof SuperEliteEnemy) {
                List<Bullet> bullets = enemy.shoot();
                enemyBullets.addAll(bullets);
            }
        }
    }

    private void moveAll() {
        // 移动敌机
        for (Aircraft enemy : enemyAircrafts) {
            enemy.forward();
            // Boss 边界反弹
            if (enemy instanceof BossEnemy) {
                if (enemy.getImage() != null) {
                    if (enemy.getLocationX() < 0 || enemy.getLocationX() > screenWidth - enemy.getImage().getWidth()) {
                        enemy.setSpeedX(-enemy.getSpeedX());
                    }
                }
                // Boss 停在顶部
                if (enemy.getLocationY() >= 20 && enemy.getSpeedY() > 0) {
                    enemy.setSpeedY(0);
                }
            }
        }

        // 移动子弹
        for (Bullet bullet : heroBullets) bullet.forward();
        for (Bullet bullet : enemyBullets) bullet.forward();

        // 移动道具
        for (Prop prop : props) prop.forward();
    }

    private void checkCollisions() {
        // 英雄子弹 vs 敌机
        for (Bullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (Aircraft enemy : enemyAircrafts) {
                if (enemy.notValid()) continue;
                if (bullet.crash(enemy)) {
                    bullet.vanish();
                    enemy.decreaseHp(bullet.getPower());
                    audioSystem.playHitSound();
                    if (enemy.isDestroyed()) {
                        int award = enemy.getScoreAward();
                        score += award;
                        generateBossScore += award;
                        // Boss 死亡
                        if (enemy instanceof BossEnemy) {
                            isBossAliveFlag = false;
                            audioSystem.switchToNormalBGM();
                            // Boss 掉落3个道具
                            for (int i = 0; i < 3; i++) {
                                generateProp(enemy.getLocationX() + i * 30, enemy.getLocationY());
                            }
                        } else {
                            // 普通/精英敌机掉落道具（90%概率）
                            if (Math.random() < 0.9) {
                                generateProp(enemy.getLocationX(), enemy.getLocationY());
                            }
                        }
                    }
                    break;
                }
            }
        }

        // 敌机子弹 vs 英雄
        for (Bullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (bullet.crash(heroAircraft)) {
                bullet.vanish();
                heroAircraft.decreaseHp(bullet.getPower());
            }
        }

        // 敌机 vs 英雄（撞机）
        for (Aircraft enemy : enemyAircrafts) {
            if (enemy.notValid()) continue;
            if (enemy.crash(heroAircraft)) {
                enemy.vanish();
                heroAircraft.decreaseHp(50);
            }
        }

        // 英雄 vs 道具
        for (Prop prop : props) {
            if (prop.notValid()) continue;
            if (prop.crash(heroAircraft)) {
                prop.vanish();
                audioSystem.playGetPropSound();
                activateProp(prop);
            }
        }
    }

    private void activateProp(Prop prop) {
        switch (prop.getType()) {
            case BLOOD:
                heroAircraft.decreaseHp(-300); // 负伤害=治疗
                break;
            case BOMB:
                audioSystem.playBombSound();
                // 观察者模式：通知所有敌机和敌方子弹
                int bombScore = 0;
                for (Aircraft enemy : enemyAircrafts) {
                    if (enemy.notValid()) continue;
                    if (enemy instanceof Observer) {
                        ((Observer) enemy).update(true);
                        if (enemy.isDestroyed()) {
                            bombScore += enemy.getScoreAward();
                            // 被炸毁的敌机掉落道具
                            if (!(enemy instanceof BossEnemy) && Math.random() < 0.5) {
                                generateProp(enemy.getLocationX(), enemy.getLocationY());
                            }
                        }
                    }
                }
                for (Bullet bullet : enemyBullets) {
                    if (bullet.notValid()) continue;
                    ((Observer) bullet).update(true);
                }
                score += bombScore;
                generateBossScore += bombScore;
                break;
            case BULLET:
                heroAircraft.setShootStrategy(new ScatterShootStrategy());
                heroAircraft.setShootNum(heroAircraft.getShootNum() + 2);
                bulletPropActive = true;
                bulletPropFrameCount = 0;
                break;
            case SUPER_BULLET:
                heroAircraft.setShootStrategy(new CircleShootStrategy());
                heroAircraft.setShootNum(heroAircraft.getShootNum() + 7);
                bulletPropActive = true;
                bulletPropFrameCount = 0;
                break;
        }
    }

    private void generateProp(int x, int y) {
        int rand = random.nextInt(4);
        Prop prop = null;
        switch (rand) {
            case 0: prop = propFactory.createBloodProp(x, y); break;
            case 1: prop = propFactory.createBombProp(x, y); break;
            case 2: prop = propFactory.createBulletProp(x, y); break;
            case 3: prop = propFactory.createSuperBulletProp(x, y); break;
        }
        if (prop != null) props.add(prop);
    }

    private void postProcess() {
        // 移除无效或出界的对象
        enemyAircrafts.removeIf(e -> e.notValid() || e.getLocationY() > screenHeight);
        heroBullets.removeIf(b -> b.notValid() || b.getLocationY() < -20 || b.getLocationY() > screenHeight + 20
                || b.getLocationX() < -20 || b.getLocationX() > screenWidth + 20);
        enemyBullets.removeIf(b -> b.notValid() || b.getLocationY() < -20 || b.getLocationY() > screenHeight + 20
                || b.getLocationX() < -20 || b.getLocationX() > screenWidth + 20);
        props.removeIf(p -> p.notValid() || p.getLocationY() > screenHeight);

        // 检查 Boss 是否还活着
        boolean bossFound = false;
        for (Aircraft e : enemyAircrafts) {
            if (e instanceof BossEnemy) { bossFound = true; break; }
        }
        if (!bossFound && isBossAliveFlag) {
            isBossAliveFlag = false;
            audioSystem.switchToNormalBGM();
        }
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                // 绘制滚动背景
                drawBackground();

                if (heroAircraft != null) {
                    // 绘制英雄子弹
                    for (Bullet bullet : heroBullets) {
                        if (bullet.getImage() != null && !bullet.notValid()) {
                            mCanvas.drawBitmap(bullet.getImage(), bullet.getLocationX(), bullet.getLocationY(), null);
                        }
                    }

                    // 绘制敌机子弹
                    for (Bullet bullet : enemyBullets) {
                        if (bullet.getImage() != null && !bullet.notValid()) {
                            mCanvas.drawBitmap(bullet.getImage(), bullet.getLocationX(), bullet.getLocationY(), null);
                        }
                    }

                    // 绘制敌机
                    for (Aircraft enemy : enemyAircrafts) {
                        if (enemy.getImage() != null && !enemy.notValid()) {
                            mCanvas.drawBitmap(enemy.getImage(), enemy.getLocationX(), enemy.getLocationY(), null);
                            drawHpBar(enemy.getLocationX(), enemy.getLocationY() - 10,
                                    enemy.getImage().getWidth(), enemy.getHp(), enemy.getMaxHp());
                        }
                    }

                    // 绘制道具
                    for (Prop prop : props) {
                        if (prop.getImage() != null && !prop.notValid()) {
                            mCanvas.drawBitmap(prop.getImage(), prop.getLocationX(), prop.getLocationY(), null);
                        }
                    }

                    // 绘制英雄飞机
                    if (heroAircraft.getImage() != null) {
                        mCanvas.drawBitmap(heroAircraft.getImage(), heroAircraft.getLocationX(), heroAircraft.getLocationY(), null);
                        drawHpBar(heroAircraft.getLocationX(), heroAircraft.getLocationY() - 10,
                                heroAircraft.getImage().getWidth(), heroAircraft.getHp(), heroAircraft.getMaxHp());
                    }
                }

                // 绘制游戏状态
                drawGameStatus();

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

    private void drawBackground() {
        if (scaledBackground != null) {
            mCanvas.drawBitmap(scaledBackground, 0, backGroundTop - screenHeight, null);
            mCanvas.drawBitmap(scaledBackground, 0, backGroundTop, null);
            backGroundTop += 1;
            if (backGroundTop >= screenHeight) {
                backGroundTop = 0;
            }
        } else {
            mCanvas.drawColor(0xFF000000);
        }
    }

    private void drawHpBar(int x, int y, int width, int hp, int maxHp) {
        if (maxHp <= 0) return;
        paint.setColor(Color.GRAY);
        mCanvas.drawRect(x, y, x + width, y + 6, paint);
        paint.setColor(Color.GREEN);
        int hpWidth = (int) ((float) hp / maxHp * width);
        mCanvas.drawRect(x, y, x + hpWidth, y + 6, paint);
    }

    private void drawGameStatus() {
        paint.setColor(Color.WHITE);
        paint.setTextSize(36);
        mCanvas.drawText("得分: " + score, 20, 50, paint);
        mCanvas.drawText("难度: " + difficulty, 20, 90, paint);
        if (heroAircraft != null) {
            mCanvas.drawText("生命: " + heroAircraft.getHp(), 20, 130, paint);
        }
    }

    private void drawGameOver() {
        // 半透明黑色遮罩
        paint.setColor(Color.argb(150, 0, 0, 0));
        mCanvas.drawRect(0, 0, screenWidth, screenHeight, paint);

        paint.setColor(Color.RED);
        paint.setTextSize(64);
        float textWidth = paint.measureText("游戏结束");
        mCanvas.drawText("游戏结束", (screenWidth - textWidth) / 2, screenHeight / 2 - 40, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        String scoreText = "最终得分: " + score;
        textWidth = paint.measureText(scoreText);
        mCanvas.drawText(scoreText, (screenWidth - textWidth) / 2, screenHeight / 2 + 30, paint);

        paint.setTextSize(30);
        String hintText = "点击屏幕返回菜单";
        textWidth = paint.measureText(hintText);
        mCanvas.drawText(hintText, (screenWidth - textWidth) / 2, screenHeight / 2 + 80, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (gameLock) {
            if (gameOver) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 返回菜单
                    if (gameOverCallback != null) {
                        mainHandler.post(() -> gameOverCallback.onGameOver(score));
                    }
                }
                return true;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (heroAircraft != null && heroAircraft.getImage() != null) {
                        int x = (int) event.getX() - heroAircraft.getImage().getWidth() / 2;
                        int y = (int) event.getY() - heroAircraft.getImage().getHeight() / 2;
                        if (x < 0) x = 0;
                        if (x > screenWidth - heroAircraft.getImage().getWidth())
                            x = screenWidth - heroAircraft.getImage().getWidth();
                        if (y < 0) y = 0;
                        if (y > screenHeight - heroAircraft.getImage().getHeight())
                            y = screenHeight - heroAircraft.getImage().getHeight();
                        heroAircraft.setLocationX(x);
                        heroAircraft.setLocationY(y);
                        heroAircraft.updateRectangle();
                    }
                    break;
            }
        }
        return true;
    }

    // 难度递增辅助方法
    protected void increaseEnemyHp() {
        EnemyFactory.mobInitialHp += 10;
        EnemyFactory.eliteInitialHp += 20;
        EnemyFactory.superEliteInitialHp += 30;
    }

    protected void decreaseEnemyCycle() {
        enemyCycleFrames = Math.max(10, enemyCycleFrames - 3);
    }

    protected void increaseEnemySpeed() {
        // 通过减少生成周期间接提高难度
        enemyBulletCycleFrames = Math.max(15, enemyBulletCycleFrames - 3);
    }

    protected void increaseBossHp() {
        bossHp += 100;
    }
}
