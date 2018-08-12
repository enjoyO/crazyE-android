package com.bai.van.radixe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bai.van.radixe.adapters.ExamScoreRecyclerViewAdapter;
import com.bai.van.radixe.adapters.ExamScoreRecyclerViewHolder;
import com.bai.van.radixe.baseclass.BaseActivity;
import com.bai.van.radixe.constantdata.SharedData;
import com.bai.van.radixe.constantdata.StaticMethod;
import com.bai.van.radixe.datastru.ExamScoreInf;
import com.bai.van.radixe.userdata.UserInformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author van
 */

public class ScoreSearchActivity extends BaseActivity implements
        TextWatcher,
        View.OnClickListener{

    private RecyclerView scoreSearchResultRecyclerView;
    private EditText scoreSearchEditText;
    private TextView majorBase, majorRequired, majorOptional, majorPra, stuRequired, stuOptional;
    private Boolean isMajorBaseSel = false, isMajorRequiredSel = false, isMajorOptionalSel = false,
            isMajorPraSel = false, isStuRequiredSel = false, isStuOptionalSel = false;

    private final String majorBaseText = "学科基础",
            majorRequiredText = "专业必修",
            majorOptionalText = "专业选修",
            majorPraText = "综合实践",
            stuRequiredText = "公共必修",
            stuOptionalText = "综合素质教育选修";



    private RecyclerView.LayoutManager layoutManager;
    private ExamScoreRecyclerViewAdapter examScoreRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_search);
        
        initial();
    }

    private void initial(){
        majorBase = (TextView) findViewById(R.id.scoreSearchLabelMajorBase);
        majorOptional = (TextView) findViewById(R.id.scoreSearchLabelmajorOptional);
        majorRequired = (TextView) findViewById(R.id.scoreSearchLabelMajorRequired);
        majorPra = (TextView) findViewById(R.id.scoreSearchLabelMajorPra);
        stuOptional = (TextView) findViewById(R.id.scoreSearchLabelStuOptional);
        stuRequired = (TextView) findViewById(R.id.scoreSearchLabelStuRequired);

        majorBase.setOnClickListener(this);
        majorOptional.setOnClickListener(this);
        majorRequired.setOnClickListener(this);
        majorPra.setOnClickListener(this);
        stuRequired.setOnClickListener(this);
        stuOptional.setOnClickListener(this);

        RelativeLayout scoreSearchBack = (RelativeLayout) findViewById(R.id.scoreSearchBack);
        scoreSearchResultRecyclerView = (RecyclerView) findViewById(R.id.scoreSearchRecyclerView);
        scoreSearchEditText = (EditText) findViewById(R.id.scoreSearchEditText);

        scoreSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        scoreSearchEditText.addTextChangedListener(this);
        scoreSearchResultRecyclerView.setNestedScrollingEnabled(false);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        examScoreRecyclerViewAdapter = new ExamScoreRecyclerViewAdapter(null, new ArrayList<ExamScoreInf>(), null);
        examScoreRecyclerViewAdapter.sign = true;

        examScoreRecyclerViewAdapter.setOnItemClickListener(new ExamScoreRecyclerViewHolder.RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedData.examScoreInf = examScoreRecyclerViewAdapter.mScoreData.get(position - 1);

                ExamScoreRecyclerViewHolder examScoreRecyclerViewHolder = (ExamScoreRecyclerViewHolder) scoreSearchResultRecyclerView.getChildViewHolder(view);
                Intent intent = new Intent(ScoreSearchActivity.this, ExamScoreDetailActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        scoreSearchResultRecyclerView.setLayoutManager(layoutManager);
        scoreSearchResultRecyclerView.setAdapter(examScoreRecyclerViewAdapter);
    }

    private void refreshRecyclerView(List<ExamScoreInf> dataList){
        examScoreRecyclerViewAdapter.mScoreData = dataList;
        examScoreRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void buildData(String filterStr){
        List<ExamScoreInf> dataFilterList = new ArrayList<>();
        for (ExamScoreInf examScoreInf : UserInformation.examScoreInfList){
            if (!"".equals(filterStr)){
                if ((isMajorBaseSel && examScoreInf.examType.contains(majorBaseText))
                        || (isMajorOptionalSel && examScoreInf.examType.contains(majorOptionalText))
                        || (isMajorRequiredSel && examScoreInf.examType.contains(majorRequiredText))
                        || (isMajorPraSel && examScoreInf.examType.contains(majorPraText))
                        || (isStuRequiredSel && examScoreInf.examType.contains(stuRequiredText))
                        || (isStuOptionalSel && examScoreInf.examType.contains(stuOptionalText))
                        ){
                    if (examScoreInf.examName.contains(filterStr)) {
                        dataFilterList.add(examScoreInf);
                    }
                }else {
                    if (!isMajorBaseSel
                            && !isMajorOptionalSel
                            && !isMajorRequiredSel
                            && !isMajorPraSel
                            && !isStuOptionalSel
                            && !isStuRequiredSel){
                        if (examScoreInf.examName.contains(filterStr)) {
                            dataFilterList.add(examScoreInf);
                        }
                    }
                }
            }else {
                if ((isMajorBaseSel && examScoreInf.examType.contains(majorBaseText))
                        || (isMajorOptionalSel && examScoreInf.examType.contains(majorOptionalText))
                        || (isMajorRequiredSel && examScoreInf.examType.contains(majorRequiredText))
                        || (isMajorPraSel && examScoreInf.examType.contains(majorPraText))
                        || (isStuRequiredSel && examScoreInf.examType.contains(stuRequiredText))
                        || (isStuOptionalSel && examScoreInf.examType.contains(stuOptionalText))
                        ) {
                    dataFilterList.add(examScoreInf);
                }
            }
        }

        HashSet hashSet = new HashSet(dataFilterList);
        dataFilterList.clear();
        dataFilterList.addAll(hashSet);
        refreshRecyclerView(dataFilterList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.scoreSearchLabelMajorBase:
                labelClick(majorBase, isMajorBaseSel);
                isMajorBaseSel = !isMajorBaseSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            case R.id.scoreSearchLabelMajorRequired:
                labelClick(majorRequired, isMajorRequiredSel);
                isMajorRequiredSel = !isMajorRequiredSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            case R.id.scoreSearchLabelmajorOptional:
                labelClick(majorOptional, isMajorOptionalSel);
                isMajorOptionalSel = !isMajorOptionalSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            case R.id.scoreSearchLabelMajorPra:
                labelClick(majorPra, isMajorPraSel);
                isMajorPraSel = !isMajorPraSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            case R.id.scoreSearchLabelStuRequired:
                labelClick(stuRequired, isStuRequiredSel);
                isStuRequiredSel = !isStuRequiredSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            case R.id.scoreSearchLabelStuOptional:
                labelClick(stuOptional, isStuOptionalSel);
                isStuOptionalSel = !isStuOptionalSel;
                buildData(scoreSearchEditText.getText().toString());
                break;
            default:
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }
    private void labelClick(TextView textView, Boolean isSelected){
        if (isSelected){
            textView.setBackground(getResources().getDrawable(R.drawable.score_search_label));
        }else {
            textView.setBackground(getResources().getDrawable(R.drawable.score_search_label_choose));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String textIn = scoreSearchEditText.getText().toString();

        if (textIn.startsWith("#") && !"#".equals(textIn)) {
            List<ExamScoreInf> dataFilterList = new ArrayList<>();
            for (ExamScoreInf examScoreInf : UserInformation.examScoreInfList) {
                if (StaticMethod.semesterTran(examScoreInf.examSemeter).replace(" ", "").contains(textIn.replace("#", ""))) {
                    dataFilterList.add(examScoreInf);
                }
            }
            refreshRecyclerView(dataFilterList);
        }else {
            buildData(scoreSearchEditText.getText().toString());
        }
    }
}
