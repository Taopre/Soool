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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.R;
import com.example.taopr.soool.View.QnaBoardActivity;
import com.example.taopr.soool.View.QnaBoardDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


public class MyBoardFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout myBoardNoBoardPage;
    private RecyclerView myBoardRecycler;
    private TextView myBoardMoveToWriteBoard;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private final int MY_BOARD_MOVE_TO_WRITE = 4110;
    private final int MY_BOARD_MOVE_TO_DETAIL = 4120;
    private final String TAG = "마이페이지 내 게시물 프래그먼트";
    private Context context;
    private Activity activity;


    private OnFragmentInteractionListener mListener;


    public MyBoardFragment() {
    }

    public static MyBoardFragment newInstance(String param1, String param2) {
        MyBoardFragment fragment = new MyBoardFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
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
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_my_board, container, false);


        // 리사이클러뷰 기본 설정
        myBoardRecycler = view.findViewById(R.id.myBoardRecycler);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        myBoardRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(view.getContext(),linearLayoutManager.getOrientation()));
        myBoardRecycler.setLayoutManager(linearLayoutManager);
        myBoardRecycler.addOnItemTouchListener(selectItemOnmyBoardRecycler(view));

        myBoardMoveToWriteBoard = view.findViewById(R.id.myBoardMoveToWriteBoard);
        myBoardMoveToWriteBoard.setOnClickListener(this);

        myBoardNoBoardPage = view.findViewById(R.id.myBoardNoBoardPage);

        // qnaboarditem 리스트 전달 받은후 리사이클러뷰 어댑터에 전달
        qnaBoardItems = getArguments().getParcelableArrayList("qnaBoardItems");
        Log.i(TAG, "onCreateView: 사이즈 " + qnaBoardItems.size());


        // 리스트 갯수가 0일 때는 작성한 게시물이 없다는 문구를 보여주면서 리사이클러뷰는 없어지지만
        // 리사이클러뷰에 어댑터를 부착한 이유는 어댑터를 연결해 둔 후에 리스트가 추가할 경우 리사이클러뷰를 업데이트
        // 리스트가 없다가 다시 생겼을 때 어댑터에 연결할 때 view 를 가져오기 힘들것이라 예상해서 이렇게 진행

        qnaAdapter = new QnaAdapter(qnaBoardItems,view.getContext());
        myBoardRecycler.setAdapter(qnaAdapter);
        context = view.getContext();

        setMyQnaPage();

        return view;

    }


    // 내 게시물 갯수가 0일 때
    // 작성한 게시물이 없다는 문구를 보여주고
    // 문구 밑에는 게시물을 작성하는 페이지로 이동하는 텍스트 버튼을 생성한다

    // 내 게시물 갯수가 0이 아닐 때
    // 리스트를 프래그먼트에 전달 후 보여주기

    private void setMyQnaPage() {

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



    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        else{
        }
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
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.myBoardMoveToWriteBoard:
                Intent intent = new Intent(view.getContext(), QnaBoardActivity.class);
                startActivityForResult(intent,MY_BOARD_MOVE_TO_WRITE);
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
                    qnaAdapter.notifyItemChanged(qnaListPosition,qnaBoardItem);
                    qnaBoardItems.set(qnaListPosition,qnaBoardItem);
                    qnaBoardItems = qnaAdapter.modifyItem(qnaBoardItem, qnaListPosition);
                    break;
                case 2:
                    qnaAdapter.notifyItemRemoved(qnaListPosition);
                    qnaBoardItems = qnaAdapter.deleteItem(qnaListPosition);
                    break;
            }
            setMyQnaPage();
        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnmyBoardRecycler(View view) {
        return new RecyclerItemClickListener(view.getContext(), myBoardRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 포지션값 : " + position);
                Intent intent = new Intent(view.getContext(), QnaBoardDetailActivity.class);
                intent.putExtra("fromActivity",0);
                intent.putExtra("actionKind", 1);
                intent.putExtra("qnaListPosition",position);
                intent.putExtra("qnaBoardItem",qnaBoardItems.get(position));
                startActivityForResult(intent,MY_BOARD_MOVE_TO_DETAIL);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Log.i(TAG, "onLongItemClick: 포지션 값" + position);

            }
        });
    }


}
