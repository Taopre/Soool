package com.example.taopr.soool.View;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.QnaPresenter;
import com.example.taopr.soool.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class QnaActivity extends BaseActivity implements QnaPresenter.View,
        SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView qnaRecycler;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private QnaPresenter qnaPresenter;
    private String TAG = "큐앤에이_activity";
    private final int QNA_MOVE_TO_WRITE = 3200;
    private final int QNA_MOVE_TO_DETAIL = 3100;
    
    @BindView(R.id.qnaSwipeRefreshLayout)
    SwipeRefreshLayout qnaSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_qna,2);

        // 리사이클러뷰 기본 설정
        qnaRecycler = findViewById(R.id.qnaRecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        qnaRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(this,linearLayoutManager.getOrientation()));
        qnaRecycler.setLayoutManager(linearLayoutManager);
        qnaRecycler.addOnItemTouchListener(selectItemOnqnaRecycler());

        // 디비에서 qna 작성글 데이터 가져오기
        qnaPresenter = new QnaPresenter(this);
        qnaPresenter.setView(this);
        qnaPresenter.loadData();



        qnaSwipeRefreshLayout.setOnRefreshListener(this);

        // 새로고침 아이콘의 색이 순서대로 변함
        // TODO: 색상 회의 적용
        qnaSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this,R.color.greenMain),
                ContextCompat.getColor(this,R.color.black),
                ContextCompat.getColor(this,R.color.grayMain),
                ContextCompat.getColor(this,R.color.sunflower_yellow));
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnqnaRecycler() {
        return new RecyclerItemClickListener(this, qnaRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                qnaPresenter.getItem(qnaBoardItems.get(position), QnaActivity.this,position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                qnaPresenter.getItem(qnaBoardItems.get(position), QnaActivity.this,position);
            }
        });
    }

    @OnClick({R.id.fab_default})
    void OnClickButton(View view){
        Log.i(TAG, "OnClickButton: ");
        switch (view.getId()){
            case R.id.fab_default:
                Toast.makeText(this, "AAAA", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(QnaActivity.this, QnaBoardActivity.class);
                intent.putExtra("actionKind", 0);
                startActivityForResult(intent, QNA_MOVE_TO_WRITE);
                break;
        }
    }

    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
        qnaSwipeRefreshLayout.setRefreshing(false);

        qnaAdapter = new QnaAdapter(QnaActivity.this,this.qnaBoardItems,this);

        qnaRecycler.setAdapter(qnaAdapter);

    }

    @Override
    public void getDataFail(String message) {
        qnaSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "getDataFail: "+message);
        Toast.makeText(this, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
    }

    // 페이지 이동
    @Override
    public void moveToPage(Intent intent,int requestCode) {
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onDestroy() {
        qnaPresenter.onUnSubscribe();  ///
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        qnaPresenter.clearSubscribe();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // 리스트 수정, 삭제
            // 리스트 수정, 삭제의 경우에는 리스트에 있는 아이템을 수정,삭제를 하는 것이기 때문에
            // 아이템의 리스트 포지션 값을 받고 , actionKind 로 수정인지 삭제인지 구별
            // 추가 --> actionKind = 0
            // 수정 --> actionKind = 1
            // 삭제 --> actionKind = 2

            QnaBoardItem qnaBoardItem = null;
            if (data != null && data.getParcelableExtra("qnaBoardItem") != null) {
                qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
            }

            int qnaListPosition = data.getIntExtra("qnaListPosition",0);
            int actionKind = data.getIntExtra("actionKind",99);

            switch (actionKind){
                case 0:
                    qnaBoardItems = qnaAdapter.addItem(qnaBoardItem);
                case 1:
                    qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem,qnaListPosition);
                    break;
                case 2:
                    qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                    break;
            }
        }
    }

    // 새로고침 시 서버에서 데이터를 새로 가져온다
    // 서버에서 데이터를 가져온 후 setRefreshing(false)을 통해 새로고침 아이콘을 사라지게 한다

    @Override
    public void onRefresh() {
       qnaPresenter.loadData();
    }
}

