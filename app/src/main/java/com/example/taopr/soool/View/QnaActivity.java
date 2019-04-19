package com.example.taopr.soool.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

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

//public class QnaActivity extends AppCompatActivity implements View.OnClickListener, QnaPresenter.View{
public class QnaActivity extends BaseActivity implements QnaPresenter.View, View.OnClickListener{

    private FloatingActionButton fab_default, fab_qnaBoard, fab_qnaVote;

    private RecyclerView qnaRecycler;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private QnaPresenter qnaPresenter;
    private String TAG = "큐앤에이_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        DoBinding(); // ui 선언 및 presenter 선언, presenter에서 넘어올 응답에 대한 변화 view? 선언까지
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnqnaRecycler() {
        return new RecyclerItemClickListener(this, qnaRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                qnaPresenter.getItem(qnaBoardItems.get(position), QnaActivity.this);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                qnaPresenter.getItem(qnaBoardItems.get(position), QnaActivity.this);
            }
        });
    }


    private void DoBinding() {

        // 탭 부분은 BaseActivity로 이동

        // 뷰들 선언하는 부분입니다.
        fab_default = (FloatingActionButton) findViewById(R.id.fab_default);
        fab_qnaBoard = (FloatingActionButton) findViewById(R.id.fab_qnaBoard);
        fab_qnaVote = (FloatingActionButton) findViewById(R.id.fab_qnaVote);

        // 뷰의 리스너 선언 부분입니다.
        fab_default.setOnClickListener(this);
        fab_qnaBoard.setOnClickListener(this);
        fab_qnaVote.setOnClickListener(this);
    }



    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
        qnaRecycler.setAdapter(new QnaAdapter(QnaActivity.this, this.qnaBoardItems, this));
        Log.i(TAG, "getDataSuccess: 리스트 수 :" + qnaBoardItems.size());
        for(int a=0; a<this.qnaBoardItems.size(); a++){
            Log.i(TAG, "getDataSuccess: 리스트 값 " +a+ this.qnaBoardItems.get(a).getTitle() +"  " + this.qnaBoardItems.get(a).getAccountNo());
        }
    }

    @Override
    public void getDataFail(String message) {
        Toast.makeText(this, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        qnaPresenter.onUnSubscribe();
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_default :
                Intent intent = new Intent(this, QnaBoardActivity.class);
                startActivity(intent);
                break;
        }
    }
}

