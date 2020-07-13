package com.bytedance.todolist.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListItemHolder> {
    private List<TodoListEntity> mDatas;
    private List<Integer> state;    //tag
    IOnCheckBoxClickListener mCheckBoxClickListener;
    IOnIVClickListener mIVClickListener;

    public TodoListAdapter() {
        mDatas = new ArrayList<>();
        state = new ArrayList<>();
    }
    @NonNull
    @Override
    public TodoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoListItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoListItemHolder holder, final int position) {
//        holder.setIsRecyclable(false);
        holder.bind(mDatas.get(position));
        holder.getIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIVClickListener.onIVClick(mDatas.get(position));   // interface implement in Activity
            }
        });
        holder.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBoxClickListener.onCheckBoxClick(mDatas.get(position),position,isChecked,holder.getTextView());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @MainThread
    public void setData(List<TodoListEntity> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    public void setIOnCheckBoxClickListener(IOnCheckBoxClickListener listener) {
        mCheckBoxClickListener = listener;
    }

    public void setIOnIVClickListener(IOnIVClickListener listener) {
        mIVClickListener = listener;
    }

    public interface IOnCheckBoxClickListener {
        void onCheckBoxClick(TodoListEntity entity, int position, boolean isChecked, TextView[] textViews);
    }

    public interface IOnIVClickListener {
        void onIVClick(TodoListEntity entity);
    }


}
