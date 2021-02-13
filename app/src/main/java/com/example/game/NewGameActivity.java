package com.example.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
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
    private DatabaseReference mGameDatabase;
    private FirebaseAuth mAuth;
    private Button toBattleButton;
    private NewGameViewModel mGameViewModel;
    public static final String ROOM_ID_EXTRA = "room_id_extra";
    private static final int BOARD_REQUEST_CODE = 1;
    private static final int GAME_REQUEST_CODE = 2;
    String uniqueID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == BOARD_REQUEST_CODE) {
            mGameViewModel.setBoardCreated(true);
        }

        if (resultCode == RESULT_OK && requestCode == GAME_REQUEST_CODE) {
            mDatabase.child(uniqueID).removeValue();
            mGameDatabase.child(uniqueID).removeValue();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        mGameViewModel = ViewModelProviders.of(this).get(NewGameViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        mBoardDatabase = FirebaseDatabase.getInstance().getReference("boards");
        mGameDatabase = FirebaseDatabase.getInstance().getReference("games");

        copyButton = findViewById(R.id.copyButton);
        idTextView = findViewById(R.id.gameIdValueTextView);
        createBoard = findViewById(R.id.createBoardButton);
        toBattleButton = findViewById(R.id.toBattleButton);

        uniqueID = UUID.randomUUID().toString();
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

        mGameViewModel.getBoardCreated().observe(this, aBoolean -> {
            toBattleButton.setEnabled(aBoolean && mGameViewModel.getOpponentConnected().getValue());
            createBoard.setEnabled(!aBoolean);
        });

        mGameViewModel.getOpponentConnected().observe(this, aBoolean -> toBattleButton.setEnabled(aBoolean && mGameViewModel.getBoardCreated().getValue()));

        createBoard.setOnClickListener(v -> startActivityForResult(new Intent(NewGameActivity.this, NewBoardActivity.class), BOARD_REQUEST_CODE));

        mDatabase.child(uniqueID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("guestReady")){
                    if (snapshot.getValue(Boolean.class)){
                        Toast.makeText(NewGameActivity.this, "Opponent connected", Toast.LENGTH_SHORT).show();
                        mGameViewModel.setOpponentConnected(true);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey().equals("guestReady")){
                    if (snapshot.getValue(Boolean.class)){
                        Toast.makeText(NewGameActivity.this, "Opponent connected", Toast.LENGTH_SHORT).show();
                        mGameViewModel.setOpponentConnected(true);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mGameViewModel.getOpponentConnected().observe(this, aBoolean -> toBattleButton.setEnabled(aBoolean && mGameViewModel.getBoardCreated().getValue()));

        toBattleButton.setOnClickListener(v -> {
            Toast.makeText(NewGameActivity.this, "Starting the game", Toast.LENGTH_SHORT).show();
            mDatabase.child(uniqueID).child("isReady").setValue(true);
            Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
            intent.putExtra(ROOM_ID_EXTRA, uniqueID);
            startActivityForResult(intent, GAME_REQUEST_CODE);
        });
    }



}