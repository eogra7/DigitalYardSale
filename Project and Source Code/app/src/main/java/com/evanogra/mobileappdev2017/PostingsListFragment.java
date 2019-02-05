package com.evanogra.mobileappdev2017;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostingsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostingsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostingsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImagePicker imagePicker;
    private OnFragmentInteractionListener mListener;
    private StorageReference mStorageRef;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PostingsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostingsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostingsListFragment newInstance(String param1, String param2) {
        PostingsListFragment fragment = new PostingsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_postings_list, container, false);


        final ListView listView = (ListView) view.findViewById(R.id.mainListView);


        String[] values = {"Item 1", "Item 2", "Item 3"};
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        Post[] posts = {new Post("", "Broken Laser Pointer", "this laser pointer is broken", 1.00)};
        final ArrayList<Post> postsList = new ArrayList<Post>();
        /*for(int i=0;i<posts.length;++i) {
            postsList.add(posts[i]);
        }*/
        final PostAdapter adapter = new PostAdapter(getActivity(), R.layout.item_main, postsList);
        listView.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("posts");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("asdf", "onChildAdded: " + dataSnapshot.getKey());
                Post p = dataSnapshot.getValue(Post.class);
                p.setPostId(dataSnapshot.getKey());
                adapter.add(p);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);

        //final ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.item_main, R.id.postingTitle, list);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostDetailFragment mFragment = null;
                Class mFragmentClass = PostDetailFragment.class;
                try {
                    mFragment = (PostDetailFragment) mFragmentClass.newInstance();
                    mFragment.setPost(postsList.get(position));

                } catch (java.lang.InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, mFragment).addToBackStack(null).commit();

            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add an item");
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_post_dialog, (ViewGroup) getView(), false);
                // Set up the input
                final EditText titleText = (EditText) viewInflated.findViewById(R.id.add_post_title);
                final EditText nameText = (EditText) viewInflated.findViewById(R.id.add_post_name);
                final EditText descText = (EditText) viewInflated.findViewById(R.id.add_post_description);
                final EditText priceText = (EditText) viewInflated.findViewById(R.id.add_post_price);
                final Spinner conditionSpinner = (Spinner) viewInflated.findViewById(R.id.add_post_spinner);
                final ImageView imageView = (ImageView) viewInflated.findViewById(R.id.add_post_image);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.conditions_array, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                conditionSpinner.setAdapter(adapter);
                imagePicker = new ImagePicker(PostingsListFragment.this);
                imagePicker.setImagePickerCallback(new ImagePickerCallback(){
                                                       @Override
                                                       public void onImagesChosen(List<ChosenImage> images) {
                                                           // Display images
                                                           String url = images.get(0).getThumbnailPath();
                                                           imageView.setImageURI(Uri.parse(url));
                                                           setUrl(url);
                                                       }

                                                       @Override
                                                       public void onError(String message) {
                                                           // Do error handling
                                                       }
                                                   }
                );
// imagePicker.allowMultiple(); // Default is false
// imagePicker.shouldGenerateMetadata(false); // Default is true
// imagePicker.shouldGenerateThumbnails(false); // Default is true

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imagePicker.pickImage();
                    }
                });

                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final DatabaseReference mPostsRef = FirebaseDatabase.getInstance().getReference("posts");
                        final String key = mPostsRef.push().getKey();
                        String localurl = getUrl();
                        Uri file = Uri.fromFile(new File(localurl));
                        StorageReference riversRef = mStorageRef.child("images/" + key + ".jpg");

                        riversRef.putFile(file)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        //Log.d("asd", downloadUrl.toString());
                                        Post p = new Post(String.valueOf(downloadUrl),
                                                titleText.getText().toString(),
                                                descText.getText().toString(),
                                                Double.parseDouble(priceText.getText().toString()),
                                                conditionSpinner.getSelectedItem().toString(),
                                                nameText.getText().toString());


                                        Map<String, Object> postValues = p.toMap();
                                        Map<String,Object> childUpdates = new HashMap<>();
                                        childUpdates.put(key, postValues);
                                        mPostsRef.updateChildren(childUpdates);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                    }
                                });
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

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(resultCode == RESULT.OK) {
            if(requestCode == Picker.PICK_IMAGE_DEVICE) {
                imagePicker.submit(data);
            }
        //}
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
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
}
