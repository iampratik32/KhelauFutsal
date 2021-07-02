package com.example.khelaufutsal.Futsal;

import com.example.khelaufutsal.Notification.MyResponse;
import com.example.khelaufutsal.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public  interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAoLRq3pA:APA91bHPO54K87twjzJ_MlONtE5zYnMxVYNN8zXMXV8ZRBAqtbKy09EvIVANzpWL1Aju3aTs_Q5hS6wbNPcDqECmWOA5ushOIAZK3YMr0fiHazMhcWVAdWk9dKVsauAbR5tKW2a5wbJ0"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
