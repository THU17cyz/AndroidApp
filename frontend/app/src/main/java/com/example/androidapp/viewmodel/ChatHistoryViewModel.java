package com.example.androidapp.viewmodel;

import android.app.Application;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidapp.repository.ChatHistory;
import com.example.androidapp.repository.ChatHistoryRepository;

import java.util.List;

public class ChatHistoryViewModel extends AndroidViewModel  {

    private ChatHistoryRepository mRepository;

    private LiveData<List<ChatHistory>> mChatHistory;

    private String user;
    private String contact;
    private Application application;

    public ChatHistoryViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
    }

    public void initData(String user, String contact){
        this.user=user;
        this.contact=contact;
        mRepository = new ChatHistoryRepository(application,user,contact);
        mChatHistory = mRepository.getChatHistory();
    }

    public LiveData<List<ChatHistory>> getChatHistory() { return mChatHistory; }

    public void insert(ChatHistory chatHistory) { mRepository.insert(chatHistory); }

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteWord(ChatHistory chatHistory) {mRepository.delete(chatHistory);}

}

