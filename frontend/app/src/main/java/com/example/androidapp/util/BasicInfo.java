package com.example.androidapp.util;

import com.example.androidapp.activity.MainActivity;
import com.example.androidapp.chatTest.model.Message;
import com.example.androidapp.entity.ApplicationInfo;
import com.example.androidapp.entity.RecruitmentInfo;
import com.example.androidapp.entity.ShortProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BasicInfo {

  public static boolean LOADED = false;
  public static String PATH;
  public static String ACCOUNT = "account";
  public static String PASSWORD = "";

  public static int ID = 0;

  public static boolean IS_TEACHER = true;
  public static String TYPE = "S";

  public static String SIGNATURE = "";

  public static String mTitle;
  public static String mMajor;
  public static String mDegree;
  public static String mTeacherNumber;
  public static String mStudentNumber;
  public static String mIdNumber;
  public static String mGender;

  public static String mName;
  public static String mSchool;
  public static String mDepartment;

  public static String mSignature;
  public static String mPhone;
  public static String mEmail;
  public static String mHomepage;
  public static String mAddress;
  public static String mIntroduction;
  public static String mUrl;
  public static String mDirection;
  public static String mInterest;
  public static String mResult;
  public static String mExperience;

  public static List<ApplicationInfo> mApplicationList= Collections.synchronizedList(new ArrayList<>());
  public static List<RecruitmentInfo> mRecruitmentList= Collections.synchronizedList(new ArrayList<>());

  public static List<Message> WELCOME_NOTIFICATIONS = Collections.synchronizedList(new ArrayList<>());
  public static List<Message> FOLLOW_NOTIFICATIONS = Collections.synchronizedList(new ArrayList<>());
  public static List<Message> PWD_CHANGE_NOTIFICATIONS = Collections.synchronizedList(new ArrayList<>());
  public static List<Message> INTENTION_NOTIFICATIONS = Collections.synchronizedList(new ArrayList<>());

  public static HashMap<String, ArrayList<Message>> CHAT_HISTORY = new HashMap<>();

  public static final int MAX_INTENTION_NUMBER = 10;

  public static String AVATAR = "";

  public static Lock lock = new ReentrantLock();
  public static List<ShortProfile> WATCH_LIST = Collections.synchronizedList(new ArrayList<>());
  public static List<ShortProfile> FAN_LIST = Collections.synchronizedList(new ArrayList<>());

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

  public static void reset() {
    LOADED = false;
    mApplicationList.clear();
    mRecruitmentList.clear();
    INTENTION_NOTIFICATIONS.clear();
    FOLLOW_NOTIFICATIONS.clear();
    WELCOME_NOTIFICATIONS.clear();
    PWD_CHANGE_NOTIFICATIONS.clear();
    WATCH_LIST.clear();
    FAN_LIST.clear();
    CHAT_HISTORY.clear();
  }
}
