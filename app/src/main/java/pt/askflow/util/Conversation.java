package pt.askflow.util;

import java.util.Date;

/**
 * Created by PhucThanh on 1/20/2016.
 */
public class Conversation {

    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;

    private String msg;
    private int status = STATUS_SENT;
    private Date date;
    private Boolean isSender;

    public Conversation(String msg, Date date, Boolean isSender) {
        this.msg = msg;
        this.date = date;
        this.isSender = isSender;
    }

    public Conversation(String msg, Boolean isSender) {
        this.msg = msg;
        this.isSender = isSender;
    }

    public Conversation() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsSender() {
        return isSender;
    }

    public void setIsSender(Boolean isSender) {
        this.isSender = isSender;
    }
}
