package com.example.anya.broadcastreceiverdemo;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.anya.broadcastreceiverdemo.MainActivity.imageName;
import static com.example.anya.broadcastreceiverdemo.MainActivity.downloadFinished;

/**
 * Created by Anya on 29.11.2016.
 */

public class LoadService extends Service {

    private boolean loadingStarted = false;
    private final String link = "https://wallpaperscraft.ru/image/volk_tigr_risunok_belyy_krasnyy_170_1920x1200.jpg";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (loadingStarted) {
            return START_STICKY;
        }
        loadingStarted = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                File f = new File(getFilesDir(), imageName);
                if (!f.exists() || BitmapFactory.decodeFile(f.getAbsolutePath()) == null) {
                    HttpURLConnection connect = null;
                    InputStream fin = null;
                    FileOutputStream fout = null;
                    try {
                        connect = (HttpURLConnection) new URL(link).openConnection();
                        fin = new BufferedInputStream(connect.getInputStream());
                        fout = new FileOutputStream(f);
                        byte[] buf = new byte[15000];
                        int lengthBuf;
                        while ((lengthBuf = fin.read(buf)) != -1) {
                            fout.write(buf, 0, lengthBuf);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (connect != null) {
                                connect.disconnect();
                            }
                            if (fin != null) {
                                fin.close();
                            }
                            if (fout != null) {
                                fout.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                sendBroadcast(new Intent(downloadFinished));
                loadingStarted = false;
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
