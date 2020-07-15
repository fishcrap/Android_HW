package com.example.hw8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_PATH = 1234;
    private Button btnPhoto;
    private Button btnRecord;
    private Button btnSysRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnPhoto = findViewById(R.id.btn_takePhoto);
        btnRecord = findViewById(R.id.btn_Record);
        btnSysRecord = findViewById(R.id.btn_SystemRecord);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                startActivity(intent);
            }
        });

        btnSysRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };
                if(!checkPermission(permissions)) return;
                Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                startActivity(intent);
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                };
                if(!checkPermission(permissions)) return;
                Intent intent = new Intent(MainActivity.this,EmbedCameraActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkPermission(String[] permissions) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            for(String permission : permissions) {
                if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,permissions,REQUEST_PATH);
                    return false;
                }
            }
        }
        return true;
    }
}
