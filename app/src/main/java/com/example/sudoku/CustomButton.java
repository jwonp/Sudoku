package com.example.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;


public class CustomButton extends FrameLayout{
    private int row;
    private int col;
    private boolean isConflicted = false;
    private boolean[] isMemo = {false,false,false,false,false,false,false,false,false};
    private TextView[] textViewMemos = new TextView[9];
    private TextView textView;
    private TableLayout memo;
    public CustomButton(@NonNull Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.button_selector);
        initTextView();
        initMemo();

    }
    private void initTextView(){
        LayoutParams layoutParams =
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT,
                        1
                );
        textView = new TextView(this.getContext());
        textView.setTextSize(30.0f);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setMinHeight(150);
        textView.setTranslationZ(1.0f);
    }
    private void initMemo(){
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memo = (TableLayout) layoutInflater.inflate(R.layout.memo_layout, null);
        int idx =0;
        for(int i=0;i<3;i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for(int j=0; j<3;j++){
                textViewMemos[idx++] = (TextView) tableRow.getChildAt(j);
            }
        }
        memo.setVisibility(INVISIBLE);
        memo.setTranslationZ(2.0f);
        addView(memo);
    }
    public void setLocation(int row, int col){
        setRow(row);
        setCol(col);
        this.addView(textView);

    }
    public void setIsConflicted(boolean conflicted) {
        isConflicted = conflicted;
    }

    public boolean isConflicted() {
        return isConflicted;
    }

    public int getRow(){
        return row;
    }
    public void setRow(int row){
        this.row =  row;
    }
    public int getCol(){
        return col;
    }
    public void setCol(int col){
        this.col = col;
    }
    public int getValue(){
        if(textView.getText().toString().equals("")) return 0;
        return Integer.parseInt(textView.getText().toString());
    }
    public void setNumber(int num) {
        textView.setText(num == 0 ? "" : String.valueOf(num));
    }
    public void setButtonFixed(boolean bool) {
        textView.setTextColor(bool ? Color.DKGRAY : Color.BLUE);
    }
    public void setMemoVisibility(boolean visibility) {
        if(visibility) memo.setVisibility(View.VISIBLE);
        else memo.setVisibility(View.INVISIBLE);
    }
    public boolean[] getIsMemo(){
        return isMemo;
    }
    public void setMemo(int target, boolean isVisible) {
        isMemo[target-1] = isVisible;
        if(isVisible)
            textViewMemos[target-1].setVisibility(VISIBLE);
        else
            textViewMemos[target-1].setVisibility(INVISIBLE);
    }
    public void resetMemo(){
        for(int i =1; i<=9; i++)
            setMemo(i, false);
    }





}
