package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lpky.taopr.soool.Object.LoginSessionItem;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.SharedPreferences.LoginSharedPreferences;
import com.lpky.taopr.soool.View.Guide.GuideBodyFragment;
import com.lpky.taopr.soool.View.Guide.GuideHeadFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.guideViewpager)
    ViewPager guideViewPager;
    @BindView(R.id.guideStartingButton)
    Button guideStartingButton;
    @BindView(R.id.guideViewpagerIndicator)
    CircleIndicator guideViewpagerIndicator;
    private MyPagerAdapter myPagerAdapter;
    private int guideTotalPage = 5;
    private LoginSharedPreferences loginSharedPreferences;

    private String TAG ="가이드 액티비티";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        loginSharedPreferences = new LoginSharedPreferences();
        checkAutoLogin();

        ButterKnife.bind(this);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        guideViewPager.setAdapter(myPagerAdapter);

        guideViewpagerIndicator.setViewPager(guideViewPager);

        myPagerAdapter.registerDataSetObserver(guideViewpagerIndicator.getDataSetObserver());


    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        String TAG =" 가이드 뷰페이저 어댑터";

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return guideTotalPage;
        }

        // Returns the fragment to display for that page
        // @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GuideHeadFragment.newInstance();
                case 1:
                    return GuideBodyFragment.newInstance(1);
                case 2:
                    return GuideBodyFragment.newInstance(2);
                case 3:
                    return GuideBodyFragment.newInstance(3);
                case 4:
                    return GuideBodyFragment.newInstance(4);

                default:
                    return null;
            }
        }
    }
    // 자동로그인 설정을 한 경우
    // Home 페이지로 이동
    private void checkAutoLogin() {
        try {
            String data = LoginSharedPreferences.LoginUserLoad(GuideActivity.this, "LoginAccount");
            Gson gson = new GsonBuilder().create();
            LoginSessionItem loginSessionItem = gson.fromJson(data, LoginSessionItem.class);

            if (loginSessionItem.isAccountAutoLogin()) {
                Intent intent = new Intent(GuideActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else{
                checkHasSeeGuide();
            }
        }
        catch (NullPointerException ex){
            checkHasSeeGuide();
        }
    }

    private void checkHasSeeGuide(){

        // 가이드는 앱 다운 후 한번 만 볼수 있도록
        if (loginSharedPreferences.getHasSeeGuide(this)){
           moveToStarting();
        }
    }

    private void moveToStarting(){
        Intent intent = new Intent(this,StartingActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.guideStartingButton})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.guideStartingButton:
                // 가이드를 다시 보지 않게 하기 위해
                // 쉐어드에 hasSeeGuide 값으로 구별
                // false 가 안봄 , true 가 봄
                loginSharedPreferences.setHasSeeGuide(this);

                moveToStarting();
                break;
        }

    }
}
