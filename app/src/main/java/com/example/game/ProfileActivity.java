package com.example.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

public class ProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    private Button mSaveButton;
    private ImageButton mPickImage;
    private TextView mGamesPlayed;
    private TextView mGamesWon;
    private TextView mWinRate;
    private ImageView mImageView;
    private EditText mNickname;

    private Uri mImageUri;
    private DatabaseReference mDatabase;
    private StorageTask mUploadTask;
    private StorageReference mStorage;
    private boolean isUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mSaveButton = findViewById(R.id.saveButton);
        mPickImage = findViewById(R.id.pickImageButton);
        mGamesPlayed = findViewById(R.id.gamesPlayedValueTextView);
        mGamesWon = findViewById(R.id.gamesWonValueTextView);
        mWinRate = findViewById(R.id.winRateValueTextView);
        mImageView = findViewById(R.id.imageView);
        mNickname = findViewById(R.id.editNickname);
        mGamesPlayed.setText("0");
        mGamesWon.setText("0");
        mWinRate.setText("100 %");

        isUploaded = false;

        mStorage = FirebaseStorage.getInstance().getReference("uploads");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        subscribe();

        mSaveButton.setOnClickListener(v -> {
            if (saveImage() && saveNickname()) { finish(); }
        });

        mPickImage.setOnClickListener(v -> chooseImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    private void subscribe(){
        String key = mAuth.getCurrentUser().getUid();
        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    return;
                }
                String nickname = user.nickname;
                if (nickname != null) {
                    mNickname.setText(nickname);
                }
                Integer gamesPlayed = user.gamesPlayed;
                Integer gamesWon = user.gamesWon;
                if (gamesPlayed == null){
                    return;
                }
                mGamesPlayed.setText(gamesPlayed.toString());
                if (gamesWon != null) {
                    mGamesWon.setText(gamesWon.toString());
                    String winRate = Double.toString(Math.floor((double)gamesWon / (double)gamesPlayed * 10000)/100).concat(" %");
                    mWinRate.setText(winRate);
                }
                String imagePath = user.imagePath;

                if (imagePath != null && !imagePath.isEmpty()) {
                    Picasso.get().load(imagePath).into(mImageView);
                    isUploaded = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private boolean saveImage() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorage.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                        String upload = task.getResult().toString();
                        mDatabase.child(mAuth.getUid()).child("imagePath").setValue(upload);
                    }))
                    .addOnFailureListener(e -> Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            return true;
        }
        else if (isUploaded) {
            return true;
        }
        else {
            Toast.makeText(this, R.string.imageNotChosen, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean saveNickname() {
        String newNickname = mNickname.getText().toString();
        if (newNickname.equals("")) {
            Toast.makeText(this, R.string.emptyNickname, Toast.LENGTH_SHORT).show();
            return false;
        }
        mDatabase.child(mAuth.getUid()).child("nickname").setValue(newNickname);
        return true;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}