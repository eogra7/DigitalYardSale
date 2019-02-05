package com.evanogra.mobileappdev2017;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2/28/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    private StorageReference mStorageRef;
    public PostAdapter(Context context, int resource) {
        super(context, resource);
    }

    public PostAdapter(Context context, int resource, ArrayList<Post> posts) {
        super(context, resource, posts);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.item_main, null);
        }

        Post post = getItem(position);

        TextView postTitle = (TextView) v.findViewById(R.id.postingTitle);
        TextView postDesc = (TextView) v.findViewById(R.id.postingDesc);
        final ImageView imageView = (ImageView) v.findViewById(R.id.postingImage);

        postTitle.setText(post.getTitle());
        postDesc.setText(post.getDesc());
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + post.getPostId() + ".jpg");


        //File localFile = null;
        try {
            final File localFile = File.createTempFile("images", "jpg");
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Successfully downloaded data to local file
                            // ...
                            imageView.setImageURI(Uri.parse(localFile.getPath()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                    // ...
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }


}
