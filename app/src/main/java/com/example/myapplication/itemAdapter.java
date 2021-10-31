package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<itemsModel> itemsModelArrayList;
    Button orderBtn;
    String tableName;
    AlertDialog.Builder builder;

    public itemAdapter(Context context, ArrayList<itemsModel> itemsModelArrayList, Button orderBtn, String tableName) {
        this.context = context;
        this.itemsModelArrayList = itemsModelArrayList;
        this.orderBtn = orderBtn;
        this.tableName = tableName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_card_layout, parent, false);
        return new itemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        itemsModel model = itemsModelArrayList.get(position);
        TextView itemQnty = holder.itemQty;
        itemQnty.setText(model.getQnty());
        holder.itemName.setText(model.getName());
        holder.itemDes.setText(model.getDes());
        holder.itemPrice.setText(model.getPrice() + "/-");
        Glide.with(context)
                .load(context.getString(R.string.url) + model.getImg())
                .centerCrop().placeholder(R.drawable.load)
                .into(holder.itemImg);

        holder.itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(itemQnty.getText().toString());
                if (n >= 0) {
                    n++;
                    model.setQnty(n + "");
                    itemQnty.setText(n + "");
                }
            }
        });
        holder.itemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(itemQnty.getText().toString());
                if (n > 0) {
                    n--;
                    if (n == 0) {
                        holder.addMoreLayout.setVisibility(View.GONE);
                        holder.itemSelect.setVisibility(View.VISIBLE);
                        holder.itemNotes.setEnabled(false);
                        holder.itemNotes.setText("Notes");
                        holder.itemNotes.setTextColor(Color.GRAY);
                        holder.itemNotes.setBackgroundResource(R.color.lightBlk);
                    }
                    model.setQnty(n + "");
                    itemQnty.setText(n + "");
                }
            }
        });
        holder.itemSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addMoreLayout.setVisibility(View.VISIBLE);
                orderBtn.setVisibility(View.VISIBLE);
                holder.itemNotes.setEnabled(true);
                holder.itemNotes.setBackgroundResource(R.color.main);
                holder.itemNotes.setHintTextColor(Color.WHITE);
                holder.itemNotes.setTextColor(Color.WHITE);
                holder.itemSelect.setVisibility(View.GONE);
                itemQnty.setText(1 + "");
                model.setQnty(1 + "");
            }
        });
        holder.itemNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(view.getRootView().getContext());
                builder.setTitle("Add Note");
                LayoutInflater inflater = LayoutInflater.from(context);
                View view1 = inflater.inflate(R.layout.design_notes, null);
                builder.setView(view1);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                EditText note = view1.findViewById(R.id.noteAdd);
                Button addNoteBtn = view1.findViewById(R.id.addNoteBtn);
                addNoteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        holder.itemNotes.setBackgroundColor(Color.WHITE);
                        holder.itemNotes.setText(note.getText().toString());
                        holder.itemNotes.setTextColor(Color.BLACK);
                        model.setItemNotes(note.getText().toString());
                    }
                });
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIinterface apIinterface = myRetro.getretrofit(context).create(APIinterface.class);
                for (int i = 0; i < getItemCount(); i++) {
                    if (Integer.parseInt(itemsModelArrayList.get(i).getQnty()) > 0) {
                        Call<String> c = apIinterface.addOrder(itemsModelArrayList.get(i).getName(), tableName, itemsModelArrayList.get(i).getQnty(), itemsModelArrayList.get(i).getItemNotes());
                        c.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body().equals("done")) {
                                    Call<String> call1 = apIinterface.changeTabStatus(tableName, "Reserved");
                                    call1.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.body().equals("error"))
                                                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("gilog", "Changing tabStatus : " + t.toString());
                                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent intent = new Intent(context, orderScreen.class);
                                    intent.putExtra("tabName", tableName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);

                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d("gilog", "Add Order : " + t.toString());
                            }
                        });
                    }
                }
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
        private TextView itemNotes;
        private Button itemSelect, itemAdd, itemRemove;
        private TextView itemQty;
        private LinearLayout addMoreLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDes = itemView.findViewById(R.id.itemDes);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImg = itemView.findViewById(R.id.itemImg);
            itemSelect = itemView.findViewById(R.id.itemSelect);
            itemAdd = itemView.findViewById(R.id.itemAdd);
            itemQty = itemView.findViewById(R.id.itemQty);
            itemRemove = itemView.findViewById(R.id.itemRemove);
            addMoreLayout = itemView.findViewById(R.id.addMoreLayout);
            itemNotes = itemView.findViewById(R.id.itemNotes);
        }
    }
}