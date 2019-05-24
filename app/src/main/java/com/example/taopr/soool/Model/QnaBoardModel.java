package com.example.taopr.soool.Model;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.LoginSessionItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Object.QnaItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.ResponseTest;
import com.example.taopr.soool.Presenter.QnaBoardPresenter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnaBoardModel {

    private APIService apiService;
    File file;

    String TAG = "QnaBoardModel", result;
    int accountNo, voteExistence;

    ArrayList<MultipartBody.Part> imageParts = new ArrayList<>();

    private QnaBoardPresenter qnaBoardPresenter;
    QnaBoardItem modelQnaBoardItem;

    Context context;

    public QnaBoardModel(QnaBoardPresenter qnaBoardPresenter, Context context) {
        this.qnaBoardPresenter = qnaBoardPresenter;
        this.context = context;
    }

    public void deleteBoardReqFromView(int deletePostNo) {
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
                            APIService service = APIClient.getClient1().create(APIService.class);
                            Call<ResponseBody> callServer = service.deleteBoardWithPostNo(deletePostNo);

                            callServer.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String msg = response.body().string();
                                        Log.d(TAG, "onResponse okhttp: " + msg);
                                        msg = msg.replace("[","");
                                        msg = msg.replace("]","");

                                        Gson gsonObject = new Gson();
                                        ResponseTest responseTest = gsonObject.fromJson(msg, ResponseTest.class);
                                        
                                        if (responseTest.isResult().equals("true")) {
                                            Log.d(TAG, "onResponse: 삭제 true");
                                            qnaBoardPresenter.deleteBoardResp(0);
                                        } else {
                                            Log.d(TAG, "onResponse: 삭제 false");
                                        }
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    // 콜백 실패
                                    qnaBoardPresenter.deleteBoardResp(1);
                                    Log.d(TAG, "onFailure: 실패");
                                    Log.e(TAG, "onFailure: ", t);
                                }
                            });
                        }
                        catch(Exception e) {
                            // 통신 실패
                            qnaBoardPresenter.deleteBoardResp(2);
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
                        Log.d(TAG, "onSubscribe "+d);
                    }
                    @Override
                    public void onNext(Boolean s)
                    {
                        Log.d(TAG, "onNext "+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError ",e);
                    }
                    @Override
                    public void onComplete()
                    {

                    }
                });
    }

    public void modifyBoardReqFromView(QnaBoardItem qnaBoardItem) {
        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        Log.d(TAG, "enrollmentReqFromView: 닉네임"+accountNo);

        Log.d(TAG, "modifyBoardReqFromView:블라블라 "+qnaBoardItem.getTag());

        try {
            file = new File(qnaBoardItem.getImage());

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


                                RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), qnaBoardItem.getTag());
                                RequestBody title = RequestBody.create(MediaType.parse("text/plain"), qnaBoardItem.getTitle());
                                RequestBody content = RequestBody.create(MediaType.parse("text/plain"), qnaBoardItem.getContent());
                                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file );
                                MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), imageBody);

                                APIService service = APIClient.getClient1().create(APIService.class);
                                Call<ResponseBody> callServer = service.sendYesImageNoVoteModify(
                                        qnaBoardItem.getPostNo(), accountNo, qnaBoardItem.getQnaCate(), tag, title, content, part);

                                callServer.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            String msg = response.body().string();
                                            msg = msg.replace("[","");
                                            msg = msg.replace("]","");

                                            Log.d(TAG, "onResponse okhttp: " + msg);

                                            Gson gsonObject = new Gson();
                                            modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                            qnaBoardPresenter.modifyBoardResp(0, modelQnaBoardItem);

                                        }catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        qnaBoardPresenter.modifyBoardResp(1, modelQnaBoardItem);
                                        Log.d(TAG, "onFailure: 실패");
                                        Log.e(TAG, "onFailure: ", t);
                                    }
                                });
                            }
                            catch(Exception e) {
                                qnaBoardPresenter.modifyBoardResp(2, modelQnaBoardItem);
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
                            Log.d(TAG, "onSubscribe "+d);
                        }
                        @Override
                        public void onNext(Boolean s)
                        {
                            Log.d(TAG, "onNext "+s);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError ",e);
                        }
                        @Override
                        public void onComplete()
                        {

                        }
                    });
        }catch (NullPointerException e) {
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
                                APIService service = APIClient.getClient1().create(APIService.class);
                                Call<ResponseBody> callServer = service.sendNoImageNoVoteModify(
                                        qnaBoardItem.getPostNo(), accountNo, qnaBoardItem.getQnaCate(), qnaBoardItem.getTag(), qnaBoardItem.getTitle(), qnaBoardItem.getContent());

                                callServer.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        try {
                                            String msg = response.body().string();
//                                            msg = msg.replace("[","");
//                                            msg = msg.replace("]","");

                                            Log.d(TAG, "onResponse okhttp: " + msg);

                                            Gson gsonObject = new Gson();
                                            modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                            qnaBoardPresenter.modifyBoardResp(0, modelQnaBoardItem);
                                        }catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        // 콜백 실패
                                        qnaBoardPresenter.modifyBoardResp(1, modelQnaBoardItem);
                                        Log.d(TAG, "onFailure: 실패");
                                        Log.e(TAG, "onFailure: ", t);
                                    }
                                });
                            }
                            catch(Exception e) {
                                // 통신 실패
                                qnaBoardPresenter.modifyBoardResp(2, modelQnaBoardItem);
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
                            Log.d(TAG, "onSubscribe "+d);
                        }
                        @Override
                        public void onNext(Boolean s)
                        {
                            Log.d(TAG, "onNext "+s);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError ",e);
                        }
                        @Override
                        public void onComplete()
                        {

                        }
                    });
        }
    }

    public void enrollmentBoardReqFromView(QnaItem qnaItem) {

        // 쉐어드로부터 로그인한 사람의 닉네임 값을 불러내는 부분

        String data = LoginSharedPreferences.LoginUserLoad(context, "LoginAccount");
        Gson gson = new GsonBuilder().create();
        // JSON 으로 변환
        LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);
        accountNo = loginSessionItem.getAccountNo();
        Log.d(TAG, "enrollmentReqFromView: 닉네임"+accountNo);


        // 이미지의 유무로 조건을 걸어놓고
        // 이미지가 없을 땐 retrofit2으로 값을 넘겼고
        // 이미지가 있을 땐 okhttp3으로 값을 넘겨주었습니다.
        // 넘어감의 응답을 view로 뿌려주게 마무리하였습니다.

        // 4/18 업데이트 해야할 것 정리
        // 1. 이미지가 있는지 없는지 체크해야함
        // 2. 투표가 있는지 없는지 체크해야함 (투표가 있다면 voteExistence = 0; 없다면 voteExistence = 1;)
        // -> qnaBoardItem.getQnaCate가 이거였어서 다시 변경했습니다.
        // -> getQnaCate 0 : 투표있음 1: 투표없음
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

        /*

        네트워크 예외처리

        1 -> 인서트 성공
        2 -> 인서트 실패
        3 -> 콜백 실패
        4 -> 전송 실패

         */

        if(qnaItem.getImage() == null) {
            if (qnaItem.getQnaCate() == 0) {
                if (qnaItem.qnaVoteStatus == 0) {
                    if (qnaItem != null) {
                        
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
                                            APIService service = APIClient.getClient1().create(APIService.class);
                                            Call<ResponseBody> callServer = service.sendNoImageYesVoteText(qnaItem);

                                            callServer.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        String msg = response.body().string();

                                                        msg = msg.replace("[","");
                                                        msg = msg.replace("]","");

                                                        Log.d(TAG, "onResponse okhttp: " + msg);

                                                        Gson gsonObject = new Gson();
                                                        modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                        Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                        qnaBoardPresenter.enrollmentBoardResp(
                                                                1, modelQnaBoardItem);

                                                    }catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    qnaBoardPresenter.enrollmentBoardResp(
                                                            2, modelQnaBoardItem);
                                                    Log.d(TAG, "onFailure: 실패");
                                                    Log.e(TAG, "onFailure: ", t);
                                                }
                                            });
                                        }
                                        catch(Exception e) {
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    3, modelQnaBoardItem);
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
                    }
                } else if (qnaItem.qnaVoteStatus == 1) {
                    if (qnaItem != null) {
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

                                            Log.d(TAG, "apply: "+qnaItem.voteImage.size());

                                            for (int i=0; i<qnaItem.voteImage.size(); i++) {
                                                File image = new File(qnaItem.voteImage.get(i));
                                                Log.d(TAG, "apply: "+qnaItem.voteImage.get(i));
                                                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), image);
                                                imageParts.add(MultipartBody.Part.createFormData("voteImages[]", image.getName(), surveyBody));
                                                Log.d(TAG, "apply: "+imageParts.get(i));
                                            }
                                            RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTag());
                                            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTitle());
                                            RequestBody content = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getContent());

                                            APIService service = APIClient.getClient1().create(APIService.class);
                                            Call<ResponseBody> callServer = service.sendNoImageYesVoteImage(
                                                    accountNo, qnaItem.getQnaCate(), tag, title,
                                                    content, qnaItem.getQnaVoteStatus(), imageParts);

                                            callServer.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        String msg = response.body().string();

                                                        msg = msg.replace("[","");
                                                        msg = msg.replace("]","");

                                                        Log.d(TAG, "onResponse okhttp: " + msg);

                                                        Gson gsonObject = new Gson();
                                                        modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                        Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                        qnaBoardPresenter.enrollmentBoardResp(
                                                                1, modelQnaBoardItem);
                                                    }catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    qnaBoardPresenter.enrollmentBoardResp(
                                                            2, modelQnaBoardItem);
                                                    Log.d(TAG, "onFailure: 실패");
                                                    Log.e(TAG, "onFailure: ", t);
                                                }
                                            });

                                        }
                                        catch(Exception e) {
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    3, modelQnaBoardItem);
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
                    }
                }
            } else if (qnaItem.getQnaCate() == 1) {
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
                                    APIService service = APIClient.getClient1().create(APIService.class);
                                    Call<ResponseBody> callServer = service.sendNoImageNoVote(qnaItem);

                                    callServer.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            try {
                                                String msg = response.body().string();
                                                msg = msg.replace("[","");
                                                msg = msg.replace("]","");

                                                Log.d(TAG, "onResponse okhttp: " + msg);

                                                Gson gsonObject = new Gson();
                                                modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                qnaBoardPresenter.enrollmentBoardResp(
                                                        1, modelQnaBoardItem);
                                            }catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            // 콜백 실패
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    2, modelQnaBoardItem);
                                            Log.d(TAG, "onFailure: 실패");
                                            Log.e(TAG, "onFailure: ", t);
                                        }
                                    });
                                }
                                catch(Exception e) {
                                    // 통신 실패
                                    qnaBoardPresenter.enrollmentBoardResp(
                                            3, modelQnaBoardItem);
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
            }


        }else {
            // 게시물 이미지 있을때
            file = new File(qnaItem.getImage());

            if (qnaItem.getQnaCate() == 0) {
                if (qnaItem.qnaVoteStatus == 0) {
                    if (qnaItem != null) {
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

                                            RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTag());
                                            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTitle());
                                            RequestBody content = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getContent());
                                            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file );
                                            MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(), imageBody);

                                            APIService service = APIClient.getClient1().create(APIService.class);
                                            Call<ResponseBody> callServer = service.sendYesImageYesVoteText(
                                                    accountNo, qnaItem.getQnaCate(), tag, title, content, part, qnaItem.getQnaVoteStatus(), qnaItem.voteText);

                                            callServer.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        String msg = response.body().string();
                                                        msg = msg.replace("[","");
                                                        msg = msg.replace("]","");

                                                        Log.d(TAG, "onResponse okhttp: " + msg);

                                                        Gson gsonObject = new Gson();
                                                        modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                        Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                        qnaBoardPresenter.enrollmentBoardResp(
                                                                1, modelQnaBoardItem);
                                                    }catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    qnaBoardPresenter.enrollmentBoardResp(
                                                            2, modelQnaBoardItem);
                                                    Log.d(TAG, "onFailure: 실패");
                                                    Log.e(TAG, "onFailure: ", t);
                                                }
                                            });
                                        }
                                        catch(Exception e) {
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    3, modelQnaBoardItem);
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
                    }
                } else if (qnaItem.qnaVoteStatus == 1) {
                    if (qnaItem != null) {
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

                                            RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTag());
                                            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTitle());
                                            RequestBody content = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getContent());
                                            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file );
                                            MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(), imageBody);

                                            for (int i=0; i<qnaItem.voteImage.size(); i++) {
                                                File image = new File(qnaItem.voteImage.get(i));
                                                Log.d(TAG, "apply: "+qnaItem.voteImage.get(i));
                                                RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), image);
                                                imageParts.add(MultipartBody.Part.createFormData("voteImages[]", image.getName(), surveyBody));
                                                Log.d(TAG, "apply: "+imageParts.get(i));
                                            }

                                            APIService service = APIClient.getClient1().create(APIService.class);
                                            Call<ResponseBody> callServer = service.sendYesImageYesVoteImage(
                                                    accountNo, qnaItem.getQnaCate(), tag, title, content, part, qnaItem.getQnaVoteStatus(), imageParts);

                                            callServer.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        String msg = response.body().string();
                                                        msg = msg.replace("[","");
                                                        msg = msg.replace("]","");

                                                        Log.d(TAG, "onResponse okhttp: " + msg);

                                                        Gson gsonObject = new Gson();
                                                        modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                        Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                        qnaBoardPresenter.enrollmentBoardResp(
                                                                1, modelQnaBoardItem);
                                                    }catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    qnaBoardPresenter.enrollmentBoardResp(
                                                            2, modelQnaBoardItem);
                                                    Log.d(TAG, "onFailure: 실패");
                                                    Log.e(TAG, "onFailure: ", t);
                                                }
                                            });
                                        }
                                        catch(Exception e) {
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    3, modelQnaBoardItem);
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
                    }
                }
            } else if (qnaItem.getQnaCate() == 1) {
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
                                    Log.d(TAG, "apply: asdfasdfasdf"+qnaItem.getTag()+qnaItem.getQnaCate()+qnaItem.title+qnaItem.getContent()+file.getName()+file);
                                    //Retrofit 사용 시 apiservice와 apiclient를 사용하자.
                                    //Retrofit 객체 불러와서 선언하는 부분.

                                    RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTag());
                                    RequestBody title = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getTitle());
                                    RequestBody content = RequestBody.create(MediaType.parse("text/plain"), qnaItem.getContent());
                                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file );
                                    MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), imageBody);

                                    APIService service = APIClient.getClient1().create(APIService.class);
                                    Call<ResponseBody> callServer = service.sendYesImageNoVote(
                                            accountNo, qnaItem.getQnaCate(), tag, title, content, part, qnaItem.getQnaVoteStatus());

                                    callServer.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            try {
                                                String msg = response.body().string();
                                                msg = msg.replace("[","");
                                                msg = msg.replace("]","");

                                                Log.d(TAG, "onResponse okhttp: " + msg);

                                                Gson gsonObject = new Gson();
                                                modelQnaBoardItem = gsonObject.fromJson(msg, QnaBoardItem.class);
//                                                ArrayList<ResponseTest> arrayList =
//                                                        gsonObject.fromJson(msg, new TypeToken<ArrayList<ResponseTest>>(){}.getType());
                                                Log.d(TAG, "onResponse: "+modelQnaBoardItem.getTag());
                                                qnaBoardPresenter.enrollmentBoardResp(
                                                        1, modelQnaBoardItem);

                                            }catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            qnaBoardPresenter.enrollmentBoardResp(
                                                    2, modelQnaBoardItem);
                                            Log.d(TAG, "onFailure: 실패");
                                            Log.e(TAG, "onFailure: ", t);
                                        }
                                    });
                                }
                                catch(Exception e) {
                                    qnaBoardPresenter.enrollmentBoardResp(
                                            3, modelQnaBoardItem);
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
}
