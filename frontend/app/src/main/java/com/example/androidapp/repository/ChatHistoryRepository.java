package com.example.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ChatHistoryRepository {
    private ChatHistoryDao mChatHistoryDao;
    private LiveData<List<ChatHistory>> mAllHistory;
    private LiveData<List<ChatHistory>> mChatHistory;

    public ChatHistoryRepository(Application application, String user) {
        ChatHistoryDb db = ChatHistoryDb.getDatabase(application);
        mChatHistoryDao = db.chatHistoryDao();
        mAllHistory = mChatHistoryDao.fetchAllHistory(user);// todo
    }

    public ChatHistoryRepository(Application application, String user, String contact) {
        ChatHistoryDb db = ChatHistoryDb.getDatabase(application);
        mChatHistoryDao = db.chatHistoryDao();
        mChatHistory = mChatHistoryDao.fetchChatHistory(user,contact);// todo
    }

    public ChatHistoryRepository(Application application) {
    }

    public LiveData<List<ChatHistory>> getAllHistory() {
        return mAllHistory;
    }

    public LiveData<List<ChatHistory>> getChatHistory() {
        return mChatHistory;
    }

    public void insert (ChatHistory chatHistory) {
        new insertAsyncTask(mChatHistoryDao).execute(chatHistory);
    }

    public void deleteAll()  {
        new deleteAllAsyncTask(mChatHistoryDao).execute();
    }

    public void delete(ChatHistory chatHistory)  { new deleteAsyncTask(mChatHistoryDao).execute(chatHistory); }

    private static class insertAsyncTask extends AsyncTask<ChatHistory, Void, Void> {

        private ChatHistoryDao mAsyncTaskDao;

        insertAsyncTask(ChatHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ChatHistory... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChatHistoryDao mAsyncTaskDao;

        deleteAllAsyncTask(ChatHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ChatHistory, Void, Void> {
        private ChatHistoryDao mAsyncTaskDao;

        deleteAsyncTask(ChatHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ChatHistory... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
