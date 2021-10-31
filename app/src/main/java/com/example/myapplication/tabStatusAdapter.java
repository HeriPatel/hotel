package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class tabStatusAdapter extends RecyclerView.Adapter<tabStatusAdapter.Viewholder> {
    private Context context;
    private ArrayList<tabStatusModel> tabStatusModelArrayList;
    onTabClickListener onTabClickListener;
//    onTabLongClickListener onTabLongClickListener;

    public tabStatusAdapter(Context context, ArrayList<tabStatusModel> tabStatusModelArrayList, onTabClickListener onTabClickListener) {
        this.context = context;
        this.tabStatusModelArrayList = tabStatusModelArrayList;
        this.onTabClickListener = onTabClickListener;
//        this.onTabLongClickListener = onTabLongClickListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_status_card_layout, parent, false);
        return new Viewholder(view, onTabClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull tabStatusAdapter.Viewholder holder, int position) {
        tabStatusModel model = tabStatusModelArrayList.get(position);
        holder.tabName.setText(model.getTabName());
        holder.tabStatus.setText(model.getTabStatus());
        if (model.getTabStatus().equals("Available")) {
            holder.cardView.setCardBackgroundColor(Color.rgb(95, 167, 119));
            holder.tableRow.setBackgroundColor(Color.rgb(95, 167, 119));
        } else {
            holder.cardView.setCardBackgroundColor(Color.rgb(255, 105, 98));
            holder.tableRow.setBackgroundColor(Color.rgb(255, 105, 98));
        }
    }

    @Override
    public int getItemCount() {
        return tabStatusModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tabName;
        private TextView tabStatus;
        private CardView cardView;
        private LinearLayout tableRow;
        onTabClickListener onTabClickListener;
//        onTabLongClickListener onTabLongClickListener;

        public Viewholder(@NonNull View itemView, onTabClickListener onTabClickListener) {
            super(itemView);
            this.onTabClickListener = onTabClickListener;
//            this.onTabLongClickListener = onTabLongClickListener;
            tabName = itemView.findViewById(R.id.tabName);
            tabStatus = itemView.findViewById(R.id.tabStatus);
            cardView = itemView.findViewById(R.id.tabStatusCard);
            tableRow = itemView.findViewById(R.id.tabStatusRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTabClickListener.onTabClick(getAdapterPosition());
        }


        /*@Override
        public boolean onLongClick(View view) {
            onTabLongClickListener.onTabLongClick(getAdapterPosition());
            return true;
        }*/
    }

    public interface onTabClickListener {
        void onTabClick(int position);
    }

    /*public interface onTabLongClickListener {
        void onTabLongClick(int position);
    }*/
}