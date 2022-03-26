package com.ruya.takimi.keenotes.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.gridlayout.widget.GridLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ruya.takimi.keenotes.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MiniGameActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_game);

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
    }

    int[] state = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    int[][] win = {{0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 4, 8}, {2, 4, 6}};

    // pl=0 - o | 1 - x | 2 - none
    int pl = 0;
    boolean playable = true;


    public void click(View view) {

        ImageView xo = (ImageView) view;


        int st = Integer.parseInt(xo.getTag().toString());

        if (state[st] == 2 && playable == true) {


            xo.setScaleX(0f);
            xo.setScaleY(0f);
            state[st] = pl;


            if (pl == 0) {
                xo.setImageResource(R.drawable.o);
                xo.animate().scaleY(1f).scaleX(1f).setDuration(300);
                pl = 1;
            } else {
                xo.setImageResource(R.drawable.x);
                xo.animate().scaleY(1f).scaleX(1f).setDuration(300);
                pl = 0;
            }

            for (int[] w : win) {

                if (state[w[0]] == state[w[1]] && state[w[1]] == state[w[2]] && state[w[1]] != 2) {


                    TextView winner = (TextView) findViewById(R.id.winnerText);
                    winner.setVisibility(View.VISIBLE);


                    playable = false;
                    if (pl == 0) {

                        winner.setText("X Won");
                        winner.animate().rotationXBy(360).setDuration(500);

                    }

                    if (pl == 1) {

                        winner.setText("O Won");
                        winner.animate().rotationXBy(360).setDuration(500);

                    }

                    Button replay = (Button) findViewById(R.id.replay);
                    replay.setVisibility(View.VISIBLE);
                }

            }
        }




    }


    public void replay(View view){

        Button replay = (Button) findViewById(R.id.replay);
        replay.setVisibility(View.INVISIBLE);
        TextView winner = (TextView) findViewById(R.id.winnerText);
        winner.setVisibility(View.INVISIBLE);
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {

            ImageView child = (ImageView) gridLayout.getChildAt(i);
            child.setImageDrawable(null);

        }

        for (int j=0; j<state.length; j++) {

            state[j]=2;
        }

        pl = 0;
        playable = true;




    }


}
