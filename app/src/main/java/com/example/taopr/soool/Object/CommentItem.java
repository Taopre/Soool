package com.example.taopr.soool.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentItem implements Parcelable
{
    @SerializedName("commentNo")  // @SerializedName 직렬화, 역직렬화 하기 위해 사용
    public int commentNo;
    @SerializedName("accountNo")
    public int accountNo;
    @SerializedName("commentWriter")
    public String commentWriter;
    @SerializedName("date")
    public String date;
    @SerializedName("commentContent")
    public String commentContent;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("recommentCount")
    public int recommentCount;
    @SerializedName("recomment")
    public List recomment;
    @SerializedName("likeList")
    public int[] likeList;

    public int[] getLikeList() {
        return likeList;
    }

    public void setLikeList(int[] likeList) {
        this.likeList = likeList;
    }



    public CommentItem(){}

    public CommentItem(int commentNo, int accountNo, String commentWriter, String date, String commentContent,
                       int likeCount, int recommentCount,List recomment,int[] likeList)
    {
        this.commentNo = commentNo;
        this.accountNo = accountNo;
        this.commentWriter = commentWriter;
        this.date = date;
        this.commentContent = commentContent;
        this.likeCount = likeCount;
        this.recommentCount = recommentCount;


        this.recomment = recomment;


        this.likeList = likeList;
    }


    public CommentItem(Parcel in)
    {
        commentNo = in.readInt();
        accountNo = in.readInt();
        commentWriter = in.readString();
        date = in.readString();
        commentContent =in.readString();
        likeCount = in.readInt();
        recommentCount = in.readInt();

        //recomment = in.readList(recomment,recomment.getClass().);

        //recomment = in.readArray(recommentitem.class.getClassLoader())

        //recomment = in.readString();
        //recomment = in.readArrayList(recommentitem.class.getClassLoader());

        //recomment = in.createStringArrayList();
        //recomment = in.readArray(recomment.getClass().getClassLoader());

    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

        dest.writeInt(commentNo);
        dest.writeInt(accountNo);
        dest.writeString(commentWriter);
        dest.writeString(date);
        dest.writeString(commentContent);
        dest.writeInt(likeCount);
        dest.writeInt(recommentCount);
        //dest.writeArray(new ArrayList[]{recomment});
        //dest.writeString(recomment);
        //dest.writeStringArray(recomment);
        //dest.writeTypedArray((Parcelable[])recomment,0);

    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>()
    {
        @Override
        public CommentItem createFromParcel(Parcel in)
        {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size)
        {
            return new CommentItem[size];
        }



    };


    public int getCommentNo() {
        return commentNo;
    }
    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getCommentWriter() {
        return commentWriter;
    }

    public void setCommentWriter(String commentWriter) {
        this.commentWriter = commentWriter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getRecommentCount() {
        return recommentCount;
    }

    public void setRecommentCount(int recommentCount) {
        this.recommentCount = recommentCount;
    }

    public List getRecomment() {
        return recomment;
    }

    public void setRecomment(List recomment) {
        this.recomment = recomment;
    }
}

