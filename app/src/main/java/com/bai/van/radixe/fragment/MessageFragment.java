package com.bai.van.radixe.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bai.van.radixe.adapters.ExamScheduleAdapter;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.MainActivity;
import com.bai.van.radixe.R;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.urlrequests.UserInfRequest;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author van
 */
public class MessageFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static Handler mMessageFrageHandler;
    public TextView myName, myMajor, examFinishHeaderColor, examUnfinishHeaderColor, examHeaderUnfinishText, examHeaderFinishText;
    private RecyclerView.LayoutManager examScheduleFinishlayoutManager, examScheduleUnfinishlayoutManager, examInScheduleLayoutManager;
    private RecyclerView examFinishRecyclerView, examUnfinishRecyclerView, examInScheduleRecyclerView;
    private ImageView imageViewIcon;

    private ExamScheduleAdapter examScheduleFinishAdapter, examScheduleUnfinishAdapter, examInScheduleAdapter;
    private Context context;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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

        mMessageFrageHandler = new Handler();


    }
    private void initial(View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.messageFragmentSwipRefreshLayout);
        LinearLayout examFinishHeaderLayout = (LinearLayout) view.findViewById(R.id.examFinishHeaderLayout);
        LinearLayout examUnfinishHeaderLayout = (LinearLayout) view.findViewById(R.id.examUnfinishHeaderLayout);
        myName = (TextView) view.findViewById(R.id.myName);
        myMajor = (TextView) view.findViewById(R.id.myMajor);
        examFinishHeaderColor = (TextView) view.findViewById(R.id.examFinishHeaderColor);
        examUnfinishHeaderColor = (TextView) view.findViewById(R.id.examHeaderUnfinishColor);
        examHeaderFinishText = (TextView) view.findViewById(R.id.examFinishHeaderText);
        examHeaderUnfinishText = (TextView) view.findViewById(R.id.examHeaderUnfinishText);
        examFinishRecyclerView = (RecyclerView) view.findViewById(R.id.examFinishRecyclerView);
        examUnfinishRecyclerView = (RecyclerView) view.findViewById(R.id.examUnfinishRecyclerView);
        examInScheduleRecyclerView = (RecyclerView) view.findViewById(R.id.examInscheduleRecyclerView);

        imageViewIcon = view.findViewById(R.id.myIcon);

        examInScheduleRecyclerView.setNestedScrollingEnabled(false);
        examUnfinishRecyclerView.setNestedScrollingEnabled(false);
        examFinishRecyclerView.setNestedScrollingEnabled(false);
        examFinishHeaderLayout.setOnClickListener(this);
        examUnfinishHeaderLayout.setOnClickListener(this);

        swipeRefreshLayout.setColorSchemeResources(R.color.violet_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadScheduledList();
                if ("".equals(myName.getText().toString())){
                    loadUserName();
                }
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        loadScheduledList();

        loadUserName();
    }

    private void loadUserName(){
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                UserInfRequest.requestUserBaseInf();
                MessageFragment.mMessageFrageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myName.setText(UserInformation.usernameChar);
                        myMajor.setText(UserInformation.userMajor);

                        if ("女".endsWith(UserInformation.userInf.baseGender)) {
                            imageViewIcon.setImageDrawable(getResources().getDrawable(
                                    ConstantValues.HEAD_NAME_GIRL[Integer.parseInt(UserInformation.username.substring(UserInformation.username.length() - 1))]));
                        }else {
                            imageViewIcon.setImageDrawable(getResources().getDrawable(
                                    ConstantValues.HEAD_NAME_BOY[Integer.parseInt(UserInformation.username.substring(UserInformation.username.length() - 1))]));
                        }
                        Objects.requireNonNull(getActivity()).getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                                .edit().putInt(Entry.SharedPreferencesEntry.USER_STA_YEAR, UserInformation.userStaYear).apply();
                    }
                });
            }
        });
    }

    private void loadScheduledList(){
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if ("".equals(UserInformation.currentTerm)){
                    GradesRequest.postRequestCurrentTerm();
                }
                UserInfRequest.requestExamScheduled();
                Log.d("ExamSchedule finished", UserInformation.examScheduledFinishList.size() + "");
                Log.d("ExamSchedule unfinished", UserInformation.examScheduledUnfinishList.size() + "");

                MessageFragment.mMessageFrageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        examHeaderFinishText.setText("已完成考试".concat(" (")
                                .concat(Integer.toString(UserInformation.examScheduledFinishList.size()))
                                .concat(")"));
                        examHeaderUnfinishText.setText("未完成考试".concat(" (")
                                .concat(Integer.toString(UserInformation.examScheduledUnfinishList.size()))
                                .concat(")"));
                        if (UserInformation.examScheduledFinishList.size() == 0){
                            examFinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.snow_white));
                        }else {
                            examFinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.silver_sand));
                        }
                        if (UserInformation.examScheduledUnfinishList.size() == 0){
                            examUnfinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.snow_white));
                        }else {
                            examUnfinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.silver_sand));
                        }
                        loadScheduledRecyclerView();
                        loadInScheduleList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
    private void loadInScheduleRecyclerView(){
        examInScheduleLayoutManager = new LinearLayoutManager(context);
        examInScheduleRecyclerView.setLayoutManager(examInScheduleLayoutManager);
        examInScheduleRecyclerView.setHasFixedSize(true);

        examInScheduleAdapter = new ExamScheduleAdapter(UserInformation.examInScheduleList, ConstantValues.EXAM_IN_SCHEDULE, context);
        examInScheduleRecyclerView.setAdapter(examInScheduleAdapter);
    }
    private void loadInScheduleList(){
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("".equals(UserInformation.currentTerm)){
                    GradesRequest.postRequestCurrentTerm();
                }
                UserInfRequest.requestExamInSchedule();
                MessageFragment.mMessageFrageHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadInScheduleRecyclerView();
                    }
                });
            }
        });
    }
    private void loadScheduledRecyclerView(){
        examScheduleFinishlayoutManager = new LinearLayoutManager(context);
        examScheduleUnfinishlayoutManager = new LinearLayoutManager(context);
        examFinishRecyclerView.setLayoutManager(examScheduleFinishlayoutManager);
        examFinishRecyclerView.setHasFixedSize(true);
        examUnfinishRecyclerView.setLayoutManager(examScheduleUnfinishlayoutManager);
        examUnfinishRecyclerView.setHasFixedSize(true);

        examScheduleFinishAdapter = new ExamScheduleAdapter(UserInformation.examScheduledFinishList, ConstantValues.EXAM_SCHEDULE_FINISHED, context);
        examScheduleUnfinishAdapter = new ExamScheduleAdapter(UserInformation.examScheduledUnfinishList, ConstantValues.EXAM_SCHEDULE_UNFINISHE, context);

        examFinishRecyclerView.setAdapter(examScheduleFinishAdapter);
        examUnfinishRecyclerView.setAdapter(examScheduleUnfinishAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        initial(view);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if ("".equals(myName.getText().toString())){
                loadUserName();
            }
            if (UserInformation.examScheduledFinishList.size() == 0 && UserInformation.examScheduledUnfinishList.size() == 0){
                loadScheduledList();
            }
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
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.examFinishHeaderLayout:
                if (examFinishRecyclerView.getAdapter().getItemCount() != 0){
                    if (examFinishRecyclerView.getVisibility() == View.VISIBLE){
                        examFinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.snow_white));
                        examFinishRecyclerView.setVisibility(View.GONE);
                    }else {
                        examFinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.silver_sand));
                        examFinishRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.examUnfinishHeaderLayout:
                if (examUnfinishRecyclerView.getAdapter().getItemCount() != 0){
                    if (examUnfinishRecyclerView.getVisibility() == View.VISIBLE){
                        examUnfinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.snow_white));
                        examUnfinishRecyclerView.setVisibility(View.GONE);
                    }else {
                        examUnfinishHeaderColor.setBackgroundColor(getResources().getColor(R.color.silver_sand));
                        examUnfinishRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
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
