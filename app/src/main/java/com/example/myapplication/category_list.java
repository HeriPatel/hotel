package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class category_list extends AppCompatActivity implements categoryAdapter.onCatClickListener {

    Retrofit rCat;
    APIinterface api;
    private RecyclerView rv;
    private ArrayList<categoryModel> categoryModelArrayList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        rv = findViewById(R.id.catLstId);
        categoryModelArrayList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        rCat = myRetro.getretrofit(this);
        api = rCat.create(APIinterface.class);
        Call<ArrayList<categoryModel>> c = api.getCategory();
        c.enqueue(new Callback<ArrayList<categoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<categoryModel>> call, Response<ArrayList<categoryModel>> response) {
                categoryModelArrayList = response.body();
                categoryAdapter categoryAdapter = new categoryAdapter(getApplicationContext(), categoryModelArrayList, category_list.this);
                rv.setAdapter(categoryAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<categoryModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(category_list.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Category List : " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this,MainActivity.class));
//        finish();
        super.onBackPressed();
    }

    @Override
    public void onCatClick(int pos) {
        String catName = categoryModelArrayList.get(pos).getCategor_name();
        Intent intent = new Intent(this, sub_category_list.class);
        intent.putExtra("tabName",getIntent().getStringExtra("tabName"));
        intent.putExtra("CatName", catName);
        startActivity(intent);
        finish();
    }
}