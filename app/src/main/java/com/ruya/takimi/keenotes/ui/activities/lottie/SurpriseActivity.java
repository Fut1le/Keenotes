package com.ruya.takimi.keenotes.ui.activities.lottie;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.ruya.takimi.keenotes.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SurpriseActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;


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

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/bold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("I love u!");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_festival);

        lottieAnimationView=findViewById(R.id.lottieView);
        lottieAnimationView.useHardwareAcceleration(true);
        lottieAnimationView.setAnimation("happy_tyan.json");
        lottieAnimationView.playAnimation();

    }
}
