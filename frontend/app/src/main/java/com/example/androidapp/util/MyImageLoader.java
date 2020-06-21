package com.example.androidapp.util;

import android.widget.ImageView;

import com.example.androidapp.R;
import com.example.androidapp.request.user.GetInfoPictureRequest;
import com.squareup.picasso.Picasso;

public class MyImageLoader {

    public static void loadImage(ImageView view, String url) {
        Picasso.get().load(url).
                placeholder(R.drawable.ic_avatarholder).into(view);
    }

    public static void loadImage(ImageView view) {
//        System.out.println("haisjfahfa" + BasicInfo.PATH);
//        Picasso.with(view.getContext()).load(BasicInfo.PATH).placeholder(R.drawable.ic_person_black_24dp).into(view);
        String url;
        if (BasicInfo.TYPE.equals("S"))
            url = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
        else
            url = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();
        Picasso.get().load(url).placeholder(R.drawable.ic_avatarholder).into(view);
    }

    public static void invalidate(String url) {
        Picasso.get().invalidate(url);
    }

    public static void invalidate() {
        String url;
        if (BasicInfo.TYPE.equals("S"))
            url = new GetInfoPictureRequest("S", null, String.valueOf(BasicInfo.ID)).getWholeUrl();
        else
            url = new GetInfoPictureRequest("T", String.valueOf(BasicInfo.ID), null).getWholeUrl();
        Picasso.get().invalidate(url);
    }
}
