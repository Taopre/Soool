package com.example.taopr.soool.Presenter;

import com.example.taopr.soool.Object.InfoOfSoool;

import java.util.List;

interface InfoInter {

    void setView(InfoPresenter.View view);

    public interface View{
        void getDataSuccess(List<InfoOfSoool> infoOfSoools);

        void getDataFail(String message);
    }
}
