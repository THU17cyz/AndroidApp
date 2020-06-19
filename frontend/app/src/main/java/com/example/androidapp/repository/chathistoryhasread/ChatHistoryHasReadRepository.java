package com.example.androidapp.repository.chathistoryhasread;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.androidapp.repository.chathistory.ChatHistory;
import com.example.androidapp.repository.chathistory.ChatHistoryDao;
import com.example.androidapp.repository.chathistory.ChatHistoryDb;
import com.example.androidapp.repository.chathistory.ChatHistoryRepository;

import java.util.List;

public class ChatHistoryHasReadRepository {
  private ChatHistoryHasReadDao mChatHistoryHasReadDao;

  public ChatHistoryHasReadRepository(Application application) {
    ChatHistoryHasReadDb db = ChatHistoryHasReadDb.getDatabase(application);
    mChatHistoryHasReadDao = db.chatHistoryHasReadDao();
  }

  public LiveData<List<ChatHistoryHasRead>> getUserHistory(String user) {
    return mChatHistoryHasReadDao.getUserHistory(user);
  }

  public LiveData<List<ChatHistoryHasRead>> getUserContactHistory(String user, String contact)
  {
    return mChatHistoryHasReadDao.getUserContactHistory(user,contact);
  }

  public void insert (ChatHistoryHasRead chatHistoryHasRead) {
    new ChatHistoryHasReadRepository.insertAsyncTask(mChatHistoryHasReadDao).execute(chatHistoryHasRead);
  }

  public void delete(ChatHistoryHasRead chatHistoryHasRead)  { new ChatHistoryHasReadRepository.deleteAsyncTask(mChatHistoryHasReadDao).execute(chatHistoryHasRead); }

  private static class insertAsyncTask extends AsyncTask<ChatHistoryHasRead, Void, Void> {

    private ChatHistoryHasReadDao mAsyncTaskDao;

    insertAsyncTask(ChatHistoryHasReadDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final ChatHistoryHasRead... params) {
      mAsyncTaskDao.insert(params[0]);
      return null;
    }
  }

  private static class deleteAsyncTask extends AsyncTask<ChatHistoryHasRead, Void, Void> {
    private ChatHistoryHasReadDao mAsyncTaskDao;

    deleteAsyncTask(ChatHistoryHasReadDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final ChatHistoryHasRead... params) {
      mAsyncTaskDao.delete(params[0]);
      return null;
    }
  }
}
