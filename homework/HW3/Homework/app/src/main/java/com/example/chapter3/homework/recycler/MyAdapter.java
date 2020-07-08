package com.example.chapter3.homework.recycler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;


import com.example.chapter3.homework.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TestData> mDataset = new ArrayList<>();
    private IOnItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvIndex;
        private TextView tvName;
        private TextView tvTime;
        private TextView tvMsg;
        private View contentView;
        private ImageView ivImage;  //image

        public MyViewHolder(View v) {
            super(v);
            contentView = v;
            tvIndex = v.findViewById(R.id.tv_index);
            tvName = v.findViewById(R.id.tv_nickname);
            tvTime = v.findViewById(R.id.tv_time);
            tvMsg = v.findViewById(R.id.tv_message);
            ivImage = v.findViewById(R.id.image_head);
        }

        public void onBind(int position, TestData data) {
            tvIndex.setText(new StringBuilder().append(position).append(".  ").toString());
            tvName.setText(data.name);
            tvMsg.setText(data.msg);
            tvTime.setText(data.time);
            switch(position) {  //set image for each item
                case 0:
                    ivImage.setImageResource(R.drawable.image_1);
                    break;
                case 1:
                    ivImage.setImageResource(R.drawable.image_2);
                    break;
                case 2:
                    ivImage.setImageResource(R.drawable.image_3);
                    break;
                case 3:
                    ivImage.setImageResource(R.drawable.image_4);
                    break;
                case 4:
                    ivImage.setImageResource(R.drawable.image_5);
                    break;
                default:
                    ivImage.setImageResource(R.drawable.image_douyin);
                    break;
            }
            if (position < 3) {
                tvIndex.setTextColor(Color.parseColor("#FFD700"));
            } else {
                tvIndex.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            if (listener != null) {
                contentView.setOnLongClickListener(listener);
            }
        }
    }


    public MyAdapter(List<TestData> myDataset) {
        mDataset.addAll(myDataset);
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addData(int position, TestData data) {
        mDataset.add(position, data);
        notifyItemInserted(position);
        if (position != mDataset.size()) {
            //刷新改变位置item下方的所有Item的位置,避免索引错乱
            notifyItemRangeChanged(position, mDataset.size() - position);
        }
    }

    public void removeData(int position) {
        if (null != mDataset && mDataset.size() > position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
            if (position != mDataset.size()) {
                //刷新改变位置item下方的所有Item的位置,避免索引错乱
                notifyItemRangeChanged(position, mDataset.size() - position);
            }
        }
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.onBind(position, mDataset.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position, mDataset.get(position));
                }
            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemLongCLick(position, mDataset.get(position));
                }
                return false;
            }

        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface IOnItemClickListener {

        void onItemCLick(int position, TestData data);

        void onItemLongCLick(int position, TestData data);
    }
}
