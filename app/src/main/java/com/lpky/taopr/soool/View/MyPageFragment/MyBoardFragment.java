package com.lpky.taopr.soool.View.MyPageFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lpky.taopr.soool.Adapter.QnaAdapter;
import com.lpky.taopr.soool.Adapter.RecyclerItemClickListener;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Presenter.MyBoardPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.View.QnaBoardActivity;

import java.util.ArrayList;

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
    private final String TAG = "마이페이지 내 큐앤에이 프래그먼트";
    private final String ACCOUNT_NO_EXTRA = "accountNo";
    private boolean onPaging = false; // 페이징 중인지 아닌지 판별하는 변수, true 일때 페이징중
    private Context context;
    private MyBoardPresenter myBoardPresenter;
    private SwipeRefreshLayout myBoardSwipeRefreshLayout;

    private MyPageView myPageView;

    private int accountNo;

    private boolean isResponse = false ; // 서버에 isResponse 를 받았다면 true , 아니면 false

    public MyBoardFragment() {
    }

    // 서버에 요청을 보내고 응답을 받았다는 것을 마이페이지에 전달.
    // 그 이유는 마이페이지에 부착한 프래그먼트의 로딩화면들을 마이페이지에서 통괄적으로 보여주고 있기 때문
    // 그리고 response 를 받았을 때 마이페이지 response 결과를 보내 마이페이지에서 response 결과가 false 일 경우
    // 서버에서 MyBoard 프래그먼트를 부착할 경우 서버에 요청을 보내고, true 일 경우에는 응답을 통해 받아왔던 게시글을 보여준다

    public interface MyPageView{
        void startMyBoardLoading(); // 마이보드에서 서버에 요청을 보내고 리스폰스를 기다리고 있다는 걸 (Home 액티비티를 통해) 마이페이지에 전달
        void endMyBoardLoading(Boolean isMyBoardRes); // 마이보드에서 서버로부터 응답을 받았다는 것을 (Home 액티비티를 통해) 마이페이지에 전달
        void updateProfileForMyBoard(); // 유저의 '내 게시글' , '내 포인트' 의 값이 변화 했을경우 Home 액티비티를 거쳐 마이페이지 프래그먼트에 알려준다
        void myBoardUpdateItem(QnaBoardItem qnaBoardItem,int actionKind);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // 프래그먼트에서는 뷰바인딩을 oncreate가 아닌 onCreateView에서 작업

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_my_board, container, false);

        this.context = view.getContext();

        accountNo = getArguments().getInt(ACCOUNT_NO_EXTRA);

        linearLayoutManager = new LinearLayoutManager(view.getContext());

        // 리사이클러뷰 기본 설정

        myBoardRecycler = view.findViewById(R.id.myBoardRecycler);

        myBoardRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(view.getContext(),linearLayoutManager.getOrientation()));
        myBoardRecycler.setLayoutManager(linearLayoutManager);
        myBoardRecycler.addOnItemTouchListener(selectItemOnMyBoardRecycler(view));

        myBoardMoveToWriteBoard = view.findViewById(R.id.myBoardMoveToWriteBoard);
        myBoardMoveToWriteBoard.setOnClickListener(this);

        myBoardNoBoardPage = view.findViewById(R.id.myBoardNoBoardPage);

        // qnaboarditem 리스트 전달 받은후 리사이클러뷰 어댑터에 전달


        if(isResponse == false) {
            myBoardPresenter = new MyBoardPresenter(context);
            myBoardPresenter.setView(this);
            myPageView.startMyBoardLoading();
            myBoardPresenter.loadData(accountNo,0,-1);
        }
        else{
            qnaAdapter = new QnaAdapter(this.qnaBoardItems,context);
            myBoardRecycler.setAdapter(qnaAdapter);
        }

        myBoardSwipeRefreshLayout = view.findViewById(R.id.myBoardSwipeRefreshLayout);

        // 당겨서 새로고침 리스너 , 색상 설정
        myBoardSwipeRefreshLayout.setOnRefreshListener(this);
        myBoardSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context,R.color.greenMain));

        // 리스트 갯수가 0일 때는 작성한 게시물이 없다는 문구를 보여주면서 리사이클러뷰는 inVisible 되지만
        // 리사이클러뷰에 어댑터를 부착한 이유는 어댑터를 연결해 둔 후에 리스트가 추가할 경우 리사이클러뷰를 업데이트 시,
        // 리스트가 없다가 다시 생겼을 때 어댑터에 연결할 때 view 를 가져오기 힘들것이라 예상해서 이렇게 진행

        setMyBoardPage();

        // 리사이클러뷰 페이징 부분
        // 스크롤 중 리스트의 최하단까지 이동했을 경우 리스트의 최하단 게시글 번호를 전송해서
        // 최하단 게시글 이후의 게시글들을 받아온다.
        // 리스트를 받아오는 중에 서버에 리퀘스트를 받아오는 것을 방지하기 위해
        // onPaging 값이 false 일때만 서버에 리퀘스트를 보낸다
        // 서버에 리퀘스트를 보낼 때는 onPaging 값을 true 로 변경 후에 전송한다
        // 그리고 onPaging 은 서버에서 리스폰스를 성공적으로 받고 리스트 갱신 후에
        // onPaging 의 값을 false 로 변경한다

        myBoardRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int listSize = qnaBoardItems.size()-1;
                int visibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();


                // 내 게시글의 경우에는 게시글 수가 4개 이상인 경우에만 페이징
                // 5개로 한 이유는 한 페이지에 최대로 들어가는 게시글 수가 4개이기 때문
                // 이 조건이 없을 시에 작성한 글이 1개일 경우 서버에서 받아왔을 때는 스크롤을 하지 않아도 페이징이 되어버림
                if (listSize == visibleItemPosition && qnaBoardItems.size()>=4){
                    if (!onPaging ){
                        onPaging = true;
                        // visibleItemPosition 대신 listSize 변수로 한 이유는
                        // onScrolled() 함수를 들어와 서버에 리퀘스트를 보내는 시간 사이에 값이 변하지 않는 변수로 하기 위해서
                        //int lastPostNo = qnaBoardItems.get(visibleItemPosition).getPostNo();
                        int lastPostNo = qnaBoardItems.get(listSize).getPostNo();

                        myPageView.startMyBoardLoading();
                        myBoardPresenter.loadData(accountNo,0,lastPostNo);
                    }
                }
            }
        });

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
        if (myPageView!=null){
            myPageView = null;
        }
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

    // 서버로부터 받아온 게시글 수가 0 일때는 Toast 메세지로 더 이상 게시글이 없다는 메세지를 표시한다

    @Override
    public void getDataSuccess(ArrayList<QnaBoardItem> qnaBoardItems,int loadingKind) {
        if (qnaBoardItems.size() == 0 && this.qnaBoardItems.size() > 0) {

            //Toast.makeText(getContext(), getString(R.string.toast_notice_no_exist_post), Toast.LENGTH_SHORT).show();
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

            if (qnaAdapter == null && qnaBoardItems.size()>0) {
                qnaAdapter = new QnaAdapter(this.qnaBoardItems, context);
                myBoardRecycler.setAdapter(qnaAdapter);
            }
            else if(qnaAdapter == null){

            }
            else {
                qnaAdapter.addItemList(this.qnaBoardItems);
            }

            setMyBoardPage();
        }

        onPaging = false;
        isResponse = true;

        myBoardSwipeRefreshLayout.setRefreshing(false);
        myPageView.endMyBoardLoading(true);
    }

    // response 를 전달받는데 실패 했을 경우 '페이지에 오류가 있다'라는 메세지를 유저에게 보여주고
    // isResponse 값을 false 로 변경
    @Override
    public void getDataFail(String message) {
        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
        myPageView.endMyBoardLoading(false);
        isResponse =false;
        myBoardSwipeRefreshLayout.setRefreshing(false);

    }


    @Override
    public void moveToPage(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
    }

    // 홈, 커뮤니티 페이지에서 유저가 작성한 글에 update 가 있을 경우
    // 마이페이지의 리스트에서도 업데이트하는 부분
    public void myBoardUpdateItem(QnaBoardItem qnaBoardItem,int actionKind){
        switch (actionKind){
            case 0:
                qnaAdapter.addItem(qnaBoardItem);
                break;
            case 1:
                qnaAdapter.modifyItem(qnaBoardItem);
                break;
            case 2:
                qnaAdapter.deleteItem(qnaBoardItem);
                break;
        }

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

            // 추가와 삭제인 경우에는 프로필에서 '내 게시물 수' 값을 동기화 해줘야 하기 때문에
            // 이 경우에는 인터페이스를 통해 마이페이지 프래그먼트에 추가인지 삭제인지에 대한 값을 전달한다

            // 삭제인 경우에는 qnaBoardItem 을 보내지 않기 때문에 예외처리
            QnaBoardItem qnaBoardItem = null;
            if (data != null && data.getParcelableExtra("qnaBoardItem") != null) {
                qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
            }

            int qnaListPosition = data.getIntExtra("qnaListPosition", 0);
            int actionKind = data.getIntExtra("actionKind", 99);

            if (actionKind != 99) {
                switch (actionKind) {
                    case 0:
                        if (qnaAdapter == null) {
                            qnaBoardItems.add(qnaBoardItem);
                            qnaAdapter = new QnaAdapter(this.qnaBoardItems, context);
                            myBoardRecycler.setAdapter(qnaAdapter);
                        } else {
                            qnaAdapter.addItem(qnaBoardItem);
                            qnaBoardItems.add(qnaBoardItem);
                            myBoardRecycler.smoothScrollToPosition(0);
                        }

                    case 1:
                        qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem, qnaListPosition);
                        break;
                    case 2:
                        qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                        break;
                }
                myPageView.myBoardUpdateItem(qnaBoardItem,actionKind);
            }

            // TODO: 포인트 정책 회의 후 댓글 작성 시에는 포인트 지급이 되지 않는다면 actionKind 값이 1일 때는 예외 처리

            // 게시글이 작성, 삭제 , 혹은 댓글 추가, 삭제할 경우 마이페이지에서 '내 게시글' , '내 포인트' 값을 갱신해야 한다
            // 게시글 작성 actionKind = 1, 게시글 삭제 actionKind = 2, 댓글 작성/삭제 actionKind = 3
            // 위 경우 발생시 HomeActivity 을 통해 MyPage 프래그먼트에 알림을 보낸다
            // MyPage 프래그먼트에서 알림을 받을 경우 '내 게시글' 과 '내 포인트' 값을 갱신한다
            myPageView.updateProfileForMyBoard();

            setMyBoardPage();
        }
    }

    // 새로고침
    // loadingKind 이 1일 때는 새로고침 이모티콘을 보여주기 위함
    // loadingKind 이 0일 때는 로딩중 이모티콘을 보여줌
    @Override
    public void onRefresh() {
        //myPageView.startMyBoardLoading();
        myBoardPresenter.loadData(accountNo,1,-1);
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnMyBoardRecycler(View view) {
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

}
