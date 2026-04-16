package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameRecordDaoImpl implements GameRecordDao {

    private final GameRecordDbHelper dbHelper;

    public GameRecordDaoImpl(Context context) {
        this.dbHelper = new GameRecordDbHelper(context);
    }

    @Override
    public List<GameRecord> getAllRecords() {
        List<GameRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                GameRecordDbHelper.TABLE_NAME,
                null, null, null, null, null,
                GameRecordDbHelper.COLUMN_SCORE + " DESC"
        );
        while (cursor.moveToNext()) {
            records.add(cursorToRecord(cursor));
        }
        cursor.close();
        return records;
    }

    @Override
    public List<GameRecord> getRecordsByDifficulty(String difficulty) {
        List<GameRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                GameRecordDbHelper.TABLE_NAME,
                null,
                GameRecordDbHelper.COLUMN_DIFFICULTY + " = ?",
                new String[]{difficulty},
                null, null,
                GameRecordDbHelper.COLUMN_SCORE + " DESC"
        );
        while (cursor.moveToNext()) {
            records.add(cursorToRecord(cursor));
        }
        cursor.close();
        return records;
    }

    @Override
    public void addRecord(GameRecord gameRecord) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GameRecordDbHelper.COLUMN_USER_NAME, gameRecord.getUserName());
        values.put(GameRecordDbHelper.COLUMN_TIME, gameRecord.getTime());
        values.put(GameRecordDbHelper.COLUMN_SCORE, gameRecord.getScore());
        values.put(GameRecordDbHelper.COLUMN_DIFFICULTY, gameRecord.getDifficulty());
        db.insert(GameRecordDbHelper.TABLE_NAME, null, values);
    }

    @Override
    public void deleteRecord(GameRecord gameRecord) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                GameRecordDbHelper.TABLE_NAME,
                GameRecordDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(gameRecord.getId())}
        );
    }

    private GameRecord cursorToRecord(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(GameRecordDbHelper.COLUMN_ID));
        String userName = cursor.getString(cursor.getColumnIndexOrThrow(GameRecordDbHelper.COLUMN_USER_NAME));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(GameRecordDbHelper.COLUMN_TIME));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow(GameRecordDbHelper.COLUMN_SCORE));
        String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(GameRecordDbHelper.COLUMN_DIFFICULTY));
        return new GameRecord(id, userName, time, score, difficulty);
    }
}
