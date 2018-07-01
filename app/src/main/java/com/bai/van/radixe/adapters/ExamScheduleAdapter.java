package com.bai.van.radixe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bai.van.radixe.constantdata.ConstantValues;
import com.bai.van.radixe.datastru.ExamInf;
import com.bai.van.radixe.R;

import java.util.List;

/**
 *
 * @author van
 * @date 2018/1/15
 */

public class ExamScheduleAdapter extends RecyclerView.Adapter<ExamScheduleAdapter.ViewHolderExamSchedule> {

    private List<ExamInf> dataList = null;
    private int dataType = 0;
    private Context context;

    public ExamScheduleAdapter(List<ExamInf> dataList, int dataType, Context context) {
        this.dataList = dataList;
        this.dataType = dataType;
        this.context = context;
    }

    @Override
    public ViewHolderExamSchedule onCreateViewHolder(ViewGroup parent, int viewType) {
        if (dataType == ConstantValues.EXAM_IN_SCHEDULE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_in_schedule_project, parent,false);
            ViewHolderExamSchedule viewHolderExamSchedule = new ViewHolderExamSchedule(view, dataType);
            return viewHolderExamSchedule;
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_scheduled_project, parent,false);
            ViewHolderExamSchedule viewHolderExamSchedule = new ViewHolderExamSchedule(view, dataType);
            return viewHolderExamSchedule;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolderExamSchedule holder, int position) {
        if (dataType == ConstantValues.EXAM_IN_SCHEDULE){
                holder.examInScheduleNo.setText(String.valueOf(position + 1));
                holder.examInScheduleId.setText(dataList.get(position).id);
                holder.examInSCheduleName.setText(dataList.get(position).name);
        }else {
            if (dataType == ConstantValues.EXAM_SCHEDULE_FINISHED){
                holder.examName.setText(dataList.get(position).name);
                holder.examType.setText(dataList.get(position).type);
                holder.examTime.setText(dataList.get(position).times);
                holder.examAdress.setText(dataList.get(position).address);
                holder.examTimeWeek.setText(dataList.get(position).timesWeek);
            }
            if (dataType == ConstantValues.EXAM_SCHEDULE_UNFINISHE){
                holder.examName.setText(dataList.get(position).name);
                holder.examType.setText(dataList.get(position).type);
                holder.examTime.setText(dataList.get(position).times);
                holder.examAdress.setText(dataList.get(position).address);
                holder.examIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.unfinish_circle_1));
                holder.examName.setTextColor(context.getResources().getColor(R.color.steel));
                holder.examTime.setTextColor(context.getResources().getColor(R.color.mine_shaft));
                holder.examAdress.setTextColor(context.getResources().getColor(R.color.mine_shaft));
                holder.examType.setBackground(context.getResources().getDrawable(R.drawable.my_exam_information_ksmc_blue));
                holder.examTimeWeek.setBackground(context.getResources().getDrawable(R.drawable.my_exam_information_ksmc_treepoly));
                holder.examTimeWeek.setText(dataList.get(position).timesWeek);
            }
            if (position == (dataList.size() - 1)){
                holder.examFootColor.setBackgroundColor(context.getResources().getColor(R.color.snow_white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolderExamSchedule extends RecyclerView.ViewHolder {
        public TextView examName, examAdress, examTime, examTimeWeek, examType, examFootColor;
        public ImageView examIcon;

        public TextView examInScheduleNo, examInSCheduleName, examInScheduleId;

        public ViewHolderExamSchedule(View itemView, int type) {
            super(itemView);
            if (type == ConstantValues.EXAM_IN_SCHEDULE){
                examInScheduleId = (TextView) itemView.findViewById(R.id.examInScheduleId);
                examInSCheduleName = (TextView) itemView.findViewById(R.id.examInScheduleName);
                examInScheduleNo = (TextView) itemView.findViewById(R.id.examInScheduleNo);
            }else {
                examName = (TextView) itemView.findViewById(R.id.examScheduleNameText);
                examAdress = (TextView) itemView.findViewById(R.id.examScheduleAddressText);
                examTime = (TextView) itemView.findViewById(R.id.examScheduleTimeText);
                examTimeWeek = (TextView) itemView.findViewById(R.id.examScheduleTimeWeekText);
                examType = (TextView) itemView.findViewById(R.id.examScheduleTypeText);
                examFootColor = (TextView) itemView.findViewById(R.id.examScheduleFootColor);
                examIcon = (ImageView) itemView.findViewById(R.id.examScheduleIcon);
            }
        }
    }
}
