package com.bai.van.radixe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.userdata.UserInformation;

/**
 * @author van
 */

public class ExamScoreDetailActivity extends BaseActivity {
    private TextView examName, examScore, examGpa, examCredit, examPeriod, examSemester,
            examLearnWay, examValidity, examPass, examType, examNatu, examAcademy, examTime,
            examUsualPerformance, examMidtermPerformance, examFinalPerformance,
            examUsualPerformanceRatio, examMidtermPerformanceRatio, examFinalPerformanceRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_score_detail);
        initial();
        loadScoreInf(getIntent().getStringExtra("examScoreName"));
    }
    private void loadScoreInf(String name){
        ExamScoreInf examScoreInf = null;
        for (int i = 0; i < UserInformation.examScoreInfList.size(); i++){
            if (UserInformation.examScoreInfList.get(i).examName.equals(name)){
                examScoreInf = UserInformation.examScoreInfList.get(i);
                break;
            }
        }
        if (examScoreInf != null){
            examName.setText(examScoreInf.examName);
            examScore.setText(examScoreInf.examScore);
            examGpa.setText(examScoreInf.examGpa);
            examCredit.setText(examScoreInf.examCredit);
            examPeriod.setText(examScoreInf.examPeriod);
            examSemester.setText(examScoreInf.examSemeter);
            examLearnWay.setText("".equals(examScoreInf.examWay) ? "无" : examScoreInf.examWay);
            examValidity.setText(examScoreInf.examValidity);
            examPass.setText(examScoreInf.examPass);
            examType.setText(examScoreInf.examType);
            examNatu.setText(examScoreInf.examNatu);
            examAcademy.setText(examScoreInf.examAcademy);
            examTime.setText(examScoreInf.examTime);

            examUsualPerformance.setText("null".equals(examScoreInf.examUsualPerformance) ? "无" : examScoreInf.examUsualPerformance);
            examMidtermPerformance.setText("null".equals(examScoreInf.examMidtermPerformance) ? "无" : examScoreInf.examMidtermPerformance);
            examFinalPerformance.setText("null".equals(examScoreInf.examFinalPerformance) ? "无" : examScoreInf.examFinalPerformance);

            examUsualPerformanceRatio.setText("null".equals(examScoreInf.examUsualPerformanceRatio) ? "无" : examScoreInf.examUsualPerformanceRatio.concat("%"));
            examMidtermPerformanceRatio.setText("null".equals(examScoreInf.examMidtermPerformanceRatio) ? "无" : examScoreInf.examMidtermPerformanceRatio.concat("%"));
            examFinalPerformanceRatio.setText("null".equals(examScoreInf.examFinalPerformanceRatio) ? "无" : examScoreInf.examFinalPerformanceRatio.concat("%"));

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

        RelativeLayout scoreDetailBack = (RelativeLayout) findViewById(R.id.examScoreDetailBack);

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
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
