package com.example.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;
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
    private int accountNo;
    private boolean onPaging = false; // 페이징 중인지 아닌지 판별하는 변수, true 일때 페이징중

    private boolean isResponse = false ; // 서버에 isResponse 를 받았다면 true , 아니면 false
                                         // 서버로부터 리스폰스를 받았다면 qna 프래그먼트가 다시 부착했을 때 서버에 다시 요청하는 것을 방지하기 위한 변수

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
        View view = inflater.inflate(R.layout.fragment_home_qna, container, false);

        context = view.getContext();

        unbinder = ButterKnife.bind(this,view);

        qnaProgress = view.findViewById(R.id.qnaProgress);

        // 리사이클러뷰 기본 설정
        qnaRecycler = view.findViewById(R.id.qnaRecycler);

        qnaSwipeRefreshLayout = view.findViewById(R.id.qnaSwipeRefreshLayout);

        accountNo = LoginSharedPreferences.getAccountNo(getContext(),"LoginAccount");

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

        if(!isResponse) {
            qnaFmPresenter = new QnaFmPresenter(context);
            qnaFmPresenter.setView(this);

            // 첫 화면 데이터 로딩시와 새로고침 로딩화면이 다르기 때문에 loadingKind 값으로 구별한다
            // loadingKind = 0 일때는 첫화면 로딩 , loadingKind = 1 일때는 새로고침 로딩
            // lastPostNo는 페이징에 필요한 게시글 번호이다.
            // 서버에 리스트의 마지막 게시글 번호를 전송해서 그 이후 리스트의 아이템을 받아오는 방식으로 페이징

            qnaFmPresenter.loadData(0,accountNo,-1);

        }
        else{
            qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
            qnaRecycler.setAdapter(qnaAdapter);
        }

        // 리사이클러뷰 페이징 부분
        // 스크롤 중 리스트의 최하단까지 이동했을 경우 리스트의 최하단 게시글 번호를 전송해서
        // 최하단 게시글 이후의 게시글들을 받아온다.
        // 리스트를 받아오는 중에 서버에 리퀘스트를 받아오는 것을 방지하기 위해
        // onPaging 값이 false 일때만 서버에 리퀘스트를 보낸다
        // 서버에 리퀘스트를 보낼 때는 onPaging 값을 true 로 변경 후에 전송한다
        // 그리고 onPaging 은 서버에서 리스폰스를 성공적으로 받고 리스트 갱신 후에
        // onPaging 의 값을 false 로 변경한다
        qnaRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int listSize = qnaBoardItems.size()-1;
                int visibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (listSize == visibleItemPosition){
                    if (!onPaging && qnaBoardItems.size()>0){
                        onPaging = true;

                        // visibleItemPosition 대신 listSize 변수로 한 이유는
                        // onScrolled() 함수를 들어와 서버에 리퀘스트를 보내는 시간 사이에 값이 변하지 않는 변수로 하기 위해서
                        //int lastPostNo = qnaBoardItems.get(visibleItemPosition).getPostNo();
                        int lastPostNo = qnaBoardItems.get(listSize).getPostNo();

                        qnaFmPresenter.loadData(0,accountNo,lastPostNo);
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

       // mListener = null;
    }

    @OnClick({R.id.fab_default})
    void OnClickButton(View view){
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
    @Override
    public void onRefresh() {
        qnaFmPresenter.loadData(1,accountNo,-1);
    }

    // response 를 성공적으로 받았다면 새로고침일 경우 새로고침 아이콘을 없애주고
    // 받아온 qnaBoardItem 리스트를 리사이클러뷰에 띄워준다
    // 다음 isResponse 값을 true 로 변경하여 response 받는데 성공했음을 구별

    // 리스폰스가 성공이던 실패던 결과값을 받았다면
    // onPaging 값을 false 로 변경하여 페이징이 가능하도록

    // 새로고침인 경우 loadingKind 값이 1
    // loadingKind 값이 1일 경우 리스트에 추가하는 것이 아니라 받아온 게시글 리스트로 대체한다
    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems,int loadingKind) {
        if (qnaBoardItems.size() == 0) {
            Toast.makeText(getContext(), getString(R.string.toast_notice_no_exist_post), Toast.LENGTH_SHORT).show();
        }
        else {
            switch (loadingKind) {
                case 0:
                    this.qnaBoardItems.addAll(qnaBoardItems);
                    break;
                case 1:
                    this.qnaBoardItems = qnaBoardItems;
                    break;
            }
            if (qnaAdapter == null) {
                qnaAdapter = new QnaAdapter(this.qnaBoardItems, context);
                qnaRecycler.setAdapter(qnaAdapter);
            } else {
                qnaAdapter.addItemList(this.qnaBoardItems);
            }
        }
        onPaging = false;
        isResponse = true;
        qnaSwipeRefreshLayout.setRefreshing(false);
    }

    // response 를 전달받는데 실패 했을 경우 '페이지에 오류가 있다'라는 메세지를 유저에게 보여주고
    // isResponse 값을 false 로 변경
    @Override
    public void getDataFail(String message) {
        qnaSwipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, " 리퀘스트 실패 ");
        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
        isResponse =false;
        onPaging = true;
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
