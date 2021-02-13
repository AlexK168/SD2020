package com.example.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.game.Models.Board;
import com.example.game.Models.CellStates;
import com.example.game.Models.Game;
import com.example.game.Models.Room;
import com.example.game.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {
    private Integer[] images = {R.mipmap.empty, R.mipmap.ship, R.mipmap.hit, R.mipmap.miss};
    private GridLayout mMyBoard;
    private GridLayout mEnemyBoard;
    private ImageView[] myCells;
    private ImageView[] enemyCells;
    private FirebaseAuth mAuth;
    private DatabaseReference mRoomDatabase;
    private DatabaseReference mBoardDatabase;
    private DatabaseReference mGameDatabase;
    private DatabaseReference mUserDatabase;
    private String hostId;
    private String guestId;
    private String userId;
    private boolean amIHost;
    private EditText mXEditText;
    private EditText mYEditText;
    private Button mFireButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        mMyBoard = findViewById(R.id.my_board);
        mEnemyBoard = findViewById(R.id.enemy_board);
        mYEditText = findViewById(R.id.yEditText);
        mXEditText = findViewById(R.id.xEditText);
        mFireButton = findViewById(R.id.shoot_button);

        mAuth = FirebaseAuth.getInstance();
        mRoomDatabase = FirebaseDatabase.getInstance().getReference("rooms");
        mBoardDatabase = FirebaseDatabase.getInstance().getReference("boards");
        mGameDatabase = FirebaseDatabase.getInstance().getReference("games");
        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        String roomId = getIntent().getStringExtra(JoinGameActivity.ROOM_ID_EXTRA);
        userId = mAuth.getUid();

        myCells = new ImageView[100];
        enemyCells = new ImageView[100];

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 10 * 7;
        int widthOfCell = width / 10;

        initCells(myCells, widthOfCell, mMyBoard);
        initCells(enemyCells, widthOfCell, mEnemyBoard);

        mMyBoard.setColumnCount(10);
        mMyBoard.setRowCount(10);
        mMyBoard.getLayoutParams().width = width;
        mMyBoard.getLayoutParams().height = width;
        mEnemyBoard.setColumnCount(10);
        mEnemyBoard.setRowCount(10);
        mEnemyBoard.getLayoutParams().width = width;
        mEnemyBoard.getLayoutParams().height = width;

        mRoomDatabase.child(roomId).addListenerForSingleValueEvent(new ValueEventListener() { // as soon as room loads from db
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room r = snapshot.getValue(Room.class);
                guestId = r.guestUId;
                hostId = r.hostUId;
                amIHost = userId.equals(hostId);
                addListenersForCells();
                if (amIHost) {
                    // create game
                    Game game = new Game(hostId, guestId);
                    mGameDatabase.child(roomId).setValue(game);

                    addHostListener(roomId); // alter this
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mGameDatabase.child(roomId).child("isHostTurn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class) != null) {
                    mFireButton.setEnabled(snapshot.getValue(Boolean.class) == amIHost);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mFireButton.setOnClickListener(v -> {
            int y = Integer.parseInt(mYEditText.getText().toString());
            int x = Integer.parseInt(mXEditText.getText().toString());
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/targetX", x);
            childUpdates.put("/targetY", y);
            childUpdates.put("/waitingForMove", false);
            mGameDatabase.child(roomId).updateChildren(childUpdates);
        });

        mGameDatabase.child(roomId).child("isFinished").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value != null && !value.isEmpty()) {
                    Toast.makeText(GameActivity.this, (value.equals(userId) ? "You won" : "You lost"), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void addListenersForCells() {
        mBoardDatabase.child(hostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Board b = snapshot.getValue(Board.class);
                updateCells(b, !amIHost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mBoardDatabase.child(guestId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Board b = snapshot.getValue(Board.class);
                updateCells(b, amIHost);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void updateCells(Board board, boolean isEnemyCells) {
        int i = 0;
        int j = 0;
        ImageView[] cells = isEnemyCells ? enemyCells : myCells;
        for (ArrayList<Integer> row : board.cells) {
            for (Integer item : row) {
                int imgId = images[item];
                if (item == 1 && isEnemyCells) {
                    imgId = images[0];
                }
                cells[i * 10 + j].setImageResource(imgId);
                j++;
            }
            i++;
            j = 0;
        }
    }

    private void initCells(ImageView[] cells, int widthOfCell, GridLayout mBoard) {
        for (int i = 0; i < 100; i++) {
            cells[i] = new ImageView(this);
            cells[i].setId(i);
            cells[i].setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfCell, widthOfCell));
            cells[i].setMaxHeight(widthOfCell);
            cells[i].setMaxWidth(widthOfCell);
            cells[i].setImageResource(R.mipmap.empty);
            mBoard.addView(cells[i]);
        }
    }

    private void addHostListener(String roomId) {
            mGameDatabase.child(roomId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Game g = snapshot.getValue(Game.class);
                    if (g == null) {
                        return;
                    }
                    if (g.waitingForMove) {
                        return;
                    }
                    String targetUserId = g.isHostTurn ? g.guestUId : g.hostUId;
                    String attackingUserId = g.isHostTurn ? g.hostUId : g.guestUId;
                    int targetX = g.targetX;
                    int targetY = g.targetY;
                    g.waitingForMove = true;

                    mBoardDatabase.child(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Board board = snapshot.getValue(Board.class);
                            if (board == null) {
                                return;
                            }
                            int state = board.cells.get(targetY).get(targetX);
                            if (state == CellStates.EMPTY) {
                                board.cells.get(targetY).set(targetX, CellStates.MISS);
                            } else if (state == CellStates.SHIP) {
                                board.cells.get(targetY).set(targetX, CellStates.HIT);
                                board.checkIfKilled(targetX, targetY);
                                if (board.isFinished()){
                                    updateStats(targetUserId, attackingUserId);
                                    mGameDatabase.child(roomId).child("isFinished").setValue(attackingUserId);
                                }
                            }
                            if (state != CellStates.SHIP) {
                                g.isHostTurn = !g.isHostTurn;
                            }
                            mBoardDatabase.child(targetUserId).setValue(board);
                            mGameDatabase.child(roomId).setValue(g);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void updateStats(String loser, String winner) {
        mUserDatabase.child(loser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                u.gamesPlayed++;
                mUserDatabase.child(loser).setValue(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mUserDatabase.child(winner).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                u.gamesPlayed++;
                u.gamesWon++;
                mUserDatabase.child(winner).setValue(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}