package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class sub_category_list extends AppCompatActivity implements subCategoryAdapter.onSubCatClickListener {
    TextView txt;
    String CatName;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private APIinterface apIinterface;
    private ArrayList<subCategoryModel> subCategoryModelArrayList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_list);
        txt = findViewById(R.id.txtVw);
        CatName = getIntent().getStringExtra("CatName");
        txt.setText(CatName);

        recyclerView = findViewById(R.id.subCatRV);
        subCategoryModelArrayList = new ArrayList<>();

        //LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        retrofit = myRetro.getretrofit(this);
        apIinterface = retrofit.create(APIinterface.class);
        Call<ArrayList<subCategoryModel>> c = apIinterface.getSubCategory(CatName);
        c.enqueue(new Callback<ArrayList<subCategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<subCategoryModel>> call, Response<ArrayList<subCategoryModel>> response) {
                subCategoryModelArrayList = response.body();
                subCategoryAdapter subCategoryAdapter = new subCategoryAdapter(getApplicationContext(), subCategoryModelArrayList, sub_category_list.this);
                recyclerView.setAdapter(subCategoryAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<subCategoryModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(sub_category_list.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Sub-Category List : " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,category_list.class));
        finish();
        super.onBackPressed();
    }
    @Override
    public void onSubCatClick(int pos) {
        String subCatName = subCategoryModelArrayList.get(pos).getSubCatName();
        Intent intent = new Intent(this,items.class);
        intent.putExtra("tabName",getIntent().getStringExtra("tabName"));
        intent.putExtra("subCatName",subCatName);
        startActivity(intent);
    }
}