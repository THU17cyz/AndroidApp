package com.example.androidapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidapp.repository.ChatHistory;
import com.example.androidapp.repository.ChatHistoryRepository;

import java.util.List;

public class ChatHistoryViewModel extends AndroidViewModel {
    private ChatHistoryRepository mRepository;

    private LiveData<List<ChatHistory>> mAllHistory;

    public ChatHistoryViewModel (Application application) {
        super(application);
        mRepository = new ChatHistoryRepository(application);
        mAllHistory = mRepository.getAllHistory();
    }

    LiveData<List<ChatHistory>> getAllHistory() { return mAllHistory; }

    public void insert(ChatHistory chatHistory) { mRepository.insert(chatHistory); }

    public void deleteAll() {mRepository.deleteAll();}

    // public void deleteWord(Word word) {mRepository.deleteWord(word);}
}
