package com.example.taopr.soool.Presenter.Interface;

import android.content.Intent;

import com.example.taopr.soool.Object.BoardRecommend;
import com.example.taopr.soool.Object.CommentItem;
import com.example.taopr.soool.Object.QnaVoteItem;
import com.example.taopr.soool.Object.RecommentItem;
import com.example.taopr.soool.Presenter.CommentPresenter;

import java.util.ArrayList;

public interface CommentInter {

    void setView(CommentPresenter.View view);

    //댓글 작성하는 부분
    void commentRequest(int postNo, int accountNo, String commentContent);

    void recommentRequest(int postNo, int commentNo, int accountNo, String commentContent);

    void commentInsertResponse(int response, int commentCount, CommentItem commentItem);

    void recommentInsertResponse(int response, RecommentItem recommentItem, int commentNo);

    //댓글 좋아요
    public void likeRequest(int postNo, int commentNo, int accountNo, int like_check, int commentORrecomment, int recommentNo);

    public void likeResponse(int response);

    //답글작성할때에 (어댑터에서 액티비티로 값넘길때)
    void CommentOrRecomment(int commentNo);

    //댓글 삭제  + 대댓글 번호추가
    void commentDeleteRequest(int post, int commentNo, int recommentNo);

    void commentDeleteResponss(int response, int commentCount, int commentNo);


    public interface View {
        //댓글 리스트받는 부분
        void getCommentDataSuccess(ArrayList<CommentItem> commentitem);

        void getCommentDataFail(String message);

        void moveToPage(Intent intent, int requestCode);

        //댓글 작성하는 부분
        public void commentInsertGoResponse(int response, int commentCount, CommentItem commentItem);

        public void recommentInsertGoResponse(int response, RecommentItem recommentItem, int commentNo);

        //댓글 좋아요
        public void likeGoResponse(int response);

        //답글작성할때에 (어댑터에서 액티비티로 값넘길때)
        public void CommentOrRecommentActivity(int commentNo);

        //댓글 삭제
        public void commentDeleteGoResponse(int response, int commentCount, int commentNo);

    }

}
