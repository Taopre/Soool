package com.example.taopr.soool.Networking;

import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.QnaVoteItemResponse;


import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> enrollQnaNoImage(@Field("accountNo") int accountNo, @Field("accountNick") String accountNick, @Field("tag") String tag,
                                        @Field("qnaBoardTitle") String qnaTitle, @Field("qnaBoardContent") String qnaContent, @Field("qnaCate") String qnaCate);

    // QnaRecyclerview 리스트 아이템 받아오기
    @POST("/qnapost/QnaList.php")
    Observable<QnaBoardList> getQnaItem();

    // MypageList 객체로 받는 형식
    // 수정해야함 php 경로
    @POST("")
    Observable<QnaBoardList> getMypageBookmarkItem();

    @POST("/qnapost/QnaList.php")  // 테스트를 위해 임의로 작성
    Observable<QnaBoardList> getMypageBoardItem();

//    @FormUrlEncoded
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendQnaItem(@Body QnaItem qnaItem);

    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendQnaBoardItem(@Body QnaBoardItem qnaBoardItem);

    @Multipart
    @POST("qnapost/QnaWrite.php")
    Call<ResponseBody> sendTest(@Part("accountNo") int accountNo, @Part("accountNick") RequestBody accountNick, @Part("qnaCate") RequestBody qnaCate, @Part("tag") RequestBody tag,
                                @Part("title") RequestBody qnaTitle, @Part("content") RequestBody qnaContent, @Part("qnaVoteStatus") RequestBody qnaVoteStatus,
                                @Part ArrayList<MultipartBody.Part> file);
}
