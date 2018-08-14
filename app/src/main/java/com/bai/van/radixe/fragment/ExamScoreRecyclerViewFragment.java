package com.bai.van.radixe.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bai.van.radixe.ExamScoreDetailActivity;
import com.bai.van.radixe.adapters.ExamScoreRecyclerViewAdapter;
import com.bai.van.radixe.adapters.ExamScoreRecyclerViewHolder;
import com.bai.van.radixe.constantdata.SharedData;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.R;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExamScoreRecyclerViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExamScoreRecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author van
 */
public class ExamScoreRecyclerViewFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExamScoreRecyclerViewAdapter examScoreRecyclerViewAdapter;
    private String tabSelectedSign = "";


    public ExamScoreRecyclerViewFragment() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ExamScoreRecyclerViewFragment(String tabSelectedSign) {
        this.tabSelectedSign = tabSelectedSign;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExamScoreRecyclerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamScoreRecyclerViewFragment newInstance(String param1, String param2) {
        ExamScoreRecyclerViewFragment fragment = new ExamScoreRecyclerViewFragment();
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
    }
    private void init(View view) {


        recyclerView = (RecyclerView) view.findViewById(R.id.examScoreRecyclerView);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        examScoreRecyclerViewAdapter = new ExamScoreRecyclerViewAdapter(this, buildScoreData(), tabSelectedSign);

        examScoreRecyclerViewAdapter.setOnItemClickListener(new ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedData.examScoreInf = examScoreRecyclerViewAdapter.mScoreData.get(position - 1);

                ExamScoreRecyclerViewHolder examScoreRecyclerViewHolder = (ExamScoreRecyclerViewHolder) recyclerView.getChildViewHolder(view);
                Intent intent = new Intent(getActivity(), ExamScoreDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(examScoreRecyclerViewAdapter);

    }

    private List<ExamScoreInf> buildScoreData(){
        List<ExamScoreInf> examScoreInfList = new ArrayList<>();
        for (int i = 0; i < UserInformation.examScoreInfList.size(); i ++){
            if (UserInformation.examScoreInfList.get(i).examSemeter.equals(tabSelectedSign)){
                examScoreInfList.add(UserInformation.examScoreInfList.get(i));
            }
        }
        return examScoreInfList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exam_score_recycler_view, container, false);
        init(view);
        return view;
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
