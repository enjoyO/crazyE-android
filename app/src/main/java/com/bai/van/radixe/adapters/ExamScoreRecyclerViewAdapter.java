package com.bai.van.radixe.adapters;

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

    private ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener recyclerViewItemClickListener;

    public ExamScoreRecyclerViewAdapter(ExamScoreRecyclerViewFragment mContext, List<ExamScoreInf> mScoreData) {
        this.mContext = mContext;
        this.mScoreData = mScoreData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_score, parent, false);
        return new ExamScoreRecyclerViewHolder(view, recyclerViewItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);

        ExamScoreRecyclerViewHolder examScoreRecyclerViewHolder = (ExamScoreRecyclerViewHolder) holder;
        ExamScoreInf examScoreInf = mScoreData.get(position);
        examScoreRecyclerViewHolder.examName.setText(examScoreInf.examName);
        examScoreRecyclerViewHolder.examValue.setText(examScoreInf.examScore);
        examScoreRecyclerViewHolder.examSemetser.setText(examScoreInf.examSemeter);
        examScoreRecyclerViewHolder.examCredit.setText("学分: " + examScoreInf.examCredit);
        examScoreRecyclerViewHolder.examGpa.setText("绩点: " + examScoreInf.examGpa);
        examScoreRecyclerViewHolder.examType.setText(examScoreInf.examType);
    }

    @Override
    public int getItemCount() {
        return mScoreData.size();
    }

    public void setOnItemClickListener(ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }
}
