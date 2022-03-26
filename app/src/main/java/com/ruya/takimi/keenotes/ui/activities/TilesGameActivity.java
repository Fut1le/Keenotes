package com.ruya.takimi.keenotes.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.adapters.GameTileAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class TilesGameActivity extends AppCompatActivity {
    public static int time = 3;
    GridView gridViewShow, gridViewResult;
    TextView tvTime, tvTiles, tvScores;
    GameTileAdapter adapterResult, adapterShow;
    List<Boolean> results, shows;
    int tiles = 3, wins = 0, loses = 0, mScore = 0;
    public static double widthScreen = 0;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title("Discard")
                .content("Are you sure do you want to go back?")
                .positiveText("Yes")
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .onPositive((dialog, which) -> finish())
                .negativeText("No")
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_tiles);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/bold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Have Fun :)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set status bar color transparent
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        // Initial
        Init();

        gridViewShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(time == 0) {
                    if (results.get(position)) { // Correct
                        shows.set(position, true);
                        adapterShow.notifyDataSetChanged();
                        // Check Complete
                        if(results.equals(shows)) {
                            Toast.makeText(TilesGameActivity.this, "Correct! Next Round...",
                                    Toast.LENGTH_LONG).show();
                            wins++;
                            loses = 0;
                            // Calculator point
                            mScore++;

                            // Check chain wins
                            if(wins == 3 && tiles < 12) {
                                tiles++;
                                wins = 0;
                            }

                            // Restart game
                            RestartGame();
                        }
                    }else { // Incorrect
                        // Restart Game
                        Toast.makeText(TilesGameActivity.this, "Wrong answer, play again!",
                                Toast.LENGTH_LONG).show();
                        loses++;
                        wins = 0;
                        // Calculator point
                        if(mScore != 0) mScore--;

                        // Check chain loses
                        if (loses == 3 && tiles > 3) {
                            tiles--;
                            loses = 0;
                        }

                        // Restart game
                        RestartGame();
                    }
                }
            }
        });
    }

    private void Init() {
        // Get device screen
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthScreen = size.x;
        gridViewShow = findViewById(R.id.myGridView);
        gridViewResult = findViewById(R.id.myGridViewResult);
        tvTime = findViewById(R.id.mTime);
        tvTiles = findViewById(R.id.mLevel);
        tvScores = findViewById(R.id.mScore);
        results = new ArrayList<>();
        shows = new ArrayList<>();

        // Make a random tiles
        CreateRandomList();
        ChangedTextView();
        adapterResult = new GameTileAdapter(this, results);
        adapterShow = new GameTileAdapter(this, shows);
        gridViewShow.setAdapter(adapterShow);
        gridViewResult.setAdapter(adapterResult);
        StartTimer();
    }

    private void RestartGame() {
        try {
            ChangedTextView();
            CreateRandomList();
            gridViewResult.setVisibility(View.VISIBLE);
            adapterShow.notifyDataSetChanged();
            adapterResult.notifyDataSetChanged();
            StartTimer();
        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        }
    }

    private void CreateRandomList() {
        Random random = new Random();
        int bound = 36;
        results.clear();
        shows.clear();
        if (tiles >= 10) {
            results.addAll(Collections.nCopies(100, false));
            shows.addAll(results);
            gridViewShow.setNumColumns(10);
            gridViewResult.setNumColumns(10);
            bound = 100;
        }else if (tiles >= 5) {
            results.addAll(Collections.nCopies(64, false));
            shows.addAll(results);
            gridViewShow.setNumColumns(8);
            gridViewResult.setNumColumns(8);
            bound = 64;
        } else {
            results.addAll(Collections.nCopies(36, false));
            shows.addAll(results);
            gridViewShow.setNumColumns(6);
            gridViewResult.setNumColumns(6);
            bound = 36;
        }
        for (int i = 0; i < tiles; i++) {
            int ranNum = random.nextInt(bound);
            while (results.get(ranNum)) {
                ranNum = random.nextInt(bound);
            }
            results.set(ranNum, true);
        }
    }

    private void StartTimer() {
        Timer timer = new Timer();
        time = 3;
        tvTime.setVisibility(View.VISIBLE);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (time == 0) {
                    timer.cancel();
                    timer.purge();
                    tvTime.setVisibility(View.INVISIBLE);
                    gridViewResult.setVisibility(View.INVISIBLE);
                } else {
                    time--;
                    tvTime.setText(String.valueOf(time));
                }
            }
        }, 1000, 1000);
    }

    private void ChangedTextView() {
        tvTiles.setText("Tiles: " + tiles);
        tvScores.setText("Score: " + mScore);
    }
}