package com.bai.van.radixe.datastru;

/**
 *
 * @author van
 * @date 2018/2/1
 */

public class ScoreAnalyseInf {
    public double gainedCredit = 0;
    public double electivedCreditCount = 0;
    public double gpa = 0;
    public double weightedGrdes = 0;
    public int unPassedCount = 0;
    public String semesterStr = "";

    @Override
    public String toString() {
        return "ScoreAnalyseInf{" +
                "gainedCredit=" + gainedCredit +
                ", electivedCreditCount=" + electivedCreditCount +
                ", gpa=" + gpa +
                ", weightedGrdes=" + weightedGrdes +
                ", unPassedCount=" + unPassedCount +
                ", semesterStr='" + semesterStr + '\'' +
                '}';
    }
}
