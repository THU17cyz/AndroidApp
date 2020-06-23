package com.example.androidapp.request.conversation;

import com.example.androidapp.request.base.BaseGetRequest;
import com.example.androidapp.util.Global;

public class GetMessagePictureRequest extends BaseGetRequest {

    private String url = "/api/conversation/get_message_picture";
    private String message_id;

    public GetMessagePictureRequest(String message_id) {
//        // 设置请求URL
//        this.to("/api/conversation/get_message_picture");
//        // 设置请求参数
//        this.put("message_id", message_id);
//        // 设置回调函数
//        this.call(callback);
        this.message_id = message_id;
    }

    public String getWholeUrl() {
        return Global.SERVER_URL + this.url + "?message_id=" + message_id;
    }
}
