package com.example.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taopr.soool.Dialog.NoticeDialog;
import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.CommentPresenter;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.TimeCalculator;

import java.util.ArrayList;
import java.util.Arrays;

public class RecommentAdapter extends RecyclerView.Adapter<RecommentAdapter.ViewHolder> implements CommentPresenter.View {

    private Activity activity;
    private Context context;
    public ArrayList<RecommentItem> recommentitems;
    private RecommentItem recommentitem;

    String TAG = "recommentTag";
    int check;

    private CommentPresenter commentPresenter;
    int postNo;
    int commentNo = 9999;
    ArrayList<Integer> commentNums = new ArrayList<>();

    private int accountNo;
    private TimeCalculator timeCalculator;
    private NoticeDialog noticeDialog;
    int recommentPosition;

    String accountNick;

    public RecommentAdapter(Context context, ArrayList<RecommentItem> recommentitems,
                            Activity activity, int postNo, int accountNo, int commentNo, String accountNick) {
        this.activity = activity;
        this.context = context;
        this.recommentitems = recommentitems;
        timeCalculator = new TimeCalculator();
        this.postNo = postNo;

        this.commentNo = commentNo;
        this.accountNo = accountNo;
        this.accountNick = accountNick;

        commentPresenter = new CommentPresenter(activity, context);
        commentPresenter.setView(this);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recomment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView recommentWriter;
        TextView recommentContent;
        TextView recommentLike;
        LinearLayout recomment_row;

        public ViewHolder(View v) {
            super(v);

            recommentWriter = v.findViewById(R.id.recommentWriter);
            recommentContent = v.findViewById(R.id.recommentContent);
            recommentLike = v.findViewById(R.id.recommentLike);
            recomment_row = v.findViewById(R.id.recomment_row);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RecommentItem recommentitem = recommentitems.get(i);


        viewHolder.recommentWriter.setText(recommentitem.getCommentWriter() + " " + timeCalculator.getbeforeTime(recommentitem.getDate()));
        viewHolder.recommentContent.setText(recommentitem.getCommentContent());

        String likeCount = String.valueOf(recommentitem.getLikeCount());

        final int[] likeList = recommentitem.getLikeList();
        Arrays.sort(likeList);
        check = Arrays.binarySearch(likeList, accountNo);//생성자에 회원번호 추가
        //Log.d(TAG, String.valueOf(check) + "@@@@@@@@@@@@@@@@@@@@@@@@@" + String.valueOf(position));
        //추후에 아이콘색 채워넣기로 바꿔야됨됨

        if (check >= 0) {
            recommentitem.setAccountNo(1);
            //이미 좋아요를 누른 댓글
            viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
        } else {
            recommentitem.setAccountNo(0);
            //안누른 댓글
            viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
        }

        //해당 댓글에 좋아요를 누르지 않은 회원 //2016/06/07 아직 진행중
        viewHolder.recommentLike.setText("추천 " + likeCount);
        viewHolder.recommentLike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //RecommentItem recommentItemA = recommentitems.get(i);
                Log.d(TAG, String.valueOf(check));
                int commentORrecomment = 1;
                if (recommentitem.getAccountNo() == 1) {
                    //좋아요 취소
                    int like_check = 0;
                    commentPresenter.likeRequest(postNo, commentNo, accountNo, like_check, commentORrecomment, recommentitem.getRecommentNo());
                    //이미 눌렀는데 또 누를경우
                    Log.d(TAG, "과연 좋아요 눌렀을때");
                    if (recommentitem.getLikeCount() == 0) {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount());

                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    } else if (recommentitem.getLikeCount() >= 1) {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount() - 1);
                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    }

                    //String update_like_count = String.valueOf(commentitem.getLikeCount()- 1);
                    viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
                    recommentitem.setAccountNo(0);
                    //notifyDataSetChanged();
                } else if (recommentitem.getAccountNo() == 0) {
                    //좋아요
                    int like_check = 1;
                    commentPresenter.likeRequest(postNo, commentNo, accountNo, like_check, commentORrecomment, recommentitem.getRecommentNo());

                    if (recommentitem.getLikeCount() == 0) {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount() + 1);
                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    } else if (recommentitem.getLikeCount() >= 1) {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount());

                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    }

                    viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
                    recommentitem.setAccountNo(1);
                }

                //like_confirm setText글자만 change
            }
        });


        //holder.comment_rowBG.setVisibility(View.GONE);
        View.OnClickListener positiveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (recommentitem.getCommentWriter().equals(accountNick)) {
                    recommentPosition = i;
                    commentPresenter.commentDeleteRequest(postNo, commentNo, recommentitem.recommentNo);
                    Log.d(TAG, "onClick: recommentDelete");
                    Log.d(TAG, "onClick: recommentDelete" + String.valueOf(recommentitem.recommentNo));
                    Log.d(TAG, "onClick: recommentDelete" + String.valueOf(commentNo));
                } else {
                    Toast.makeText(context, "본인 댓글만 삭제 하실 수 있습니다", Toast.LENGTH_LONG).show();
                }
                noticeDialog.dismiss();
            }
        };

        View.OnClickListener negativeListener = new View.OnClickListener() {
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        };

        //viewHolder.recomment_row.setAlpha(0.01f);
        viewHolder.recomment_row.setLongClickable(true);
        viewHolder.recomment_row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                noticeDialog = new NoticeDialog(context,
                        "답글을 삭제하시겠습니까?", false, "예",
                        "아니요", positiveListener, negativeListener);
                noticeDialog.show();

                return false;
            }
        });


        Log.d(TAG, String.valueOf(recommentitem.getRecommentNo()));
    }

    @Override
    public int getItemCount() {
        return recommentitems.size();
    }


    @Override
    public void getCommentDataSuccess(ArrayList<CommentItem> commentitem) {

    }

    @Override
    public void getCommentDataFail(String message) {

    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {

    }

    @Override
    public void commentInsertGoResponse(int response, int commentCount, CommentItem commentItem) {

    }

    @Override
    public void recommentInsertGoResponse(int response, RecommentItem recommentItem, int commentNo) {


    }

    @Override
    public void likeGoResponse(int response) {

    }

    @Override
    public void CommentOrRecommentActivity(int commentNo) {

    }

    @Override
    public void commentDeleteGoResponse(int response, int commentCount, int commentNo) {
        recommentitems.remove(recommentPosition);
        notifyDataSetChanged();
    }
}
