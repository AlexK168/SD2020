package com.example.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public static final String ROOM_ID_EXTRA = "room_id_extra";
    private static final int BOARD_REQUEST_CODE = 1;
    private static final int GAME_REQUEST_CODE = 2;
    private String Uid;

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
        toBattleButton.setEnabled(false);

        Uid = mAuth.getUid();
        toBattleButton.setOnClickListener(v -> {
            String roomId = idEditText.getText().toString();
            DatabaseReference assumedRoom = mDatabase.child(roomId);
            mJoinGameViewModel.setRoomId(roomId);
            mJoinGameViewModel.setAssumedRoom(assumedRoom);
        });

        mJoinGameViewModel.getAssumedRoom().observe(this, databaseReference -> {
            if (databaseReference != null) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Room room = snapshot.getValue(Room.class);
                        if (room != null) {
                            room.guestReady = true;
                            room.guestUId = Uid;
                            String roomId = snapshot.getKey();
                            mDatabase.child(roomId).setValue(room);
                            setListenerForHostCommand(databaseReference);
                            toBattleButton.setEnabled(false);
                        } else {
                            Toast.makeText(JoinGameActivity.this, "Type a valid game id", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(JoinGameActivity.this, "Type a valid game id", Toast.LENGTH_SHORT).show();
            }
        });

        mJoinGameViewModel.getBoardCreated().observe(this, aBoolean -> {
            toBattleButton.setEnabled(aBoolean);
            createBoard.setEnabled(!aBoolean);
        });
        createBoard.setOnClickListener(v -> startActivityForResult(new Intent(JoinGameActivity.this, NewBoardActivity.class), BOARD_REQUEST_CODE));
    }

    private void setListenerForHostCommand(DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);
                if (room.isReady) {
                    Intent intent = new Intent(JoinGameActivity.this, GameActivity.class);
                    intent.putExtra(JoinGameActivity.ROOM_ID_EXTRA, snapshot.getKey());
                    startActivityForResult(intent, GAME_REQUEST_CODE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == BOARD_REQUEST_CODE) {
            mJoinGameViewModel.setBoardCreated(true);
        }
        if (resultCode == RESULT_OK && requestCode == GAME_REQUEST_CODE) {
            finish();
        }
    }
}