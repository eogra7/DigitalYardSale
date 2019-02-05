package com.evanogra.mobileappdev2017;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2/28/2017.
 */

public class Post {
    private String imageUrl;
    private String title;
    private String desc;
    private double price;
    private List<Comment> comments;
    private String postId;
    private String rating;
    private String author;

    public List<Comment> getComments() {
        if(comments==null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(Map<String, Map<String, Object>> commentsListMap)
    {
        /*comments = new ArrayList<Comment>();
        HashMap map = (HashMap) map0.entrySet().iterator().next().getValue();
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Comment c = new Comment();
            c.setBody(pair.);
            comments.add(c);
        }*/

        comments = new ArrayList<Comment>();
        Iterator commentsListIterator = commentsListMap.entrySet().iterator();
        while(commentsListIterator.hasNext()) {
            Comment c = new Comment();
            HashMap.Entry commentPair = (HashMap.Entry) commentsListIterator.next();
            HashMap<String, Object> commentMap = (HashMap<String, Object>) commentPair.getValue();
            Iterator commentDataIterator = commentMap.entrySet().iterator();
            while(commentDataIterator.hasNext()) {
                HashMap.Entry dataPair = (HashMap.Entry) commentDataIterator.next();

                Object value = dataPair.getValue();
                switch((String) dataPair.getKey()) {
                    case "author":
                        c.setAuthor((String) value);
                        break;
                    case "body":
                        c.setBody((String) value);
                        break;
                    case "time":
                        c.setTime((Long) value);
                }
            }
            comments.add(c);
        }
    }

    public Post() {
    }

    public Post(String imageUrl, String title, String desc, double price) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.price = price;
        comments = new ArrayList<Comment>();
        comments.add(new Comment("eogra7", "This is a comment.", 1488273770451L));
    }

    public Post(String imageUrl, String title, String desc, double price, String rating, String author) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.rating = rating;
        this.author = author;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> r = new HashMap<>();
        r.put("imageUrl", imageUrl);
        r.put("title", title);
        r.put("desc", desc);
        r.put("price", price);
        r.put("rating", rating);
        r.put("author", author);

        return r;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


}
