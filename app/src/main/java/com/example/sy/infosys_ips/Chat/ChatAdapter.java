package com.example.sy.infosys_ips.Chat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sy.infosys_ips.R;
import com.example.sy.infosys_ips.models.User;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private android.content.Context context;
    private List<User> muser;


    public ChatAdapter(android.content.Context mcontext, List<User> muser) {

        this.context = mcontext;
        this.muser = muser;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new ChatAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder viewHolder, int position) {

        final User musers = muser.get(position);
        viewHolder.usernamechat.setText(musers.getUsername());
        if (musers.getImageURL().equals("DEFAULT")){
            viewHolder.userprofile.setImageResource(R.mipmap.infosysimage);
        }
        else {
            Glide.with(context).load(musers.getImageURL()).into(viewHolder.userprofile);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,MessageActivity.class);
                intent.putExtra("idmessage",musers.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView usernamechat;
        ImageView userprofile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamechat = itemView.findViewById(R.id.usernamechat);
            userprofile = itemView.findViewById(R.id.userimage);
        }
    }
}

