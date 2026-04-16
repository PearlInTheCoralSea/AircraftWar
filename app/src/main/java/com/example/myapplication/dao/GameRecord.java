package com.example.myapplication.dao;

public class GameRecord {

    private final int id;
    private final String userName;
    private final String time;
    private final int score;
    private final String difficulty;

    public GameRecord(String userName, String time, int score, String difficulty) {
        this.id = -1;
        this.userName = userName;
        this.time = time;
        this.score = score;
        this.difficulty = difficulty;
    }

    public GameRecord(int id, String userName, String time, int score, String difficulty) {
        this.id = id;
        this.userName = userName;
        this.time = time;
        this.score = score;
        this.difficulty = difficulty;
    }

    public int getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getTime() {
        return this.time;
    }

    public int getScore() {
        return this.score;
    }

    public String getDifficulty() {
        return this.difficulty;
    }
}
