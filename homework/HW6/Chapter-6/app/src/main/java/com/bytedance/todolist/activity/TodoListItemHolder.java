package com.bytedance.todolist.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private TextView mContent;
    private TextView mTimestamp;
    private ImageView iv;
    private CheckBox checkBox;

    public TodoListItemHolder(@NonNull View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        iv = itemView.findViewById(R.id.iv_x);
        checkBox = itemView.findViewById(R.id.checkbox);
    }

    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(entity.getIsComplete() != 0);
        if(entity.getIsComplete() == 1L) {
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //set strike-out
            mTimestamp.setPaintFlags(mTimestamp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            mContent.setTextColor(Color.GRAY);  //set color
            mTimestamp.setTextColor(Color.GRAY);
        }
        else {
            mContent.setPaintFlags(mContent.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG); //delete strike-out
            mTimestamp.setPaintFlags(mTimestamp.getPaintFlags() & ~ Paint.STRIKE_THRU_TEXT_FLAG);

            mContent.setTextColor(Color.BLACK); //set color
            mTimestamp.setTextColor(0xFFAAAAAA);
        }
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }

    public ImageView getIv() { return iv; } //return imageView

    public CheckBox getCheckBox() { return checkBox; }  //return checkbox

    public TextView[] getTextView() { return new TextView[]{mContent,mTimestamp}; } //return textView
}
