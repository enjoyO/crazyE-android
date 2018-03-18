package com.bai.van.radixe.datastru;

/**
 *
 * @author van
 * @date 2018/1/16
 */

public class ExamScoreInf {
    public String examName = "";
    public String examScore = "";
    public String examSemeter = "";
    public String examGpa = "";
    public String examCredit = "";

    public String examUsualPerformance = "";
    public String examMidtermPerformance = "";
    public String examFinalPerformance = "";

    public String examUsualPerformanceRatio = "";
    public String examMidtermPerformanceRatio = "";
    public String examFinalPerformanceRatio = "";

    public String examType = "";
    public String examAcademy = "";
    public String examPass = "";
    public String examValidity = "";

    public String examTime = "";
    public String examWay = "";

    public String examPeriod = "";
    public String examNatu = "";

    @Override
    public String toString() {
        return "ExamScoreInf{" +
                "examName='" + examName + '\'' +
                ", examScore='" + examScore + '\'' +
                ", examSemeter='" + examSemeter + '\'' +
                ", examGpa='" + examGpa + '\'' +
                ", examCredit='" + examCredit + '\'' +
                '}';
    }
}
