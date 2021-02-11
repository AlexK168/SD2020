package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.Models.Room;
import com.example.game.ViewModels.JoinGameViewModel;
import com.example.game.ViewModels.NewGameViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGameActivity extends AppCompatActivity {

    private Button createBoard;
    private EditText idEditText;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Button toBattleButton;
    private JoinGameViewModel mJoinGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        mJoinGameViewModel = ViewModelProviders.of(this).get(JoinGameViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        idEditText= findViewById(R.id.gameIdValueTextView);
        createBoard = findViewById(R.id.createBoardButton);
        toBattleButton = findViewById(R.id.toBattleButton);

        toBattleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(idEditText.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Room room = snapshot.getValue(Room.class);
                        if (room != null) {
                            room.guestUId = mAuth.getUid();
                            mDatabase.child(idEditText.getText().toString()).setValue(room);
                            mJoinGameViewModel.isConnected.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        mJoinGameViewModel.isConnected.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mDatabase.child(idEditText.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Room r = snapshot.getValue(Room.class);
                            if (r != null) {
                                if (r.isReady) {
                                    Toast.makeText(JoinGameActivity.this, "Joining to game", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(JoinGameActivity.this, GameActivity.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        createBoard.setOnClickListener(v -> startActivity(new Intent(JoinGameActivity.this, NewBoardActivity.class)));
    }
}