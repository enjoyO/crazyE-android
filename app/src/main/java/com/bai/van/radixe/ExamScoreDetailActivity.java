package com.bai.van.radixe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.constantdata.SharedData;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * @author van
 */

public class ExamScoreDetailActivity extends Activity {
    private TextView examName, examScore, examGpa, examCredit, examPeriod, examSemester,
            examLearnWay, examValidity, examPass, examType, examNatu, examAcademy, examTime,
            examUsualPerformance, examMidtermPerformance, examFinalPerformance,
            examUsualPerformanceRatio, examMidtermPerformanceRatio, examFinalPerformanceRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_exam_score_detail);
        initial();
        loadScoreInf(getIntent().getStringExtra("examScoreName"));
    }
    private void loadScoreInf(String name){

        if (SharedData.examScoreInf != null){
            examName.setText(SharedData.examScoreInf.examName);
            examScore.setText("成绩: ".concat(SharedData.examScoreInf.examScore));
            examGpa.setText("绩点: ".concat(SharedData.examScoreInf.examGpa));
            examCredit.setText("学分: ".concat(SharedData.examScoreInf.examCredit));
            examPeriod.setText(SharedData.examScoreInf.examPeriod);
            examSemester.setText(SharedData.examScoreInf.examSemeter);
            examLearnWay.setText("".equals(SharedData.examScoreInf.examWay) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examWay);
            examValidity.setText(SharedData.examScoreInf.examValidity);
            examPass.setText(SharedData.examScoreInf.examPass);
            examType.setText(SharedData.examScoreInf.examType);
            examNatu.setText(SharedData.examScoreInf.examNatu);
            examAcademy.setText(SharedData.examScoreInf.examAcademy);
            examTime.setText(SharedData.examScoreInf.examTime);

            examUsualPerformance.setText("null".equals(SharedData.examScoreInf.examUsualPerformance) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examUsualPerformance);
            examMidtermPerformance.setText("null".equals(SharedData.examScoreInf.examMidtermPerformance) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examMidtermPerformance);
            examFinalPerformance.setText("null".equals(SharedData.examScoreInf.examFinalPerformance) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examFinalPerformance);

            examUsualPerformanceRatio.setText("null".equals(SharedData.examScoreInf.examUsualPerformanceRatio) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examUsualPerformanceRatio.concat("%"));
            examMidtermPerformanceRatio.setText("null".equals(SharedData.examScoreInf.examMidtermPerformanceRatio) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examMidtermPerformanceRatio.concat("%"));
            examFinalPerformanceRatio.setText("null".equals(SharedData.examScoreInf.examFinalPerformanceRatio) ? ConstantValues.NO_DATA_TEXT : SharedData.examScoreInf.examFinalPerformanceRatio.concat("%"));

        }else {
            Log.d("ExamScoreDetail", "error");
        }
    }
    private void initial(){
        examName = (TextView) findViewById(R.id.examNameText);
        examScore = (TextView) findViewById(R.id.examScoreText);
        examGpa = (TextView) findViewById(R.id.examGpaText);
        examCredit = (TextView) findViewById(R.id.examCreditText);
        examPeriod = (TextView) findViewById(R.id.examPeriodText);
        examSemester = (TextView) findViewById(R.id.examSemesterText);
        examLearnWay = (TextView) findViewById(R.id.examLearnWayText);
        examValidity = (TextView) findViewById(R.id.examValidityText);
        examPass = (TextView) findViewById(R.id.examPassText);
        examType = (TextView) findViewById(R.id.examTypeText);
        examNatu = (TextView) findViewById(R.id.examNatuText);
        examAcademy = (TextView) findViewById(R.id.examAcademyText);
        examTime = (TextView) findViewById(R.id.examTimeText);

        examUsualPerformance = (TextView) findViewById(R.id.examUsualPerformanceText);
        examMidtermPerformance = (TextView) findViewById(R.id.examMidtermPerformanceText);
        examFinalPerformance = (TextView) findViewById(R.id.examFinalPerformanceText);
        examUsualPerformanceRatio = (TextView) findViewById(R.id.examUsualPerformanceRatioText);
        examMidtermPerformanceRatio = (TextView) findViewById(R.id.examMidtermPerformanceRatioText);
        examFinalPerformanceRatio = (TextView) findViewById(R.id.examFinalPerformanceRatioText);

        LinearLayout scoreDetailBack = findViewById(R.id.examScoreDetailBack);

        scoreDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.accelerate_decelerate_interpolator, android.R.anim.accelerate_interpolator);
    }
}
