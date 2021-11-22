package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements tabStatusAdapter.onTabClickListener{

    Retrofit retrofit;
    SliderLayout sliderLayout;
    int[] imgSldr;
    APIinterface apIinterface;
    private RecyclerView rvTab;
    private ArrayList<tabStatusModel> tabStatusModelArrayList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTab = findViewById(R.id.tabStatusRV);
        sliderLayout = findViewById(R.id.imgSlider);
        imgSldr = new int[]{R.drawable.food1, R.drawable.food2, R.drawable.food3, R.drawable.food4};
        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.DROP);
        sliderLayout.setScrollTimeInSec(3);

        tabStatusModelArrayList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvTab.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait!");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        for (int i = 0; i < imgSldr.length; i++) {
            SliderView sliderView = new SliderView(this);
            sliderView.setImageDrawable(imgSldr[i]);
            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
            sliderLayout.addSliderView(sliderView);
        }

        retrofit = myRetro.getretrofit(getResources().getString(R.string.url));
        apIinterface = retrofit.create(APIinterface.class);
        Call<ArrayList<tabStatusModel>> c = apIinterface.getTables();
        c.enqueue(new Callback<ArrayList<tabStatusModel>>() {
            @Override
            public void onResponse(Call<ArrayList<tabStatusModel>> call, Response<ArrayList<tabStatusModel>> response) {
                tabStatusModelArrayList = response.body();
                tabStatusAdapter adapter = new tabStatusAdapter(getApplicationContext(), tabStatusModelArrayList, MainActivity.this);
                rvTab.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<tabStatusModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d("gilog", "Error in table status : " + t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public void onTabClick(int position) {
        String name = tabStatusModelArrayList.get(position).getTabName();
        String status = tabStatusModelArrayList.get(position).getTabStatus();
        if (status.equals("Available")) {
            Intent intent = new Intent(this, category_list.class);
            intent.putExtra("tabName", name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, orderScreen.class);
            intent.putExtra("tabName", name);
            startActivity(intent);
            finish();
        }
    }
}