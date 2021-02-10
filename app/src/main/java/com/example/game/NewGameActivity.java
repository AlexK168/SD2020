package com.example.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game.Models.Board;
import com.example.game.Models.Room;
import com.example.game.Models.User;
import com.example.game.ViewModels.NewGameViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class NewGameActivity extends AppCompatActivity {

    private Button createBoard;
    private TextView idTextView;
    private ImageButton copyButton;
    private DatabaseReference mDatabase;
    private DatabaseReference mBoardDatabase;
    private FirebaseAuth mAuth;
    private Button toBattleButton;
    private NewGameViewModel mGameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        mGameViewModel = ViewModelProviders.of(this).get(NewGameViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        mBoardDatabase = FirebaseDatabase.getInstance().getReference("boards");

        copyButton = findViewById(R.id.copyButton);
        idTextView = findViewById(R.id.gameIdValueTextView);
        createBoard = findViewById(R.id.createBoardButton);
        toBattleButton = findViewById(R.id.toBattleButton);

        String uniqueID = UUID.randomUUID().toString();
        idTextView.setText(uniqueID);
        Room room = new Room(mAuth.getUid());
        mDatabase.child(uniqueID).setValue(room);

        copyButton.setOnClickListener(v -> {
            Toast.makeText(NewGameActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            String label = "Copied";
            ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, idTextView.getText().toString());
            clipboard.setPrimaryClip(clip);
        });

        createBoard.setOnClickListener(v -> startActivity(new Intent(NewGameActivity.this, NewBoardActivity.class)));

        mDatabase.child(uniqueID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);
                if (room != null && room.guestUId != null) {
                    mGameViewModel.opponentId = room.guestUId;
                    mGameViewModel.opponentConnected.setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mGameViewModel.opponentConnected.observe(this, aBoolean -> {
            if (aBoolean) {
                mBoardDatabase.child(mGameViewModel.opponentId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Board opponentBoard = snapshot.getValue(Board.class);
                        if (opponentBoard != null) {
                            mGameViewModel.opponentBoardExists = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        mBoardDatabase.child(uniqueID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Board board = snapshot.getValue(Board.class);
                if (board != null) {
                    mGameViewModel.selfBoardExists = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toBattleButton.setOnClickListener(v -> {
            Toast.makeText(NewGameActivity.this, (mGameViewModel.ready() ? "Starting the game" : "Not ready yet"), Toast.LENGTH_SHORT).show();
            if (mGameViewModel.ready()) {
                // start game activity
            }
        });
    }

}