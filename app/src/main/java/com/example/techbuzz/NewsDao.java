package com.example.techbuzz;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(NewsEntity news);

    @Query("SELECT * FROM news_table WHERE category = :category")
    List<NewsEntity> getNewsByCategory(String category);
}
