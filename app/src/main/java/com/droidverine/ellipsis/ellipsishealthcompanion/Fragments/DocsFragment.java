package com.droidverine.ellipsis.ellipsishealthcompanion.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droidverine.ellipsis.ellipsishealthcompanion.Activities.MainActivity;
import com.droidverine.ellipsis.ellipsishealthcompanion.Adapters.DocsAdapter;
import com.droidverine.ellipsis.ellipsishealthcompanion.Models.Uploaddocs;
import com.droidverine.ellipsis.ellipsishealthcompanion.R;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.Connmanager;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.DetailsManager;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.EllipsisHealthCompanion;
import com.droidverine.ellipsis.ellipsishealthcompanion.Utils.Offlinedatabase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DocsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DocsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String urlofimg;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;
    FloatingActionButton floatingActionButton;
    private Uri mImageUri;
    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    ProgressBar progressBar;
    String selectedoption;

    RecyclerView.LayoutManager layoutManager;
    public Context context;
    List<Uploaddocs> list;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerview;
    DocsAdapter recycler;
    Offlinedatabase offlinedatabase;

    private StorageTask mUploadTask;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DocsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocsFragment newInstance(String param1, String param2) {
        DocsFragment fragment = new DocsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_docs, container, false);
        floatingActionButton=view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selector();
            }
        });

        //

        progressBar = view.findViewById(R.id.docs_fragment_progressBar);
        mStorageRef = FirebaseStorage.getInstance().getReference("Documents");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Documents");
    /*    mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Uploaddocs in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
*/
        recyclerview = (RecyclerView) view.findViewById(R.id.docsrecyler);
        Connmanager connmanager = new Connmanager(getActivity());

        if (connmanager.checkNetworkConnection()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            offlinedatabase = new Offlinedatabase(getContext());
            DatabaseReference myRef1 = database.getReference();
            myRef1.keepSynced(true);
            myRef1.child("Documents").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("he ", "chlla");
                    offlinedatabase.truncateDocs();


                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Uploaddocs messages = dataSnapshot1.getValue(Uploaddocs.class);
                        Uploaddocs listdata = new Uploaddocs();
                        String head = messages.getName();
                        String body = messages.getImgurl();

                        Log.d("image url", "" + body.toString());
                        listdata.setName(head);
                        listdata.setImgurl(body);
                        list.add(listdata);
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put(DetailsManager.DOCS_COLUMN_TITLE, head);
                        hashMap.put(DetailsManager.DOCS_COLUMN_IMG, body);

                        offlinedatabase.insertDocs(hashMap);
                        // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                    }
                    new DocsFragment.getMessagesFromDb().execute();


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    //  Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        }


        new DocsFragment.getMessagesFromDb().execute();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         selectedoption= parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getContext()).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        Context applicationContext = MainActivity.getContextOfApplication();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(applicationContext.getContentResolver().getType(uri));
    }

    private void uploadFile() {

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
         /*   Uploaddocs upload = new Uploaddocs(mEditTextFileName.getText().toString().trim(),
                    taskSnapshot.getDownloadUrl().toString());
            String uploadId = mDatabaseRef.push().getKey();
            mDatabaseRef.child(uploadId).setValue(upload);
            */

            mUploadTask = fileReference.putFile(mImageUri)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 500);

                            Toast.makeText(getContext(), "Uploaddocs successful", Toast.LENGTH_LONG).show();
                            final UploadTask uploadTask = fileReference.putFile(mImageUri);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();

                                            }
                                            // Continue with the task to get the download URL
                                            return fileReference.getDownloadUrl();

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                urlofimg = task.getResult().toString();

                                                String uploadId = mDatabaseRef.push().getKey();
                                                mDatabaseRef.child(FirebaseAuth.getInstance().getUid()).child(uploadId).child("imgurl").setValue(urlofimg);
                                                mDatabaseRef.child(FirebaseAuth.getInstance().getUid()).child(uploadId).child("name").setValue(mEditTextFileName.getText().toString());


                                            }
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public class getMessagesFromDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.d("MessageFragment", "onPreExecute ");
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {


            Offlinedatabase offlinedatabase = new Offlinedatabase(EllipsisHealthCompanion.getInstance().getApplicationContext());
            list = offlinedatabase.getAllDocs();
            Log.d("listofdocs", "" + offlinedatabase.getAllDocs().toString());


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recycler = new DocsAdapter(getActivity(), list);
            LinearLayoutManager layoutmanager = new LinearLayoutManager(getActivity());
            layoutmanager.setReverseLayout(false);
            layoutmanager.setStackFromEnd(false);
            recyclerview.setLayoutManager(layoutmanager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(recycler);
            recycler.notifyDataSetChanged();

            progressBar.setVisibility(View.GONE);
            if (list.isEmpty()) {
//                textView.setVisibility(View.VISIBLE);
            }
        }
    }
    public void selector()
    {
       final Dialog myDialog = new Dialog(getContext());

        TextView txtclose,particollege,username;
        final EditText contactno,height,weight;
        myDialog.setContentView(R.layout.custom_popup_docs);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        mButtonChooseImage = myDialog.findViewById(R.id.selectimgbtn);
        mButtonUpload = myDialog.findViewById(R.id.uploadimgbtn);
        mEditTextFileName = myDialog.findViewById(R.id.previewnamedt);
        mImageView = myDialog.findViewById(R.id.previewimg);
        txtclose.setText("X");
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"upload clicked",Toast.LENGTH_LONG);
                uploadFile();
            }
        });
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Selector",Toast.LENGTH_LONG);
                openFileChooser();


            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}

