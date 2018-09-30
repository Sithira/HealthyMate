package com.example.user.healthymate.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.example.user.healthymate.activities.AnimeActivity;
import com.example.user.healthymate.model.Anime;
import com.example.user.healthymate.R;
import com.example.user.healthymate.pojos.Food;

import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Food> mData;
    RequestOptions option;


    public RecyclerViewAdapter(Context mContext, List<Food> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.dietcard_item,parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.view_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnimeActivity.class);
                intent.putExtra("title",mData.get(viewHolder.getAdapterPosition()).getName());
                intent.putExtra("description",mData.get(viewHolder.getAdapterPosition()).getDescription());
                intent.putExtra("background",mData.get(viewHolder.getAdapterPosition()).getImg());

                mContext.startActivity(intent);
            }
        });

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_title.setText(mData.get(position).getName());
        Glide.with(mContext).load(mData.get(position).getImg()).apply(option).into(holder.background);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        ImageView background;
        ConstraintLayout view_b;

        public MyViewHolder(View view){
            super(view);
            view_b = view.findViewById(R.id.container);
            tv_title = view.findViewById(R.id.tv_title);
            background = view.findViewById(R.id.card_background);
        }
    }
}
