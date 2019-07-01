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

import com.example.taopr.soool.Adapter.InfoAdapter;
import com.example.taopr.soool.Adapter.RecyclerItemClickListener;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Presenter.InfoFmPresenter;
import com.example.taopr.soool.Presenter.Interface.InfoFmInter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class InfoFragment extends BaseFragment implements InfoFmInter.View, SwipeRefreshLayout.OnRefreshListener {

    String TAG = "인포 프래그먼트";
    private RecyclerView infoRecyclerView;
    private LinearLayoutManager infoLinearLayoutManager;
    private InfoAdapter infoAdapter;
    private Context context;
    private ArrayList<InfoItem> infoItems = new ArrayList<>();
    private InfoFmPresenter infoFmPresenter;
    private final int infoToDetail = 1212; // 상세페이지에서 북마크, 댓글 등 변화가 생겼을때 받아와야함
    private SwipeRefreshLayout infoSwipeRefresh;
    private ProgressBar infoProgress;
    private boolean hasResponded = false; // 서버 응답 여부
    private boolean alreadyRequested = false; // 서버에 데이터 요청 여부 (요청하면 값을 true로 변경)
    private int accountNo;


    public InfoFragment() {
        // Required empty public constructor
        Log.i(TAG, "InfoFragment: ");
    }

    // 액티비티에서 프래그먼트로 값 넘기는 거 확인 --> 원래 왜 파라미터를 스트링 2개로 보내려 했는건가.
    public static InfoFragment newInstance(Context context) {

        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        //   args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        if (getArguments() != null) {
            //  mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");

        View view = inflater.inflate(R.layout.fragment_home_info, container, false);

        context = view.getContext();
        unbinder = ButterKnife.bind(this, view);
        infoProgress = view.findViewById(R.id.infoProgress);
        accountNo = LoginSharedPreferences.getAccountNo(getContext(), "LoginAccount");

        // RecyclerView
        infoRecyclerView = view.findViewById(R.id.infoRecyclerView);
        infoSwipeRefresh = view.findViewById(R.id.infoSwipeRefresh);
        infoLinearLayoutManager = new LinearLayoutManager(context);

        infoRecyclerView.addItemDecoration(new DividerItemDecoration(context, infoLinearLayoutManager.getOrientation())); // divider
        infoRecyclerView.setLayoutManager(infoLinearLayoutManager);
        infoRecyclerView.addOnItemTouchListener(infoItemClickListener());

        // onRefreshListener
        infoSwipeRefresh.setOnRefreshListener(this);
        infoSwipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.greenMain)
        );

        // 서버에서 정보 글 목록을 못받아올 때 예외처리
        if(hasResponded == false) {
            Log.i(TAG, "onCreateView: server response" + hasResponded);
            infoFmPresenter = new InfoFmPresenter(context);
            infoFmPresenter.setView(this);
            infoFmPresenter.loadData(0, accountNo, -1);
        } else {
            Log.i(TAG, "onCreateView: server response" + hasResponded);
            infoAdapter = new InfoAdapter(this.infoItems, context);
            infoRecyclerView.setAdapter(infoAdapter); //
        }

        // Paging by scrolling (Scroll to Load Data)
        infoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int listSize = infoItems.size() - 1; // infoItem 배열 사이즈 (-1를 한 이유는 배열이 0부터 시작하기 때문)
                int visibleItemPosition = infoLinearLayoutManager.findLastCompletelyVisibleItemPosition(); // 제일 마지막 리스트의 포지션 값

                if (listSize == visibleItemPosition) {
                    // 현재 아이템 포지션이 (기존에 서버로부터 받아온) 목록의 마지막 인덱스(배열 크기 -1 값)와 같다면
                    // 받아온 목록을 다 보았다는 뜻이니까

                    if (!alreadyRequested && infoItems.size() > 0) {
                        // (서버에 요청을 보낸 적이 없을 때)
                        // visibleItemPosition 대신 listSize 변수로 한 이유는
                        // 서버에 데이터 요청 중에 onScrolled 함수가 재실행되어도 영향받지 않을 값을 전달하기 위해
                        alreadyRequested = true;
                        int lastPostNo = infoItems.get(listSize).getPostNo();

                        infoFmPresenter.loadData(0, accountNo, lastPostNo);

                    }
                }
            }
        });


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


    // RecyclerView ClickEvent 설정 (click + longClick 모두 상세보기로)
    private RecyclerItemClickListener infoItemClickListener() {
        return new RecyclerItemClickListener(context, infoRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                infoFmPresenter.sendItem(infoItems.get(position), getActivity(), position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                infoFmPresenter.sendItem(infoItems.get(position), getActivity(), position);
            }
        });
    }


    @Override
    public void onRefresh() {
        // 글 목록을 새로고침 할 경우에 서버에 다시 요청보내기
        // 페이징 넘버를 여기서 초기화한다는 건 무슨말일까? (qnaFragment.java onRefresh()참조)

        infoFmPresenter.loadData(1, accountNo, -1);

    }


    @Override
    public void getDataSuccess(ArrayList<InfoItem> infoItems, int loadingKind) {

        // response 를 성공적으로 받았다면 새로고침일 경우 새로고침 아이콘을 없애주고
        // 받아온 infoItem 리스트를 리사이클러뷰에 띄워준다
        // 다음 hasResponded 값을 true 로 변경하여 response 받는데 성공했음을 구별

        if (infoItems.size() == 0) {
            // 더이상 받아올 게시물이 없을 때 (end of page)
            // (stringName => no_more_post로 변경하면 좋겠군)
            Toast.makeText(getContext(), getString(R.string.toast_notice_no_exist_post), Toast.LENGTH_SHORT).show();

        } else {

            switch (loadingKind){
                case 0:
                    // 처음 받아올 때
                    this.infoItems.addAll(infoItems);
                    break;
                case 1:
                    this.infoItems = infoItems;
                    break;
            }

            if (infoAdapter == null) {
                infoAdapter = new InfoAdapter(this.infoItems, context);
                infoRecyclerView.setAdapter(infoAdapter);
            } else {
                infoAdapter.addList(this.infoItems);
            }

        }

        alreadyRequested = false;
        hasResponded = true;
        infoSwipeRefresh.setRefreshing(false);

    }

    @Override
    public void getDataFail(String message) {
        infoSwipeRefresh.setRefreshing(false);

        Log.d(TAG, "getDataFail: " + message);
        Toast.makeText(context, "데이터를 불러오는 데 실패했습니다", Toast.LENGTH_SHORT).show();
        hasResponded = false;
        alreadyRequested = true;

    }


    // 상세보기 페이지로 이동
    @Override
    public void showInfoDetail(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    // 서버와 데이터 통신을 하는 동안 progressBar 띄우기
    @Override
    public void showLoading() {
        infoProgress.setVisibility(View.VISIBLE);
    }

    // 데이터 로딩 성공 후 progressBar 숨기기
    @Override
    public void hideLoading() {
        infoProgress.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + resultCode);

        if (resultCode == RESULT_OK) {
            // 북마크 리스트 삭제 시 아이템의 리스트 포지션 값을 받고, actionKind 로 구별
            // 여기서는 상태 변경밖에 없겠고, 북마크 프래그먼트에서는 삭제, 수정이 가능


            Log.i(TAG, "onActivityResult: RESULT_OK");
            InfoItem infoItem = null;

            if (data!= null && data.getParcelableExtra("infoItem") != null) {
                infoItem = data.getParcelableExtra("infoItem");
                Log.i(TAG, "onActivityResult: ");
            }

            int infoPosition = data.getIntExtra("infoPosition", 0);
            int actionKind = data.getIntExtra("actionKind", 88);

            Log.i(TAG, "onActivityResult: actionKind : " + actionKind);

            infoItems = infoAdapter.updateItem(infoItem, infoPosition);


        }

    }

}
