package com.example.sudoku;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected CustomButton[][] buttons = new CustomButton[9][9];
    protected CustomButton previousButton = null;
    protected TextView counterView;
    protected InputPanel inputPanel;
    protected FrameLayout opacityPanel;
    protected LinearLayout mainLayout;
    protected MemoPanel memoPanel;
    protected float probability = 0.43f;
    protected ConflictChecker conflictChecker = new ConflictChecker();
    protected int answerCounter = 0;
    protected int row = -1;
    protected int col = -1;
    protected boolean isSelectingNumber = false;
    protected boolean isMemoPanelVisible = false;
    BoardGenerator board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        board = new BoardGenerator();
        counterView = (TextView) findViewById(R.id.answer_count);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        opacityPanel = (FrameLayout) findViewById(R.id.opacityPanel);

        mainLayout.setTranslationZ(1.0f);
        opacityPanel.setTranslationZ(2.0f);

        initInputPanel();
        initMemoPanel();
        createButtons();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("answerCounter",answerCounter);
        outState.putFloat("probability",probability);
        outState.putBoolean("isSelectingNumber",isSelectingNumber);
        outState.putBoolean("isMemoPanelVisible",isMemoPanelVisible);
    }
    @Override
    protected  void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        answerCounter = savedInstanceState.getInt("answerCounter");
        setCounter();

        probability = savedInstanceState.getFloat("probability");
        isSelectingNumber = savedInstanceState.getBoolean("isSelectingNumber");
        isMemoPanelVisible = savedInstanceState.getBoolean("isMemoPanelVisible");
    }
    public void initInputPanel() {
        inputPanel = new InputPanel(this);
        inputPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invisibleInputPanel();
                int[] loc = getSelectedLocation();
                if(inputPanel.getIsButtonClicked()) {
                    changeNumber(loc[0], loc[1], inputPanel.getSelectedNumber());
                }

            }
        });
        ConstraintLayout app_layout = (ConstraintLayout) findViewById(R.id.app_layout);
        app_layout.addView(inputPanel);
    }

    public void initMemoPanel(){
        memoPanel = new MemoPanel(this);
    }

    public void onClickResetButton(View v) {
        conflictChecker = new ConflictChecker();
        board = new BoardGenerator();
        resetCounter();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].setNumber(0);
                buttons[i][j].resetMemo();
                setButton(buttons[i][j]);
            }
        }
    }

    private void createButtons() {
        TableLayout table = (TableLayout) findViewById(R.id.sudoku_table_layout);
            for (int i = 0; i < 9; i++) {
                TableRow tableRow = new TableRow(this);
                    for (int j = 0; j < 9; j++) {
                        tableRow.addView(createButton(i, j));
                    }
                table.addView(tableRow);
            }

    }

    private CustomButton createButton(int i, int j) {
        TableRow.LayoutParams layoutParams =
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                );

        int right = 0;
        int bottom = 0;
        if(i%3 == 2) {
            bottom = 30;
        }
        if(j%3 == 2) {
            right = 10;
        }
        layoutParams.setMargins(6, 6, right ,bottom);
        buttons[i][j] = new CustomButton(this);
        buttons[i][j].setLocation(i,j);

        buttons[i][j].setLayoutParams(layoutParams);
        buttons[i][j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(true);
                setSelectedLocation(i,j);
                visibleInputPanel();
            }
        });
        buttons[i][j].setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                if(buttons[i][j].getValue() == 0)
                    memoPanel.showMemoDialog(buttons[i][j]);
                return false;
            }
        });
        setButton(buttons[i][j]);
        return buttons[i][j];
    }

    public void onClickOpacityPanel(View v) {
        invisibleInputPanel();
    }
    private void visibleInputPanel() {
        if(isSelectingNumber == false && isMemoPanelVisible == false) {
            opacityPanel.setVisibility(View.VISIBLE);
            inputPanel.setVisibility(View.VISIBLE);
            isSelectingNumber = true;
        }
    }
    private void invisibleInputPanel() {
        if(isSelectingNumber == true) {
            opacityPanel.setVisibility(View.INVISIBLE);
            inputPanel.setVisibility(View.INVISIBLE);
            isSelectingNumber = false;
        }
    }


    private void setSelectedLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }
    private int[] getSelectedLocation() {
        int[] location = {row, col};
        return location;
    }
    private void setButton(CustomButton button) {
        int number = board.get(button.getRow(), button.getCol());
        button.setBackgroundColor(Color.WHITE);
        button.setButtonFixed(false);
        button.setMemoVisibility(true);
        button.setEnabled(true);
        if(Math.random() > probability) {
            setCounter(true);
            conflictChecker.addInputNumberLocation(number, new Location(button.getRow(), button.getCol()));
            button.setNumber(number);
            button.setButtonFixed(true);
            button.setMemoVisibility(false);
            button.setEnabled(false);
        }

    }

    private void changeNumber(int targetRow, int targetCol, int targetNumber) {
        if (targetNumber == -1) return;
        CustomButton button = buttons[targetRow][targetCol];
        button.setMemoVisibility(false);
        int preNumber = button.getValue();
        button.setNumber(targetNumber);

        if(preNumber != 0) {
            conflictChecker.removeInputNumberLocation(
                    preNumber, new Location(targetRow, targetCol)
            );
            clearConflictingButtons(preNumber);
            displayConflictingButton(conflictChecker.getConflictingNumbers(preNumber));
        }

        if(targetNumber == 0) {
            conflictChecker.removeInputNumberLocation(
                    preNumber, new Location(targetRow, targetCol)
            );
            button.setBackgroundColor(Color.WHITE);
            button.setMemoVisibility(true);
            if(preNumber != 0 ) setCounter(false);
        }
        else {
            conflictChecker.addInputNumberLocation(
                    targetNumber, new Location(targetRow, targetCol)
            );
            clearConflictingButtons(targetNumber);
            displayConflictingButton(conflictChecker.getConflictingNumbers(targetNumber));
            if((!button.equals(previousButton) || preNumber == 0  || previousButton == null)&& answerCounter < 81) setCounter(true);
        }
        if(answerCounter == 81) {
            if(isVictory()) {
                counterView.setText("완성");
                for(CustomButton[] buttonRow : buttons){
                    for(CustomButton customButton : buttonRow){
                        customButton.setEnabled(false);
                    }
                }
            }
            else {
                counterView.setText("아직 오답이 남아있습니다.");
            }
        }
        previousButton = button;
    }
    private void clearConflictingButtons(int number) {
       for(Location location :conflictChecker.getLocationsByNumber(number)) {
           buttons[location.getRow()][location.getCol()].setIsConflicted(false);
           buttons[location.getRow()][location.getCol()].setBackgroundColor(Color.WHITE);
       }
    }

    private void displayConflictingButton(ArrayList<Location> locations) {
        for(Location location : locations) {
            buttons[location.getRow()][location.getCol()].setIsConflicted(true);
            buttons[location.getRow()][location.getCol()].setBackgroundColor(Color.RED);
        }

    }
    private boolean isVictory(){
        for(CustomButton[] buttonRow : buttons){
            for(CustomButton button : buttonRow){
                if(button.isConflicted()) return false;
            }
        }
        return true;
    }

    private void setCounter(boolean isAdd) {
        if(isAdd) answerCounter++;
        else answerCounter--;

        counterView.setText("남은 빈 칸 : " + String.valueOf(81-answerCounter));
    }
    private void setCounter() {
        counterView.setText("남은 빈 칸 : " + String.valueOf(81-answerCounter));
    }
    private void resetCounter(){
        answerCounter = 0;
        counterView.setText("남은 빈 칸 : " + String.valueOf(81-answerCounter));
    }
}
