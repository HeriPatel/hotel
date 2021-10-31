package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<itemsModel> itemsModelArrayList;
    private float orderTotal = 0;
    private TextView orderTotalTxt;

    public orderAdapter(Context context, ArrayList<itemsModel> itemsModelArrayList,TextView orderTotalTxt) {
        this.context = context;
        this.itemsModelArrayList = itemsModelArrayList;
        this.orderTotalTxt = orderTotalTxt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_layout, parent, false);
        return new orderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemsModel model = itemsModelArrayList.get(position);
        holder.orderItemNotes.setText(model.getItemNotes());
        TextView itemQnty = holder.itemQty;
        itemQnty.setText(model.getQnty());
        float price = Float.parseFloat(model.getQnty()) * Float.parseFloat(model.getPrice());
        orderTotal += price;
//        Log.d("gilog", "Total : " + orderTotal);
        holder.itemName.setText(model.getName());
        holder.itemDes.setText(model.getDes());
        holder.itemPrice.setText(model.getPrice() + "/-");
        Glide.with(context)
                .load(context.getString(R.string.url) + model.getImg())
                .centerCrop().placeholder(R.drawable.load)
                .into(holder.itemImg);
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIinterface apIinterface = myRetro.getretrofit(context).create(APIinterface.class);
                Call<String> c = apIinterface.cancelOrdr(Integer.parseInt(model.getId()));
                c.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.body().equals("done"))
                            Toast.makeText(context, "Can't remove!", Toast.LENGTH_SHORT).show();
                        else {
                            itemsModelArrayList.remove(position);
                            notifyDataSetChanged();
                            orderTotal = 0;
                            for (int i = 0; i < itemsModelArrayList.size(); i++) {
                                float price = Float.parseFloat(itemsModelArrayList.get(i).getQnty()) * Float.parseFloat(itemsModelArrayList.get(i).getPrice());
                                orderTotal += price;
                            }
                            orderTotalTxt.setText("Total : " + orderTotal + "/-");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("gilog", "Remove order : " + t.toString());
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName, itemPrice, itemDes;
        private ImageView itemImg;
        private TextView itemQty, orderItemNotes;
        private ImageButton cancelOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDes = itemView.findViewById(R.id.itemDes);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImg = itemView.findViewById(R.id.itemImg);
            itemQty = itemView.findViewById(R.id.itemQty);
            cancelOrder = itemView.findViewById(R.id.cancelOrder);
            orderItemNotes = itemView.findViewById(R.id.orderItemNotes);
        }
    }
}