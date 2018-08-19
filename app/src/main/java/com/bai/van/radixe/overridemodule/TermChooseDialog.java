package com.bai.van.radixe.overridemodule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.MainActivity;
import com.bai.van.radixe.R;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.TermInf;
import com.bai.van.radixe.fragment.TimeTableFragment;
import com.bai.van.radixe.urlrequests.TimeTableRequest;
import com.bai.van.radixe.userdata.UserInformation;

/**
 *
 * @author van
 * @date 2018/1/28
 */

public class TermChooseDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Handler handler;
    private MyDialogListener listener;
    private LinearLayout timeTableTermchooseLayout;

    private float density;
    private Typeface typefaceOrchid;

    public TermChooseDialog(Context context, int theme, MyDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.time_table_term_choose_dialog);
        handler = new Handler();
        density = TimeTableFragment.density;


        typefaceOrchid = Typeface.create("orchid_pavilion_black.TTF", Typeface.NORMAL);

        timeTableTermchooseLayout = (LinearLayout) findViewById(R.id.timeTableTermChooseLayout);
        Log.d("Termchoose", timeTableTermchooseLayout.toString());
        MainActivity.mMainThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (UserInformation.termInfList.size() == 0){
                    TimeTableRequest.requestTermAll();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        initialTermChooseView();
                    }
                });
            }
        });
    }
    private void initialTermChooseView(){

        RelativeLayout.LayoutParams layoutParamsLine = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        layoutParamsLine.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParamsLine.setMargins((int) (15 * density), 0, (int) (15 * density), 0);
        LinearLayout.LayoutParams layoutParamsRelativeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (35 * density));
        RelativeLayout.LayoutParams layoutParamsText = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        RelativeLayout relativeLayout;
        TextView textView;
        TextView textViewLine;
        TermInf termInf;
        String semesterStr;
        for (int i = 0; i < UserInformation.termInfList.size(); i++){
            termInf = UserInformation.termInfList.get(i);
            semesterStr = StaticMethod.semesterTran(termInf.termChar);
            if (semesterStr.contains("季")){
                continue;
            }
            textViewLine = new TextView(context);
            textViewLine.setBackgroundColor(context.getResources().getColor(R.color.geyser));
            relativeLayout = new RelativeLayout(context);
            textView = new TextView(context);
            textView.setText(termInf.termChar.concat("    ").concat(semesterStr));
            textView.setBackground(context.getResources().getDrawable(R.drawable.term_choose_text_background));
            textView.setTextColor(context.getResources().getColor(R.color.tuatara));
            textView.setTextSize(16);
            textView.setPadding((int) (20 * density), 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTypeface(typefaceOrchid);

            textView.setOnClickListener(this);

            relativeLayout.addView(textView, layoutParamsText);
            relativeLayout.addView(textViewLine, layoutParamsLine);
            timeTableTermchooseLayout.addView(relativeLayout, layoutParamsRelativeLayout);
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
