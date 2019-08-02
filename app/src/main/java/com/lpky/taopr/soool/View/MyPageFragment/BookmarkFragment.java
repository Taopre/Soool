package com.lpky.taopr.soool.View.MyPageFragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lpky.taopr.soool.Adapter.InfoAdapter;
import com.lpky.taopr.soool.Adapter.RecyclerItemClickListener;
import com.lpky.taopr.soool.Object.InfoItem;
import com.lpky.taopr.soool.Presenter.BookmarkPresenter;
import com.lpky.taopr.soool.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class BookmarkFragment extends Fragment implements BookmarkPresenter.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String TAG = "마이페이지 북마크 프래그먼트";
    private static final String ARG_PARAM1 = "book";
    private static final String ARG_PARAM2 = "mark";

    private RelativeLayout noBookmark;
    private RecyclerView bookmarkRecycler;
    private LinearLayoutManager linearLayoutManager;
    private InfoAdapter infoAdapter;
    private ArrayList<InfoItem> infoItems = new ArrayList<>();
    private final int bookmarkToDetail = 1212;
    private final String ACCOUNT_NO_EXTRA = "accountNo";
    private Context context;
    private BookmarkPresenter bookmarkPresenter;
    private SwipeRefreshLayout bookmarkSwipeRefresh;
    private BookmarkView bookmarkView;
    private int accountNo;

    private boolean hasResponded = false;


    public BookmarkFragment() {
        // Required empty public constructor
    }

    public interface BookmarkView {
        // 현재 프래그먼트가 서버에 요청을 보내고 응답을 받았는지 여부를 마이페이지에 전달
        // (각 프래그먼트가 서버와 통신시에 보여주는 모든 로딩화면을 마이페이지에서 총괄하고 있기 때문)
        // 해당 인터페이스는 HomeActivity 에서 implemented 된다

        void waitingForResponse(); // 북마크 -> 서버 통신요청했음을 홈액티비티를 통해 마이페이지 프래그먼트에 전달
        void receivedResponse(Boolean serverResponded);
        void updateAccountInfo();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // onCreateView, onAttach, onDetach, and onClick are all came from Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        this.context = view.getContext();
        accountNo = getArguments().getInt(ACCOUNT_NO_EXTRA);
        linearLayoutManager = new LinearLayoutManager(view.getContext()); // 리사이클러뷰에 선 추가할때 쓰임(가로 세로 여부)

        // RecyclerView
        bookmarkRecycler = view.findViewById(R.id.bookmarkRecycler);
        bookmarkRecycler.addItemDecoration(new DividerItemDecoration(view.getContext(),
                linearLayoutManager.getOrientation()));
        bookmarkRecycler.setLayoutManager(linearLayoutManager);
        bookmarkRecycler.addOnItemTouchListener(itemClickListener(view));

        // Layout to be displayed when there's nothing to load to the recylerView
        noBookmark = view.findViewById(R.id.noBookmark);

        bookmarkPresenter = new BookmarkPresenter(context);
        bookmarkPresenter.setView(this);

        if(hasResponded == false) {
            // user first visits this fragment (default value = false)
            // loadingKind = 0 when user first visits the fragment and asks to load data for the first time
            bookmarkView.waitingForResponse();
            bookmarkPresenter.loadData(accountNo, 0);
        } else {
            infoAdapter = new InfoAdapter(this.infoItems, context);
            Log.e(TAG, "onCreateView: infoItems" + infoItems);
            bookmarkRecycler.setAdapter(infoAdapter);
            displayLayout();
        }

        // Swipe to Refresh
        bookmarkSwipeRefresh = view.findViewById(R.id.bookmarkSwipeRefresh);
        bookmarkSwipeRefresh.setOnRefreshListener(this);
        bookmarkSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(context, R.color.greenMain));


        // TODO: paging(RecyclerView.addOnScrollListener)



        return view;
    }

    private void displayLayout() {
        // if the user hasn't bookmarked any articles before
        // let the user know he/she has none.
        // if there is a list of bookmarks,
        // the presenter would have put them into infoItems ArrayList already

        if(infoItems.size() == 0) {
            noBookmark.setVisibility(View.VISIBLE);
            bookmarkRecycler.setVisibility(View.GONE);
        } else {
            noBookmark.setVisibility(View.GONE);
            bookmarkRecycler.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookmarkView) {
            bookmarkView = (BookmarkView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach: ");

        if (bookmarkView != null){
            bookmarkView = null;
        }

    }

    @Override
    public void onClick(View view) {

    }


    // onRefresh came from the SwipeToRefresh Interface
    @Override
    public void onRefresh() {
        // loadingKind = 1 when user swiped to refresh
        bookmarkPresenter.loadData(accountNo, 1);
    }


    // getDataSuccess, getDataFail, showInfoDetail from BookmarkInterface
    @Override
    public void getDataSuccess(ArrayList<InfoItem> infoItems, int loadingKind) {

        // loadingKind 0 : 첫 로딩
        //             1 : 새로고침 재요청
        this.infoItems = infoItems;
        if (infoAdapter == null) {
            infoAdapter = new InfoAdapter(this.infoItems, context);
            bookmarkRecycler.setAdapter(infoAdapter);
        } else {
            infoAdapter.addList(this.infoItems);
        }

        displayLayout();

        hasResponded = true;

        bookmarkSwipeRefresh.setRefreshing(false);
        bookmarkView.receivedResponse(true);

    }

    @Override
    public void getDataFail(String msg) {
        Log.d(TAG, "getDataFail: "+ msg);
        Toast.makeText(context, "페이지에 오류가 있습니다", Toast.LENGTH_SHORT).show();
        bookmarkView.receivedResponse(false);
        hasResponded = false;
        bookmarkSwipeRefresh.setRefreshing(false);

    }

    @Override
    public void showInfoDetail(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }


    /*

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/

    private RecyclerItemClickListener itemClickListener(View view) {
        return new RecyclerItemClickListener(view.getContext(), bookmarkRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bookmarkPresenter.sendItem(infoItems.get(position), getActivity(), position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                bookmarkPresenter.sendItem(infoItems.get(position), getActivity(), position);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // 북마크 리스트 삭제 시 아이템의 리스트 포지션 값을 받고, actionKind 로 구별
            // 아이템 업데이트는 디폴트


            Log.i(TAG, "onActivityResult: RESULT_OK");
            InfoItem infoItem = null;

            if (data!= null && data.getParcelableExtra("infoItem") != null) {
                infoItem = data.getParcelableExtra("infoItem");
            }

            int infoPosition = data.getIntExtra("infoPosition", 0);
            int actionKind = data.getIntExtra("actionKind", 88);


            if (actionKind == 1) {
                infoItems = infoAdapter.removeItem(infoPosition);
            } else {
                infoItems = infoAdapter.updateItem(infoItem, infoPosition);
            }


            // TODO: 포인트 정책 회의 후 댓글 작성 시에는 포인트 지급이 되지 않는다면 actionKind 값이 1일 때는 예외 처리

            // 게시글이 작성, 삭제 , 혹은 댓글 추가, 삭제할 경우 마이페이지에서 '내 게시글' , '내 포인트' 값을 갱신해야 한다
            // HomeActivity 을 통해 MyPage 프래그먼트에 알림을 보낸다
            // MyPage 프래그먼트에서 알림을 받을 경우 '내 게시글' 과 '내 포인트' 값을 갱신한다
            bookmarkView.updateAccountInfo();

            displayLayout();
        }

    }

}
