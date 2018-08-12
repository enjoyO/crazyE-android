package com.bai.van.radixe.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bai.van.radixe.MainActivity;
import com.bai.van.radixe.R;
import com.bai.van.radixe.ScoreAnalyseActivity;
import com.bai.van.radixe.SettingActivity;
import com.bai.van.radixe.UserInfActivity;
import com.bai.van.radixe.urlrequests.UserInfRequest;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author van
 * @date 2018/1/30
 */

public class OtherFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView gainedCreditText, electivedCreditText, gpaText, weightedGradesText, unPassedCountText;
    private LinearLayout myInfLayout, myScoreAnalyseLyout, settingLayout;

    private Handler handler;

    public OtherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFragment newInstance(String param1, String param2) {
        OtherFragment fragment = new OtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        handler = new Handler();
    }

    private void initial(View view) {
        gainedCreditText = (TextView) view.findViewById(R.id.gainedCreditCountText);
        electivedCreditText = (TextView) view.findViewById(R.id.electivedCreditCountText);
        gpaText = (TextView) view.findViewById(R.id.gpaAllText);
        weightedGradesText = (TextView) view.findViewById(R.id.weightedMeanGradesText);
        unPassedCountText = (TextView) view.findViewById(R.id.unPassedCountText);
        myInfLayout = (LinearLayout) view.findViewById(R.id.myInfLayout);
        myScoreAnalyseLyout = (LinearLayout) view.findViewById(R.id.myScoreAnalyseLayout);
        settingLayout = (LinearLayout) view.findViewById(R.id.settingLayout);

        myInfLayout.setOnClickListener(this);
        myScoreAnalyseLyout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);

        loadGradesInfData();
    }

    private void loadGradesInfData() {
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                UserInfRequest.requestGradesInfAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gainedCreditText.setText(UserInformation.gainedCreditAll + "");
                        electivedCreditText.setText(UserInformation.electivedCreditAll + "");
                        gpaText.setText(UserInformation.gpaAll + "");
                        weightedGradesText.setText(UserInformation.weightedGrdesAll + "");
                        unPassedCountText.setText(UserInformation.unPassedCountAll + "");

                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        initial(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && UserInformation.gpaAll == 0) {
            loadGradesInfData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myInfLayout:
                startActivity(new Intent(getActivity(), UserInfActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.myScoreAnalyseLayout:
                startActivity(new Intent(getActivity(), ScoreAnalyseActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case R.id.settingLayout:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            default:
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
