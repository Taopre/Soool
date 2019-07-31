package com.lpky.taopr.soool.View.Guide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lpky.taopr.soool.R;


public class GuideHeadFragment extends Fragment {



    public GuideHeadFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GuideHeadFragment newInstance() {
        GuideHeadFragment fragment = new GuideHeadFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_head, container, false);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
