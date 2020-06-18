package com.example.androidapp.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.LinkedList;

public class SearchHistory {
    private static final int maxNum = 10;

    private static LinkedList<String> history;

    public SearchHistory() {
        history = new LinkedList<>();
    }

    public static void addHistory(String query) {
        if (history.size() >= maxNum) {
            history.removeFirst();
        }
        history.push(query);
    }

    public static LinkedList<String> getHistory() {
        return history;
    }

    public static void deleteHistory(int position) {
        history.remove(position);
    }

    public static void persistHistory(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int i = 0;
        for (String s: history) {

            i++;
        }
    }
}
