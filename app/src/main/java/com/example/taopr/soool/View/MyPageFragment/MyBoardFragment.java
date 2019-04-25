package com.example.taopr.soool.View.MyPageFragment;

import android.app.Activity;
import android.content.Context;
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


import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.R;

import java.util.ArrayList;


public class MyBoardFragment extends Fragment {

    private RecyclerView myBoardRecycler;
    private LinearLayoutManager linearLayoutManager;
    private QnaAdapter qnaAdapter;
    private ArrayList<QnaBoardItem> qnaBoardItems = new ArrayList<>();
    String TAG = "내 게시물 프래그먼트";
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


        // qnaboarditem 리스트 전달 받은후 리사이클러뷰 어댑터에 전달
        qnaBoardItems = getArguments().getParcelableArrayList("qnaBoardItems");
        for(int a=0; a<this.qnaBoardItems.size(); a++){
            Log.i(TAG, "getDataSuccess: 리스트 값 " +a+ this.qnaBoardItems.get(a).getTitle() +"  " + this.qnaBoardItems.get(a).getAccountNo());
        }
        myBoardRecycler.setAdapter(new QnaAdapter(qnaBoardItems, view.getContext()));
        return view;

    }

    public void onRecyclerSetting(ArrayList<QnaBoardItem> qnaBoardItems){
        this.qnaBoardItems = qnaBoardItems ;
        myBoardRecycler.setAdapter(new QnaAdapter(this.qnaBoardItems, MyBoardFragment.context));
    }


    // TODO: Rename method, update argument and hook method into UI event
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnmyBoardRecycler() {
        return new RecyclerItemClickListener(MyBoardFragment.context, myBoardRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: 포지션값 : " + position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Log.i(TAG, "onLongItemClick: 포지션 값" + position);

            }
        });
    }
}
