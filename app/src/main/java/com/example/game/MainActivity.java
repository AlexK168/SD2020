package com.example.game;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.game.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private Button mLogOutButton;
    private Button mNewGameButton;
    private Button mJoinGameButton;
    private Button mProfileButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mJoinGameButton = findViewById(R.id.join_game_button);
        mNewGameButton = findViewById(R.id.new_game_button);
        mLogOutButton = findViewById(R.id.log_out_button);
        mProfileButton = findViewById(R.id.profile_button);

        mLogOutButton.setOnClickListener(v -> {
            mAuth.signOut();
            recreate();
        });

        mProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        mNewGameButton.setOnClickListener(v -> {
            // start new game here
            startActivity(new Intent(MainActivity.this, NewGameActivity.class));
            // board activity
            // startActivity(new Intent(MainActivity.this, NewBoardActivity.class));
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.EmailBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                createUser();
            } else {
                Toast.makeText(MainActivity.this, R.string.failedLoginMsg, Toast.LENGTH_SHORT).show();
                // Sign in failed
            }
        }
    }

    private void createUser() {
        // create game user if doesn't exist yet
        String key = mAuth.getCurrentUser().getUid();
        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dbUser = snapshot.getValue(User.class);
                if (dbUser == null) {
                    User user = new User();
                    mDatabase.child(key).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}