package com.example.taopr.soool.View.MyPageFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.MyBoardPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.QnaBoardActivity;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class MyBoardFragment extends Fragment implements MyBoardPresenter.View,View.OnClickListener ,
        SwipeRefreshLayout.OnRefreshListener{

    private RelativeLayout myBoardNoBoardPage;
    private RecyclerView myBoardRecycler;
    private TextView myBoardMoveToWriteBoard;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private final int MY_BOARD_MOVE_TO_WRITE = 4110;
    private final int MY_BOARD_MOVE_TO_DETAIL = 4120;
    private final String TAG = "마이페이지 내 게시물 프래그먼트";
    private final String ACCOUNT_NO_EXTRA = "accountNo";

    private Context context;
    private MyBoardPresenter myBoardPresenter;
    private SwipeRefreshLayout myBoardSwipeRefreshLayout;


    private MyPageView myPageView;

    private int accountNo;

    private boolean isResponse = false ; // 서버에 isResponse 를 받았다면 true , 아니면 false2

    public MyBoardFragment() {
    }

    public interface MyPageView{
        void getMyBoardRes(Boolean isMyBoardRes);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

    }

    // 프래그먼트에서는 뷰바인딩을 oncreate가 아닌 onCreateView에서 작업

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_my_board, container, false);

        this.context = view.getContext();

        accountNo = getArguments().getInt(ACCOUNT_NO_EXTRA);

        linearLayoutManager = new LinearLayoutManager(view.getContext());

        // 리사이클러뷰 기본 설정
        myBoardRecycler = view.findViewById(R.id.myBoardRecycler);

        myBoardRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(view.getContext(),linearLayoutManager.getOrientation()));
        myBoardRecycler.setLayoutManager(linearLayoutManager);
        myBoardRecycler.addOnItemTouchListener(selectItemOnmyBoardRecycler(view));

        myBoardMoveToWriteBoard = view.findViewById(R.id.myBoardMoveToWriteBoard);
        myBoardMoveToWriteBoard.setOnClickListener(this);

        myBoardNoBoardPage = view.findViewById(R.id.myBoardNoBoardPage);

        // qnaboarditem 리스트 전달 받은후 리사이클러뷰 어댑터에 전달


        if(isResponse == false) {
            myBoardPresenter = new MyBoardPresenter(context);
            myBoardPresenter.setView(this);
            myBoardPresenter.loadData(accountNo,0);
        }
        else{
            Log.i(TAG, "onCreateView: ture / size :" + qnaBoardItems.size() );
            qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
            myBoardRecycler.setAdapter(qnaAdapter);
        }
        myBoardSwipeRefreshLayout = view.findViewById(R.id.myBoardSwipeRefreshLayout);

        // 당겨서 새로고침 리스너 , 색상 설정
        myBoardSwipeRefreshLayout.setOnRefreshListener(this);
        myBoardSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context,R.color.greenMain));

        // 리스트 갯수가 0일 때는 작성한 게시물이 없다는 문구를 보여주면서 리사이클러뷰는 없어지지만
        // 리사이클러뷰에 어댑터를 부착한 이유는 어댑터를 연결해 둔 후에 리스트가 추가할 경우 리사이클러뷰를 업데이트
        // 리스트가 없다가 다시 생겼을 때 어댑터에 연결할 때 view 를 가져오기 힘들것이라 예상해서 이렇게 진행

        setMyBoardPage();

        return view;

    }


    // 내 게시물 갯수가 0일 때
    // 작성한 게시물이 없다는 문구를 보여주고
    // 문구 밑에는 게시물을 작성하는 페이지로 이동하는 텍스트 버튼을 생성한다

    // 내 게시물 갯수가 0이 아닐 때
    // 리스트를 프래그먼트에 전달 후 보여주기

    // 리스트 업데이트가 될때마다 이 메서드를 실행

    private void setMyBoardPage() {

        // 작성한 게시물이 없을 때는 게시물을 보여주는 view가 아닌
        // 작성한 게시물이 없다는 문구와 게시물 작성 페이지로 이동하는 텍스트 버튼 생성

        if(qnaBoardItems.size()==0){
            myBoardNoBoardPage.setVisibility(View.VISIBLE);
            myBoardRecycler.setVisibility(View.GONE);
        }

        // 작성한 게시물이 있을 때는 게시물들을 보여주기
        else {
            myBoardNoBoardPage.setVisibility(View.GONE);
            myBoardRecycler.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        if (context instanceof MyPageView) {
            myPageView = (MyPageView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
        myPageView = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myBoardMoveToWriteBoard:
                Intent intent = new Intent(getActivity(), QnaBoardActivity.class);
                intent.putExtra("actionKind", 0);
                startActivityForResult(intent, MY_BOARD_MOVE_TO_WRITE   );
                break;
        }
    }

    // response 를 성공적으로 받았다면 새로고침일 경우 새로고침 아이콘을 없애주고
    // 받아온 qnaBoardItem 리스트를 리사이클러뷰에 띄워준다
    // 다음 isResponse 값을 true 로 변경하여 response 받는데 성공했음을 구별

    // 내 게시물의 응답을 받아온 후 응답에 대한 결과를 마이페이지 프래그먼트에 전달한다.
    // 그래서 마이페이지에서  onCreate 될때 프로필 정보와 내 게시물 데이터에 대한
    // 서버의 응답을 모두 받았을 때 로딩화면이 종료된다
    // 그리고 마이페이지에서 내 게시물에 대한 응답이 실패했을 경우에는 생성한 MyBoardFragment 를 null 값으로
    // 변환하여 프래그먼트를 다시 부착 시 서버에 데이터를 요청할 수 있도록 한다

    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        this.qnaBoardItems = qnaBoardItems;

        qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
        myBoardRecycler.setAdapter(qnaAdapter);

        isResponse = true;
        setMyBoardPage();

        myBoardSwipeRefreshLayout.setRefreshing(false);
        myPageView.getMyBoardRes(true);
    }

    // response 를 전달받는데 실패 했을 경우 '페이지에 오류가 있다'라는 메세지를 유저에게 보여주고
    // isResponse 값을 false 로 변경
    @Override
    public void getDataFail(String message) {

        Log.d(TAG, "getDataFail: "+message);
        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
        myPageView.getMyBoardRes(false);
        isResponse =false;

        myBoardSwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void moveToPage(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // qna 처리랑 똑같음

        if(resultCode == RESULT_OK) {

            // 리스트 수정, 삭제
            // 리스트 수정, 삭제의 경우에는 리스트에 있는 아이템을 수정,삭제를 하는 것이기 때문에
            // 아이템의 리스트 포지션 값을 받고 , actionKind 로 수정인지 삭제인지 구별
            // 추가 --> actionKind = 0
            // 수정 --> actionKind = 1
            // 삭제 --> actionKind = 2

            // TODO:::::::::::::
            // 추가와 삭제인 경우에는 프로필에서 '내 게시물 수' 값을 동기화 해줘야 하기 때문에
            // 이 경우에는 인터페이스를 통해 마이페이지 프래그먼트에 추가인지 삭제인지에 대한 값을 전달한다

            // 삭제인 경우에는 qnaBoardItem 을 보내지 않기 때문에 예외처리
            QnaBoardItem qnaBoardItem = null;
            if (data != null && data.getParcelableExtra("qnaBoardItem") != null) {
                qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
            }

            int qnaListPosition = data.getIntExtra("qnaListPosition", 0);
            int actionKind = data.getIntExtra("actionKind", 99);

            switch (actionKind) {
                case 0:
                    qnaAdapter.addItem(qnaBoardItem);
                    qnaBoardItems.add(qnaBoardItem);
                    myBoardRecycler.smoothScrollToPosition(0);
                case 1:

                    qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem,qnaListPosition);
                    break;
                case 2:
                    qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                    break;
            }
            setMyBoardPage();
        }

    }

    // 새로고침
    // actionKind 이 1일 때는 새로고침 이모티콘을 보여주기 위함
    // actionKind 이 0일 때는 로딩중 이모티콘을 보여줌
    @Override
    public void onRefresh() {
        myBoardPresenter.loadData(accountNo,1);
    }


    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnmyBoardRecycler(View view) {
        return new RecyclerItemClickListener(view.getContext(), myBoardRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myBoardPresenter.getItem(qnaBoardItems.get(position), getActivity(),position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                myBoardPresenter.getItem(qnaBoardItems.get(position), getActivity(),position);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
    }

}
