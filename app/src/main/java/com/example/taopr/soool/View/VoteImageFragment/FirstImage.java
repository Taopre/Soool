package com.example.taopr.soool.View.VoteImageFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.taopr.soool.R;
import com.example.taopr.soool.Whatisthis;


public class FirstImage extends Fragment {
    // Store instance variables
    private String image;
    private int page;
    String TAG = "프래그먼트 1";

    public FirstImage () {}

    // newInstance constructor for creating fragment with arguments
    public static FirstImage newInstance(int page, String image) {
        FirstImage fragment = new FirstImage();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someImage", image);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        image = getArguments().getString("someImage");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voteimage_first, container, false);
        ImageView voteImage = view.findViewById(R.id.voteImage);

        Log.i(TAG, "onCreateView: "+Whatisthis.serverIp+image);

        Glide.with(view.getContext())
                .load(Whatisthis.serverIp+image)
//                .override(368, 244)
                .fitCenter()
                .into(voteImage);

        return view;
    }
}
