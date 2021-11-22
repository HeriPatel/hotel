package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.provider.ContactsContract;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Objects;

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
    ArrayList<itemsModel> remainingOrder;

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
                        holder.itemNotes.setHint("Notes");
                        holder.itemNotes.setText("");
                        holder.itemNotes.setHintTextColor(Color.GRAY);
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
                model.setItemNotes("");
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
                note.setText(holder.itemNotes.getText());
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
                ProgressDialog progressDialog = new ProgressDialog(view.getRootView().getContext(), R.style.MyAlertDialogStyle);
                progressDialog.setMax(100);
                progressDialog.setMessage("Please wait!");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                APIinterface apIinterface = myRetro.getretrofit(context.getResources().getString(R.string.url)).create(APIinterface.class);
                for (int i = 0; i < getItemCount(); i++) {
                    if (Integer.parseInt(itemsModelArrayList.get(i).getQnty()) > 0) {
                        Call<String> c = apIinterface.addOrder(itemsModelArrayList.get(i).getName(), tableName, itemsModelArrayList.get(i).getQnty(), itemsModelArrayList.get(i).getItemNotes());
                        int finalI = i;
                        c.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (!response.body().equals("error")) {
                                    String orderId = response.body();
                                    //Notification
                                    APIinterface apIinterface1 = myRetro.getretrofit(context.getResources().getString(R.string.url)).create(APIinterface.class);
                                    apIinterface1.getChefId().enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (!response.body().equals("Error!")) {
                                                Integer chefID = Integer.valueOf(response.body());
                                                apIinterface1.getKey(chefID).enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String usertoken = response.body();
                                                        if (usertoken == null) {
//                                                            remainingOrder.add(itemsModelArrayList.get(finalI));
//                                                            Log.d("gilog","Remain order : "+remainingOrder);
                                                            apIinterface1.addDummyOrder(Integer.parseInt(orderId)).enqueue(new Callback<String>() {
                                                                @Override
                                                                public void onResponse(Call<String> call, Response<String> response) {
                                                                    Log.d("gilog","Res in add dummy order : "+response.body());
                                                                }

                                                                @Override
                                                                public void onFailure(Call<String> call, Throwable t) {
                                                                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                                                                    Log.d("gilog","Error in add dummy order : "+t.toString());
                                                                }
                                                            });
                                                            Toast.makeText(context, "No chef is available!", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            /*Log.d("gilog","name : "+itemsModelArrayList.get(finalI).getName());
                                                            Log.d("gilog","order id : "+Integer.parseInt(orderId));
                                                            Log.d("gilog","cheg Id : "+chefID);*/
                                                            sendNotifications(usertoken, "Order", itemsModelArrayList.get(finalI).getName(), Integer.parseInt(orderId), chefID);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Toast.makeText(context, "Notification not sent!", Toast.LENGTH_SHORT).show();
                                                        Log.d("gilog", "Notification : " + t.toString());
                                                    }
                                                });
                                            } else
                                                Toast.makeText(context, "No chef is Available!", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d("gilog", "Error in getting Available chef : " + t.toString());
                                        }
                                    });

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
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(context, orderScreen.class);
                                    intent.putExtra("tabName", tableName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d("gilog", "Add Order : " + t.toString());
                            }
                        });
                    }
                }
            }
        });
    }

    private void sendNotifications(String usertoken, String msg, String order, int orderId, Integer chefId) {
        APIinterface apIinterface1 = myRetro.getretrofit(context.getResources().getString(R.string.url)).create(APIinterface.class);
        apIinterface1.addChef(orderId, chefId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                apIinterface1.changeChefStatus(chefId, "Occupied").enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
//                        Log.d("gilog", "Chef status change : " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("gilog", "Error in chef status change : " + t.toString());
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Error in adding chef id : " + t.toString());
            }
        });

        Data data = new Data(msg, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        APIinterface apIinterface = Client_sendNoti.getClient("https://fcm.googleapis.com/").create(APIinterface.class);
        apIinterface.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                Log.d("gilog", "in notify res : " + response.body().success);
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Notification sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Log.d("gilog", "In Notify " + t.toString());
                Toast.makeText(context, "Notification not sent!", Toast.LENGTH_SHORT).show();
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