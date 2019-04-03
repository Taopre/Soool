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

import java.io.File;

public interface APIService {

    // public static final String API_URL="3.16.214.73/";

    // 이메일 중복 확인
    @FormUrlEncoded
    @POST("/Signup/overlapConfirm.php")
    Call<ResponseBody> checkEmailDup(@Field("accountEmail") String accountEmail);

    // 닉네임 중복 확인
    @FormUrlEncoded
    @POST("/Signup/overlapConfirm.php")
    Call<ResponseBody> checkNickDup(@Field("accountNick") String accountNick);

    //  @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    // Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
    @FormUrlEncoded
    @POST("/Signup/Signup.php")
    Call<ResponseBody> signUpRes(@Field("accountEmail") String accountEmail, @Field("accountPW") String accountPW,
                                 @Field("accountNick") String accountNick);

    @FormUrlEncoded
    @POST("/Login/Login.php")
    Call<ResponseBody> getUserItem(@Field("accountEmail") String accountEmail, @Field("accountPW") String accountPW);

    @FormUrlEncoded
    @POST("/qnapost/postWrite.php")
    Call<ResponseBody> enrollQna(@Field("accountNick") String accountNick,@Field("qnaTag") String qnaTag, @Field("qnaTitle") String qnaTitle,
                                 @Field("qnaContent") String qnaContent, @Field("qnaImage") File qnaImage);

    @FormUrlEncoded
    @POST("/qnapost/postWrite.php")
    Call<ResponseBody> enrollQnaNoImage(@Field("accountNick") String accountNick, @Field("qnaTag") String qnaTag,
                                        @Field("qnaTitle") String qnaTitle, @Field("qnaContent") String qnaContent);
}
