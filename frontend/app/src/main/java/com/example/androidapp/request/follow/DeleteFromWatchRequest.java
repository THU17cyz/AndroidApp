package com.example.androidapp.request.follow;

import com.example.androidapp.request.base.BasePostRequest;

import okhttp3.Callback;

public class DeleteFromWatchRequest extends BasePostRequest {
    public DeleteFromWatchRequest(Callback callback, String teacher_id, String student_id) {
        // 设置请求URL
        this.to("/api/follow/delete_from_watch");
        // 设置请求参数
        this.put("teacher_id", teacher_id);
        this.put("student_id", student_id);
        // 设置回调函数
        this.call(callback);
    }

    public DeleteFromWatchRequest(Callback callback, String id, boolean isTeacher) {
        // 设置请求URL
        this.to("/api/follow/delete_from_watch");

        // 设置请求参数
        if (isTeacher) {
            this.put("teacher_id", id);
        } else {
            this.put("student_id", id);
        }

        // 设置回调函数
        this.call(callback);
    }
}
