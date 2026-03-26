package com.example.myapplication.strategy;

import com.example.myapplication.basic.Bullet;
import com.example.myapplication.aircraft.HeroAircraft;

import java.util.List;

public interface ShootStrategy {
    /**
     * 射击策略
     * @param aircraft 飞机
     * @return 子弹列表
     */
    List<Bullet> shoot(HeroAircraft aircraft);
}