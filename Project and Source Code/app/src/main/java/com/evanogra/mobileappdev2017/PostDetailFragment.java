package com.evanogra.mobileappdev2017;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailFragment extends Fragment {

    private Post post;
    private StorageReference mStorageRef;

    private OnFragmentInteractionListener mListener;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(Post mPost) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_post_detail, container, false);

        TextView pdTitle = (TextView) v.findViewById(R.id.postDetailTitle);
        TextView pdDesc = (TextView) v.findViewById(R.id.postDetailDesc);
        final ImageView imageView = (ImageView) v.findViewById(R.id.postDetailImage);
        pdTitle.setText(post.getTitle());
        pdDesc.setText(post.getDesc() + "\r\nSuggested Price: $" + post.getPrice() + "\r\nCondition: " + post.getRating());


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


        ListView listView = (ListView) v.findViewById(R.id.postCommentsList);
        final CommentAdapter adapter = new CommentAdapter(getActivity(), R.layout.post_comment, post.getComments());
        listView.setAdapter(adapter);

        Button addCommentBtn = (Button) v.findViewById(R.id.postAddCommentButton);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a comment");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_comment_dialog, (ViewGroup) getView(), false);
                // Set up the input
                final EditText name = (EditText) viewInflated.findViewById(R.id.add_comment_name);
                final EditText body = (EditText) viewInflated.findViewById(R.id.add_comment_body);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String commentAuthor= name.getText().toString();
                        String commentBody = body.getText().toString();
                        Comment c = new Comment(commentAuthor, commentBody);
                        //Log.d("asf", m_Text + ":" + bodyText);

                        DatabaseReference mCommentsRef = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId()).child("comments");
                        String key = mCommentsRef.push().getKey();
                        Map<String, Object> commentValues = c.toMap();
                        Map<String,Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, commentValues);
                        mCommentsRef.updateChildren(childUpdates);

                        adapter.add(c);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(getContext());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
