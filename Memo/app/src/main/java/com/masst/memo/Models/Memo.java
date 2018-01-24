package com.masst.memo.Models;

/**
 * Created by Dell on 1/19/2018.
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Memo {
    private Date date;
    private String text;
    private int id;
    private String title;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");

    public Memo() {
        this.date = new Date();
    }

    public Memo(int id,String title, long time, String text) {
        this.id = id;
        this.title = title;
        this.date = new Date(time);
        this.text = text;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public void setId(int id) {
        this.id= id;
    }

    public int getId() {
        return this.id;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setTitle(String text) {
        this.title = text;
    }
    public String getTitle() {
        return this.title;
    }

    public String getShortText() {
        String temp = text.replaceAll("\n", " ");
        if (temp.length() > 25) {
            return temp.substring(0, 25) + "...";
        } else {
            return temp;
        }
    }

    public void setFullDisplayed(boolean fullDisplayed) {
        this.fullDisplayed = fullDisplayed;
    }

    public boolean isFullDisplayed() {
        return this.fullDisplayed;
    }
    @Override
    public String toString() {
        return this.text;
    }
}