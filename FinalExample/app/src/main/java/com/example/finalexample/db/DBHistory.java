package com.example.finalexample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {History.class},version = 1,exportSchema = false)
public abstract class DBHistory extends RoomDatabase {
    public static final String DB_NAME="history.db";
    private static volatile DBHistory mdbInstance;

    public static synchronized DBHistory getInstance(Context c){
        if(mdbInstance==null){
            mdbInstance=createDB(c);
        }
        return mdbInstance;
    }

    private static DBHistory createDB(final Context c){
        return Room.databaseBuilder(c,DBHistory.class, DB_NAME).allowMainThreadQueries().build();
    }
    public abstract DaoHistory getDaoHistory();
}
