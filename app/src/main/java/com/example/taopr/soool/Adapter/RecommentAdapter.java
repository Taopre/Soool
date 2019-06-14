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
import android.widget.TextView;

import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.QnaDetailPresenter;
import com.example.taopr.soool.R;
import com.example.taopr.soool.TimeCalculator;

import java.util.ArrayList;
import java.util.Arrays;

public class RecommentAdapter extends RecyclerView.Adapter<RecommentAdapter.ViewHolder> implements QnaDetailPresenter.View
        {

private Activity activity;
private Context context;
private ArrayList<RecommentItem> recommentitems;
private RecommentItem recommentitem;

            String TAG = "recommentTag";
            int check;
            int like_confirm = 0;
            private QnaDetailPresenter qnaDetailPresenter;
            int postNo;
            int commentNo;

private int accountNo;
private TimeCalculator timeCalculator;

public RecommentAdapter(Context context, ArrayList<RecommentItem> recommentitems,Activity activity,int postNo,int commentNo)
        {
        this.activity = activity;
        this.context = context;
        this.recommentitems = recommentitems;
        timeCalculator = new TimeCalculator();
        this.postNo = postNo;
        this.commentNo = commentNo;
        //this.accountNo = accountNo;
        }
public RecommentAdapter(Context context,RecommentItem recommentitem)
        {
        this.context = context;
        this.recommentitem = recommentitem;

        Log.d(TAG,"생성완료");
        }


@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recomment,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
        }



            class ViewHolder extends RecyclerView.ViewHolder
{
    //TextView recommentNo;
    //TextView accountNo;
    //TextView date;
    TextView recommentWriter;
    TextView recommentContent;
    TextView recommentLike;


    public ViewHolder(View v)
    {
        super(v);

        //recommentNo = v.findViewById(R.id.recommentNo);
        //accountNo = v.findViewById(R.id.accountNo);
        //date = v.findViewById(R.id.date);

        recommentWriter = v.findViewById(R.id.recommentWriter);
        recommentContent = v.findViewById(R.id.recommentContent);
        recommentLike  = v.findViewById(R.id.recommentLike);
    }
}

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        RecommentItem recommentitem = recommentitems.get(i);

//        viewHolder.date.setText(recommentitem.getDate());
//        viewHolder.recommentNo.setText(String.valueOf(recommentitem.getRecommentNo()));
//        viewHolder.accountNo.setText(String.valueOf(recommentitem.getAccountNo()));

        viewHolder.recommentWriter.setText(recommentitem.getCommentWriter() + " " + timeCalculator.getbeforeTime(recommentitem.getDate()));
        viewHolder.recommentContent.setText(recommentitem.getCommentContent());

//        viewHolder.recommentNo.setText(recommentNoS);
//        viewHolder.accountNo.setText(accountNoS);
//        viewHolder.commentWriter.setText(commentWriterS);
//        viewHolder.date.setText(dateS);
//        viewHolder.commentContent.setText(commentContentS);


        String likeCount  = String.valueOf(recommentitem.getLikeCount());

        final int[] likeList = recommentitem.getLikeList();
        Arrays.sort(likeList);
        check = Arrays.binarySearch(likeList,accountNo);//생성자에 회원번호 추가
        //Log.d(TAG, String.valueOf(check) + "@@@@@@@@@@@@@@@@@@@@@@@@@" + String.valueOf(position));
        //추후에 아이콘색 채워넣기로 바꿔야됨됨

        if (check >= 0)
        {
            like_confirm = like_confirm + 1;
            //이미 좋아요를 누른 댓글
            viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
        }
        else
        {
            like_confirm = like_confirm + 0;
            //안누른 댓글
            viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
        }
        qnaDetailPresenter = new QnaDetailPresenter(activity,context);
        qnaDetailPresenter.setView(this);
        //해당 댓글에 좋아요를 누르지 않은 회원 //2016/06/07 아직 진행중
        viewHolder.recommentLike.setText("추천 " +likeCount);
        viewHolder.recommentLike.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                Log.d(TAG,String.valueOf(check));
                int commentORrecomment = 1;
                if (like_confirm == 1)
                {
                    //좋아요 취소
                    int like_check = 0;
                    qnaDetailPresenter.likeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentitem.getRecommentNo());
                    //이미 눌렀는데 또 누를경우
                    Log.d(TAG,"과연 좋아요 눌렀을때");
                    if (recommentitem.getLikeCount() == 0)
                    {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount());

                        viewHolder.recommentLike.setText("추천" + update_like_count);
                    }
                    else if (recommentitem.getLikeCount() >= 1)
                    {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount()- 1);
                        viewHolder.recommentLike.setText("추천"+update_like_count);
                    }

                    //String update_like_count = String.valueOf(commentitem.getLikeCount()- 1);
                    viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
                    like_confirm = like_confirm - 1;
                    //notifyDataSetChanged();
                }
                else if (like_confirm == 0)
                {
                    //좋아요
                    int like_check = 1;
                    qnaDetailPresenter.likeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentitem.getRecommentNo());

                    if (recommentitem.getLikeCount() == 0)
                    {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount() + 1);
                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    }
                    else if (recommentitem.getLikeCount() >= 1)
                    {
                        String update_like_count = String.valueOf(recommentitem.getLikeCount());

                        viewHolder.recommentLike.setText("추천 " + update_like_count);
                    }

                    viewHolder.recommentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
                    like_confirm = like_confirm + 1;
                    // check = check * -1;
                    //notifyDataSetChanged();
                }

                //like_confirm setText글자만 change
            }
        });


        Log.d(TAG,String.valueOf(recommentitem.getRecommentNo()));
    }

    @Override
    public int getItemCount()
    {
        return recommentitems.size();
    }


            @Override
            public void getDataSuccess(QnaVoteItem qnaVoteItem) {

            }

            @Override
            public void getDataFail(String message) {

            }

            @Override
            public void recommendComplete(boolean flag, BoardRecommend boardRecommend) {

            }

            @Override
            public void updateVoteResultComplete(boolean flag, QnaVoteItem qnaVoteItem) {

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
            public void commentInsertGoResponse(int response,int commentCount) {

            }

            @Override
            public void recommentInsertGoResponse(int response) {

            }

            @Override
            public void likeGoResponse(int response) {

            }

            @Override
            public void CommentOrRecommentActivity(int commentNo) {

            }

            @Override
            public void commentDeleteGoResponse(int response,int commentCount) {

            }
        }
