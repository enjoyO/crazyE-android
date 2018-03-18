package com.bai.van.radixe;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.entry.Entry;
import com.bai.van.radixe.urlrequests.UserInfRequest;
import com.bai.van.radixe.userdata.UserInformation;

/**
 *
 * @author van
 * @date 2018/2/1
 */
public class UserInfActivity extends BaseActivity {

    private TextView baseNameText, baseNameSpellText, baseStuNumberText, baseGenderText, baseBornDateText,
            baseFromText, basePoliticsStatusText, baseNationText, baseCountryText, baseIdCardNOText, baseIdCardTypeText,
            rollGradeText, rollAcademyText, rollMajorText, rollMajorDirectionText, rollClassText,
            rollLengthSysText, rollTypeText, rollLearnTypeText, rollIsInRollText, rollIsInSchoolText, rollIsHaveRollText,
            entranDateText, entranGradeText, entranFromText, entranForeignLanTypeText, entranLearnTypeText,
            entranSpecialStuTypeText, entranTrainTypeText, entranEnrollYearText, entranEnrollScoreText, entranEnrollNOText, entranMidSchoolText;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inf);
        handler = new Handler();
        initial();
    }
    private void initial(){
        RelativeLayout userInfBackLayout = findViewById(R.id.userInfBackLayout);
        userInfBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        baseNameText = findViewById(R.id.userInfBaseName);
        baseNameSpellText = findViewById(R.id.userInfBaseNameSpell);
        baseStuNumberText = findViewById(R.id.userInfBaseStuNO);
        baseGenderText = findViewById(R.id.userInfBaseGender);
        baseBornDateText = findViewById(R.id.userInfBaseBornDate);
        baseFromText = findViewById(R.id.userInfBaseFrom);
        basePoliticsStatusText = findViewById(R.id.userInfBasePoliticsStatus);
        baseNationText = findViewById(R.id.userInfBaseNational);
        baseCountryText = findViewById(R.id.userInfBaseCountry);
        baseIdCardNOText = findViewById(R.id.userInfBaseIdCardNO);
        baseIdCardTypeText = findViewById(R.id.userInfBaseIdCardType);

        rollGradeText = findViewById(R.id.userInfRollGrade);
        rollAcademyText = findViewById(R.id.userInfRollAcademy);
        rollMajorText = findViewById(R.id.userInfRollMajor);
        rollMajorDirectionText = findViewById(R.id.userInfRollMajorDir);
        rollClassText = findViewById(R.id.userInfRollClass);
        rollLengthSysText = findViewById(R.id.userInfRollGradeLengthSys);
        rollTypeText = findViewById(R.id.userInfRollType);
        rollLearnTypeText = findViewById(R.id.userInfRollLearnType);
        rollIsInRollText = findViewById(R.id.userInfRollIsInRoll);
        rollIsInSchoolText = findViewById(R.id.userInfRollIsInSchool);
        rollIsHaveRollText = findViewById(R.id.userInfRollIsHaveRoll);

        entranDateText = findViewById(R.id.userInfEntranDate);
        entranGradeText = findViewById(R.id.userInfEntranDGrade);
        entranFromText = findViewById(R.id.userInfEntranFrom);
        entranForeignLanTypeText = findViewById(R.id.userInfEntranForeignLanType);
        entranLearnTypeText = findViewById(R.id.userInfEntranLearnType);
        entranSpecialStuTypeText = findViewById(R.id.userInfEntranSpecialStuType);
        entranTrainTypeText = findViewById(R.id.userInfEntranTrainType);
        entranEnrollYearText = findViewById(R.id.userInfEntranEnrollYear);
        entranEnrollScoreText = findViewById(R.id.userInfEntranEnrollScore);
        entranEnrollNOText = findViewById(R.id.userInfEntranEnrollNO);
        entranMidSchoolText = findViewById(R.id.userInfEntranMidSchool);

        displayInf();
    }
    private void displayInf(){
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if ("".equals(UserInformation.userInf.baseName)
                        || "".equals(UserInformation.userInf.rollAcademy)
                        || "".equals(UserInformation.userInf.entranMidSchool)){
                    UserInfRequest.requestUserBaseInf();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getSharedPreferences(Entry.SharedPreferencesEntry.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                                .edit().putInt(Entry.SharedPreferencesEntry.USER_STA_YEAR, UserInformation.userStaYear).apply();

                        baseNameText.setText(UserInformation.userInf.baseName);
                        baseNameSpellText.setText(UserInformation.userInf.baseNameSpell);
                        baseStuNumberText.setText(UserInformation.userInf.baseStuNumber);
                        baseGenderText.setText(UserInformation.userInf.baseGender);
                        baseBornDateText.setText(UserInformation.userInf.baseBornDate);
                        baseFromText.setText(UserInformation.userInf.baseFrom);
                        basePoliticsStatusText.setText(UserInformation.userInf.basePoliticsStatus);
                        baseNationText.setText(UserInformation.userInf.baseNation);
                        baseCountryText.setText(UserInformation.userInf.baseCountry);
                        baseIdCardNOText.setText(UserInformation.userInf.baseIdCardNO);
                        baseIdCardTypeText.setText(UserInformation.userInf.baseIdCardType);

                        rollGradeText.setText(UserInformation.userInf.rollGrade);
                        rollAcademyText.setText(UserInformation.userInf.rollAcademy);
                        rollMajorText.setText(UserInformation.userInf.rollMajor);
                        rollMajorDirectionText.setText(UserInformation.userInf.rollMajorDirection);
                        rollClassText.setText(UserInformation.userInf.rollClass);
                        rollLengthSysText.setText(UserInformation.userInf.rollLengthSys);
                        rollTypeText.setText(UserInformation.userInf.rollType);
                        rollLearnTypeText.setText(UserInformation.userInf.rollLearnType);
                        rollIsInRollText.setText(UserInformation.userInf.rollIsInRoll);
                        rollIsInSchoolText.setText(UserInformation.userInf.rollIsInSchool);
                        rollIsHaveRollText.setText(UserInformation.userInf.rollIsHaveRoll);

                        entranDateText.setText(UserInformation.userInf.entranDate);
                        entranGradeText.setText(UserInformation.userInf.entranGrade);
                        entranFromText.setText(UserInformation.userInf.entranFrom);
                        entranForeignLanTypeText.setText(UserInformation.userInf.entranForeignLanType);
                        entranLearnTypeText.setText(UserInformation.userInf.entranLearnType);
                        entranSpecialStuTypeText.setText(UserInformation.userInf.entranSpecialStuType);
                        entranTrainTypeText.setText(UserInformation.userInf.entranTrainType);
                        entranEnrollYearText.setText(UserInformation.userInf.entranEnrollYear);
                        entranEnrollScoreText.setText(UserInformation.userInf.entranEnrollScore);
                        entranEnrollNOText.setText(UserInformation.userInf.entranEnrollNO);
                        entranMidSchoolText.setText(UserInformation.userInf.entranMidSchool);
                    }
                });
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
}
