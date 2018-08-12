package com.bai.van.radixe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bai.van.radixe.R;

/**
 *
 * @author van
 * @date 2018/1/23
 */

public class ExamScoreRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView examValue, examName, examCredit, examGpa, examType;

    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public TextView examHeaderSemester, examHeaderCredit, examHeaderCount;

    public ExamScoreRecyclerViewHolder(View itemView, RecyclerViewItemClickListener recyclerViewItemClickListener, int type) {
        super(itemView);

        if (type == 1) {
            this.recyclerViewItemClickListener = recyclerViewItemClickListener;
            itemView.setOnClickListener(this);

            examName = (TextView) itemView.findViewById(R.id.examScoreName);
            examValue = (TextView) itemView.findViewById(R.id.examScoreValue);
            examCredit = (TextView) itemView.findViewById(R.id.examScoreCredit);
            examGpa = (TextView) itemView.findViewById(R.id.examScoreGpa);
            examType = (TextView) itemView.findViewById(R.id.examType);
        }else if (type == 0){
            examHeaderSemester = itemView.findViewById(R.id.item_score_head_semester_text);
            examHeaderCredit = itemView.findViewById(R.id.item_score_head_get_credit_text);
            examHeaderCount = itemView.findViewById(R.id.item_score_head_count_text);
        }
    }

    @Override
    public void onClick(View view) {
        if(recyclerViewItemClickListener != null){
            recyclerViewItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface RecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}