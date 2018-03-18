package com.bai.van.radixe.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bai.van.radixe.R;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.ScoreAnalyseInf;

import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/31
 */

public class ScoreAnalyseRecyclerViewAdapter extends RecyclerView.Adapter<ScoreAnalyseRecyclerViewAdapter.ScoreAnalyseViewHolder>{
    public List<ScoreAnalyseInf> dataList;

    public ScoreAnalyseRecyclerViewAdapter(List<ScoreAnalyseInf> dataList) {
        this.dataList = dataList;
    }

    @Override
    public ScoreAnalyseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_analyse_item, parent,false);
        return new ScoreAnalyseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreAnalyseViewHolder holder, int position) {
        holder.gainedCreditText.setText(dataList.get(position).gainedCredit + "");
        holder.electivedCreditText.setText(dataList.get(position).electivedCreditCount + "");
        holder.gpa.setText(dataList.get(position).gpa + "");
        holder.weightedGrdes.setText(dataList.get(position).weightedGrdes + "");
        holder.unPassedCountText.setText(dataList.get(position).unPassedCount + "");
        holder.semesterText.setText(dataList.get(position).semesterStr);
        holder.semesterTranText.setText(StaticMethod.semesterTran(dataList.get(position).semesterStr));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public static class ScoreAnalyseViewHolder extends RecyclerView.ViewHolder {
        public TextView gainedCreditText, electivedCreditText, gpa, weightedGrdes, unPassedCountText, semesterText, semesterTranText;

        public ScoreAnalyseViewHolder(View itemView) {
            super(itemView);
            gainedCreditText = itemView.findViewById(R.id.scoreAnalyseItemGainedCreditCountText);
            electivedCreditText = itemView.findViewById(R.id.scoreAnalyseItemElectivedCreditCountText);
            gpa = itemView.findViewById(R.id.scoreAnalyseItemGpaText);
            weightedGrdes = itemView.findViewById(R.id.scoreAnalyseItemWeightedMeanGradesText);
            unPassedCountText = itemView.findViewById(R.id.scoreAnalyseItemUnPassedCountText);
            semesterText = itemView.findViewById(R.id.scoreAnalyseItemSemesterText);
            semesterTranText = itemView.findViewById(R.id.scoreAnalyseItemSemesterTranText);
        }
    }
}
