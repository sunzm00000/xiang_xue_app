package com.example.fishingport.app.model;

/**
 * Created by wushixin on 2017/5/8.
 *
 * 聊天列表
 */

public class MessageList {
    private String msgId;
    private String content;
    private String img;
    private String talker;
    private int isSend; //1 已发送 0 正在发送 2 发送失败
    private Long createTime;//时间
    private String string;
    private String avatar;
    private String additional;
    private String isme;
    private String nickname;
    private String huanxin_name;

    public String getHuanxin_name() {
        return huanxin_name;
    }

    public void setHuanxin_name(String huanxin_name) {
        this.huanxin_name = huanxin_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIsme() {
        return isme;
    }

    public void setIsme(String isme) {
        this.isme = isme;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
