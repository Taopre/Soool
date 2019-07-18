package com.example.taopr.soool.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.taopr.soool.Model.CommentModel;
import com.example.taopr.soool.Networking.APICallback;
import com.example.taopr.soool.Networking.APIClient;
import com.example.taopr.soool.Networking.APIService;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.CommentListObject;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.Interface.CommentInter;

import java.util.ArrayList;

public class CommentPresenter extends BasePresenter implements CommentInter
{

    public Activity activity;
    private Context context;
    private CommentModel commentModel;
    private APIService apiService;
    private CommentInter.View view;

    public CommentPresenter(Activity activity, Context context)
    {
        this.activity = activity;
        this.commentModel = new CommentModel(context,this);
        this.apiService = APIClient.getClient1().create(APIService.class);
    }

    @Override
    public void setView(View view)
    {
        this.view = view;
    }

    public void loadData(int postNo)
    {
        addSubscription
                (
                        apiService.getCommentItem(postNo),
                        new APICallback<CommentListObject>()
                        {
                            @Override
                            public void onSuccess(CommentListObject commentList_object)
                            {
                                ArrayList<CommentItem> commentitem = new ArrayList(commentList_object.getQnaCommentItems());
                                view.getCommentDataSuccess(commentitem);
                            }

                            @Override
                            public void onFailure(String msg)
                            {

                            }
                            @Override
                            public void onFinish()
                            {

                            }
                        }
                );
    }
    @Override
    public void commentRequest(int postNo, int accountNo, String commentContent)
    {
        commentModel.commentRequest(postNo,accountNo,commentContent);
    }

    @Override
    public void recommentRequest(int postNo, int commentNo, int accountNo, String commentContent)
    {
        commentModel.recommentRequest(postNo,commentNo,accountNo,commentContent);
    }

    @Override
    public void likeRequest(int postNo, int commentNo, int accountNo, int like_check,int commentORrecomment,int recommentNo) {
        commentModel.commentLikeRequest(postNo,commentNo,accountNo,like_check,commentORrecomment,recommentNo);
    }
    @Override
    public void commentDeleteRequest(int post, int commentNo,int recommentNo)
    {
        commentModel.commentDeleteRequest(post,commentNo,recommentNo);
    }

    @Override
    public void commentDeleteResponss(int response,int commentCount,int commentNo)
    {
        view.commentDeleteGoResponse(response,commentCount,commentNo);
    }

    @Override
    public void commentInsertResponse(int response,int commentCount,CommentItem commentItem) {
        view.commentInsertGoResponse(response,commentCount,commentItem);
    }

    @Override
    public void recommentInsertResponse(int response, RecommentItem recommentItem, int commentNo) {
        view.recommentInsertGoResponse(response,recommentItem,commentNo);
    }
    @Override
    public void likeResponse(int response)
    {
        view.likeGoResponse(response);
    }

    @Override
    public void CommentOrRecomment(int commentNo)
    {
        view.CommentOrRecommentActivity(commentNo);
    }
}

