package com.bai.van.radixe.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bai.van.radixe.adapters.ExamScoreViewPagerAdapter;
import com.bai.van.radixe.MainActivity;
import com.bai.van.radixe.R;
import com.bai.van.radixe.ScoreSearchActivity;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamScoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author van
 */
public class ExamScoreFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private Handler handler;
    private OnFragmentInteractionListener mListener;

    private ViewPager examScoreViewPager;
    private static TabLayout examScoreTabLayout;
    private ExamScoreViewPagerAdapter examScoreViewPagerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CardView scoreSearchCard;

    public ExamScoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamScoreFragment newInstance(String param1, String param2) {
        ExamScoreFragment fragment = new ExamScoreFragment();
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
    private void initial(View view){
        scoreSearchCard = (CardView) view.findViewById(R.id.scoreSearchCard);

        scoreSearchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ScoreSearchActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.scoreSwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.violet_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                regreshScore();
            }
        });
        examScoreViewPager = (ViewPager) view.findViewById(R.id.examScoreViewPager);
        examScoreTabLayout = (TabLayout) view.findViewById(R.id.examScoreTabLayout);

        swipeRefreshLayout.setRefreshing(true);

        if (UserInformation.examScoreInfList.size() != 0){
            loadScoreViewPager();
        }else {
            regreshScore();
        }

    }
    public void regreshScore(){
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("".equals(UserInformation.currentTerm)){
                    GradesRequest.postRequestCurrentTerm();
                }
                GradesRequest.requestGradesAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadScoreViewPager();
                    }
                });
            }
        });
    }
    private void loadScoreViewPager(){
        examScoreViewPagerAdapter = new ExamScoreViewPagerAdapter(getActivity().getSupportFragmentManager(),
                UserInformation.userSemesterDeList, buidScoreFragment());
        examScoreViewPager.setAdapter(examScoreViewPagerAdapter);
        examScoreTabLayout.setupWithViewPager(examScoreViewPager);
        swipeRefreshLayout.setRefreshing(false);
    }
    private List<Fragment> buidScoreFragment(){
        List<Fragment> examScoreFragments=new ArrayList<>();
        for (int i = 0; i < UserInformation.userSemesterList.size(); i++){
            examScoreFragments.add(new ExamScoreRecyclerViewFragment(UserInformation.userSemesterList.get(i)));
        }
        return examScoreFragments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exam_score, container, false);

        initial(view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && UserInformation.examScoreInfList.size() == 0){
            swipeRefreshLayout.setRefreshing(true);
            regreshScore();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
