package com.lpky.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.lpky.taopr.soool.Adapter.MainInfoAdapter;
import com.lpky.taopr.soool.Adapter.QnaAdapter;
import com.lpky.taopr.soool.Adapter.RecyclerItemClickListener;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.Object.QnaBoardItem;
import com.lpky.taopr.soool.Presenter.Interface.MainFmInter;
import com.lpky.taopr.soool.Presenter.MainFmPresenter;
import com.lpky.taopr.soool.R;

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
    private MainInfoAdapter mainInfoAdapter = null;
    private MainFmView mainFmView;
    private Handler handler = null;
    private Thread thread = null;
    private int infoCurrentPage=0;	// info 현재 페이지번호
    private int infoPageMoveDirection=0; // info 뷰페이저 화면 전환. 방향 0이 오른쪽, 1이 왼쪽
    private boolean viewPagerthreadDead = false; // false 가 살아있음 , true 가 죽은거
    private final int MAIN_MOVE_TO_QNA = 1100;
    private final int MAIN_MOVE_TO_INFO = 1200;

    public interface MainFmView{
        void mainUpdateQnaItem(QnaBoardItem qnaBoardItem,int actionKind,boolean updateByUser); // qnaBoard
        void mainUpdateInfoItem();
    }

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

        viewPagerthreadDead = false; // 메인 화면을 부착할 경우 스레드가 살아 있다는 상태를 저장하는 변수
        if (mainInfoAdapter != null ){
            infoViewAutoChange();
        }

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

            handler = new Handler(){

                public void handleMessage(android.os.Message msg) {
                    if (mainInfoViewPager != null) {
                        if (mainInfoViewPager.getCurrentItem() == 0) {

                            mainInfoViewPager.setCurrentItem(1);

                            //  infoCurrentPage++;
                            infoPageMoveDirection = 0;

                        } else if (mainInfoViewPager.getCurrentItem() == 1 && infoPageMoveDirection == 0) {
                            mainInfoViewPager.setCurrentItem(2);
                            infoPageMoveDirection = 1;
                            //  infoCurrentPage++;

                        } else if (mainInfoViewPager.getCurrentItem() == 1 && infoPageMoveDirection == 1) {
                            mainInfoViewPager.setCurrentItem(0);
                            infoPageMoveDirection = 0;
                            //infoCurrentPage++;

                        } else if (mainInfoViewPager.getCurrentItem() == 2) {
                            mainInfoViewPager.setCurrentItem(1);
                            //  infoCurrentPage=0;
                            infoPageMoveDirection = 1;

                        }
                    }

                }
            };
            infoViewAutoChange();
        }
    }

    private void infoViewAutoChange(){

        if (thread == null) {
      //  if (viewPagerthreadDead) {
            thread = new Thread() {

                //run은 jvm이 쓰레드를 채택하면, 해당 쓰레드의 run메서드를 수행한다.

                public void run() {

                    super.run();

                    while (!thread.isInterrupted()) {

                        try {
                            Thread.sleep(4000);
                            if (viewPagerthreadDead) {
                                thread.interrupt();
                                thread = null;
                                break;
                            }

                            if (!thread.isInterrupted()) {
                                handler.sendEmptyMessage(0);
                            }

                        } catch (InterruptedException e) {
                            // Log.i(TAG, "run: int");
                            e.printStackTrace();
                        }
                    }
                }
            };
            if (thread != null) {
                thread.start();
            }
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

    // TODO: info 게시글인지 qna 게시글인지에 대한 구분자 값을 통해서 따로 처리 필요

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MAIN_MOVE_TO_QNA:
                    // 리스트 수정, 삭제
                    // 리스트 수정, 삭제의 경우에는 리스트에 있는 아이템을 수정,삭제를 하는 것이기 때문에
                    // 아이템의 리스트 포지션 값을 받고 , actionKind 로 수정인지 삭제인지 구별
                    // 수정 --> actionKind = 1
                    // 삭제 --> actionKind = 2
                    int qnaListPosition = data.getIntExtra("qnaListPosition", 0);
                    int actionKind = data.getIntExtra("actionKind", 99);

                    QnaBoardItem qnaBoardItem = null;
                    if (data != null && data.getParcelableExtra("qnaBoardItem") != null) {
                        qnaBoardItem = data.getParcelableExtra("qnaBoardItem");
                    }

                    // main 에서는 글 작성하는 페이지로 이동하는 경우가 없기 때문에
                    // actionKind 값이 0일 경우에는 예외처리
                    if (actionKind != 99 && actionKind != 0) {
                        switch (actionKind) {

                            case 1:
                                qnaAdapter.modifyItem(qnaBoardItem, qnaListPosition);
                                break;
                            case 2:
                                qnaAdapter.deleteItem(qnaListPosition);
                                break;
                        }
                        // 업데이트된 글의 작성자가 유저와 일치할 경우 true , 아닐 경우 false
                        // true 일 경우일 때만 마이페이지, 커뮤니트의 글 업데이트
                        // false 커뮤니티만 업데이트

                        // 업데이트된 QnaBoardItem을 Home 액티비티에 전달
                        if (qnaBoardItem.getAccountNo() == mainFmPresenter.accountNo) {
                            mainFmView.mainUpdateQnaItem(qnaBoardItem, actionKind, true);
                        } else {
                            mainFmView.mainUpdateQnaItem(qnaBoardItem, actionKind, false);
                        }
                    }
                    break;
                case MAIN_MOVE_TO_INFO:
                    // actionKind 가 1일때 업데이트된 정보가 있는 경우

                    Log.i(TAG, "onActivityResult: 인포");
                    InfoItem infoItem;
                    if (data != null && data.getParcelableExtra("infoItem") != null) {
                        infoItem = data.getParcelableExtra("infoItem");
                        int infoPosition = data.getIntExtra("infoPosition",99);
                        if (infoPosition!= 99){
                            mainInfoAdapter.infoItemUpdate(infoItem,infoPosition);
                        }
                    }

            }
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFmView) {
            mainFmView = (MainFmView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mainFmView!=null){
            mainFmView = null;
        }
        thread.interrupt();
        viewPagerthreadDead = true;
       // thread = null;
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


    public void updateQnaBoardItem(QnaBoardItem qnaBoardItem,int actionKind){
        switch (actionKind){
            case 1:
                qnaAdapter.modifyItem(qnaBoardItem);
                break;
            case 2:
                qnaAdapter.deleteItem(qnaBoardItem);
                break;
        }
    }
    public void updateMainInfo(){}
}
