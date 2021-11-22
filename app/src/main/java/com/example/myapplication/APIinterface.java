package com.example.myapplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    Call<String> changeTabStatus(@Query("tabName") String tabName, @Query("tabStatus") String tabStatus);

    @GET("complete_order.php")
    Call<String> completeOrder(@Query("tabName") String tabName);

    @GET("cancel_order.php")
    Call<String> cancelOrdr(@Query("id") int id);

    @POST("get_available_chef.php")
    Call<String> getChefId();

    @POST("addChef.php")
    Call<String> addChef(@Query("id") int id, @Query("chef_id") int chef_id);

    @POST("change_chef_status.php")
    Call<String> changeChefStatus(@Query("id") int id,@Query("status") String status);

    @POST("add_dummy_order.php")
    Call<String> addDummyOrder(@Query("id") int id);

    //Notification
    //to send notification

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAYx-XAIU:APA91bG4aZ9xbm-L39o08mrhpu4oe4MdbanN9kW_3AgNh_4jJ4HgYwzt6QB1pJRwMY4_Y2_DAO5x2XH7wgp4TWby37jVsxFDqTykyfHR_ULlrC7h0aAsrKF_jUCINqehjW6G_ijmxJkc"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
    //to read token from database

    @Headers("Content-Type: application/json")
    @GET("get_key.php")
    Call<String> getKey(@Query("id") Integer id);
}