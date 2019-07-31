package com.lpky.taopr.soool.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lpky.taopr.soool.Dialog.NoticeDialog;
import com.lpky.taopr.soool.Object.CommentItem;
import com.lpky.taopr.soool.Object.RecommentItem;
import com.lpky.taopr.soool.Presenter.CommentPresenter;
import com.lpky.taopr.soool.Presenter.QnaDetailPresenter;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.TimeCalculator;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements CommentPresenter.View
{

    private Activity activity;
    private Context context;
    private ArrayList<CommentItem> commentitems;
    String TAG = "commentTag";
    private boolean recommentViewOrHide = false; //답글 보기 숨기기 버튼 클릭시 쓰는 boolean
    private boolean CommentOrRecomment = false; //답글insert할때에 쓰는 boolean
    private boolean afterRecommentInsertListView = false;
    private QnaDetailPresenter qnaDetailPresenter;

    int postNo;
    int accountNo;//좋아요기능시 필요
    String accountNick;
    int check;
    int[] likeList;

    private TimeCalculator timeCalculator;
    private NoticeDialog noticeDialog;


    RecommentItem recommentitem;
    ArrayList<RecommentItem> recommentitems = new ArrayList<>();
    ArrayList<Integer> commnetNums = new ArrayList<>();
    RecommentAdapter recommentAdapter;



    public interface toss_commentNo_interface
    {
        public void toss_comment_position(int position);
        public void toss_commentNo_atActivity(int commentNo,String commentWriter,int position);
        public void toss_commentCount_actiivity(int commentCount,int commentDeleteOrRecommentDelete,int deletePosition);
        public void toss_likeRequest_activity(int postNo,int commentNo,int accountNo,int like_check,int commentORrecomment,int recommentNo);

    }
    private toss_commentNo_interface toss_commentNo_interface;

    public void toss_commentNo_Methods(toss_commentNo_interface toss_commentNo_interface)
    {
        this.toss_commentNo_interface = toss_commentNo_interface;
    }


    public CommentAdapter(Activity activity, ArrayList<CommentItem> commentitems, Context context,int postNo,int accountNo,String accountNick)
    {
        this.activity = activity;
        this.context = context;
        this.commentitems = commentitems;
        this.postNo = postNo;
        this.accountNo = accountNo;
        timeCalculator = new TimeCalculator();
        this.accountNick = accountNick;

        recommentAdapter = new RecommentAdapter(context,recommentitems,activity,postNo,accountNo,0,accountNick);

    }

    @Override
    public int getItemCount()
    {
        return commentitems.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }



    class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView commentWriter;
        TextView date;
        TextView commentContent;
        TextView commentLike;
        TextView recommentCount;


        TextView recommentView; //답글보기 / 숨기기 버튼
        RecyclerView recommentList; //답글 RecyclerView

        TextView recomment_insert; // 답글달기 버튼
        TextView commentDelete; //댓삭제 버튼


        LinearLayout comment_row;
        ImageView comment_delete_img;

        public ViewHolder(View v)
        {
            super(v);


            commentWriter = v.findViewById(R.id.commentWriter);
            date = v.findViewById(R.id.date);
            commentContent = v.findViewById(R.id.commentContent);
            commentLike = v.findViewById(R.id.commentLike);
            recommentCount = v.findViewById(R.id.recommentCount);
            comment_delete_img = v.findViewById(R.id.comment_delete_img);


            recommentView = v.findViewById(R.id.recommentView);
            recommentList = v.findViewById(R.id.recommentList);
            recomment_insert = v.findViewById(R.id.recomment_insert);
            commentDelete = v.findViewById(R.id.commentDelete);

            comment_row = v.findViewById(R.id.comment_row);

            LinearLayoutManager manager = new LinearLayoutManager(context)
            {
                @Override
                public  boolean canScrollVertically()
                {
                    return false;
                }
            };
            recommentList.setLayoutManager(manager);
            recommentList.setAdapter(recommentAdapter);

        }


    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position)
    {

       CommentItem commentitem = commentitems.get(position);


        if (commentitem.getRecommentCount() == 0)
        {
            holder.recomment_insert.setVisibility(View.GONE);
            holder.recommentView.setText("답글달기");
        }
        else
        {
            holder.recomment_insert.setVisibility(View.VISIBLE);
            holder.recommentView.setVisibility(View.VISIBLE);
            holder.recommentView.setText("답글보기");
            if (afterRecommentInsertListView == true)
            {
                int rightpadding =
                        (int) context.getResources().getDimension(R.dimen.recomment_hide_padding);
                holder.recomment_insert.setPadding(0,0,rightpadding,0);
                holder.recommentView.setText("답글숨기기");
            }
        }



        holder.commentWriter.setText(commentitem.getCommentWriter());
        //글쓴이 작성자 옆에 받은 date계산해서 몇분전 text띄우기
        holder.date.setText(timeCalculator.getbeforeTime(commentitem.date));


        holder.commentContent.setText(commentitem.getCommentContent());
        holder.recommentCount.setText("답글 " +String.valueOf(commentitem.getRecommentCount()));




        //답글 달기라는 버튼 클릭시
        //commentActivity에 있는 commentContent EditText에 @댓글 작성자 hint 생성
        //commentActivity에선 작성버튼 click할때 이게 답글인지 댓글인지 판단 필요
        //판단어떻게
        //CommentOrRecomment 기본 false
        //답글 달기라는 버튼 1번 클릭시 -
        //CommentOrRecomment  true setText(답글 취소)
        //답글 버튼 2번클릭시
        //CommentOrRecomment false setText(답글 달기)
        //
        //댓글번호와 CommentOrRecomment 넘겨받는 interface생성
        //CommentOrRecomment 넘겨받고 Activity에넘김
        //Activity에선 작성버튼 클릭시
        //해당 메소드로 true , false판단
        //false일경우 commentpresenter.commentRequset()
        //true일경우  recomment.Requset()호출



        //textChage메소드 답글작성버튼 클릭시 아래있는 코드 삽입 -> @답글작성자 텍스트 붙이는 메소드

        holder.recomment_insert.setText("답글달기");
        holder.recomment_insert.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (CommentOrRecomment == false)
                {
                    CommentOrRecomment = true;
                    toss_commentNo_interface.toss_commentNo_atActivity(commentitem.getCommentNo(),commentitem.getCommentWriter(),position);
                }
                else if(CommentOrRecomment == true)
                {

                    CommentOrRecomment = false;
                    holder.recomment_insert.setText("답글달기");
                    toss_commentNo_interface.toss_commentNo_atActivity(0,commentitem.getCommentWriter(),position);
                }
                
            }
        });
        

        //interface에선 댓글번호를 넘겨받으면 메소드가 호출되며
        //해당 메소드는 commentActivity에 댓글번호를 넘겨주는 메소드 호출
        //
        //댓글일경우 현재 폼 commentInsertPresent.commentRequest()
        //답글일경우
        //답글 붙일떄 위작업만 하면 끝, view에 뿌리는거 insert는 다 했기 떄문
        //2019/05/29 = insert전까지의 과정 위에 적어놓은 문제때문에 정리 아직안됨
        //commentList - 필요없는 activity
        //Networking폴더에있는파일,basePresentet파일,MainActivity,recommentList_objects,Whatisthis
        //=원 프로젝트에 있는 파일 또는 필요없는파일(recommentList_objects)를 제외하고 정리 - 내가 보기 어려움
        //5월 30일 까지 답글쓰고 불러오는거까지 붙이고 파일 정리 mvp적용후
        //본 프로젝트에 붙이고 좋아요와 수정삭제가 제플린에없네? ㅇㅋ 건너띔 ㅅㄱ 해도 삭제만하자..
        //좋아요는 바로아래있음


        ///// 댓글 좋아요 기능 ////
        String likeCount  = String.valueOf(commentitem.getLikeCount());
        likeList = commentitem.getLikeList();
        Arrays.sort(likeList);
        check = Arrays.binarySearch(likeList,accountNo);//생성자에 회원번호 추가
        if (check >= 0)
        {
            //이미 좋아요를 누른 댓글
            holder.commentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));
            commentitem.setAccountNo(1);
        }
        else
        {
            //안누른 댓글
            holder.commentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));
            commentitem.setAccountNo(0);
        }

         //해당 댓글에 좋아요를 누르지 않은 회원
        holder.commentLike.setText("추천 " +likeCount);
        holder.commentLike.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                int commentORrecomment = 0;
                Log.d(TAG, "GDGSDGSDG" + commentitem.getAccountNo());
                if (commentitem.getAccountNo() == 1)
                {
                    //좋아요 취소
                    //이미 눌렀는데 또 누를경우
                        int like_check = 0;
                    Log.d(TAG, "GDGSDGSDG" + "취소" + like_check);
                        toss_commentNo_interface.toss_likeRequest_activity(postNo,commentitem.getCommentNo(),accountNo,like_check,commentORrecomment,0);

                        if (commentitem.getLikeCount() == 0)
                        {
                            String update_like_count = String.valueOf(commentitem.getLikeCount());
                            holder.commentLike.setText("추천 " + update_like_count);
                        }
                        else if (commentitem.getLikeCount() >= 1)
                        {
                            String update_like_count = String.valueOf(commentitem.getLikeCount() - 1);
                            holder.commentLike.setText("추천 " + update_like_count);
                        }

                        holder.commentLike.setTextColor(ContextCompat.getColor(context, R.color.grayMain));

                        commentitem.setAccountNo(0);
                }
                else if (commentitem.getAccountNo() == 0)
                {
                        //좋아요
                    int like_check = 1;
                    toss_commentNo_interface.toss_likeRequest_activity(postNo,commentitem.getCommentNo(),accountNo,like_check,commentORrecomment,0);
                        if (commentitem.getLikeCount() == 0)
                        {
                            String update_like_count = String.valueOf(commentitem.getLikeCount() + 1);
                            holder.commentLike.setText("추천 " + update_like_count);
                        }
                        else if (commentitem.getLikeCount() >= 1)
                        {
                            String update_like_count = String.valueOf(commentitem.getLikeCount());

                            holder.commentLike.setText("추천 " + update_like_count);
                        }
                        holder.commentLike.setTextColor(ContextCompat.getColor(context, R.color.greenDark));

                     commentitem.setAccountNo(1);
                }

            }
        });

        if (afterRecommentInsertListView == false)
        {
            holder.recommentList.setVisibility(View.GONE);
        }
        else
        {
            if (recommentitems.size() > 0 )
            {
                if (commentitem.recommentCount == 0)
                {
                    recommentitems = new ArrayList<>();
                    holder.recommentList.setVisibility(View.GONE);
                }
                if (recommentitems.get(0).getCommentNo() == commentitem.getCommentNo())
                {
                    recommentAdapter.commentNo = commentitem.getCommentNo();
                    recommentAdapter.recommentitems = recommentitems;
                    holder.recommentList.setVisibility(View.VISIBLE);
                }

                afterRecommentInsertListView= false;
            }
        }


        //2019/07/20 16:28 action6이 안됨
        //action6
        //1: 1번 답글 보기 버튼 클릭
        //2: 1번 답글 작성
        //3: 2번 댓글 답글 보기 버튼 클릭
        //action5번 문제 and 대댓글이 없는글에 대댓글을 달고 다른 댓글에 대댓글을 달면 error
        //


            holder.recommentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    v.setTag(position);
                    if (holder.recommentView.getText().toString().equals("답글달기"))
                    {
                        if (CommentOrRecomment == false)
                        {
                            CommentOrRecomment = true;
                            toss_commentNo_interface.toss_commentNo_atActivity(commentitem.getCommentNo(),commentitem.getCommentWriter(),position);
                        }
                        else if(CommentOrRecomment == true)
                        {

                            CommentOrRecomment = false;
                            holder.recomment_insert.setText("답글달기");
                            toss_commentNo_interface.toss_commentNo_atActivity(0,commentitem.getCommentWriter(),position);

                        }                    }
                    else
                    {
                        if (!holder.recommentList.isShown())
                        {
                            if (recommentitems.size() > 0) //이미 Arraylist에 item이 존재할때
                            {
                                if (recommentitems.get(0).getCommentNo() != commentitem.getCommentNo())
                                {
                                    recommentitems = new ArrayList<>();
                                }
                            }
                            if(afterRecommentInsertListView == false)
                            {
                                CreateRecommentItem(commentitem);
                            }

                            recommentAdapter.commentNo = commentitem.getCommentNo();
                            recommentAdapter.recommentitems = recommentitems;
                            holder.recommentList.setVisibility(View.VISIBLE);
                            int rightpadding =
                                    (int) context.getResources().getDimension(R.dimen.recomment_hide_padding);
                            holder.recomment_insert.setPadding(0,0,rightpadding,0);
                            holder.recommentView.setText("답글숨기기");
                            recommentViewOrHide = true;

                        }
                        else
                        {

                            String DeleteContent =  holder.commentContent.getText().toString();
                            if(DeleteContent.equals("삭제된 댓글입니다."))
                            {
                                recommentitems.clear();
                            }


                            holder.recommentList.setVisibility(View.GONE);
                            int rightpadding =
                                    (int) context.getResources().getDimension(R.dimen.recomment_view_padding);
                            holder.recomment_insert.setPadding(0,0,rightpadding,0);
                            holder.recommentView.setText("답글보기");
                            recommentViewOrHide = false;

                        }
                    }
                }
            });





        //댓글 삭제
        View.OnClickListener positiveListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (commentitem.getCommentWriter().equals(accountNick))
                {
                    toss_commentNo_interface.toss_commentCount_actiivity(commentitem.getCommentNo(),0,position);
                }
                else
                {
                    Toast.makeText(context,"본인 댓글만 삭제 하실 수 있습니다",Toast.LENGTH_LONG).show();
                }
                noticeDialog.dismiss();
            }
        };

        View.OnClickListener negativeListener = new View.OnClickListener() {
            public void onClick(View v) {
                noticeDialog.dismiss();
            }
        };

        holder.comment_row.setLongClickable(true);
        holder.comment_row.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                noticeDialog = new NoticeDialog(context,
                        "댓글을 삭제하시겠습니까?", false, "예",
                        "아니요", positiveListener, negativeListener);
                noticeDialog.show();

                return false;
            }
        });

        if (commentitem.getCommentContent().equals("삭제된 댓글입니다."))
        {
            holder.commentDelete.setText("");
            holder.commentLike.setText("");
            holder.recomment_insert.setText("");
            holder.comment_delete_img.setVisibility(View.VISIBLE);
            holder.commentContent.setTextColor(context.getResources().getColor(R.color.grayMain));

            int recommentCountPadding = (int)context.getResources().getDimension(R.dimen.all_space_between_18dp);
            holder.recommentCount.setPadding(0,0,0,0);
        }



    }
    @Override
    public void getCommentDataSuccess(ArrayList<CommentItem> commentitem,int position)
    {

    }



    @Override
    public void getCommentDataFail(String message) {

    }

    @Override
    public void moveToPage(Intent intent, int requestCode) {

    }

    @Override
    public void commentInsertGoResponse(int response,int commentCount,CommentItem commentItem)
    {

    }

    @Override
    public void recommentInsertGoResponse(int response,RecommentItem recommentItem,int commentNo)
    {
        //commentNo = adapter에서 넘긴 position
        CommentItem commentItem = commentitems.get(commentNo);

        if (commentItem != null)
        {
           if (recommentitems.size() == 0)
           {
               recommentitems = new ArrayList<>();
               CreateRecommentItem(commentItem);
           }
           else
           {
                if (recommentitems.get(0).getCommentNo() != commentItem.getCommentNo())
                {
                    recommentitems = new ArrayList<>();
                    CreateRecommentItem(commentItem);
                }
           }

           afterRecommentInsertListView = true;
           recommentitems.add(recommentitems.size() ,recommentItem);
           commentItem.setRecommentCount(recommentitems.size());
           recommentAdapter.notifyItemChanged(recommentitems.size());
           notifyItemChanged(commentNo,commentItem);

       }

    }
    public void CreateRecommentItem(CommentItem commentItem)
    {
        int recommentCount = commentItem.getRecommentCount();
        for (int i = 0;i < recommentCount ; i++)
        {
            try
            {
                JSONArray jsonArray = new JSONArray(commentItem.getRecomment());
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Gson gsonobject = new Gson();

                recommentitem = gsonobject.fromJson(String.valueOf(jsonObject),RecommentItem.class);
                Log.d(TAG,String.valueOf(jsonObject));
                recommentitems.add(recommentitem);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void likeGoResponse(int response)
    {
    }

    @Override
    public void CommentOrRecommentActivity(int commentNo)
    {

    }

    @Override
    public void commentDeleteGoResponse(int response,int commentCount,int commentNo)
    {
        //commentNo = adapter에서 넘긴 position
        if (response == 0)//댓글
        {
            commentitems.remove(commentNo);
            notifyDataSetChanged();
        }
        else if (response == 1)
        {
            recommentAdapter.commentDeleteGoResponse(response,commentCount,commentNo);
            CommentItem commentItem = commentitems.get(commentNo);
            commentItem.setRecommentCount(recommentitems.size());
            notifyItemChanged(commentNo);
        }
    }

}


