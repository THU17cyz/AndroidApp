package com.example.androidapp.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatHistoryDao {

    @Insert
    Long insert(ChatHistory chatHistory);

    @Query("SELECT * FROM chat_history WHERE user = :user and contact = :contact ORDER BY id asc")
    LiveData<List<ChatHistory>> fetchChatHistory(String user, String contact);

    @Query("SELECT * FROM chat_history WHERE user = :user ORDER BY id asc")
    LiveData<List<ChatHistory>> fetchAllHistory(String user);

    @Delete()
    void delete(ChatHistory chatHistory);

    @Query("DELETE FROM chat_history")
    void deleteAll();
}
