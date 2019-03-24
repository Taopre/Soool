package com.example.taopr.soool.Model;

import android.util.Log;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.google.gson.JsonArray;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpModel {

    String TAG = "SignUpModel";

    private static String emailOrNick;
    private static APIClient apiClient;

    // presenter를 통해 전달 받은 구분자 값과 이메일 혹은 닉네임 값을 서버에 전달.
    // 1. retrofit 객체를 생성
    // 2. restAPI 명세에 맞는 인터페이스 생성
    // 서버는 중복여부에 관한 result값을 전달 받고


    public boolean checkDuplicity(int separator, String emailOrNick) {

        Log.i(TAG, "checkDuplicity: 구분자 값 : " + separator + " , 중복체크 값 : " + emailOrNick);

        APIService service = APIClient.getClient().create(APIService.class);
        Call<ResponseBody> request = null;

        if(separator == 0){
            Log.i(TAG, "checkDuplicity: 00");
           request = service.checkEmailDup(emailOrNick);
        }

        else{
            Log.i(TAG, "checkDuplicity: 111");
           // request = service.checkNickDup(emailOrNick);
        }

        /// jsonarray방식으로 받아올때는 Call<JsonArray>로 해야함
       /* request.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                // Code...
                Log.i(TAG, "onResponse: 결과 값 :" + response.message());
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.i(TAG, "onFailure: 에러 :" + t.toString());
                // Code...
            }
        });*/

        // Callback 데이터 구조가 JsonArray 구조가 아니기 때문에 ResponseBody로 변경해놈
        // 다시 수정해줘야함

        /*테스트 위해서 잠시 주석
       request.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try {
                   Log.i(TAG, "onResponse: 결과 값 :" + response.message() + response.body().string());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
               Log.i(TAG, "onFailure: 에러 :" + t.toString());
           }
        });*/

        return true;
    }

    public boolean signUpReq(String accountEmail, String accountPW, String accountNick){

        return true;
    }

}
