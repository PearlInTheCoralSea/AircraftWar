package com.example.myapplication.strategy;

import com.example.myapplication.basic.Aircraft;
import com.example.myapplication.basic.Bullet;

import java.util.List;

public interface ShootStrategy {
    List<Bullet> shoot(Aircraft aircraft);
}
