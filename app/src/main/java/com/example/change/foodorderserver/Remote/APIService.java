package com.example.change.foodorderserver.Remote;


import com.example.change.foodorderserver.Model.FBResponse;
import com.example.change.foodorderserver.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(

            {"Content-Type:application/json",
                    "Authorization:key=AAAAz5i1Dbc:APA91bE-hB-_oa9mqQnyAyKIl2RM77zgTuctbH_LuAXaM_mJSHuaBa5M6rQM6m3hPvz3Z_JZAz7ffn7nmr69TNSQQiJcoXJuIp2cKveOMoHi8NH_IIr2_9m7OgBWp13gxarlrLBbcQbJ"

            }
    )
    @POST("fcm/send")
    Call<FBResponse> sendNotification(@Body Sender body);


}
