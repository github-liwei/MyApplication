package com.liwei.clock.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * 聊条消息记录
 * Created by LIWEI on 2017/10/20.
 */
public class ChatMessageRecord {
    private long id = 0;

    /**
     * 发送者电话
     */
    private String speakerPhone;

    /**
     * 接收者电话
     */
    private String audiencePhone;

    /**
     * 消息类内容
     */
    private String contentMsg;

    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSpeakerPhone() {
        return speakerPhone;
    }

    public void setSpeakerPhone(String speakerPhone) {
        this.speakerPhone = speakerPhone;
    }

    public String getAudiencePhone() {
        return audiencePhone;
    }

    public void setAudiencePhone(String audiencePhone) {
        this.audiencePhone = audiencePhone;
    }

    public String getContentMsg() {
        return contentMsg;
    }

    public void setContentMsg(String contentMsg) {
        this.contentMsg = contentMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
