package com.example.androidapp.repository.chathistoryhasread;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.androidapp.repository.chathistory.ChatHistoryDao;
import com.example.androidapp.repository.chathistory.ChatHistoryDb;

@Database(entities = {ChatHistoryHasRead.class}, version = 1, exportSchema = false)
public abstract class ChatHistoryHasReadDb extends RoomDatabase {

  public abstract ChatHistoryHasReadDao chatHistoryHasReadDao();

  private static ChatHistoryHasReadDb INSTANCE;

  static ChatHistoryHasReadDb getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (ChatHistoryHasReadDb.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                  ChatHistoryHasReadDb.class, "chat_history_has_read")
                  .fallbackToDestructiveMigration()
                  .addCallback(sRoomDatabaseCallback)
                  .build();
        }
      }
    }
    return INSTANCE;
  }

  private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
    @Override
    public void onOpen (@NonNull SupportSQLiteDatabase db){
      super.onOpen(db);
      new ChatHistoryHasReadDb.PopulateDbAsync(INSTANCE).execute();
    }
  };

  /**
   * Populate the database in the background.
   */
  private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

    private final ChatHistoryHasReadDao mDao;

    PopulateDbAsync(ChatHistoryHasReadDb db) {
      mDao = db.chatHistoryHasReadDao();
    }

    @Override
    protected Void doInBackground(final Void... params) {
      return null;
    }
  }


}
