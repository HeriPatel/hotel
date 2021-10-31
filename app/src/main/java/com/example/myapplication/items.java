package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class items extends AppCompatActivity {
    TextView txt;
    Button orderBtn;
    private RecyclerView recyclerView;
    private APIinterface apIinterface;
    private Retrofit retrofit;
    private ArrayList<itemsModel> itemsModelArrayList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        txt = findViewById(R.id.txtVwItems);
        String name = getIntent().getStringExtra("subCatName");
        txt.setText(name);
        orderBtn = findViewById(R.id.order);

        recyclerView = findViewById(R.id.itemsRV);
        itemsModelArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        retrofit = myRetro.getretrofit(this);
        apIinterface = retrofit.create(APIinterface.class);
        Call<ArrayList<itemsModel>> c = apIinterface.getItems(name);
        c.enqueue(new Callback<ArrayList<itemsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<itemsModel>> call, Response<ArrayList<itemsModel>> response) {
                itemsModelArrayList = response.body();
                itemAdapter itemAdapter = new itemAdapter(getApplicationContext(), itemsModelArrayList, orderBtn, getIntent().getStringExtra("tabName"));
                recyclerView.setAdapter(itemAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<itemsModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(items.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Item List : " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}