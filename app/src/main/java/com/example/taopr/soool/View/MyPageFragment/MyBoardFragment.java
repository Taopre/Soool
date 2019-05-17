package com.example.taopr.soool.View.MyPageFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import butterknife.OnClick;


public class MyBoardFragment extends Fragment implements View.OnClickListener{



    private RelativeLayout myBoardNoBoardPage;
    private RecyclerView myBoardRecycler;
    private TextView myBoardMoveToWriteBoard;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    private final int MY_BOARD_MOVE_TO_WRITE_BOARD = 4110;
    private final String TAG = "내 게시물 프래그먼트";
    private static Context context;
    private static Activity activity;

    private OnFragmentInteractionListener mListener;

    public MyBoardFragment() {
        Log.i(TAG, "MyBoardFragment: ");
    }

    public static MyBoardFragment newInstance(String param1, String param2) {
        MyBoardFragment fragment = new MyBoardFragment();
        Bundle args = new Bundle();
       // args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
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
        myBoardRecycler.addOnItemTouchListener(selectItemOnmyBoardRecycler());

        myBoardMoveToWriteBoard = view.findViewById(R.id.myBoardMoveToWriteBoard);
        myBoardMoveToWriteBoard.setOnClickListener(this);

        myBoardNoBoardPage = view.findViewById(R.id.myBoardNoBoardPage);


        // qnaboarditem 리스트 전달 받은후 리사이클러뷰 어댑터에 전달
        qnaBoardItems = getArguments().getParcelableArrayList("qnaBoardItems");


        // 리스트 갯수가 0일 때는 작성한 게시물이 없다는 문구를 보여주면서 리사이클러뷰는 없어지지만
        // 리사이클러뷰에 어댑터를 부착한 이유는 어댑터를 연결해 둔 후에 리스트가 추가할 경우 리사이클러뷰를 업데이트
        // 리스트가 없다가 다시 생겼을 때 어댑터에 연결할 때 view 를 가져오기 힘들것이라 예상해서 이렇게 진행

        for (int a = 0; a < this.qnaBoardItems.size(); a++) {
            Log.i(TAG, "getDataSuccess: 리스트 값 " + a + this.qnaBoardItems.get(a).getTitle() + "  " + this.qnaBoardItems.get(a).getAccountNo());
        }
        myBoardRecycler.setAdapter(new QnaAdapter(qnaBoardItems, view.getContext()));

        // 내 게시물 갯수가 0일 때
        // 작성한 게시물이 없다는 문구를 보여주고
        // 문구 밑에는 게시물을 작성하는 페이지로 이동하는 텍스트 버튼을 생성한다

        // 내 게시물 갯수가 0이 아닐 때
        // 리스트를 프래그먼트에 전달 후 보여주기
        if(qnaBoardItems.size()==0){
            SetNoBoardPage();
        }
        else {
            SetMyBoardPage(view);
        }
        return view;

    }

    // 작성한 게시물이 있을 때는 게시물들을 보여주기
    private void SetMyBoardPage(View view) {
        myBoardNoBoardPage.setVisibility(View.GONE);
        myBoardRecycler.setVisibility(View.VISIBLE);
    }

    // 작성한 게시물이 없을 때는 게시물을 보여주는 view가 아닌
    // 작성한 게시물이 없다는 문구와 게시물 작성 페이지로 이동하는 텍스트 버튼 생성

    private void SetNoBoardPage() {
        myBoardNoBoardPage.setVisibility(View.VISIBLE);
        myBoardRecycler.setVisibility(View.GONE);
    }

    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
            Log.i(TAG, "onButtonPressed: 리스너 notnull");
        }
        else{
            Log.i(TAG, "onButtonPressed: 리스너 null");
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
 /*       if (context instanceof OnFragmentInteractionListener) {
            Log.i(TAG, "onAttach: ");
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
                startActivityForResult(intent,MY_BOARD_MOVE_TO_WRITE_BOARD);
                break;
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnmyBoardRecycler() {
        return new RecyclerItemClickListener(MyBoardFragment.context, myBoardRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 포지션값 : " + position);
                Intent intent = new Intent(view.getContext(), QnaBoardDetailActivity.class);
                intent.putExtra("QnaBoardItem",qnaBoardItems.get(position));
                activity.startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {
                Log.i(TAG, "onLongItemClick: 포지션 값" + position);

            }
        });
    }

}
