package com.example.hw8;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity {
    private final static int PERMISSION_REQUEST_CODE = 321;
    private final static int REQUEST_CODE_RECORD = 321;

    private String mp4Path;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
//        ActivityCompat.requestPermissions(VideoActivity.this,permissions,PERMISSION_REQUEST_CODE);
        SystemVideo();
        videoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(videoView.isPlaying())
                    videoView.pause();
                else
                    videoView.start();
            }
        });
    }

    private void SystemVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mp4Path = getOutputMediaPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this,mp4Path));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,REQUEST_CODE_RECORD);
        }
    }

    public static Uri getUriForFile(Context context, String path) {
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".fileprovider", new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    private String getOutputMediaPath(){
        String videoName = "Video_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".mp4";
        File mediaFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),videoName);
        if(!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_RECORD && resultCode == RESULT_OK){
            play();
        }
    }

    private void play(){
        videoView.setVideoPath(mp4Path);
        videoView.start();
    }
}
