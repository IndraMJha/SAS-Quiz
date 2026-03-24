package com.indrajha.sasquiz;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private QuestionDao questionDao;
    private AppDatabase database;

    public DataRepository(Context context) {
        database = AppDatabase.getInstance(context);
        questionDao = database.questionDao();
    }

    public List<Question> getQuestions(String category, String paper) {
        return questionDao.getQuestionsBySet(category, paper);
    }
}