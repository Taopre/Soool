package com.lpky.taopr.soool.View.Guide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lpky.taopr.soool.R;


public class GuideBodyFragment extends Fragment {

    private int position;


    public GuideBodyFragment() {
        // Required empty public constructor
    }
    public static GuideBodyFragment newInstance(int position) {
        GuideBodyFragment fragment = new GuideBodyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.position =  getArguments().getInt("position", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_body, container, false);

        TextView guideBodyTitle = view.findViewById(R.id.guideBodyTitle);
        TextView guideBodyExplain = view.findViewById(R.id.guideBodyExplain);
        ImageView guideBodyImage = view.findViewById(R.id.guideBodyImage);

        switch (position){
            case 1:
                guideBodyTitle.setText(getString(R.string.main_tab_label_info));
                guideBodyExplain.setText(getString(R.string.guide_explain_info));
                Glide.with(getContext())
                        .load(R.drawable.intro_info)
                        .into(guideBodyImage);


                break;
            case 2:
                guideBodyTitle.setText(getString(R.string.main_tab_label_qna));
                guideBodyExplain.setText(getString(R.string.guide_explain_community));

                Glide.with(getContext())
                        .load(R.drawable.intro_qna)
                        .into(guideBodyImage);

                break;
            case 3:
                guideBodyTitle.setText(getString(R.string.guide_label_vote));
                guideBodyExplain.setText(getString(R.string.guide_explain_vote));
                Glide.with(getContext())
                        .load(R.drawable.intro_vote)
                        .into(guideBodyImage);
                break;
            case 4:
                guideBodyTitle.setText(getString(R.string.my_page_tab_label_calendar));
                guideBodyExplain.setText(getString(R.string.guide_explain_calendar));

                Glide.with(getContext())
                        .load(R.drawable.intro_calendar)
                        .into(guideBodyImage);

                break;
        }

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
          //  mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
