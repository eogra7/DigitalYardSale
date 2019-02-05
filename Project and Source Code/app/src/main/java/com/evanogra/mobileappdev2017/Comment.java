package com.evanogra.mobileappdev2017;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Evan on 2/28/2017.
 */

public class Comment {
    private String author, body;
    private long time;



    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.time = System.currentTimeMillis();
    }


    public Comment(String author, String body, long time) {
        this.author = author;
        this.body = body;
        this.time = time;
    }

    public Comment() {
    }

    public String getTitle() {
        String titleString = null;
        try {
            titleString = author + " • " + MyUtil.getRelativeTime(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return titleString;
    }

    public String getBody() {
        return body;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", author);
        result.put("body", body);
        result.put("time", time);

        return result;
    }
}
