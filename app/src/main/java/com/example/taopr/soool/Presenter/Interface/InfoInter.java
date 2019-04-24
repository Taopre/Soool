package com.example.taopr.soool.Presenter.Interface;

import com.example.taopr.soool.Object.InfoOfSoool;
import com.example.taopr.soool.Presenter.InfoPresenter;


import java.util.List;

public interface InfoInter {

    void setView(InfoPresenter.View view);

    public interface View{
        void getDataSuccess(List<InfoOfSoool> infoOfSoools);

        void getDataFail(String message);
    }
}
