package com.lpky.taopr.soool.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.View.VoteImageFragment.ImageDetail;

import java.util.ArrayList;

public class VoteImageActivity  extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    TextView closeFragment, nowImagePosition, totalImagePosition;

    String TAG ="투표 뷰 페이저 ";
    int num, nowposition;

    ArrayList<String> imageItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voteimage);

        ViewPager vpPager = findViewById(R.id.vpPager);
        closeFragment = findViewById(R.id.closeFragment);
        nowImagePosition = findViewById(R.id.nowImagePosition);
        totalImagePosition = findViewById(R.id.totalImagePosition);

        Intent intent = getIntent();

        if (intent != null) {
            num = intent.getIntExtra("totalImageNum", 9999);
            imageItems = intent.getStringArrayListExtra("imageList");
            nowposition = intent.getIntExtra("nowPosition", 9999);

            nowImagePosition.setText((nowposition+1)+"");
            totalImagePosition.setText(num+"");
        }

        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), num, imageItems);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setCurrentItem(nowposition);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d(TAG, "onPageSelected: "+i);
                nowImagePosition.setText((i+1)+"");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS;
        private ArrayList<String> imageItems;
        String TAG ="투표 뷰 페이저 ";

        public MyPagerAdapter(FragmentManager fragmentManager, int imageNum, ArrayList<String> imageItems) {
            super(fragmentManager);
            this.NUM_ITEMS = imageNum;
            this.imageItems = imageItems;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
       // @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ImageDetail.newInstance(0, imageItems.get(position));
                case 1:
                    return ImageDetail.newInstance(1, imageItems.get(position));
                case 2:
                    return ImageDetail.newInstance(2, imageItems.get(position));
                case 3:
                    return ImageDetail.newInstance(3, imageItems.get(position));
                case 4:
                    return ImageDetail.newInstance(4, imageItems.get(position));
                case 5:
                    return ImageDetail.newInstance(5, imageItems.get(position));
                default:
                    return null;
            }
        }
    }
}
