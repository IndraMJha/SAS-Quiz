package com.indrajha.sasquiz;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Question> questions);

    @Query("SELECT * FROM questions WHERE category = :category AND paper = :paper")
    List<Question> getQuestionsBySet(String category, String paper);

    @Query("DELETE FROM questions")
    void deleteAll();
}