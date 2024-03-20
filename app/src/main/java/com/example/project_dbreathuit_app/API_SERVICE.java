package com.example.project_dbreathuit_app;


import com.example.project_dbreathuit_app.model.Asset;
import com.example.project_dbreathuit_app.model.LoginResponseModel;
import com.example.project_dbreathuit_app.model.UserResponseModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface API_SERVICE {
    @FormUrlEncoded
    @POST("token") // Replace "login" with your API endpoint
    Call<LoginResponseModel> login(
            @Field("client_id") String client_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );

    @GET("api/master/user/user")
    Call<UserResponseModel> getUser();//, @Header("Authorization") String auth);
    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);//, @Header("Authorization") String auth);
    @GET("https://uiot.ixxc.dev/manager/#/insights/")
    Call<YourDataModel> getChartData();
//    @GET("api/master/asset/{assetID}")
//    Call<List<Asset>> getAssetsWithDateRange(
//            @Path("assetID") String assetID,
//            @QueryMap Map<String, String> dateRangeParams
//    );
}
