package com.example.taopr.soool.Networking;

import com.example.taopr.soool.Object.PassFIndResponse;
import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Object.CommentListObject;
import com.example.taopr.soool.Object.InfoBookmark;
import com.example.taopr.soool.Object.InfoContent;
import com.example.taopr.soool.Object.InfoList;
import com.example.taopr.soool.Object.ProfileInfo;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaBoardList;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.SooolResponseBody;
import com.example.taopr.soool.Object.UserProfile;


import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    Call<ResponseBody> checkEmailDup(@Field("separator") int separator,
                                     @Field("accountEmail") String accountEmail);

    // 닉네임 중복 확인
    @FormUrlEncoded
    @POST("/Signup/overlapConfirm.php")
    Call<ResponseBody> checkNickDup(@Field("separator") int separator,
                                    @Field("accountNick") String accountNick);

    //  @GET("/answers?order=desc&sort=activity&site=stackoverflow")
    // Call<SOAnswersResponse> getAnswers(@Query("tagged") String tags);
    @FormUrlEncoded
    @POST("/Signup/Signup.php")
    Call<ResponseBody> signUpRes(@Field("accountEmail") String accountEmail,
                                 @Field("accountPW") String accountPW,
                                 @Field("accountNick") String accountNick);

    @FormUrlEncoded
    @POST("/Login/Login.php")
    Call<ResponseBody> getUserItem(@Field("accountEmail") String accountEmail,
                                   @Field("accountPW") String accountPW);

    @FormUrlEncoded
    @POST("/test/postWrite.php")
    Call<ResponseBody> enrollQna(@Field("accountNick") String accountNick,
                                 @Field("qnaTag") String qnaTag,
                                 @Field("qnaTitle") String qnaTitle,
                                 @Field("qnaContent") String qnaContent);

    @FormUrlEncoded
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> enrollQnaNoImage(@Field("accountNo") int accountNo,
                                        @Field("accountNick") String accountNick,
                                        @Field("tag") String tag,
                                        @Field("qnaBoardTitle") String qnaTitle,
                                        @Field("qnaBoardContent") String qnaContent,
                                        @Field("qnaCate") String qnaCate);

    // QnaRecyclerview 리스트 아이템 받아오기
    @FormUrlEncoded
    @POST("/qnapost/QnaList.php")
    Observable<QnaBoardList> getQnaItem(@Field("accountNo") int accountNo,
                                        @Field("lastPostNo") int lastPostNo);

    // MypageList 객체로 받는 형식
    // 수정해야함 php 경로
    @POST("")
    Observable<QnaBoardList> getMypageBookmarkItem();

    @POST("/qnapost/QnaList.php")  // 테스트를 위해 임의로 작성
    Observable<QnaBoardList> getMypageBoardItem();

//    @FormUrlEncoded
//    @POST("/qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendNoImageYesVoteText(@Body QnaItem qnaItem);

//    @POST("/qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendNoImageNoVote(@Body QnaItem qnaItem);

    @FormUrlEncoded
    @POST("/qnapost/QnaDetail.php")
    Call<ResponseBody> receiveVoteItem(@Field("accountNo") int accountNo,
                                       @Field("postNo") int postNo);

    @FormUrlEncoded
    @POST("/qnapost/voteResult.php")
    Call<ResponseBody> updateVoteResult(@Field("accountNo") int accountNo,
                                        @Field("postNo") int postNo,
                                        @Field("voteChoice") int voteChoice);

    @Multipart
//    @POST("qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendNoImageYesVoteImage(@Part("accountNo") int accountNo,
                                               @Part("qnaCate") int qnaCate,
                                               @Part("tag") RequestBody tag,
                                               @Part("title") RequestBody qnaTitle,
                                               @Part("content") RequestBody qnaContent,
                                               @Part("qnaVoteStatus") int qnaVoteStatus,
                                               @Part ArrayList<MultipartBody.Part> file);

    @Multipart
//    @POST("qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendYesImageYesVoteText(@Part("accountNo") int accountNo,
                                               @Part("qnaCate") int qnaCate,
                                               @Part("tag") RequestBody tag,
                                               @Part("title") RequestBody qnaTitle,
                                               @Part("content") RequestBody qnaContent,
                                               @Part MultipartBody.Part image,
                                               @Part("qnaVoteStatus") int qnaVoteStatus,
                                               @Part("voteText[]") ArrayList<String> voteText);

    @Multipart
//    @POST("qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendYesImageYesVoteImage(@Part("accountNo") int accountNo,
                                                @Part("qnaCate") int qnaCate,
                                                @Part("tag") RequestBody tag,
                                                @Part("title") RequestBody qnaTitle,
                                                @Part("content") RequestBody qnaContent,
                                                @Part MultipartBody.Part image,
                                                @Part("qnaVoteStatus") int qnaVoteStatus,
                                                @Part ArrayList<MultipartBody.Part> file);

    // 마이페이지 내 게시물
    @FormUrlEncoded
    @POST("/accountManage/mypost.php")
    Observable<QnaBoardList> getMypageBoardItem(@Field("accountNo") int accountNo,
                                                @Field("lastPostNo") int lastPostNo);

    // 마이페이지 유저 프로필
    @FormUrlEncoded
    @POST("/accountManage/myinfo.php")
    Observable<UserProfile> getUserProfile(@Field("accountNo") int accountNo);


    @Multipart
//    @POST("qnapost/QnaWrite.php")
    @POST("/qnapost/QnaWrite.php")
    Call<ResponseBody> sendYesImageNoVote(@Part("accountNo") int accountNo,
                                          @Part("qnaCate") int qnaCate,
                                          @Part("tag") RequestBody tag,
                                          @Part("title") RequestBody qnaTitle,
                                          @Part("content") RequestBody qnaContent,
                                          @Part MultipartBody.Part image,
                                          @Part("qnaVoteStatus") int qnaVoteStatus);

    @Multipart
//    @POST("qnapost/QnaWrite.php")
    @POST("/qnapost/QnaUpdate.php")
    Call<ResponseBody> sendYesImageNoVoteModify(@Part("postNo") int postNo,
                                                @Part("accountNo") int accountNo,
                                                @Part("qnaCate") int qnaCate,
                                                @Part("tag") RequestBody tag,
                                                @Part("title") RequestBody qnaTitle,
                                                @Part("content") RequestBody qnaContent,
                                                @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/qnapost/QnaUpdate.php")
    Call<ResponseBody> sendNoImageNoVoteModify(@Field("postNo") int postNo,
                                               @Field("accountNo") int accountNo,
                                               @Field("qnaCate") int qnaCate,
                                               @Field("tag") String tag,
                                               @Field("title") String qnaTitle,
                                               @Field("content") String qnaContent);

    @FormUrlEncoded
    @POST("/qnapost/QnaDelete.php")
    Call<ResponseBody> deleteBoardWithPostNo(@Field("postNo") int postNo);

    @FormUrlEncoded
    @POST("/accountManage/myinfoUpdate.php")
    Observable<ProfileInfo> getProfileInfo(@Field("accountNo") int accountNo);

    // 프로필 이미지 교환
    @Multipart
    @POST("/accountManage/accountImage.php")
    Observable<SooolResponseBody> changeProfileImage(@Part("accountNo") int accountNo,
                                                     @Part MultipartBody.Part accountImage );

    // 프로필 이미지 삭제
    @FormUrlEncoded
    @POST("/accountManage/accountImage.php")
    Observable<SooolResponseBody> deleteProfileImage(@Field("accountNo") int accountNo, @Field("delete") String delete);

    // 프로필 수정
    @FormUrlEncoded
    @POST("/accountManage/myinfoUpdateResult.php")
    Observable<ProfileInfo> changeProfileInfo(@Field("accountNo") int accountNo,
                                              @Field("accountEmail") String accountEmail,
                                              @Field("accountNick") String accountNick,
                                              @Field("accountImage") String accountImage,
                                              @Field("accountPw") String accountPw,
                                              @Field("capacity") int capacity,
                                              @Field("favorite") int favorite,
                                              @Field("reason") int reason);


    @FormUrlEncoded
    @POST("/qnapost/QnaPostLike.php")
    Call<ResponseBody> recommendOnOffReq(@Field("postNo") int postNo,
                                         @Field("accountNo") int accountNo,
                                         @Field("likeType") int likeType,
                                         @Field("btnOnOff") int btnOnOff);

    // 정보 게시물 리스트 아이템 받아오기(infoRecyclerView)
    @FormUrlEncoded
    @POST("/infoPost/infoList.php")
    Observable<InfoList> getInfoItem(@Field("accountNo") int accountNo,
                                     @Field("lastPostNo") int lastPostNo);


    // 정보 게시물 상세보기
    @FormUrlEncoded
    @POST("/infoPost/infoDetail.php")
    Observable<InfoContent> getInfoText(@Field("postNo") int postNo,
                                        @Field("accountNo") int accountNo);


    // 북마크 누를 때
    @FormUrlEncoded
    @POST("/infoPost/infoBookmark.php")
    Observable<InfoBookmark> updateBookmark(@Field("postNo") int postNo,
                                            @Field("accountNo") int accountNo);



    // 내 북마크 목록 받아오기
    @FormUrlEncoded
    @POST("/accountManage/myBookmark.php")
    Observable<InfoList> getMyBookmarkItem(@Field("accountNo") int accountNo);




    @FormUrlEncoded
    @POST("/passFind/passFind.php")
    Call<ResponseBody> checkEmailInPassFind(@Field("email") String email);


    @FormUrlEncoded
    @POST("/comment/commentinsert.php")
    Call<ResponseBody> commentRequest(@Field("postNo") int postNo,
                                      @Field("accountNo") int accountNo,
                                      @Field("commentContent") String commentContent);


    @FormUrlEncoded
    @POST("/comment/commentinsert.php")
    Call<ResponseBody> recommentRequest(@Field("postNo") int postNo,
                                        @Field("commentNo") int commentNo,
                                        @Field("accountNo") int accountNo,
                                        @Field("commentContent") String commentContent);

    @FormUrlEncoded
    @POST("/comment/commentLike.php")
    Call<ResponseBody> commentLikeRequest(@Field("postNo") int postNo,
                                          @Field("commentNo") int commentNo,
                                          @Field("accountNo") int accountNo,
                                          @Field("like_check") int like_check,
                                          @Field("commentORrecomment") int commentORrecomment,
                                          @Field("recommentNo") int recommentNo
    );

    @FormUrlEncoded
    @POST("/comment/commentDelete.php")
    Call<ResponseBody> commentDeleteRequest(@Field("postNo") int postNo,
                                            @Field("commentNo") int commentNo
                                            );

    @FormUrlEncoded
    @POST("/comment/commentList.php")//게시물 번호 쏴줘야됨
    Observable<CommentListObject> getCommentItem(@Field("postNo") int postNo);


    // 회원탈퇴
    @FormUrlEncoded
    @POST("/accountManage/accountDelete.php")
    Observable<SooolResponseBody> deleteAccount(@Field("accountNo") int accountNo);

    // Main 프래그먼트 Qna 리스트 아이템 받아오기
    @FormUrlEncoded
    @POST("/mainPost/mainPostQna.php")
    Observable<QnaBoardList> MainGetQnaItem(@Field("accountNo") int accountNo);

    // Main 프래그먼트 info 리스트 아이템 받아오기
    @FormUrlEncoded
    @POST("/qnapost/QnaList.php")
    Observable<QnaBoardList> MainGetInfoItem(@Field("accountNo") int accountNo,
                                            @Field("lastPostNo") int lastPostNo);
}