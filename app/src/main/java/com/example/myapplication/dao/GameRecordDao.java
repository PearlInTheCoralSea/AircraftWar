package com.example.myapplication.dao;

import java.util.List;

public interface GameRecordDao {

    /**
     * 返回全部游戏记录
     */
    List<GameRecord> getAllRecords();

    /**
     * 按难度返回游戏记录（按分数降序）
     */
    List<GameRecord> getRecordsByDifficulty(String difficulty);

    /**
     * 添加游戏记录
     */
    void addRecord(GameRecord gameRecord);

    /**
     * 删除游戏记录
     */
    void deleteRecord(GameRecord gameRecord);
}
