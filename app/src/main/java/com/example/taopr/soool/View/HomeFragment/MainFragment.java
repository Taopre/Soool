package com.example.taopr.soool.View.HomeFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taopr.soool.R;

public class MainFragment extends Fragment {
    String TAG = "큐앤에이 프래그먼트";

    public MainFragment() {
        // Required empty public constructor
        Log.i(TAG, "MainFragment: ");
    }

    // 액티비티에서 프래그먼트로 값 넘기는 거 확인
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        //   args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        if (getArguments() != null) {
            //  mParam1 = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_home_main, container, false);
    }


    // 아직 어떻게 사용하는지 확인 x
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            Log.i(TAG, "onButtonPressed: 리스너 notnull");
            mListener.onFragmentInteraction(uri);
        }
        else{
            Log.i(TAG, "onButtonPressed: 리스너 null ");
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.i(TAG, "onAttach: context");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
        // mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
