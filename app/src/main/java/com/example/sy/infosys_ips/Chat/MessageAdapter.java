package com.example.sy.infosys_ips.Chat;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private android.content.Context context;
    private List<Chat> mChat;
    private FirebaseUser fuser;

    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;
    private String imageurl;


    public MessageAdapter(android.content.Context mcontext, List<Chat> mChat,String imageurl) {


        this.context = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == MSG_RIGHT){
        View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
        return new MessageAdapter.ViewHolder(v);
        }

        else
            {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int position) {
        Chat chat = mChat.get(position);
        viewHolder.showchat.setText(chat.getMessage());
        if (imageurl.equals("DEFAULT")){
            viewHolder.userprofile.setImageResource(R.mipmap.infosysimage);
        }
        else {
            Glide.with(context).load(imageurl).into(viewHolder.userprofile);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView showchat;
        ImageView userprofile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showchat = itemView.findViewById(R.id.smschat);
            userprofile = itemView.findViewById(R.id.imagechat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_RIGHT;
        } else {
            return MSG_LEFT;
        }
    }
}
