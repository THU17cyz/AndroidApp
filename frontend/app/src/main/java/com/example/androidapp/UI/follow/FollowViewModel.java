package com.example.androidapp.UI.follow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FollowViewModel  extends ViewModel {
  private MutableLiveData<String> mText;

  public FollowViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is dashboard fragment");
  }

  public LiveData<String> getText() {
    return mText;
  }
}
