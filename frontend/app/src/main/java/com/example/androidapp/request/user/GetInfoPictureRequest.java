package com.example.androidapp.request.user;

import com.example.androidapp.util.Global;

public class GetInfoPictureRequest {
    private final String url = "/api/user/get_info_picture";
    private String type;
    private String teacher_id;
    private String student_id;

    public GetInfoPictureRequest(String type, String teacher_id, String student_id) {
        this.type = type;
        this.student_id = student_id;
        this.teacher_id = teacher_id;
    }

    public String getWholeUrl() {
        if (type.equals("I")) {
            return Global.SERVER_URL + this.url + "?type=" + type;
        } else if (type.equals("T") || student_id == null) {
            return Global.SERVER_URL + this.url + "?type=T&teacher_id=" + teacher_id;
        } else {
            return Global.SERVER_URL + this.url + "?type=S&student_id=" + student_id;
        }

    }
}
