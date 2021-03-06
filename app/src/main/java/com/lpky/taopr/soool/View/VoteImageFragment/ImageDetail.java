package com.lpky.taopr.soool.View.VoteImageFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lpky.taopr.soool.R;
import com.lpky.taopr.soool.Util.Whatisthis;


public class ImageDetail extends Fragment {
    // Store instance variables
    private String image;
    private int page;

    public ImageDetail() {}

    // newInstance constructor for creating fragment with arguments
    public static ImageDetail newInstance(int page, String image) {
        ImageDetail fragment = new ImageDetail();
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
        View view = inflater.inflate(R.layout.fragment_voteimage, container, false);
        ImageView voteImage = view.findViewById(R.id.voteImage);

        Glide.with(view.getContext())
                .load(Whatisthis.serverIp+image)
//                .override(368, 244)
                .fitCenter()
                .into(voteImage);
        return view;
    }
}

