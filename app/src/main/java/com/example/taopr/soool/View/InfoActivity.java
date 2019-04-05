/*
package com.example.taopr.soool.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.InfoAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Presenter.InfoPresenter;
import com.example.taopr.soool.R;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity implements InfoPresenter.View {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private InfoAdapter infoAdapter;
    private List<InfoOfSoool> infoOfSoools = new ArrayList<>();
    private InfoPresenter infoPresenter;


    // presenter의 loadData() 함수를 불러와 리스트 데이터를 가져온다
    // 가져온 데이터를 model 객체화한 후 view의 getDataSource()를 통해 액티비티에 전달
    // 가져온 객체를 List<InfoOfSoool> infoOfSoools에 추가
    // 추가를 끝낸 후 리사이클러뷰의 어댑터에 infoSoools 전달
    // 리사이클러뷰 갱신

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);


        // 리사이클러뷰 기본 설정
        recyclerView.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(selectItemOnRecyclerView());
        // 디비에서 정보 관련 데이터 가져오기
        infoPresenter = new InfoPresenter(this);
        infoPresenter.setView(this);
        infoPresenter.loadData();

    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnRecyclerView() {
        return new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                infoPresenter.getItem(infoOfSoools.get(position), InfoActivity.this);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                infoPresenter.getItem(infoOfSoools.get(position), InfoActivity.this);
            }
        });
    }

    /// 서버에서 데이터를 성공적으로 가져올 경우
    @Override
    public void getDataSuccess(List<InfoOfSoool> infoOfSoools) {
        this.infoOfSoools = infoOfSoools;

        recyclerView.setAdapter(new InfoAdapter(InfoActivity.this,this.infoOfSoools,this));

    }
    /// 서버에서 데이터를 가져올 때 에러가 있을 경우
    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
*/
package com.example.taopr.soool.View;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.InfoAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Presenter.InfoPresenter;
import com.example.taopr.soool.R;

import java.util.ArrayList;
import java.util.List;


public class InfoActivity extends AppCompatActivity implements InfoPresenter.View {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private InfoAdapter infoAdapter;
    private List<InfoOfSoool> infoOfSoools = new ArrayList<>();
    private InfoPresenter infoPresenter;


    // presenter의 loadData() 함수를 불러와 리스트 데이터를 가져온다
    // 가져온 데이터를 model 객체화한 후 view의 getDataSource()를 통해 액티비티에 전달
    // 가져온 객체를 List<InfoOfSoool> infoOfSoools에 추가
    // 추가를 끝낸 후 리사이클러뷰의 어댑터에 infoSoools 전달
    // 리사이클러뷰 갱신

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        recyclerView = findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);


        // 리사이클러뷰 기본 설정
        recyclerView.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnItemTouchListener(selectItemOnRecyclerView());
        // 디비에서 정보 관련 데이터 가져오기
        infoPresenter = new InfoPresenter(this);
        infoPresenter.setView(this);
        infoPresenter.loadData();

    }


    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnRecyclerView() {
        return new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                infoPresenter.getItem(infoOfSoools.get(position), InfoActivity.this);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                infoPresenter.getItem(infoOfSoools.get(position), InfoActivity.this);
            }
        });
    }

    /// 서버에서 데이터를 성공적으로 가져올 경우
    @Override
    public void getDataSuccess(List<InfoOfSoool> infoOfSoools) {
        this.infoOfSoools = infoOfSoools;

        recyclerView.setAdapter(new InfoAdapter(InfoActivity.this,this.infoOfSoools,this));

    }

    /// 서버에서 데이터를 가져올 때 에러가 있을 경우
    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

