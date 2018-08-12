package com.bai.van.radixe.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.fragment.ExamScoreRecyclerViewFragment;
import com.bai.van.radixe.R;

import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/16
 */

public class ExamScoreRecyclerViewAdapter extends RecyclerView.Adapter{

    private ExamScoreRecyclerViewFragment mContext;
    public List<ExamScoreInf> mScoreData;

    private String semester = "";

    public boolean sign = false;

    private ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener recyclerViewItemClickListener;

    public ExamScoreRecyclerViewAdapter(ExamScoreRecyclerViewFragment mContext, List<ExamScoreInf> mScoreData, String semester) {
        this.mContext = mContext;
        this.mScoreData = mScoreData;
        this.semester = semester;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_score, parent, false);
            return new ExamScoreRecyclerViewHolder(view, recyclerViewItemClickListener, viewType);
        }else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_head, parent, false);
            return new ExamScoreRecyclerViewHolder(view, recyclerViewItemClickListener, viewType);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_foot, parent, false);
            return new ExamScoreRecyclerViewHolder(view, recyclerViewItemClickListener, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        ExamScoreRecyclerViewHolder examScoreRecyclerViewHolder = (ExamScoreRecyclerViewHolder) holder;

        if (getItemViewType(position) == 1) {

            ExamScoreInf examScoreInf = mScoreData.get(position - 1);
            examScoreRecyclerViewHolder.examName.setText(examScoreInf.examName);
            examScoreRecyclerViewHolder.examValue.setText(examScoreInf.examScore);
            examScoreRecyclerViewHolder.examCredit.setText("学分: " + examScoreInf.examCredit);
            examScoreRecyclerViewHolder.examGpa.setText("绩点: " + examScoreInf.examGpa);
            examScoreRecyclerViewHolder.examType.setText(examScoreInf.examType);
        }else if (getItemViewType(position) == 0) {
            float credit = 0;
            for (ExamScoreInf examScoreInf : mScoreData) {
                credit += Float.parseFloat(examScoreInf.examCredit);
            }

            if (sign) {

                float temp = 0;
                for (ExamScoreInf examScoreInf : mScoreData) {
                    temp += Float.parseFloat(examScoreInf.examCredit) * Float.parseFloat(examScoreInf.examGpa);
                }

                examScoreRecyclerViewHolder.examHeaderSemester.setText("结果");
                examScoreRecyclerViewHolder.examHeaderCount.setText("GPA: " + temp/credit);
                examScoreRecyclerViewHolder.examHeaderCredit.setText("总学分: " + credit);


            }else {
                examScoreRecyclerViewHolder.examHeaderSemester.setText(this.semester);
                examScoreRecyclerViewHolder.examHeaderCount.setText("已出成绩: " + this.mScoreData.size());
                examScoreRecyclerViewHolder.examHeaderCredit.setText("已出学分: " + credit);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mScoreData.size() > 0) {
            return mScoreData.size() + 2;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }else if (position == this.mScoreData.size() + 1) {
            return 2;
        }else {
            return 1;
        }
    }


    public void setOnItemClickListener(ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }
}
