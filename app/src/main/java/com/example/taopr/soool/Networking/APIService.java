package com.example.taopr.soool.Networking;

import com.example.taopr.soool.R;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.google.gson.JsonArray;

import org.json.JSONArray;

public interface APIService {

   // public static final String API_URL="3.16.214.73/";

    // 이메일 중복 확인
    @GET("/Signup.php")
    Call<ResponseBody> checkEmailDup(@Query("accountEmail") String accountEmail);

    // 닉네임 중복 확인
    @GET("/test.php?separator=1&")
    Call<JsonArray> checkNickDup(@Field("accountNick") String accountNick);

  //  @GET("/answers?order=desc&sort=activity&site=stackoverflow")
   // Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);

    @GET("/Signup.php")
    Call<JSONArray> signUpRes(@Query("accountEmail") String accountEmail, @Query("accountPW") String accountPW,
                              @Query("accountNick") String accountNick);

    @FormUrlEncoded
    @POST("/Login/Login.php")
    Call<ResponseBody> getUserItem(@Field("accountEmail") String accountEmail, @Field("accountPW") String accountPW);
}
