package com.example.taopr.soool.View.HomeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;

public class BaseFragment extends Fragment {
   public Unbinder unbinder; // 버터나이프 바인더를 해제하게 할 변수

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder!=null) unbinder.unbind();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
