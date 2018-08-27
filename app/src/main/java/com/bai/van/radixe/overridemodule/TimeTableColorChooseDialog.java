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
 * @author van
 * @date 2018/8/15
 */

public class TimeTableColorChooseDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private TimeTableRemindIntervalDialog.MyDialogListener listener;
    private RelativeLayout colorDefault, colorTwo, blackColor;


    public TimeTableColorChooseDialog(Context context, int theme, TimeTableRemindIntervalDialog.MyDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.timt_table_display_color_pattern_choose);

        colorDefault = findViewById(R.id.colorDefault);
        colorTwo = findViewById(R.id.colortwo);

        blackColor = findViewById(R.id.blackColor);

        colorDefault.setOnClickListener(this);
        colorTwo.setOnClickListener(this);

        blackColor.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

    public interface MyDialogListener {
        public void onClick(View view);
    }
}
