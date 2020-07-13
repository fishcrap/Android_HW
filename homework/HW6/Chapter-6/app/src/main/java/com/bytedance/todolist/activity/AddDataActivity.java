package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.todolist.R;

public class AddDataActivity extends AppCompatActivity {

    public static final String KEY = "CONTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_add_data_activity_layout);
        initView();
    }

    private void initView() {
        final EditText editText = findViewById(R.id.editText);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(KEY,content);
                if(content.isEmpty()) {
                    setResult(RESULT_CANCELED,intent);
                }
                else {
                    setResult(RESULT_OK,intent);
                }
                finish();
            }
        });
    }
}
