package com.lpky.taopr.soool.Object;

import java.util.ArrayList;

public class CommentListObject
{
    private ArrayList<CommentItem> commentitem;

    public ArrayList<CommentItem> getQnaCommentItems()
    {
        return commentitem;
    }

    public void setCommentitem(ArrayList<CommentItem> commentitem) {
        this.commentitem = commentitem;
    }
}
