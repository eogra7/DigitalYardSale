package com.evanogra.mobileappdev2017;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/28/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Comment comment;
    private List<Comment> comments;
    public CommentAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        comments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.post_comment, null);
        }

        comment = comments.get(position);

        TextView cTitle = (TextView) v.findViewById(R.id.commentTitle);
        TextView cBody = (TextView) v.findViewById(R.id.commentBody);

        cTitle.setText(comment.getTitle());
        cBody.setText(comment.getBody());
        Log.d("asdf", "comment added");
        return v;

    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
