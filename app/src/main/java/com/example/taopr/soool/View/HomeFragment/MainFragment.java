package com.example.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.taopr.soool.Adapter.MainInfoAdapter;
import com.example.taopr.soool.Adapter.QnaAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Object.QnaBoardItem;
import com.example.taopr.soool.Presenter.Interface.MainFmInter;
import com.example.taopr.soool.Presenter.MainFmPresenter;
import com.example.taopr.soool.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends BaseFragment  implements MainFmInter.View{

    @BindView(R.id.mainProgress)
    ProgressBar mainProgress;
    @BindView(R.id.mainQnaRecycler)
    RecyclerView mainQnaRecycler;
    @BindView(R.id.mainInfoViewPager)
    ViewPager mainInfoViewPager;

    String TAG = "홈 메인 프래그먼트";
    private LinearLayoutManager linearLayoutManager;
    private MainFmPresenter mainFmPresenter;
    private QnaAdapter qnaAdapter;
    private MainInfoAdapter mainInfoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // qna , info 글 리스트를 받아왔는지 확인을 하고 못 받은 글 리스트에 대해서 서버에 요청한다

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main, container, false);
        unbinder = ButterKnife.bind(this,view);

        linearLayoutManager = new LinearLayoutManager(getContext());
        mainQnaRecycler.addItemDecoration(              // divider 구분선
                new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation()));
        mainQnaRecycler.setLayoutManager(linearLayoutManager);
        mainQnaRecycler.addOnItemTouchListener(selectItemOnqnaRecycler());

        if (mainFmPresenter == null){
            mainFmPresenter = new MainFmPresenter(getContext());
            mainFmPresenter.setView(this);
        }

        infoList(); // info 글 리스트 관련
        qnaBoardList(); // qna 글 리스트 관련

        return view;
    }

    private void infoList() {

        // info response 를 받지 못했다면
        if (!mainFmPresenter.infoResSuccess){
            mainFmPresenter.loadInfoList();
        }
        else{
            // info 글 뷰로 보여주는 부분
            mainInfoViewPager.setAdapter(mainInfoAdapter);
        }
    }

    private void qnaBoardList() {
        // qna 리스트 글 부분
        if (!mainFmPresenter.qnaBoardResSuccess){
            mainFmPresenter.loadQnaBoardList();
        }
        else{
            // qna 글 리스트 뷰로 보여주는 부분
            mainQnaRecycler.setAdapter(qnaAdapter);
        }
    }

    // qna 게시글을 가져오는데 성공했을 경우 받아온 게시글을 게시글 리사이클러뷰의 어댑터에 전달
    @Override
    public void getQnaBoardSuccess(ArrayList<QnaBoardItem> qnaBoardItems) {
        if (qnaAdapter == null) {
            qnaAdapter = new QnaAdapter(qnaBoardItems, getContext());
            mainQnaRecycler.setAdapter(qnaAdapter);
        }
    }

    @Override
    public void getInfoSuccess(ArrayList<InfoItem> infoItems) {
        if( mainInfoAdapter == null){
            mainInfoAdapter = new MainInfoAdapter(getContext(),infoItems,new MainInfoAdapter.OnItemClick() {
                @Override
                public void onItemClick(int position) {
                    InfoItem clickInfoItem = mainInfoAdapter.getInfoItem(position);
                    mainFmPresenter.getIntentMoveInfo(getActivity(),position,clickInfoItem);
                }
            });
            mainInfoViewPager.setAdapter(mainInfoAdapter);
        }

    }

    // 서버로부터 리스폰스를 받는데 실패 했을 경우
    // 0 -> info 리스트 , 1 -> qna 리스트
    @Override
    public void getDataFail(int dataType) {
        switch (dataType){
            case 0:
                break;
            case 1:
                break;
        }
    }

    // 메인 페이지에서는 게시글을 추가하는 경우는 없다
    // 그래서 수정과 삭제 두 가지 상황에 대한 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 리스트 수정, 삭제
            // 리스트 수정, 삭제의 경우에는 리스트에 있는 아이템을 수정,삭제를 하는 것이기 때문에
            // 아이템의 리스트 포지션 값을 받고 , actionKind 로 수정인지 삭제인지 구별
            // 수정 --> actionKind = 1
            // 삭제 --> actionKind = 2
            int qnaListPosition = data.getIntExtra("qnaListPosition",0);
            int actionKind = data.getIntExtra("actionKind",99);

            switch (actionKind){

                case 1:
                    QnaBoardItem qnaBoardItem = null;
                    if (data.getParcelableExtra("qnaBoardItem") != null) {
                        qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
                    }
                    qnaAdapter.modifyItem(qnaBoardItem,qnaListPosition);
                    break;
                case 2:
                    qnaAdapter.deleteItem(qnaListPosition);
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    // 리사이클러뷰 클릭 이벤트
    // click과 Longclick 둘 다 해당 리스트 상세보기로 넘어가는게 설정
    private RecyclerItemClickListener selectItemOnqnaRecycler() {
        return new RecyclerItemClickListener(getContext(), mainQnaRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                QnaBoardItem qnaBoardItem = qnaAdapter.getQnaBoardItem(position);
                mainFmPresenter.getIntentMoveQna(getActivity(),position,qnaBoardItem);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                QnaBoardItem qnaBoardItem = qnaAdapter.getQnaBoardItem(position);
                mainFmPresenter.getIntentMoveQna(getActivity(),position,qnaBoardItem);
            }
        });
    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void showLoading() { mainProgress.setVisibility(View.VISIBLE); }

    @Override
    public void hideLoading() { mainProgress.setVisibility(View.GONE);}


}
