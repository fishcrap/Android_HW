package com.example.chapter2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter2.recycler.LinearItemDecoration;
import com.example.chapter2.recycler.MyAdapter;
import com.example.chapter2.recycler.TestData;
import com.example.chapter2.recycler.TestDataSet;

public class RecyclerViewActivity extends AppCompatActivity implements MyAdapter.IOnItemClickListener {

    private static final String TAG = "TAG";

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridLayoutManager;

    public static final String ID = "item_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        Log.i(TAG, "RecyclerViewActivity onCreate");
        Toast.makeText(this,"RecyclerViewActivity onCreate",Toast.LENGTH_SHORT).show();
        initView();
    }

    private int getViewsCount(ViewGroup viewgroup) {
        if(viewgroup == null) return 0;
        int num = viewgroup.getChildCount();
        int count = 1;
        for(int i = 0; i < num; ++i) {
            View child = viewgroup.getChildAt(i);
            if(child instanceof ViewGroup) {
                count += getViewsCount((ViewGroup) child);
            }
            else {
                count++;
            }
        }
        return count;
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(TestDataSet.getData());
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        LinearItemDecoration itemDecoration = new LinearItemDecoration(Color.WHITE);
        recyclerView.addItemDecoration(itemDecoration);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        DefaultItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(3000);
//        recyclerView.setItemAnimator(animator);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "RecyclerViewActivity onStart");
        Toast.makeText(this,"RecyclerViewActivity onStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout linearlayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_recyclerview,null);
        int viewsnum = getViewsCount(linearlayout);
        Log.i(TAG, "RecyclerViewActivity onResume");
        Toast.makeText(this,"RecyclerViewActivity onResume\nviews number: " + viewsnum,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "RecyclerViewActivity onRestart");
        Toast.makeText(this,"RecyclerViewActivity onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "RecyclerViewActivity onPause");
        Toast.makeText(this,"RecyclerViewActivity onPause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "RecyclerViewActivity onStop");
        Toast.makeText(this,"RecyclerViewActivity onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "RecyclerViewActivity onDestroy");
        Toast.makeText(this,"RecyclerViewActivity onDestroy",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemCLick(int position, TestData data) {
        Toast.makeText(RecyclerViewActivity.this, "点击了第" + position + "条", Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                Intent intent = new Intent(this, FrameLayoutActivity.class);
                intent.putExtra(ID,position);
                startActivity(intent);
                break;
            case 1:
                Intent intent2 = new Intent(this, LinearLayoutActivity.class);
                intent2.putExtra(ID,position);
                startActivity(intent2);
                break;
            case 2:
                Intent intent3 = new Intent(this, RelativeLayoutActivity.class);
                intent3.putExtra(ID,position);
                startActivity(intent3);
                break;
            default:
                Intent intent4 = new Intent(this, UIActivity.class);
                intent4.putExtra(ID,position);
                startActivity(intent4);
                break;
        }

//        mAdapter.addData(position + 1, new TestData("新增头条", "0w"));
    }

    @Override
    public void onItemLongCLick(int position, TestData data) {
        Toast.makeText(RecyclerViewActivity.this, "长按了第" + position + "条", Toast.LENGTH_SHORT).show();
        mAdapter.removeData(position);
    }
}