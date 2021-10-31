package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class orderScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private APIinterface apIinterface;
    private Retrofit retrofit;
    private ArrayList<itemsModel> itemsModelArrayList;
    ProgressDialog progressDialog;
    int itemCount;
    String tableName;
    TextView orderTotalTxt;
    private float orderTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);
        recyclerView = findViewById(R.id.orderRV);
        itemCount = getIntent().getIntExtra("itemSize", 1);
        tableName = getIntent().getStringExtra("tabName");
        orderTotalTxt = findViewById(R.id.orderTotal);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        retrofit = myRetro.getretrofit(this);
        apIinterface = retrofit.create(APIinterface.class);
        Call<ArrayList<itemsModel>> c = apIinterface.getOrder(tableName);
        c.enqueue(new Callback<ArrayList<itemsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<itemsModel>> call, Response<ArrayList<itemsModel>> response) {
                itemsModelArrayList = response.body();
                orderAdapter orderAdapter = new orderAdapter(getApplicationContext(), itemsModelArrayList,orderTotalTxt);
                recyclerView.setAdapter(orderAdapter);
                for (int i = 0; i < itemsModelArrayList.size(); i++) {
                    float price = Float.parseFloat(itemsModelArrayList.get(i).getQnty()) * Float.parseFloat(itemsModelArrayList.get(i).getPrice());
                    orderTotal += price;
                }
                orderTotalTxt.setText("Total : " + orderTotal + "/-");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<itemsModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(orderScreen.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Order List : " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        super.onBackPressed();
    }

    public void add(View view) {
        Intent intent = new Intent(this, category_list.class);
        intent.putExtra("tabName", tableName);
        startActivity(intent);
    }

    public void complt(View view) {
        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        retrofit = myRetro.getretrofit(this);
        apIinterface = retrofit.create(APIinterface.class);
        Call<String> c = apIinterface.completeOrder(tableName);
        c.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("done")) {
                    Call<String> call1 = apIinterface.changeTabStatus(tableName, "Available");
                    call1.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            progressDialog.dismiss();
                            if (response.body().equals("error"))
                                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d("gilog", "Changing tabStatus : " + t.toString());
                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(orderScreen.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Complete Order : " + t.toString());
            }
        });
    }
}