package com.example.androidapp.repository.chathistoryhasread;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.repository.chathistoryhasread.ChatHistoryHasRead;

import java.util.List;

@Dao
public interface ChatHistoryHasReadDao {
  @Insert
  Long insert(ChatHistoryHasRead chatHistoryHasRead);

  @Query("SELECT * FROM chat_history_has_read WHERE user = :user")
  LiveData<List<ChatHistoryHasRead>> getUserHistory(String user);

  @Query("SELECT * FROM chat_history_has_read WHERE user = :user AND contact = :contact")
  LiveData<List<ChatHistoryHasRead>> getUserContactHistory(String user, String contact);

  @Delete()
  void delete(ChatHistoryHasRead chatHistoryHasReady);

}
