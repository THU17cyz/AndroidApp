package com.example.androidapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ChatHistoryRepository {
    private ChatHistoryDao mChatHistoryDao;
    private LiveData<List<ChatHistory>> mAllHistory;

    public ChatHistoryRepository(Application application) {
        ChatHistoryDb db = ChatHistoryDb.getDatabase(application);
        mChatHistoryDao = db.chatHistoryDao();
        mAllHistory = mChatHistoryDao.fetchAll();
    }

    public LiveData<List<ChatHistory>> getAllHistory() {
        return mAllHistory;
    }

    public void insert (ChatHistory chatHistory) {
        new insertAsyncTask(mChatHistoryDao).execute(chatHistory);
    }

    public void deleteAll()  {
        new deleteAllWordsAsyncTask(mChatHistoryDao).execute();
    }

//    public void deleteWord(Word word)  {
//        new deleteWordAsyncTask(mWordDao).execute(word);
//    }

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

    private static class deleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChatHistoryDao mAsyncTaskDao;

        deleteAllWordsAsyncTask(ChatHistoryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


//    private static class deleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
//        private WordDao mAsyncTaskDao;
//
//        deleteWordAsyncTask(WordDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Word... params) {
//            mAsyncTaskDao.deleteWord(params[0]);
//            return null;
//        }
//    }
}
