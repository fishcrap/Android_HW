package com.example.hw8;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageActivity extends AppCompatActivity {

    private final static int REQUEST_PATH = 1234;
    private final static int REQUEST_CAMERA = 123;

    private Button btnCamera;
    private ImageView ivPhoto;

    private File imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
    }

    private void initView() {
        btnCamera = findViewById(R.id.btn_camera);
        ivPhoto = findViewById(R.id.iv_photo);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    private void openCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,permissions,REQUEST_PATH);
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imagePath = getOutputMediaPath();
        Uri outUri = FileProvider.getUriForFile(this,getPackageName() + ".fileprovider", imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public File getOutputMediaPath() {
        String imageName = "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date()) + ".jpg";
        File mediaFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),imageName);
        if(!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Glide.with(this).load(imagePath).into(ivPhoto);
        }
    }
}
