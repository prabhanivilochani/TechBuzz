package com.example.techbuzz;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsEntity.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    private static volatile NewsDatabase instance;

    public abstract NewsDao newsDao();

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NewsDatabase.class, "news_database")
                    .fallbackToDestructiveMigration() // handles schema changes safely
                    .allowMainThreadQueries() // Avoid in real apps, use async (LiveData or Coroutines)
                    .build();
        }
        return instance;
    }
}
