//package com.example.taopr.soool.Networking;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.POST;
//
//public interface LoginNetworkInter {
//    //retrofit 인터페이스
//    @FormUrlEncoded
//    @POST("/Login/Login.php")
//    Call<ResponseBody> getUserItem(@Field("accountEmail") String accountEmail, @Field("accountPW") String accountPW);
////    @FormUrlEncoded
////    @POST("/loginTest.php")
////    Call<ResponseBody> getUserItem(@Field("accountEmail") String accountEmail, @Field("accountPW") String accountPW);
//}