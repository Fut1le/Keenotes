package com.ruya.takimi.keenotes.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ruya.takimi.keenotes.BuildConfig;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.ui.activities.MiniGameActivity;
import com.ruya.takimi.keenotes.ui.activities.TilesGameActivity;
import com.ruya.takimi.keenotes.ui.activities.WebViewActivity;

public class About extends Fragment {

    LinearLayout support,website,author,author_2,sp_thanks,game;
    ImageView keenotes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        support=view.findViewById(R.id.email);
        website=view.findViewById(R.id.website);
        author=view.findViewById(R.id.author);
        author_2=view.findViewById(R.id.author_2);
        sp_thanks=view.findViewById(R.id.sp_thanks);
        keenotes=view.findViewById(R.id.keenotes);
        game=view.findViewById(R.id.game);


        TextView version=view.findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);

        support.setOnClickListener(v -> {

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"mr.vovanumberone@gmail.com"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Sent from "+Build.BRAND+" "+Build.DEVICE);
            email.putExtra(Intent.EXTRA_TEXT, "Keenotes\nversion: "+BuildConfig.VERSION_NAME+"\nandroid version: "+Build.VERSION.CODENAME);
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Select email app"));

        });

        website.setOnClickListener(v -> {

           // String url = "https://vl-keenotes.web.app/";
           // Intent i = new Intent(Intent.ACTION_VIEW);
           // i.setData(Uri.parse(url));
           // startActivity(i);
            Intent intentK= new Intent(getActivity(), WebViewActivity.class);
            startActivity(intentK);

        });

        author.setOnClickListener(v -> {

            String url = "https://www.instagram.com/mr.vovanumberone/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });


        author_2.setOnClickListener(v -> {

            String url = "https://www.instagram.com/_magmasig_/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

        sp_thanks.setOnClickListener(v -> {

            String url = "https://www.instagram.com/djcomp125/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        });

        keenotes.setOnClickListener(v -> {
            Intent intentS= new Intent(getActivity(), MiniGameActivity.class);
            startActivity(intentS);

        });

        game.setOnClickListener(v -> {
            Intent intentS= new Intent(getActivity(), TilesGameActivity.class);
            startActivity(intentS);

        });

    }

}
