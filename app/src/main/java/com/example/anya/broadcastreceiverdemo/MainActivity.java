package com.example.anya.broadcastreceiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String downloadFinished = "Download finished";
    public static final String imageName = "animals.jpg";
    private File saveImage;
    private TextView error;
    private ImageView image;
    private BroadcastReceiver imageResult, serviceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        error = (TextView) findViewById(R.id.error);
        serviceResult = new Receiver();
        imageResult = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                saveImage = new File(getFilesDir(), imageName);
                if (saveImage.exists()) {
                    image.setImageBitmap(BitmapFactory.decodeFile(saveImage.getAbsolutePath()));
                    image.setVisibility(VISIBLE);
                    error.setVisibility(INVISIBLE);
                } else {
                    error.setText(R.string.image_is_not_downloaded);
                    image.setVisibility(INVISIBLE);
                    error.setVisibility(VISIBLE);
                }
            }
        };
        registerReceiver(serviceResult, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(imageResult, new IntentFilter(downloadFinished));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceResult);
        unregisterReceiver(imageResult);
    }

}