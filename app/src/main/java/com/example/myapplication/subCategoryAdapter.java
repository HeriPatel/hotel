package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class subCategoryAdapter extends RecyclerView.Adapter<subCategoryAdapter.Viewlder>{
    private Context context;
    private ArrayList<subCategoryModel> subCategoryModelArrayList;
    private onSubCatClickListener onSubCatClickListener;

    public subCategoryAdapter(Context context, ArrayList<subCategoryModel> subCategoryModelArrayList, onSubCatClickListener onSubCatClickListener) {
        this.context = context;
        this.subCategoryModelArrayList = subCategoryModelArrayList;
        this.onSubCatClickListener = onSubCatClickListener;
    }

    @NonNull
    @Override
    public subCategoryAdapter.Viewlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        return new Viewlder(view,onSubCatClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewlder holder, int position) {
        subCategoryModel model = subCategoryModelArrayList.get(position);
        holder.CatName.setText(model.getSubCatName());
        Glide.with(context)
                .load(context.getString(R.string.url) + model.getSubCatImg())
                .centerCrop().placeholder(R.drawable.load)
                .into(holder.CatImg);
    }

    @Override
    public int getItemCount() {
        return subCategoryModelArrayList.size();
    }

    public static class Viewlder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView CatName;
        private ImageView CatImg;
        onSubCatClickListener onSubCatClickListener;

        public Viewlder(@NonNull View itemView, onSubCatClickListener onSubCatClickListener) {
            super(itemView);
            this.onSubCatClickListener = onSubCatClickListener;
            CatName = itemView.findViewById(R.id.CatLstName);
            CatImg = itemView.findViewById(R.id.CatLstImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSubCatClickListener.onSubCatClick(getAdapterPosition());
        }
    }
    public interface onSubCatClickListener{
        void onSubCatClick(int pos);
    }
}