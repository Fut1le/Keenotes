package com.ruya.takimi.keenotes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruya.takimi.keenotes.R;
import com.ruya.takimi.keenotes.models.MessageReply;
import com.ruya.takimi.keenotes.ui.activities.notification.NotificationReplyActivity;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageTextReplyAdapter extends RecyclerView.Adapter<MessageTextReplyAdapter.ViewHolder> {

    private List<MessageReply> messageList;
    private Context context;
    private FirebaseFirestore mFirestore;

    public MessageTextReplyAdapter(List<MessageReply> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageTextReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mFirestore=FirebaseFirestore.getInstance();
        View view=LayoutInflater.from(context).inflate(R.layout.message_text_item,parent,false);
        return new ViewHolder(view);    }

	@Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
	
    @Override
    public void onBindViewHolder(@NonNull final MessageTextReplyAdapter.ViewHolder holder, int position) {

        try {
            if (messageList.get(position).getRead()=="true") {
                holder.read_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.read_icon));
                holder.read_icon.setVisibility(View.VISIBLE);
                holder.read_icon.setAlpha(0.0f);
                holder.read_icon.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .start();
            } else {
                holder.read_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.unread_icon));
                holder.read_icon.setVisibility(View.VISIBLE);
                holder.read_icon.setAlpha(0.0f);
                holder.read_icon.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mFirestore.collection("Users")
                .document(messageList.get(position).getFrom())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    Glide.with(context)
                            .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.default_profile_picture))
                            .load(documentSnapshot.getString("image"))
                            .into(holder.image);

                    holder.name.setText(documentSnapshot.getString("name"));

                });

        holder.mView.setOnClickListener(view -> {
            Intent intent=new Intent(context, NotificationReplyActivity.class);
            intent.putExtra("doc_id", messageList.get(holder.getAdapterPosition()).msgId);
            intent.putExtra("read", messageList.get(holder.getAdapterPosition()).getRead());
            intent.putExtra("from_id", messageList.get(holder.getAdapterPosition()).getFrom());
            intent.putExtra("reply_for", messageList.get(holder.getAdapterPosition()).getReply_for());
            intent.putExtra("message", messageList.get(holder.getAdapterPosition()).getMessage());
            context.startActivity(intent);

            messageList.get(holder.getAdapterPosition()).setRead("true");
            holder.read_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.read_icon));
            holder.read_icon.setVisibility(View.VISIBLE);
            holder.read_icon.setAlpha(0.0f);
            holder.read_icon.animate()
                    .alpha(1.0f)
                    .setDuration(300)
                    .start();

        });

        String timeAgo = TimeAgo.using(Long.parseLong(messageList.get(holder.getAdapterPosition()).getTimestamp()));
        holder.time.setText(timeAgo);

        holder.message.setText(String.format(Locale.ENGLISH,"Reply for: %s\nMessage: %s",messageList.get(holder.getAdapterPosition()).getReply_for(),messageList.get(holder.getAdapterPosition()).getMessage()));

        holder.mView.setOnLongClickListener(v -> {

            new MaterialDialog.Builder(context)
                    .title("Delete message")
                    .content("Are you sure do you want to delete this message?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive((dialog, which) -> mFirestore.collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("Notifications_reply")
                            .document(messageList.get(holder.getAdapterPosition()).msgId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                messageList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> e.printStackTrace()))
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .show();

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private CircleImageView image;
        private TextView message,name,time;
        private ImageView read_icon;

        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
            image = mView.findViewById(R.id.image);
            name = mView.findViewById(R.id.name);
            message = mView.findViewById(R.id.message);
            time = mView.findViewById(R.id.time);
            read_icon=mView.findViewById(R.id.read);

        }
    }
}
