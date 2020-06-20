package com.example.androidapp.util;

import com.example.androidapp.entity.ShortProfile;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicInfo {

  public static String PATH;
  public static String ACCOUNT = "account";

  public static int ID = 0;

  public static boolean IS_TEACHER = true;
  public static String TYPE = "S";

  public static String SIGNATURE = "";

  public static final int MAX_INTENTION_NUMBER = 10;

  public static String AVATAR = "";

  public static Lock lock = new ReentrantLock();;
  public static ArrayList<ShortProfile> WATCH_LIST = new ArrayList<>();
  public static ArrayList<ShortProfile> FAN_LIST = new ArrayList<>();

  public static void addToWatchList(ShortProfile shortProfile) {
    shortProfile.isFan = true;
    WATCH_LIST.add(shortProfile);
  }

  public static void removeFromWatchList(int id, boolean isTeacher) {
    lock.lock();
    int i = 0;
    for (ShortProfile shortProfile: WATCH_LIST) {
      if (shortProfile.id == id && shortProfile.isTeacher == isTeacher) break;
      i++;
    }
    if (i < WATCH_LIST.size()) WATCH_LIST.remove(i);
    lock.unlock();
  }

  public static boolean isInWatchList(int id, boolean isTeacher) {
    lock.lock();
    for (ShortProfile shortProfile: WATCH_LIST) {
      if (shortProfile.id == id && shortProfile.isTeacher == isTeacher) {
        lock.unlock();
        return true;
      }
    }
    lock.unlock();
    return false;

  }

  public static void printWatchList() {
    for (ShortProfile shortProfile: WATCH_LIST) {
      System.out.println(shortProfile.name + shortProfile.isFan);
    }
  }
}
