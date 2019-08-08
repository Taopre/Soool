package com.example.taopr.soool.Object;

public class QnaBoardVoteItem {

    private String editTextValue;
    private int voteboard = 0;
    private boolean flag = false;

    public QnaBoardVoteItem() {}

    public String getEditTextValue() {
        return editTextValue;
    }

    public void setEditTextValue(String editTextValue) {
        this.editTextValue = editTextValue;
    }

    public int getVoteboard() {
        return voteboard;
    }

    public void setVoteboard(int voteboard) {
        this.voteboard = voteboard;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}