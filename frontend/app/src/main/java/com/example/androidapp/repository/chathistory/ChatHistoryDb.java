package com.example.androidapp.repository.chathistory;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ChatHistory.class}, version = 2, exportSchema = false)
public abstract class ChatHistoryDb extends RoomDatabase {

    public abstract ChatHistoryDao chatHistoryDao();

    private static ChatHistoryDb INSTANCE;

    static ChatHistoryDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ChatHistoryDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ChatHistoryDb.class, "chat_history")
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
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ChatHistoryDao mDao;
        String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(ChatHistoryDb db) {
            mDao = db.chatHistoryDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            /*mDao.deleteAll();

            for (int i = 0; i <= words.length - 1; i++) {
                Word word = new Word(words[i]);
                mDao.insert(word);
            }
            return null;*/

//            if (mDao.getAllHistory().length < 1) {
//                for (int i = 0; i <= words.length - 1; i++) {
//                    ChatHistory word = new ChatHistory(words[i]);
//                    mDao.insert(word);
//                }
//            }
            return null;
        }
    }
}
