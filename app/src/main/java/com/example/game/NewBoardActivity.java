package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.game.Adapters.ShipAdapter;
import com.example.game.Models.Board;
import com.example.game.Models.Ship;
import com.example.game.ViewModels.NewBoardViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewBoardActivity extends AppCompatActivity {

    private GridLayout mBoard;
    private NewBoardViewModel mNewGameViewModel;
    private ImageView[] cells;
    private EditText xEditText;
    private EditText yEditText;
    private EditText sizeEditText;
    private SwitchCompat switchView;
    private Button placeButton;
    private ImageButton left;
    private ImageButton right;
    private ImageButton up;
    private ImageButton down;
    private ImageButton rotate;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_board);

        mDatabase = FirebaseDatabase.getInstance().getReference("boards");
        mAuth = FirebaseAuth.getInstance();
        mBoard = findViewById(R.id.board);
        yEditText = findViewById(R.id.yEditText);
        xEditText = findViewById(R.id.xEditText);
        placeButton = findViewById(R.id.placeButton);
        switchView = findViewById(R.id.switch1);
        sizeEditText = findViewById(R.id.sizeEditText);
        saveButton = findViewById(R.id.save_button);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        up = findViewById(R.id.upImageButton);
        down = findViewById(R.id.downImageButton);
        rotate = findViewById(R.id.rotateImageButton);

        mNewGameViewModel = ViewModelProviders.of(this).get(NewBoardViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final ShipAdapter adapter = new ShipAdapter(this);
        adapter.setOnItemClickListener(new ShipAdapter.ClickListener() {
            @Override
            public void onDeleteClicked(int position, View v) {
                Ship s = adapter.getItem(position);
                mNewGameViewModel.deleteShip(s);
            }

            @Override
            public void onItemClicked(int position, View v) {
                Ship s = adapter.getItem(position);
                mNewGameViewModel.setCurrentShip(s);
            }
        });

        mNewGameViewModel.mShips.observe(this, adapter::setItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cells = new ImageView[100];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels / 10 * 7;
        int widthOfCell = width / 10;
        mBoard.setColumnCount(10);
        mBoard.setRowCount(10);
        mBoard.getLayoutParams().width = width;
        mBoard.getLayoutParams().height = width;
        for (int i = 0; i < 100; i++) {
            cells[i] = new ImageView(this);
            cells[i].setId(i);
            cells[i].setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfCell, widthOfCell));
            cells[i].setMaxHeight(widthOfCell);
            cells[i].setMaxWidth(widthOfCell);
            cells[i].setImageResource(R.mipmap.empty);
            mBoard.addView(cells[i]);
        }

        mNewGameViewModel.getShips().observe(this, ships -> {
            clearBoard();
            for (Ship ship : ships) {
                boolean isHorizontal = ship.orientation;
                int x = ship.x;
                int y = ship.y;
                for (int i = 0; i < ship.length; i++) {
                    int index = 10 * y + x;
                    cells[index].setImageResource(R.mipmap.ship);
                    if (isHorizontal){
                        x++;
                    } else{
                        y++;
                    }
                }
            }
        });

        placeButton.setOnClickListener(v -> {
            int x = Integer.parseInt(xEditText.getText().toString());
            int y = Integer.parseInt(yEditText.getText().toString());
            int size = Integer.parseInt(sizeEditText.getText().toString());
            boolean isHorizontal = switchView.isChecked();
            Ship ship = new Ship(x, y, size, isHorizontal);
            mNewGameViewModel.addShip(ship);
        });

        down.setOnClickListener(v -> mNewGameViewModel.move(0, 1));
        up.setOnClickListener(v -> mNewGameViewModel.move(0, -1));
        left.setOnClickListener(v -> mNewGameViewModel.move(-1, 0));
        right.setOnClickListener(v -> mNewGameViewModel.move(1, 0));
        rotate.setOnClickListener(v-> mNewGameViewModel.rotate());
        saveButton.setOnClickListener(v -> {
            if (saveBoard()) {
                Toast.makeText(NewBoardActivity.this, "Board is saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void clearBoard() {
        for (int i = 0; i < 100; i++) {
            cells[i].setImageResource(R.mipmap.empty);
        }
    }

    public boolean saveBoard() {
        String key = mAuth.getCurrentUser().getUid();
        Board b = mNewGameViewModel.saveBoard();
        if (b == null) {
           return false;
        }
        mDatabase.child(key).setValue(b);
        return true;
    }
}