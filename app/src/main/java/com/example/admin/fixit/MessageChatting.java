package com.example.admin.fixit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 2017/03/06.
 */

public class MessageChatting extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private DatabaseReference mDatabaseRef;
    private RecyclerView recyclerList;

    private FirebaseAuth mAuth;


    private ImageButton mImagePicker;
    private TextView textViewText;
    private Button mSend;
    private Uri imageUri;

    private StorageReference mStorage;

    private ProgressDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_list);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("message");
        mDatabaseRef.keepSynced(true);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        recyclerList = (RecyclerView) findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));

        mImagePicker = (ImageButton) findViewById(R.id.photoPickerButton);
        textViewText = (TextView) findViewById(R.id.messageEditText);
        mSend = (Button) findViewById(R.id.sendB);
        mDialog = new ProgressDialog(this);


        mImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.setMessage("Sending ...");
                mDialog.show();

                if (imageUri != null) {
                    StorageReference filePath = mStorage.child("image_picker").child(imageUri.getLastPathSegment());

                    filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            DatabaseReference databaseReference = mDatabaseRef.push();
                            databaseReference.child("name").setValue(mAuth.getCurrentUser().getDisplayName());
                            databaseReference.child("text").setValue(textViewText.getText().toString());
                            databaseReference.child("image").setValue(downloadUrl.toString());

                            mDialog.dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Try to insert an image.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else if (imageUri == null){

                    DatabaseReference databaseReference = mDatabaseRef.push();
                    databaseReference.child("name").setValue(mAuth.getCurrentUser().getDisplayName());
                    databaseReference.child("text").setValue(textViewText.getText().toString());

                    mDialog.dismiss();

                }
            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();
            mImagePicker.setImageURI(imageUri);

        }

    }

    //
    public void submitText() {


        String text = textViewText.getText().toString().trim();

        if (!TextUtils.isEmpty(text) && imageUri != null) {


        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<LandLordMessaging, MsgViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LandLordMessaging, MsgViewHolder>(
                LandLordMessaging.class,
                R.layout.item_message,
                MsgViewHolder.class,
                mDatabaseRef
        ) {
            @Override
            protected void populateViewHolder(MsgViewHolder viewHolder, LandLordMessaging model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setText(model.getText());
                viewHolder.setPhotoUrl(getApplicationContext(), model.getImage());

            }
        };

        recyclerList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MsgViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public MsgViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setText(String text) {

            TextView tvText = (TextView) mView.findViewById(R.id.messageTextView);
            tvText.setText(text);

        }

        public void setName(String name) {

            TextView tvName = (TextView) mView.findViewById(R.id.nameTextView);
            tvName.setText(name);

        }

        public void setPhotoUrl(Context c, String image) {

            ImageView imageView = (ImageView) mView.findViewById(R.id.photoImageView);
            Picasso.with(c).load(image).into(imageView);

        }

    }

}


//    DatabaseReference newMessage = mDatabaseRef.push();
//                    newMessage.child("name").setValue("Unknown");
//                            newMessage.child("image").setValue(donwloadUri.toString());
//                            newMessage.child("text").setValue(textViewText.getText().toString());