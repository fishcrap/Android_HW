package com.example.chapter2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LinearLayoutActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linearlayout);
        Log.i(TAG, "LinearLayoutActivity onCreate");
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(RecyclerViewActivity.ID,-1);
        if(position != -1) {
            Toast.makeText(this,"item " + position,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"get item error",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "LinearLayoutActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "LinearLayoutActivity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "LinearLayoutActivity onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "LinearLayoutActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "LinearLayoutActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "LinearLayoutActivity onDestroy");
    }

}