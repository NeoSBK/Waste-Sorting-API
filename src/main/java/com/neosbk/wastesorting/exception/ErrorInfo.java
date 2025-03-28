package com.neosbk.wastesorting.exception;

import java.util.Date;

public class ErrorInfo {

    private String message;
    private String info;
    private Date date;

    public ErrorInfo(String message, String info, Date date) {
        this.message = message;
        this.info = info;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
