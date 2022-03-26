package com.ruya.takimi.keenotes.ui.activities.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.ui.activities.friends.SendActivity;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class NotificationReplyActivity extends AppCompatActivity {

    private TextView nameTxt, messageTxt;
    private String msg, reply;
    private CircleImageView imageView;

    private TextView replyFor;
    private String user_id, current_id;
    private Button mSend;
    private EditText message;
    private FirebaseFirestore mFirestore;
    private ProgressBar mBar;
    private TextView username;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

        setContentView(R.layout.activity_notification_reply);
        Toolbar toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTxt = findViewById(R.id.name);
        messageTxt = findViewById(R.id.messagetxt);
        imageView = findViewById(R.id.circleImageView);
        replyFor = findViewById(R.id.replytxt);

        current_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mSend = (Button) findViewById(R.id.send);
        message = (EditText) findViewById(R.id.message);
        mBar = (ProgressBar) findViewById(R.id.progressBar);


        msg = getIntent().getStringExtra("message");
        reply = getIntent().getStringExtra("reply_for");
        user_id = getIntent().getStringExtra("from_id");

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String image_ = documentSnapshot.getString("image");
                CircleImageView imageView=findViewById(R.id.currentProfile);

                Glide.with(NotificationReplyActivity.this)
                        .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.default_profile_picture))
                        .load(image_)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        mFirestore.collection("Users").document(user_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("name");
                nameTxt.setText(name);

                String image_ = documentSnapshot.getString("image");

                Glide.with(NotificationReplyActivity.this)
                        .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.default_profile_picture))
                        .load(image_)
                        .into(imageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imageView.setVisibility(View.GONE);
                nameTxt.setVisibility(View.GONE);
            }
        });

        replyFor.setText(reply);
        messageTxt.setText(msg);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null && getIntent().getStringExtra("notification_id") != null) {
            notificationManager.cancel(Integer.parseInt(getIntent().getStringExtra("notification_id")));
        }
        updateReadStatus();
        initReply();

    }

    private void updateReadStatus() {

        String read=getIntent().getStringExtra("read");
        if(read=="false"){
            Map<String,Object> readMap=new HashMap<>();
            readMap.put("read","true");

            mFirestore.collection("Users").document(current_id).collection("Notifications_reply")
                    .document(getIntent().getStringExtra("doc_id")).update(readMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("done","read:true");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("error","read:false::"+e.getLocalizedMessage());
                }
            });
        }

    }


    private void initReply() {


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message_ = message.getText().toString();

                if (!TextUtils.isEmpty(message_)) {
                    mBar.setVisibility(View.VISIBLE);
                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("reply_for", msg);
                    notificationMessage.put("message", message_);
                    notificationMessage.put("from", current_id);
                    notificationMessage.put("notification_id", String.valueOf(System.currentTimeMillis()));
                    notificationMessage.put("timestamp", String.valueOf(System.currentTimeMillis()));

                    mFirestore.collection("Users/" + user_id + "/Notifications_reply").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toasty.success(NotificationReplyActivity.this, "Message sent!", Toasty.LENGTH_SHORT, true).show();
                            message.setText("");
                            mBar.setVisibility(View.GONE);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(NotificationReplyActivity.this, "Error : " + e.getMessage(), Toasty.LENGTH_SHORT,true).show();
                            mBar.setVisibility(View.GONE);
                        }
                    });

                }

            }
        });

    }


    public void SendNew(View view) {
        SendActivity.startActivityExtra(NotificationReplyActivity.this, user_id);
        overridePendingTransitionExit();
    }


}
