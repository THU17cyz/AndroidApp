package com.example.androidapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.repository.chathistory.ChatHistoryRepository;
import com.example.androidapp.repository.chathistoryhasread.ChatHistoryHasRead;
import com.example.androidapp.repository.chathistoryhasread.ChatHistoryHasReadRepository;

import java.util.List;

public class ChatHistoryHasReadViewModel extends AndroidViewModel {
  private ChatHistoryHasReadRepository mRepository;

  private LiveData<List<ChatHistoryHasRead>> mChatHistory;


  private String user;
  private Application application;

  public ChatHistoryHasReadViewModel(@NonNull Application application) {
    super(application);
    this.application=application;
  }

  public void initData(String user){
    this.user=user;
    mRepository = new ChatHistoryHasReadRepository(application,user);
    mChatHistory = mRepository.getAllHistory();
  }

  public LiveData<List<ChatHistoryHasRead>> getAllHistory() { return mChatHistory; }

  public void insert(ChatHistoryHasRead chatHistoryHasRead) { mRepository.insert(chatHistoryHasRead); }

  public void delete(ChatHistoryHasRead chatHistoryHasRead) {mRepository.delete(chatHistoryHasRead);}

}
