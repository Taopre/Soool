package com.example.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.QnaFmPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.QnaBoardActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class QnaFragment extends BaseFragment implements QnaFmPresenter.View,SwipeRefreshLayout.OnRefreshListener{

    String TAG = "홈 큐앤에이 프래그먼트";
    private RecyclerView qnaRecycler;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private Context context;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private QnaFmPresenter qnaFmPresenter;
    private final int QNA_MOVE_TO_WRITE = 3200;
    private final int QNA_MOVE_TO_DETAIL = 3100;
    private SwipeRefreshLayout qnaSwipeRefreshLayout;
    private ProgressBar qnaProgress;


    private boolean isResponse = false ; // 서버에 isResponse 를 받았다면 true , 아니면 false


    public QnaFragment() {
        Log.i(TAG, "QnaFragment: ");
    }

    // 액티비티에서 프래그먼트로 값 넘기는 거 확인
    public static QnaFragment newInstance(Context context) {
        QnaFragment fragment = new QnaFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_home_qna, container, false);

        context = view.getContext();

        unbinder = ButterKnife.bind(this,view);

        qnaProgress = view.findViewById(R.id.qnaProgress);

        // 리사이클러뷰 기본 설정
        qnaRecycler = view.findViewById(R.id.qnaRecycler);

        qnaSwipeRefreshLayout = view.findViewById(R.id.qnaSwipeRefreshLayout);

        linearLayoutManager = new LinearLayoutManager(context);
        qnaRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(context,linearLayoutManager.getOrientation()));
        qnaRecycler.setLayoutManager(linearLayoutManager);
        qnaRecycler.addOnItemTouchListener(selectItemOnqnaRecycler());

        // 당겨서 새로고침 리스너 , 색상 설정
        qnaSwipeRefreshLayout.setOnRefreshListener(this);
        qnaSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context,R.color.greenMain));


        // 디비에서 qna 작성글 데이터 가져오기 하지만 서버에 계속해서 요청 보내는 것을 방지하기 위해
        // doRequest 값이 false 인 경우에만 서버에 request 전달
        // 그리고 response 받는데 실패할 경우(getDataFail) isResponse = false
        //                       성공할 경우(getDataSuccess) isResponse = true

        if(isResponse == false) {
            Log.i(TAG, "onCreateView:  " + isResponse);
            qnaFmPresenter = new QnaFmPresenter(context);
            qnaFmPresenter.setView(this);
            qnaFmPresenter.loadData(0);
        }
        else{
            Log.i(TAG, "onCreateView:  " + isResponse);
            qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
            qnaRecycler.setAdapter(qnaAdapter);
        }

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
       // mListener = null;
    }

    @OnClick({R.id.fab_default})
    void OnClickButton(View view){
        Log.i(TAG, "OnClickButton: ");
        switch (view.getId()){
            case R.id.fab_default:
                Intent intent = new Intent(getActivity(), QnaBoardActivity.class);
                intent.putExtra("actionKind", 0);
                startActivityForResult(intent, QNA_MOVE_TO_WRITE);
                break;
        }
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnqnaRecycler() {
        return new RecyclerItemClickListener(context, qnaRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                qnaFmPresenter.getItem(qnaBoardItems.get(position), getActivity(),position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                qnaFmPresenter.getItem(qnaBoardItems.get(position), getActivity(),position);
            }
        });
    }

    // qna 글 목록을 새로고침 할 경우 서버에 글 목록을 새로 요청한다
    // 페이징을 하게 될 경우 여기서 페이징 넘버를 초기화 해줘야함
    @Override
    public void onRefresh() {
        qnaFmPresenter.loadData(1);
    }

    // response 를 성공적으로 받았다면 새로고침일 경우 새로고침 아이콘을 없애주고
    // 받아온 qnaBoardItem 리스트를 리사이클러뷰에 띄워준다
    // 다음 isResponse 값을 true 로 변경하여 response 받는데 성공했음을 구별

    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;

        qnaSwipeRefreshLayout.setRefreshing(false);

        qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
        qnaRecycler.setAdapter(qnaAdapter);

        isResponse = true;
    }

    // response 를 전달받는데 실패 했을 경우 '페이지에 오류가 있다'라는 메세지를 유저에게 보여주고
    // isResponse 값을 false 로 변경
    @Override
    public void getDataFail(String message) {
        qnaSwipeRefreshLayout.setRefreshing(false);

        Log.d(TAG, "getDataFail: "+message);
        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
        isResponse =false;
    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
    }


    // 서버에서 데이터 통신을 하는 동안 progress 바로 로딩화면을 보여줌
    @Override
    public void showLoading() {
        qnaProgress.setVisibility(View.VISIBLE);
    }

    // 서버에서 데이터를 성공적으로 가져왔다면 로딩화면을 종료
    @Override
    public void hideLoading() {
        qnaProgress.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult:  " +resultCode);
        if (resultCode == RESULT_OK) {

            // 리스트 수정, 삭제
            // 리스트 수정, 삭제의 경우에는 리스트에 있는 아이템을 수정,삭제를 하는 것이기 때문에
            // 아이템의 리스트 포지션 값을 받고 , actionKind 로 수정인지 삭제인지 구별
            // 추가 --> actionKind = 0
            // 수정 --> actionKind = 1
            // 삭제 --> actionKind = 2

            Log.i(TAG, "onActivityResult: RESULT_OK");
            QnaBoardItem qnaBoardItem = null;
            if (data != null && data.getParcelableExtra("qnaBoardItem") != null) {
                qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
                Log.i(TAG, "onActivityResult: ");
            }

            int qnaListPosition = data.getIntExtra("qnaListPosition",0);
            int actionKind = data.getIntExtra("actionKind",99);

            Log.i(TAG, "onActivityResult: actionKind : " + actionKind);

            switch (actionKind){
                case 0:
                    qnaAdapter.addItem(qnaBoardItem);
                    qnaBoardItems.add(qnaBoardItem);
                    qnaRecycler.smoothScrollToPosition(0);
                case 1:
                    qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem,qnaListPosition);
                    break;
                case 2:
                    qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                    break;
            }
        }
    }


}
