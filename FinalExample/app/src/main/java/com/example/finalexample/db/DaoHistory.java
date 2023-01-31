package com.example.finalexample.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoHistory {
    @Insert
    long[] insertHistories(History... histories);
    @Insert
    long insertHistory(History history);

    @Delete
    void delHistories(History... histories);

    @Update
    void updateHistories(History... histories);

    @Query("SELECT * FROM history_city WHERE cityID=:id")
    History getHistoryById(String id);
    @Query("SELECT * FROM history_city ORDER BY id DESC")
    List<History> getAllHistory();

}
