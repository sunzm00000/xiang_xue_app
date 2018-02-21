package com.example.fishingport.app.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wushixin on 2017/5/4.
 * 聊天记录
 */
@Entity
public class Message {
    @Id(autoincrement = true)
    private Long id;
    private String msgId;
    private String content;
    private String img;
    private String talker;
    private String avatar;
    private String additional;
    private String isme;
    private int isSend; //1 已发送 0 正在发送 2 发送失败
    private Long createTime;//时间
    @Generated(hash = 150811588)
    public Message(Long id, String msgId, String content, String img, String talker,
            String avatar, String additional, String isme, int isSend,
            Long createTime) {
        this.id = id;
        this.msgId = msgId;
        this.content = content;
        this.img = img;
        this.talker = talker;
        this.avatar = avatar;
        this.additional = additional;
        this.isme = isme;
        this.isSend = isSend;
        this.createTime = createTime;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMsgId() {
        return this.msgId;
    }
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getTalker() {
        return this.talker;
    }
    public void setTalker(String talker) {
        this.talker = talker;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAdditional() {
        return this.additional;
    }
    public void setAdditional(String additional) {
        this.additional = additional;
    }
    public int getIsSend() {
        return this.isSend;
    }
    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
    public Long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getIsme() {
        return this.isme;
    }
    public void setIsme(String isme) {
        this.isme = isme;
    }


    
}
