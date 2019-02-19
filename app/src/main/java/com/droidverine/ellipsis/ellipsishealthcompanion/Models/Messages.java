package com.droidverine.ellipsis.ellipsishealthcompanion.Models;

/**
 * Created by DELL on 25-11-2017.
 */

public class Messages {
    private String msghead,msgtext;
    long msgtime;
    public Messages()
    {

    }

    public Messages(String msghead, String msgtext, long msgtime) {
        this.msghead = msghead;
        this.msgtext = msgtext;
        this.msgtime = msgtime;
    }

    public long getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(long msgtime) {
        this.msgtime = msgtime;
    }

    public String getMsghead() {
        return msghead;
    }

    public void setMsghead(String msghead) {
        this.msghead = msghead;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }
}
