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
import com.example.taopr.soool.Object.InfoBookmark;
import com.example.taopr.soool.Object.InfoContent;
import com.example.taopr.soool.Object.InfoContentText;
import com.example.taopr.soool.Object.InfoItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.Interface.InfoDetailInter;
import com.example.taopr.soool.SharedPreferences.LoginSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class InfoDetailPresenter extends BasePresenter implements InfoDetailInter {
    private APIService apiService;
    private Context context;
    private InfoDetailPresenter.View view;
    private String TAG = "Info_Detail_Presenter";

    public Activity activity;
    String accountNick;
    int accountNo;
    InfoItem infoItem;

    //댓글 model
    private CommentModel commentModel;

    public InfoDetailPresenter(Activity activity, Context context)
    {
        this.activity = activity;
        this.commentModel = new CommentModel(context,this);
        this.apiService = APIClient.getClient1().create(APIService.class);
        Log.d(TAG, "infoDetail: 프레젠터(activity, context)");
    }

    public InfoDetailPresenter(Context context) {
        this.context = context;
        this.apiService = APIClient.getClient1().create(APIService.class);
        Log.d(TAG, "infoDetail: 프레젠터(context)");
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void getAdditionalData(int accountNo, int postNo) {

        view.showLoading();

        addSubscription(
                apiService.getInfoText(postNo, accountNo),
                new APICallback<InfoContent>() {
                    @Override
                    public void onSuccess(InfoContent infoContent) {

                        if (infoContent != null) {

                            ArrayList<InfoContentText> infoTexts = new ArrayList(infoContent.getInfoText());
                            ArrayList<InfoBookmark> infoBookmarks = new ArrayList(infoContent.getInfoBookmark());

                            int bookmarkNo = infoBookmarks.get(0).getBookmarkNo();
                            boolean hasBookmarked = false;
                            String stBookmarked = infoBookmarks.get(0).getHasBookmarked();
                            if (stBookmarked.equals("true")) {
                                hasBookmarked = true;
                            } else {
                                hasBookmarked = false;
                            }
                            view.getDataSuccess(infoTexts, bookmarkNo, hasBookmarked);
                            Log.e(TAG, "onSuccess: BOOKMARK!!" + bookmarkNo +  " | " + hasBookmarked);
                            Log.d(TAG, "onSuccess: " + infoTexts.get(0).getText() + bookmarkNo + " + " + hasBookmarked);
                        } else {
                            Log.d(TAG, "onSuccess: infoTexts = null");
                        }

                    }

                    @Override
                    public void onFailure(String msg) {

                        Log.i(TAG, "onFailure : info " + msg);
                        view.getDataFail(msg);

                    }

                    @Override
                    public void onFinish() {

                        view.hideLoading();
                        Log.i(TAG, "onFinish: info");

                    }
                }
        );

    }

    @Override
    public void getExistingData() {

        // SESSION DATA - LoginSharedPreferences.java에 만들어둔 파일 가져옴
        accountNo = LoginSharedPreferences.getAccountNo(context, "LoginAccount");


        // TIME - 굳이 있어야하는 이유를 잘 모르겠긴 한데..
        long currentTime = System.currentTimeMillis();
        Date currentDate = new Date(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedTime = dateFormat.format(currentDate);

        view.getSessionData(accountNo, formattedTime);

    }

    @Override
    public void getIntentFromActivity(InfoItem infoItem) {
        this.infoItem = infoItem;
    }

    @Override
    public void updateBookmarkStatus() {

        int postNo = infoItem.getPostNo();
        //view.showLoading();

        addSubscription(
                apiService.updateBookmark(postNo, accountNo),
                new APICallback<InfoBookmark>() {
                    @Override
                    public void onSuccess(InfoBookmark infoBookmark) {

                        boolean hasBookmarked = false;

                        switch(infoBookmark.hasBookmarked){
                            case "true":
                                hasBookmarked = true;
                                break;
                            case "false":
                                hasBookmarked = false;
                                break;
                        }

                        view.notifyBookmarkChange(infoBookmark.postNo, infoBookmark.bookmarkNo, hasBookmarked);

                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.i(TAG, "onFailure : updateBookmark " + msg);
                    }

                    @Override
                    public void onFinish() {

                        //view.hideLoading();
                    }

                }
        );
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
        Log.d(TAG, "commentRequest: 성공?????");
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
