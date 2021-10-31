package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.Viewlder> {
    private Context context;
    private ArrayList<categoryModel> categoryModelArrayList;
    private onCatClickListener onCatClickListener;

    public categoryAdapter(Context context, ArrayList<categoryModel> categoryModelArrayList, onCatClickListener onCatClickListener) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;
        this.onCatClickListener = onCatClickListener;
    }

    @NonNull
    @Override
    public categoryAdapter.Viewlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        return new Viewlder(view, onCatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewlder holder, int position) {
        categoryModel model = categoryModelArrayList.get(position);
        holder.CatName.setText(model.getCategor_name());
        Glide.with(context)
                .load(context.getString(R.string.url) + model.getCategor_img())
                .centerCrop().placeholder(R.drawable.load)
                .into(holder.CatImg);
    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public static class Viewlder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView CatName;
        private ImageView CatImg;
        onCatClickListener onCatClickListener;

        public Viewlder(@NonNull View itemView, onCatClickListener onCatClickListener) {
            super(itemView);
            this.onCatClickListener = onCatClickListener;
            CatName = itemView.findViewById(R.id.CatLstName);
            CatImg = itemView.findViewById(R.id.CatLstImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCatClickListener.onCatClick(getAdapterPosition());
        }
    }

    public interface onCatClickListener {
        void onCatClick(int pos);
    }
}