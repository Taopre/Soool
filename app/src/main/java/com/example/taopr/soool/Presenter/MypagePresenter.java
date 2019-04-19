package com.example.taopr.soool.Presenter;

import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.MypageBookmarkItem;
import com.example.taopr.soool.Object.MypageList;
import com.example.taopr.soool.Object.MypageMyboardItem;
import com.example.taopr.soool.Object.QnaBoardItem;

import java.util.ArrayList;

public class MypagePresenter extends BasePresenter implements MypageInter {

    private APIService apiService;
    private Context context;
    private MypagePresenter.View view;
    private String TAG = "MypagePresenter";

    ArrayList<MypageBookmarkItem> mypageBookmarkItems;
    ArrayList<MypageMyboardItem> myboardItems;
    ArrayList<QnaBoardItem> qnaBoardItems;

    // 아래 apiservice는 진태 정리대로 해볼려고 일단 받아뒀는데 mypage에 맞게 정리해야할듯??

    public MypagePresenter(Context context){
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);

    }

    @Override
    public void setView(View view) {
        this.view = view;
    }


    // 달력 데이터 업로드하게되면 작성하면 될곳.

    @Override
    public void uploadSchedule() {

    }

    // 이 함수 int 변수값에 따라 구별하자 그럼 일단..
    // i = 0 -> 북마크 리스트 객체
    // i = 1 -> 내 게시물 리스트 객체
    // 혹시 몰라서 2일때 대비로 해둔다. (켈린더 정보)

    // i = 1일때 왜 두개로 구분해뒀냐면 객체를 어떻게 정의해야될지 고민중이라서 qnaboard로 가게되면 위에꺼 지우면 될듯.
    // 현제 mypagebookmarkitem mypagemyboarditem 객체는 아무것도 없는 상태입니다.

    public void loadMypageData(int i){
        addSubscription(
                apiService.getMypageItem(),
                new APICallback<MypageList>() {

                    @Override
                    public void onSuccess(MypageList mypageList) {
                        if (mypageList != null) {
                            if (i == 0) {
                                mypageBookmarkItems = new ArrayList(mypageList.getMypageBookmarkItems());
                                view.getBookmarkResponse(mypageBookmarkItems);
                            }else if (i == 1){
                                myboardItems = new ArrayList(mypageList.getMypageMyboardItems());
                                view.getMyboardResponse(myboardItems);

                                qnaBoardItems = new ArrayList(mypageList.getQnaBoardItems());
                                view.getQnaMyboardResponse(qnaBoardItems);
                            }else {
                                // 달력 대비해서 만들어뒀음
                                // 나중에 결정되면 처리.
                            }
                        }
                        else{
                            Log.i(TAG, "onSuccess: list = null");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure: qna" + msg);

                    }

                    @Override
                    public void onFinish() {
                        Log.i(TAG, "onFinish: qna");
                        //dismissProgressDialog();
                    }
                });
    }
}
