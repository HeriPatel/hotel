package com.example.myapplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIinterface {
    @GET("table_details.php")
    Call<ArrayList<tabStatusModel>> getTables();

    @GET("loadcategories.php")
    Call<ArrayList<categoryModel>> getCategory();

    @GET("loadsubcategories.php")
    Call<ArrayList<subCategoryModel>> getSubCategory(@Query("catName") String catName);

    @GET("loaditems.php")
    Call<ArrayList<itemsModel>> getItems(@Query("subCatName") String subCatName);

    @GET("add_order.php")
    Call<String> addOrder(@Query("itemName") String itemName, @Query("tableName") String tableName, @Query("itemQnty") String itemQnty, @Query("itemNotes") String itemNotes);

    @GET("load_order_details.php")
    Call<ArrayList<itemsModel>> getOrder(@Query("tabName") String tabName);

    @GET("update_table_status.php")
    Call<String> changeTabStatus(@Query("tabName") String tabName,@Query("tabStatus") String tabStatus);

    @GET("complete_order.php")
    Call<String> completeOrder(@Query("tabName") String tabName);

    @GET("cancel_order.php")
    Call<String> cancelOrdr(@Query("id") int id);
}