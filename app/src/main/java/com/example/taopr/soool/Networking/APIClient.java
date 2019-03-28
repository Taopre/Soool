package com.example.taopr.soool.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


     // 이 부분은 http상에서 나오는 에러를 로그를 찍어보기 위해서
     //   나중에 추가하면 좋을듯
     //   HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
     //   interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
     //   OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();

       Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.16.214.73")  //
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }
}
