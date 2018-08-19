package com.bai.van.radixe.overridemodule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.R;
import com.bai.van.radixe.fragment.TimeTableFragment;

/**
 *
 * @author van
 * @date 2018/2/13
 */

public class TimeTableRemindIntervalDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TimeTableRemindIntervalDialog.MyDialogListener listener;
    private LinearLayout timeTableRemindIntervalChooseLayout;

    private float density;
    private Typeface typefaceOrchid;

    public TimeTableRemindIntervalDialog(Context context, int theme, TimeTableRemindIntervalDialog.MyDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.time_table_alarm_remind_time_interval_dialog);

        density = TimeTableFragment.density;

        typefaceOrchid = Typeface.create("orchid_pavilion_black.TTF", Typeface.NORMAL);

        timeTableRemindIntervalChooseLayout = (LinearLayout) findViewById(R.id.timeTableRemindIntervalChooseLayout);

        initialRemiondTimeIntervalChooseView();
    }
    private void initialRemiondTimeIntervalChooseView(){

        RelativeLayout.LayoutParams layoutParamsLine = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        layoutParamsLine.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParamsLine.setMargins((int) (15 * density), 0, (int) (15 * density), 0);
        LinearLayout.LayoutParams layoutParamsRelativeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (35 * density));
        RelativeLayout.LayoutParams layoutParamsText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout relativeLayout;
        TextView textView;
        TextView textViewLine;

        for (int i = 0; i < 8; i++){

            textViewLine = new TextView(context);
            textViewLine.setBackgroundColor(context.getResources().getColor(R.color.geyser));
            relativeLayout = new RelativeLayout(context);
            textView = new TextView(context);

            textView.setText("上课前 ".concat(Integer.toString(5 * (i + 1))).concat(" 分钟提醒"));

            textView.setBackground(context.getResources().getDrawable(R.drawable.term_choose_text_background));
            textView.setTextColor(context.getResources().getColor(R.color.tuatara));
            textView.setTextSize(16);
            textView.setPadding((int) (20 * density), 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTypeface(typefaceOrchid);

            textView.setOnClickListener(this);

            relativeLayout.addView(textView, layoutParamsText);
            relativeLayout.addView(textViewLine, layoutParamsLine);
            timeTableRemindIntervalChooseLayout.addView(relativeLayout, layoutParamsRelativeLayout);
        }
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

    public interface MyDialogListener {
        public void onClick(View view);
    }
}
