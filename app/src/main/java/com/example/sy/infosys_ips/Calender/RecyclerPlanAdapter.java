package com.example.sy.infosys_ips.Calender;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.sy.infosys_ips.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecyclerPlanAdapter extends FirestoreRecyclerAdapter<ListModel,RecyclerPlanAdapter.PlanHolder> {

    onItemClickListener listener;

    public RecyclerPlanAdapter(@NonNull FirestoreRecyclerOptions<ListModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlanHolder holder, int position, @NonNull ListModel model) {



        holder.daterecycler.setText(model.getDate());
        holder.titlerecycler.setText(model.getTitle());
        holder.descriptionrecycler.setText(model.getDescription());
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_plan_layout,viewGroup,false);
        return new PlanHolder(v);
    }


    public void deleteitem(int position){

    getSnapshots().getSnapshot(position).getReference().delete();
    }


    class PlanHolder extends RecyclerView.ViewHolder{

        TextView daterecycler;
        TextView titlerecycler;
        TextView descriptionrecycler;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            daterecycler = itemView.findViewById(R.id.date);
            titlerecycler = itemView.findViewById(R.id.title);
            descriptionrecycler = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int positions = getAdapterPosition();
                    if (positions != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(positions),positions);
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;

    }
}
