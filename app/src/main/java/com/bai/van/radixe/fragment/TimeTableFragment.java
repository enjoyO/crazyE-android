package com.bai.van.radixe.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bai.van.radixe.MainActivity;
import com.bai.van.radixe.R;
import com.bai.van.radixe.TimeTableItemDetailActivity;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.overridemodule.TermChooseDialog;
import com.bai.van.radixe.service.TimeTableAlarmSetting;
import com.bai.van.radixe.sqlite.TimeTableSqliteHandle;
import com.bai.van.radixe.urlrequests.GradesRequest;
import com.bai.van.radixe.urlrequests.TimeTableRequest;
import com.bai.van.radixe.userdata.UserInformation;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author van
 */

public class TimeTableFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Handler handler;

    private TextView weekDisplayTextView;
    private TextView weekSemesterDisplayTextView;
    private RelativeLayout[] weekDisplayLayout;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private HorizontalScrollView weekChooseHorizontalScrollView;
    private LinearLayout weekChooseLinearLayout;
    private Toolbar timetableToolbar;

    private TranslateAnimation mShowAnimation, mHideAnimation;

    Dialog termChooseBottomDialog;

    private boolean isWeekChooseViewOnshow = false, isLoadWeekChooseView = true, isCreatTimeTableAlarm;
    public static float density, widthPixels;
    private int currentWeekNo = 1;
    private String timeTableYear;
    private Typeface typefaceOrchid;
    private TextView currentWeekChooseTextView;
    private TextView currentWeekTextView, firstWeekTextView;

    private List<List<TimeTableInf>> chooseTimetable;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeTableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(String param1, String param2) {
        TimeTableFragment fragment = new TimeTableFragment();
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
        isCreatTimeTableAlarm = getActivity().getSharedPreferences("myspl", Context.MODE_PRIVATE).getBoolean("isReceiveTimeTableAlarm", true);
        setHasOptionsMenu(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.density;
        widthPixels = displayMetrics.widthPixels;

        typefaceOrchid = Typeface.create("orchid_pavilion_black.TTF", Typeface.BOLD);
        handler = new Handler();

        requestCurrentTimetable();
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TimeTableRequest.requestTermAll();
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(timetableToolbar);
        initialBottomDialog();
        if (!isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_LONG).show();
        }
        saveCurrentWeekNoTime();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void saveCurrentWeekNoTime() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);

        getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit().putLong(Entry.SharedPreferencesEntry.CURRENT_WEEK_TIMEINMILLIS, calendar.getTimeInMillis()).apply();

        Log.d("noFirstWeek", calendar.getTimeInMillis() + "");
    }

    private void initial(View view) {
        initialTableView(view);
    }

    private void initialTimeTable(List<List<TimeTableInf>> timeTableList) {
        weekDisplayTextView.setText("第".concat(currentWeekNo + "周"));
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String str;
                str = StaticMethod.semesterTran(timeTableYear);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        weekSemesterDisplayTextView.setText(str);
                    }
                });
            }
        });

        for (RelativeLayout aWeekDisplayLayout : weekDisplayLayout) {
            aWeekDisplayLayout.removeAllViews();
        }

        TimeTableInf timeTableInf;
        int length, colorChoPar;
        boolean isAddNote;

        RelativeLayout.LayoutParams layoutParamsText;
        RelativeLayout.LayoutParams layoutParamsNote = new RelativeLayout.LayoutParams((int) (15 * density), (int) (15 * density));
        layoutParamsNote.addRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParamsNote.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        CardView cardView;
        RelativeLayout relativeLayout;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < timeTableList.get(i).size(); j++) {
                timeTableInf = timeTableList.get(i).get(j);
                isAddNote = false;
                relativeLayout = new RelativeLayout(getActivity());
                final TextView textView = new TextView(getActivity());
                textView.setText(timeTableInf.className.concat("@").concat(timeTableInf.address));

                textView.setTextSize(13);
                textView.setTypeface(typefaceOrchid);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setClickable(true);

                colorChoPar = (((int) (timeTableInf.className.charAt(0)) % 20) *
                        (Integer.parseInt(UserInformation.username.substring(UserInformation.username.length() - 3))) % 10 + 1);

                Log.d("classNO", String.valueOf(((int) (timeTableInf.className.charAt(0)) % 20)));
                Log.d("classNo1", String.valueOf((Integer.parseInt(UserInformation.username.substring(UserInformation.username.length() - 3))) % 10 + 1));


                if (j > 0 && timeTableInf.minKnob == timeTableList.get(i).get(j - 1).minKnob) {
                    if (currentWeekNo >= timeTableInf.minWeek && currentWeekNo <= timeTableInf.maxWeek
                            && ((timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_SINGLE && currentWeekNo % 2 == 1)
                            || (timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_DOUBLE && currentWeekNo % 2 == 0)
                            || timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_ALL)) {
                        setTextBackground(textView, colorChoPar);
                    } else {
                        continue;
                    }
                    isAddNote = true;
                }

                if (j < timeTableList.get(i).size() - 1 && timeTableInf.minKnob == timeTableList.get(i).get(j + 1).minKnob) {
                    if (currentWeekNo >= timeTableInf.minWeek && currentWeekNo <= timeTableInf.maxWeek
                            && ((timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_SINGLE && currentWeekNo % 2 == 1)
                            || (timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_DOUBLE && currentWeekNo % 2 == 0)
                            || timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_ALL)) {
                        setTextBackground(textView, colorChoPar);
                    }
                    isAddNote = true;
                }

                if (currentWeekNo >= timeTableInf.minWeek && currentWeekNo <= timeTableInf.maxWeek
                        && ((timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_SINGLE && currentWeekNo % 2 == 1)
                        || (timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_DOUBLE && currentWeekNo % 2 == 0)
                        || timeTableInf.weekHow == ConstantValues.TIMETABLE_WEEK_ALL)) {
                    setTextBackground(textView, colorChoPar);
                } else {
                    setTextBackground(textView, 0);
                }

                textView.setPadding((int) (2 * density), (int) (2 * density), (int) (2 * density), (int) (2 * density));

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), TimeTableItemDetailActivity.class);
                        intent.putExtra("dayInWeek", deterDayInWeek(view.getParent().getParent().getParent()));
                        intent.putExtra("courseName", textView.getText().toString());
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                    }
                });

                length = timeTableInf.maxKnob - timeTableInf.minKnob + 1;

                cardView = new CardView(getActivity());
                cardView.setCardElevation(1 * density);
                cardView.setRadius(5 * density);

                layoutParamsText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams layoutParamsRelativeLayout = new RelativeLayout
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((length * 50 - 6) * density));

                relativeLayout.addView(textView, layoutParamsText);

                if (isAddNote) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus));
                    relativeLayout.addView(imageView, layoutParamsNote);
                }

                cardView.addView(relativeLayout, layoutParamsText);

                layoutParamsRelativeLayout.setMargins((int) (3 * density), (int) (3 * density), (int) (3 * density), (int) (3 * density));
                layoutParamsRelativeLayout.topMargin = (int) ((50 * (timeTableInf.minKnob - 1) + 3) * density);

                weekDisplayLayout[i].addView(cardView, layoutParamsRelativeLayout);
            }
        }
        avLoadingIndicatorView.setVisibility(View.INVISIBLE);
    }

    private void requestCurrentTimetable() {
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("".equals(UserInformation.currentTerm)) {
                    GradesRequest.postRequestCurrentTerm();
                }

                if ("".equals(UserInformation.currentTerm)) {
                    UserInformation.currentTermChar = getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                            .getString(Entry.SharedPreferencesEntry.CURRENT_TERM, "");
                    UserInformation.userStaYear = getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                            .getInt(Entry.SharedPreferencesEntry.USER_STA_YEAR, 0);
                } else {
                    getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                            .putString(Entry.SharedPreferencesEntry.CURRENT_TERM, UserInformation.currentTermChar).apply();
                }

                TimeTableRequest.currentWeekNoRequest();
                if (UserInformation.currentWeekNo == -1) {
                    UserInformation.currentWeekNo = getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                            .getInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, 1);
                } else {
                    getActivity().getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                            .putInt(Entry.SharedPreferencesEntry.CURRENT_WEEK_NO, UserInformation.currentWeekNo).apply();
                }

                currentWeekNo = UserInformation.currentWeekNo;
                timeTableYear = UserInformation.currentTermChar;

                if (TimeTableSqliteHandle.isTimeTableEmpty(getActivity())) {
                    TimeTableRequest.timetableRequest(UserInformation.currentTerm);
                    TimeTableSqliteHandle.creatOrRefreshSQLite(getActivity());
                } else {
                    TimeTableSqliteHandle.loadData(getActivity());
                }
                if (isCreatTimeTableAlarm) {
                    TimeTableAlarmSetting.cancelTimeTableAlarm(getActivity(), UserInformation.timeTableList);
                    TimeTableAlarmSetting.createTimeTableAlarm(getActivity(), UserInformation.timeTableList);
                }
                UserInformation.currentTimeTableList = UserInformation.timeTableList;
                chooseTimetable = UserInformation.timeTableList;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoadWeekChooseView) {
                            loadWeekChooseView();
                            isLoadWeekChooseView = false;
                        }
                        initialTimeTable(UserInformation.timeTableList);
                    }
                });
            }
        });

    }

    private void initialTableView(View view) {
        timetableToolbar = (Toolbar) view.findViewById(R.id.timeTableToolbar);
        timetableToolbar.inflateMenu(R.menu.time_table_menu);
        timetableToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.time_table_change_term) {
                    termChooseBottomDialog.show();
                    WindowManager.LayoutParams layoutParams = termChooseBottomDialog.getWindow().getAttributes();
                    layoutParams.width = (int) (widthPixels);
                    termChooseBottomDialog.getWindow().setAttributes(layoutParams);
                    return true;
                } else if (item.getItemId() == R.id.time_table_currentTerm) {
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    weekDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                    weekSemesterDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                    if (isWeekChooseViewOnshow) {
                        weekChooseHorizontalScrollView.startAnimation(mHideAnimation);
                        weekChooseHorizontalScrollView.setVisibility(View.GONE);
                        weekChooseHorizontalScrollView.clearAnimation();
                        isWeekChooseViewOnshow = false;
                    }
                    if (currentWeekChooseTextView != null) {
                        currentWeekChooseTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label));
                    }
                    if (currentWeekTextView != null) {
                        currentWeekTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label_choose));
                    }
                    currentWeekChooseTextView = currentWeekTextView;
                    requestCurrentTimetable();
                    return true;
                } else {
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean isEqual = UserInformation.currentTerm.equals(UserInformation.termTranMap.get(timeTableYear));
                            if (isEqual) {
                                TimeTableAlarmSetting.cancelTimeTableAlarm(getActivity(), UserInformation.timeTableList);
                            }
                            TimeTableRequest.timetableRequest(UserInformation.termTranMap.get(timeTableYear));
                            if (isEqual) {
                                TimeTableSqliteHandle.creatOrRefreshSQLite(getActivity());
                                UserInformation.currentTimeTableList = UserInformation.timeTableList;
                                if (isCreatTimeTableAlarm) {
                                    TimeTableAlarmSetting.createTimeTableAlarm(getActivity(), UserInformation.timeTableList);
                                }
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    currentWeekNo = 1;
                                    chooseTimetable = UserInformation.timeTableList;
                                    initialTimeTable(UserInformation.timeTableList);
                                }
                            });
                        }
                    });
                    return true;
                }
            }
        });

        weekDisplayTextView = (TextView) view.findViewById(R.id.timeTableWeekDisplayText);
        weekSemesterDisplayTextView = (TextView) view.findViewById(R.id.timeTableSemesterDisplayText);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.timeTableAviloading);
        weekChooseHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.weekChooseScrollView);
        LinearLayout weekDisplayTitleLayout = (LinearLayout) view.findViewById(R.id.weekDisplayTitleLayout);
        weekChooseLinearLayout = (LinearLayout) view.findViewById(R.id.weekChooseLinearLayout);

        weekDisplayTitleLayout.setOnClickListener(this);

        LinearLayout[] weekLinearLayouts = new LinearLayout[7];
        TextView[] weekDateTextViews = new TextView[7];
        weekDisplayLayout = new RelativeLayout[7];

        TextView weekMonthTextView = (TextView) view.findViewById(R.id.weekMonth);
        weekLinearLayouts[0] = (LinearLayout) view.findViewById(R.id.weekLayout0);
        weekLinearLayouts[1] = (LinearLayout) view.findViewById(R.id.weekLayout1);
        weekLinearLayouts[2] = (LinearLayout) view.findViewById(R.id.weekLayout2);
        weekLinearLayouts[3] = (LinearLayout) view.findViewById(R.id.weekLayout3);
        weekLinearLayouts[4] = (LinearLayout) view.findViewById(R.id.weekLayout4);
        weekLinearLayouts[5] = (LinearLayout) view.findViewById(R.id.weekLayout5);
        weekLinearLayouts[6] = (LinearLayout) view.findViewById(R.id.weekLayout6);

        weekDateTextViews[0] = (TextView) view.findViewById(R.id.weekDate0);
        weekDateTextViews[1] = (TextView) view.findViewById(R.id.weekDate1);
        weekDateTextViews[2] = (TextView) view.findViewById(R.id.weekDate2);
        weekDateTextViews[3] = (TextView) view.findViewById(R.id.weekDate3);
        weekDateTextViews[4] = (TextView) view.findViewById(R.id.weekDate4);
        weekDateTextViews[5] = (TextView) view.findViewById(R.id.weekDate5);
        weekDateTextViews[6] = (TextView) view.findViewById(R.id.weekDate6);

        weekDisplayLayout[0] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout0);
        weekDisplayLayout[1] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout1);
        weekDisplayLayout[2] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout2);
        weekDisplayLayout[3] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout3);
        weekDisplayLayout[4] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout4);
        weekDisplayLayout[5] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout5);
        weekDisplayLayout[6] = (RelativeLayout) view.findViewById(R.id.weekDisplayLayout6);

        Calendar calendarNow = Calendar.getInstance();
        int week = calendarNow.get(Calendar.DAY_OF_WEEK);
        weekMonthTextView.setText(String.format("%d月", calendarNow.get(Calendar.MONTH) + 1));

        weekLinearLayouts[(week + 5) % 7].setBackgroundColor(getResources().getColor(R.color.tree_poppy));
        calendarNow.add(Calendar.DAY_OF_MONTH, -((week + 5) % 7));
        for (int i = 0; i < 7; i++) {
            weekDateTextViews[i].setText(String.format("%d日", calendarNow.get(Calendar.DAY_OF_MONTH)));
            calendarNow.add(Calendar.DAY_OF_MONTH, 1);
        }

        mShowAnimation = new TranslateAnimation(Animation.ZORDER_NORMAL, 0.0f,
                Animation.ZORDER_NORMAL, 0.0f, Animation.ZORDER_NORMAL,
                -1.0f, Animation.ZORDER_NORMAL, 0.0f);
        mShowAnimation.setDuration(500);
        mHideAnimation = new TranslateAnimation(Animation.ZORDER_NORMAL,
                0.0f, Animation.ZORDER_NORMAL, 0.0f,
                Animation.ZORDER_NORMAL, 0.0f, Animation.ZORDER_NORMAL,
                -1.0f);
        mHideAnimation.setDuration(500);

    }

    private void loadWeekChooseView() {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams((int) (density * 70), ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        TextView textView;
        RelativeLayout relativeLayout;
        int weekNum = 30;
        for (int i = 0; i < weekNum; i++) {
            textView = new TextView(getActivity());
            textView.setText("第".concat(Integer.toString(i + 1)).concat("周"));
            textView.setTextColor(getResources().getColor(R.color.steel));
            textView.setTypeface(typefaceOrchid);
            textView.setTextSize(15);
            textView.setPadding((int) (5 * density), (int) (5 * density), (int) (5 * density), (int) (5 * density));

            if (i == 0) {
                firstWeekTextView = textView;
            }
            if (i == currentWeekNo - 1) {
                textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label_choose));
                currentWeekChooseTextView = textView;
                currentWeekTextView = textView;
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentWeekChooseTextView != null) {
                        currentWeekChooseTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label));
                    }
                    currentWeekChooseTextView = (TextView) view;
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label_choose));
                    weekChooseClick(((TextView) view).getText().toString());
                }
            });

            relativeLayout = new RelativeLayout(getActivity());

            relativeLayout.addView(textView, relativeLayoutParams);
            weekChooseLinearLayout.addView(relativeLayout, linearLayoutParams);
        }
    }

    private void weekChooseClick(String weekStr) {
        int chooseWeek = Integer.parseInt(weekStr.substring(1, weekStr.length() - 1));
        if (chooseWeek != UserInformation.currentWeekNo) {
            weekDisplayTextView.setTextColor(getResources().getColor(R.color.bittersweet));
            weekSemesterDisplayTextView.setTextColor(getResources().getColor(R.color.bittersweet));
        } else {
            weekDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
            weekSemesterDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
        }
        scrollToTarget(chooseWeek, true);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        currentWeekNo = chooseWeek;

        initialTimeTable(chooseTimetable);
    }

    public void scrollToTarget(final int i, boolean isSmooth) {
        if (isSmooth) {
            weekChooseHorizontalScrollView.post(new Runnable() {
                @Override
                public void run() {
                    weekChooseHorizontalScrollView.smoothScrollTo((int) (70 * density * (i - 1)
                            - widthPixels / 2 + 35 * density), 0);
                }
            });
        } else {
            weekChooseHorizontalScrollView.post(new Runnable() {
                @Override
                public void run() {
                    weekChooseHorizontalScrollView.scrollTo((int) (70 * density * (i - 1)
                            - widthPixels / 2 + 35 * density), 0);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

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
            case R.id.weekDisplayTitleLayout:
                if (isWeekChooseViewOnshow) {
                    weekChooseHorizontalScrollView.startAnimation(mHideAnimation);
                    weekChooseHorizontalScrollView.setVisibility(View.GONE);
                    weekChooseHorizontalScrollView.clearAnimation();
                    isWeekChooseViewOnshow = false;
                } else {
                    weekChooseHorizontalScrollView.startAnimation(mShowAnimation);
                    weekChooseHorizontalScrollView.setVisibility(View.VISIBLE);
                    weekChooseHorizontalScrollView.clearAnimation();
                    isWeekChooseViewOnshow = true;
                }
                if (currentWeekNo != UserInformation.currentWeekNo) {
                    currentWeekNo = UserInformation.currentWeekNo;
                    scrollToTarget(currentWeekNo, false);
                    weekDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                    weekSemesterDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                    if (currentWeekChooseTextView != null) {
                        currentWeekChooseTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label));
                    }
                    if (currentWeekTextView != null) {
                        currentWeekTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label_choose));
                    }
                    currentWeekChooseTextView = currentWeekTextView;
                    initialTimeTable(chooseTimetable);
                } else {
                    scrollToTarget(currentWeekNo, false);
                }
                break;
            default:
        }
    }

    private void initialBottomDialog() {
        termChooseBottomDialog = new TermChooseDialog(getContext(), R.style.BottomDialog, new TermChooseDialog.MyDialogListener() {
            @Override
            public void onClick(final View view) {
                termChooseBottomDialog.dismiss();
                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                final String term = UserInformation.termTranMap.get(((TextView) view).getText().toString().substring(0, 11));

                MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        TimeTableRequest.timetableRequest(term);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isWeekChooseViewOnshow) {
                                    weekChooseHorizontalScrollView.startAnimation(mHideAnimation);
                                    weekChooseHorizontalScrollView.setVisibility(View.GONE);
                                    weekChooseHorizontalScrollView.clearAnimation();
                                    isWeekChooseViewOnshow = false;
                                }
                                weekDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                                weekSemesterDisplayTextView.setTextColor(getResources().getColor(R.color.white_smoke));
                                if (currentWeekChooseTextView != null) {
                                    currentWeekChooseTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label));
                                }
                                firstWeekTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.week_choose_label_choose));
                                currentWeekChooseTextView = firstWeekTextView;
                                timeTableYear = ((TextView) view).getText().toString().substring(0, 11);
                                currentWeekNo = 1;
                                chooseTimetable = UserInformation.timeTableList;
                                initialTimeTable(UserInformation.timeTableList);
                            }
                        });
                    }
                });
            }
        });
        termChooseBottomDialog.setCanceledOnTouchOutside(true);
        termChooseBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        termChooseBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }


    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert mConnectivityManager != null;
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private int deterDayInWeek(ViewParent viewParent) {
        if (weekDisplayLayout[0].equals(viewParent)) {
            return 0;
        } else if (weekDisplayLayout[1].equals(viewParent)) {
            return 1;
        } else if (weekDisplayLayout[2].equals(viewParent)) {
            return 2;
        } else if (weekDisplayLayout[3].equals(viewParent)) {
            return 3;
        } else if (weekDisplayLayout[4].equals(viewParent)) {
            return 4;
        } else if (weekDisplayLayout[5].equals(viewParent)) {
            return 5;
        } else {
            return 6;
        }
    }

    private void setTextBackground(TextView textView, int no) {
        switch (no) {
            case 0:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_gray));
                break;
            case 1:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_bittersweet));
                break;
            case 2:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_mandy));
                break;
            case 3:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_flamenco));
                break;
            case 4:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_java));
                break;
            case 5:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_seagreen));
                break;
            case 6:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_fuchsiablue));
                break;
            case 7:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_tuliptree));
            case 8:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_lima));
                break;
            case 9:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_dodgerblue));
                break;
            case 10:
                textView.setBackground(getResources().getDrawable(R.drawable.time_table_panel_larioja));
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
