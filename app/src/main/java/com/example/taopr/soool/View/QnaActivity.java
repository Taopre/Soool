package com.example.taopr.soool.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.OnClick;

//public class QnaActivity extends AppCompatActivity implements View.OnClickListener, QnaPresenter.View{
public class QnaActivity extends BaseActivity implements QnaPresenter.View{

   private FloatingActionButton fab_default;

    private RecyclerView qnaRecycler;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private QnaPresenter qnaPresenter;
    private String TAG = "큐앤에이_activity";
    private final int QNA_MOVE_TO_WRITE = 3200;
    private final int QNA_MOVE_TO_DETAIL = 3100;

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

        //DoBinding(); // ui 선언
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
        switch (view.getId()){
            case R.id.fab_default:
                Intent intent = new Intent(QnaActivity.this, QnaBoardActivity.class);
                intent.putExtra("actionKind", 0);
                startActivityForResult(intent, QNA_MOVE_TO_WRITE);
               // qnaBoardItems = qnaAdapter.deleteItem(0);
                //qnaBoardItems = qnaAdapter.addItem(qnaBoardItems.get());
                break;
        }
    }


    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;
        qnaAdapter = new QnaAdapter(QnaActivity.this,this.qnaBoardItems,this);
        qnaRecycler.setAdapter(qnaAdapter);
        Log.i(TAG, "getDataSuccess: 리스트 수 :" + qnaBoardItems.size());
        for(int a=0; a<this.qnaBoardItems.size(); a++){
            Log.i(TAG, "getDataSuccess: 리스트 값 " +a+ this.qnaBoardItems.get(a).getTitle() +"  "
                    + this.qnaBoardItems.get(a).getAccountNo());
        }
    }

    @Override
    public void getDataFail(String message) {
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

            QnaBoardItem qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
            int qnaListPosition = data.getIntExtra("qnaListPosition",0);
            int actionKind = data.getIntExtra("actionKind",99);

            switch (actionKind){
                case 0:
                    qnaBoardItems = qnaAdapter.addItem(qnaBoardItem);
                case 1:
                    //qnaAdapter.notifyItemChanged(qnaListPosition,qnaBoardItem);
                    //qnaBoardItems.set(qnaListPosition,qnaBoardItem);
                    qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem,qnaListPosition);
                    break;
                case 2:
                    //qnaAdapter.notifyItemRemoved(qnaListPosition);
                    qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                    break;

            }


        }
    }

}

