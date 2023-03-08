package com.example.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MemoPanel {
    AlertDialog memoDialog;
    HashMap<Integer, ToggleButton> toggleButtonMap = new HashMap<Integer, ToggleButton>();
    CustomButton target;
    public MemoPanel(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Memo");
        builder.setView(initToggleButtonTable(context));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(target != null) {
                    for(int idx =1; idx <= toggleButtonMap.size();idx++){
                        target.setMemo(idx,toggleButtonMap.get(idx).isChecked());
                    }
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int key : toggleButtonMap.keySet()){
                    target.setMemo(key,false);
                }
            }
        });
        memoDialog = builder.create();

    }
    public TableLayout initToggleButtonTable(Context context){
        TableLayout toggleButtonsTable = new TableLayout(context);
        for(int i = 0; i<3; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            for(int j = 0; j<3;j++) {
                ToggleButton toggleButton = new ToggleButton(context);
                int innerNumber = (i * 3) + j + 1;
                String innerText = String.valueOf(innerNumber);
                toggleButton.setText(innerText);
                toggleButton.setTextOn(innerText);
                toggleButton.setTextOff(innerText);
                tableRow.addView(toggleButton);
                toggleButtonMap.put(innerNumber, toggleButton);
            }
            toggleButtonsTable.addView(tableRow);
        }
        return toggleButtonsTable;
    }

    public void showMemoDialog(CustomButton button) {
            target = button;
            for (int i = 0; i < button.getIsMemo().length; i++) {
                toggleButtonMap.get(i + 1).setChecked(button.getIsMemo()[i]);
            }
            memoDialog.show();

    }

    public AlertDialog getMemoDialog(){
        return memoDialog;
    }

    public HashMap<Integer, ToggleButton> getToggleButtonMap(){
        return toggleButtonMap;
    }
}
