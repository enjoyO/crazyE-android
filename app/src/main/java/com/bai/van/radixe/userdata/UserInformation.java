package com.bai.van.radixe.userdata;

import com.bai.van.radixe.datastru.ExamInf;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.datastru.ScoreAnalyseInf;
import com.bai.van.radixe.datastru.TermInf;
import com.bai.van.radixe.datastru.TimeTableInf;
import com.bai.van.radixe.datastru.UserInf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/13
 */

public class UserInformation {

    public static String currentTerm = "";
    public static String currentTermChar = "";

    public static int currentWeekNo = -1;

    public static String username = "";
    public static String usernameChar = "";
    public static String password = "";

    public static String userAcademy = "";
    public static String userMajor = "";
    public static int userStaYear = 0;

    public static UserInf userInf = new UserInf();

    public static double gainedCreditAll = 0;
    public static double electivedCreditAll = 0;
    public static double gpaAll = 0;
    public static double weightedGrdesAll = 0;
    public static int unPassedCountAll = 0;

    public static List<ScoreAnalyseInf> scoreAnalyseInfList = new ArrayList<>();

    public static List<ExamInf> examScheduledFinishList = new ArrayList<>();
    public static List<ExamInf> examScheduledUnfinishList = new ArrayList<>();
    public static List<ExamInf> examInScheduleList = new ArrayList<>();

    public static List<ExamScoreInf> examScoreInfList = new ArrayList<>();
    public static List<String> userSemesterList = new ArrayList<>();
    public static List<String> userSemesterDeList = new ArrayList<>();

    public static List<List<TimeTableInf>> timeTableList = new ArrayList<>();
    public static List<List<TimeTableInf>> currentTimeTableList = new ArrayList<>();

    public static List<TermInf> termInfList = new ArrayList<>();
    public static HashMap<String, String> termTranMap = new HashMap<>();

    public static void clearValue(){
        currentTerm = "";
        currentTermChar = "";

        currentWeekNo = 1;

        username = "";
        usernameChar = "";
        password = "";

        userAcademy = "";
        userMajor = "";
        userStaYear = 0;

        userInf = new UserInf();

        gainedCreditAll = 0;
        electivedCreditAll = 0;
        gpaAll = 0;
        weightedGrdesAll = 0;
        unPassedCountAll = 0;

        scoreAnalyseInfList = new ArrayList<>();

        examScheduledFinishList = new ArrayList<>();
        examScheduledUnfinishList = new ArrayList<>();
        examInScheduleList = new ArrayList<>();

        examScoreInfList = new ArrayList<>();
        userSemesterList = new ArrayList<>();
        userSemesterDeList = new ArrayList<>();

        timeTableList = new ArrayList<>();

        termInfList = new ArrayList<>();
        termTranMap = new HashMap<>();
    }
}
