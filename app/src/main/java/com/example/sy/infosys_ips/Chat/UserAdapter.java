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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private android.content.Context context;
    private List<User> muser;
    private boolean isChat;


    public UserAdapter(android.content.Context mcontext, List<User> muser,boolean isChat) {

        this.context = mcontext;
        this.muser = muser;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_item,viewGroup,false);
        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final User musers = muser.get(position);
        viewHolder.usernamechat.setText(musers.getUsername());
        if (musers.getImageURL().equals("DEFAULT")){
            viewHolder.userprofile.setImageResource(R.mipmap.infosysimage);
        }
        else {
            Glide.with(context).load(musers.getImageURL()).into(viewHolder.userprofile);
        }

        if (isChat){
            if (musers.getStatus().equals("online")){

                viewHolder.useron.setVisibility(View.VISIBLE);
                viewHolder.useroff.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.useron.setVisibility(View.GONE);
                viewHolder.useroff.setVisibility(View.VISIBLE);
            }
        }else {
            viewHolder.useron.setVisibility(View.GONE);
            viewHolder.useroff.setVisibility(View.GONE);
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
        ImageView useron;
        ImageView useroff;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernamechat = itemView.findViewById(R.id.usernamechat);
            userprofile = itemView.findViewById(R.id.userimage);
            useron = itemView.findViewById(R.id.useron);
            useroff = itemView.findViewById(R.id.useroff);
        }
    }
}
