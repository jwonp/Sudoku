package com.example.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import org.w3c.dom.Text;

public class InputPanel extends LinearLayout {
    protected int selectedNumber = -2;
    protected boolean isButtonClicked = false;
    public InputPanel(@NonNull Context context) {
        super(context);
        setAttributes();
        initChildren();
    }
    private void initChildren() {
        this.addView(initTitle());
        LinearLayout[] pads = initNumberPads();
        for(LinearLayout item : pads) {
            this.addView(item);
        }
    }
    private TextView initTitle(){
        TextView title = new TextView(this.getContext());
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1
        );
        title.setLayoutParams(layoutParams);
        title.setText("Input Number");
        title.setId(R.id.inputLayoutTitle);
        title.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        title.setTextSize(34);
        return title;
    }
    private LinearLayout[] initNumberPads() {
        LinearLayout[] linearLayouts = new LinearLayout[4];
        Button[] buttons = initInputNumberButtons();
        int btnIdx = 0;

        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                1
        );

        for(int i =0; i<4;i++){
            LinearLayout layout = new LinearLayout(this.getContext());
            layout.setLayoutParams(layoutParams);
            layout.setOrientation(HORIZONTAL);

            layout.addView(buttons[btnIdx]);
            layout.addView(buttons[btnIdx + 1]);
            if(i<3)
                layout.addView(buttons[btnIdx + 2]);
            btnIdx += 3;
            linearLayouts[i] = layout;
        }

        return linearLayouts;
    }
    private Button[] initInputNumberButtons() {
        Button[] buttons = new Button[11];
        int[] ids = {
                R.id.inputOne, R.id.inputTwo,R.id.inputThree,
                R.id.inputFour,R.id.inputFive,R.id.inputSix,
                R.id.inputSeven,R.id.inputEight,R.id.inputNine,
                R.id.inputCancelBtn,R.id.inputDeleteBtn
        };
        String[] innerTexts = {
                "1","2", "3",
                "4","5","6",
                "7","8", "9",
                "Cancel","Del"
        };


        LayoutParams btnParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                1
        );
        for(int i = 1; i<=11; i++) {
            Button btn = new Button(this.getContext());
            btn.setLayoutParams(btnParams);
            btn.setId(ids[i-1]);
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setSelected(true);
                    isButtonClicked = true;
                    selectedNumber = getClickedButton(view);
                    LinearLayout inputPanel = (LinearLayout) findViewById(R.id.inputPanel);
                    inputPanel.performClick();
                    isButtonClicked = false;
                }
            });
            btn.setText(innerTexts[i-1]);
            buttons[i-1] = btn;
        }
        return buttons;
    }

    private void setAttributes() {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
        this.setLayoutParams(layoutParams);
        this.setPadding(7,7,7,7);
        this.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_VERTICAL);
        this.setVisibility(INVISIBLE);
        this.setOrientation(VERTICAL);
        this.setBackgroundColor(Color.WHITE);
        this.setId(R.id.inputPanel);
        this.setTranslationZ(3.0f);
    }

    public int getClickedButton(View view) {
        int id = view.getId();
        Button button = (Button) findViewById(id);
        switch (id) {
            case R.id.inputOne:
            case R.id.inputTwo:
            case R.id.inputThree:
            case R.id.inputFour:
            case R.id.inputFive:
            case R.id.inputSix:
            case R.id.inputSeven:
            case R.id.inputEight:
            case R.id.inputNine:
                return Integer.parseInt(button.getText().toString());
            case R.id.inputDeleteBtn:
                return 0;
            default:
                return -1;
        }
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }
    public boolean getIsButtonClicked() {
        return isButtonClicked;
    }

}
