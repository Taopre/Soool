package com.example.taopr.soool.Model;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnaBoardModel {

    String TAG = "QnaBoardModel", accountNick, result;
    int accountNo, voteExistence;

    private QnaBoardPresenter qnaBoardPresenter;

    Context context;

    public QnaBoardModel(QnaBoardPresenter qnaBoardPresenter, Context context) {
        this.qnaBoardPresenter = qnaBoardPresenter;
        this.context = context;
    }

    // QnaDrawUpActivity로 게시물 관련 데이터들을 객체로 받아서 서버로 저장하는 함수.

    public void enrollmentBoardReqFromView(QnaBoardItem item, QnaVoteItem qnaVoteItem) {

        // 쉐어드로부터 로그인한 사람의 닉네임 값을 불러내는 부분

        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNick = loginSessionItem.getAccountNick();
        accountNo = loginSessionItem.getAccountNo();
        Log.d(TAG, "enrollmentReqFromView: 닉네임"+accountNick);


        // 이미지의 유무로 조건을 걸어놓고
        // 이미지가 없을 땐 retrofit2으로 값을 넘겼고
        // 이미지가 있을 땐 okhttp3으로 값을 넘겨주었습니다.
        // 넘어감의 응답을 view로 뿌려주게 마무리하였습니다.

        // 4/18 업데이트 해야할 것 정리
        // 1. 이미지가 있는지 없는지 체크해야함
        // 2. 투표가 있는지 없는지 체크해야함 (투표가 있다면 voteExistence = 0; 없다면 voteExistence = 1;) -> qnaBoardItem.getQnaCate가 이거였어서 다시 변경했습니다.
        // 3. 투표가 이미지 or 텍스트인지 체크해야함
        // 4. 투표 객체도 서버에 저장되도록 디버깅해야함
        // 5. 투표가 있는 경우, view쪽에 보내줄 메서드에 투표 존재유무를 보내줘야함

        /*

        @@@@@@@@@@@@@@@@@@@앞으로 처리해야할 사항@@@@@@@@@@@@@@@@@@@@@@@

        1. 위의 예외처리에 맞게 통신부분 적절하게 넣어서 디버깅하기.
        2. 통신에서 식별자를 넣어서 view에서 처리하게 해주자

        2번의 경우를 풀어서 설명하자면
        qnaBoardPresenter.enrollmentBoardRes 이 함수에서 실패의 경우가 여러가지가 된다.
        a. 통신이 되었지만 insert가 실패해서 Fail인 경우
        b. 통신이 실패해서 Fail인 경우

        더 나아가 어느 경우의 Fail인지 (이미지없는 경우 or 이미지 있는경우 or 투표있고 없고 등등등) 식별해서
        view에서 유저한테 메시지 예외처리 어떻게 할지 처리해야한다.

         */

        if(item.getImage() == null) {
            if (item.getQnaCate().equals("yes vote")) {
                if (qnaVoteItem.qnaVoteStatus.equals("text")) {

                } else if (qnaVoteItem.qnaVoteStatus.equals("image")) {

                }
            } else if (item.getQnaCate().equals("no vote")) {

            }

            Observable.just("")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, Boolean>()
                    {
                        @Override
                        public Boolean apply(String s) throws Exception
                        {
                            try
                            {
                                Log.d(TAG, "시작 바로 전");
                                //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                                //Retrofit 객체 불러와서 선언하는 부분.
                                APIService service = APIClient.getClient().create(APIService.class);

                                //Call함수로 LoginActivity(view)로부터 받은 인자를 서버로 넘기는 부분.
                                Call<ResponseBody> callServer = service.enrollQnaNoImage(accountNick, item.getTag(), item.getTitle(), item.getContent());

                                //서버로 부터 응답을 받는 부분.
                                callServer.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            String msg = response.body().string();
                                            Log.d(TAG, "onResponse 이미지 없을때 : " + msg);
                                            JSONArray jsonArray = new JSONArray(msg);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject returnData = jsonArray.getJSONObject(i);

                                                result = returnData.getString("result");
                                                if(result.equals("true")){
                                                    Log.d(TAG, "onResponse: 인서트 성공");
                                                    qnaBoardPresenter.enrollmentBoardResp(true, item.getQnaCate());
                                                }else {
                                                    Log.d(TAG, "onResponse: 인서트 실패");
                                                    qnaBoardPresenter.enrollmentBoardResp(false, item.getQnaCate());
                                                }
                                            }
                                        }catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        Log.d(TAG, "onFailure: 실패");
                                        Log.e(TAG, "onFailure: ", t);
                                    }
                                });
                            }
                            catch(Exception e) {
                                Log.d(TAG, "onFailure: 실패2");
                                Log.e(TAG, "apply: ", e);
                            }

                            return true;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            Log.d(TAG, "onSubscribe : 구독!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                        @Override
                        public void onNext(Boolean s)
                        {
                            Log.d(TAG, "onNext: 다음!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError : 에러발생!!!!!!!!!!!!!!!!!!",e);
                        }
                        @Override
                        public void onComplete()
                        {
                            Log.d(TAG, "onComplete : 완료!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    });
        }else {
            if (item.getQnaCate().equals("yes vote")) {
                if (qnaVoteItem.qnaVoteStatus.equals("text")) {

                } else if (qnaVoteItem.qnaVoteStatus.equals("image")) {

                }
            } else if (item.getQnaCate().equals("no vote")) {

            }

            File file = new File(item.getImage());

            Observable.just("")
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, Boolean>()
                    {
                        @Override
                        public Boolean apply(String s) throws Exception
                        {
                            try
                            {
                                Log.d(TAG, "시작 바로 전");

                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("accountNo", accountNo+"")
                                        .addFormDataPart("accountNick", accountNick)
                                        .addFormDataPart("qnaBoardTag", item.getTag())
                                        .addFormDataPart("qnaBoardTitle", item.getTitle())
                                        .addFormDataPart("qnaBoardContent", item.getContent())
                                        .addFormDataPart("qnaBoardImage",file.getName(), RequestBody.create(MultipartBody.FORM, file))
                                        .build();

                                Request request = new Request.Builder()
                                        .url("http://54.180.90.184/test/postWrite.php")
                                        .post(requestBody)
                                        .build();

                                OkHttpClient client = new OkHttpClient();

                                client.newCall(request).enqueue(new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(okhttp3.Call call, IOException e) {
                                        Log.d(TAG, "onFailure: ",e);
                                    }

                                    @Override
                                    public void onResponse(okhttp3.Call call, okhttp3.Response response){
                                        try {
                                            String msg = response.body().string();
                                            Log.d(TAG, "onResponse okhttp: " + msg);
                                            JSONArray jsonArray = new JSONArray(msg);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject returnData = jsonArray.getJSONObject(i);

                                                result = returnData.getString("result");

                                                if(result.equals("true")){
                                                    Log.d(TAG, "onResponse: 인서트 성공");
                                                    qnaBoardPresenter.enrollmentBoardResp(true, item.getQnaCate());
                                                }else {
                                                    Log.d(TAG, "onResponse: 인서트 실패");
                                                    qnaBoardPresenter.enrollmentBoardResp(false, item.getQnaCate());
                                                }
                                            }
                                        }catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            catch(Exception e) {
                                Log.d(TAG, "onFailure : 실패2");
                                Log.e(TAG, "apply : ", e);
                            }

                            return true;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            Log.d(TAG, "onSubscribe 이미지 있을때 이미지뺴고 보내는 부분: 구독!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                        @Override
                        public void onNext(Boolean s)
                        {
                            Log.d(TAG, "onNext 이미지 있을때 이미지뺴고 보내는 부분: 다음!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError 이미지 있을때 이미지뺴고 보내는 부분: 에러발생!!!!!!!!!!!!!!!!!!",e);
                        }
                        @Override
                        public void onComplete()
                        {
                            Log.d(TAG, "onComplete 이미지 있을때 이미지뺴고 보내는 부분: 완료!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    });
        }
    }
}
