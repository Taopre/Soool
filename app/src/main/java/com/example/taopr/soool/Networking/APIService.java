package com.example.taopr.soool.Networking;

import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.R;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.io.File;
import java.util.Map;

public interface APIService {
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
    @POST("/test/postWrite.php")
    Call<ResponseBody> enrollQna(@Field("accountNick") String accountNick,@Field("qnaTag") String qnaTag, @Field("qnaTitle") String qnaTitle,
                                 @Field("qnaContent") String qnaContent);

    @FormUrlEncoded
    @POST("/test/postWrite.php")
    Call<ResponseBody> enrollQnaNoImage(@Field("accountNick") String accountNick, @Field("qnaBoardTag") String qnaTag,
                                        @Field("qnaBoardTitle") String qnaTitle, @Field("qnaBoardContent") String qnaContent);

    // QnaRecyclerview 리스트 아이템 받아오기
    @POST("/qnapost/QnaList.php")
    Observable<QnaBoardList> getQnaItem();
}
