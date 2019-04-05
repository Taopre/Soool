package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.taopr.soool.Object.InfoOfSoool;

import java.util.ArrayList;
import java.util.List;

public class InfoPresenter implements InfoInter {

    // 생성장에서 context 전달 받기
    // 그 후 성공, 실패 시 info 액티비티의 successdata() 함수와 fail 함수 가져오기
    private Context context;
    private InfoPresenter.View view;
    private Activity activity;
    private List<InfoOfSoool> infoOfSoools = new ArrayList<>();
    private static String TAG = "정보_presenter";

    public InfoPresenter(Context context){
        this.context = context;

    }

    // 서버에서 데이터를 가져온 후 객체로 만들어서 전송
    public void loadData(){
        Log.i(TAG, "loadData: ");
        Boolean response=true; // true 성공 , false 실패


        // 데이터 하드코딩
        for(int a=0; a<10; a++) {
            infoOfSoools.add(new InfoOfSoool("aa", "soool", "2019-10-11", 4, 1));
            Log.i(TAG, "loadData: " + infoOfSoools.size() + " 제목 " + infoOfSoools.get(a).getTitle());
        }

        // 성공시
        if(response) view.getDataSuccess(infoOfSoools);
        // 실패시
        else view.getDataFail("fail");

    }

    /// 아이템 선택 시 선택한 아이템 상세 보기로 이동

    public void getItem(InfoOfSoool infoOfSoool, Activity activity) {
        this.activity = activity;
        Toast.makeText(activity, infoOfSoool.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setView(InfoPresenter.View view) {
        this.view = view;
    }
}
