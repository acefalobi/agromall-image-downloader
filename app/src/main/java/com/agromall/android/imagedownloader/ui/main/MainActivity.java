package com.agromall.android.imagedownloader.ui.main;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.agromall.android.imagedownloader.R;
import com.agromall.android.imagedownloader.data.local.AppDatabase;
import com.agromall.android.imagedownloader.data.local.dao.ImagesDao;
import com.agromall.android.imagedownloader.data.model.Image;
import com.agromall.android.imagedownloader.ui.base.BaseActivity;
import com.agromall.android.imagedownloader.util.AppExecutors;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;

public class MainActivity extends BaseActivity {

    private ImagesDao imagesDao;

    private static final int PERMISSION_WRITE_READ_EXTERNAL_STORAGE = 1000;

    private static final int NOTIFICATION_ID = 100;

    private static final String CHANNEL_ID = "com.argomall.android.imagedownloader.DOWNLOADER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagesDao = AppDatabase.getInstance(this).imagesDao();

        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) || !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_WRITE_READ_EXTERNAL_STORAGE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPageAdapter pageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        TabLayout tabLayout = findViewById(R.id.tabs);

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    private NotificationCompat.Builder buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Image Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        builder.setProgress(100, 0, false);

        return builder;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Downloader", importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void downloadImage(final Image image) {
        image.setDownloadStatus(Image.DOWNLOAD_ONGOING);

        final AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                imagesDao.insertImage(image);
            }
        });

        createNotificationChannel();
        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        final NotificationCompat.Builder builder = buildNotification();
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AgroMall");
        boolean result = directory.mkdirs();

        Ion.with(this)
                .load(image.getImageUrl())
                .progress(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        Log.i("MainActivity", downloaded + " out of " + total);

                        builder.setProgress((int) total, (int) downloaded,false);
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    }
                })
                .write(new File(directory, image.getImageId() + ".jpg"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        Log.i("MainActivity", "Completed download");

                        image.setDownloadStatus(Image.DOWNLOAD_COMPLETED);

                        appExecutors.diskIO.execute(new Runnable() {
                            @Override
                            public void run() {
                                imagesDao.updateImage(image);
                            }
                        });

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(result), "image/jpeg");
                        PendingIntent pendingIntent = PendingIntent
                                .getActivity(MainActivity.this, 0, intent, 0);

                        builder.setContentIntent(pendingIntent);
                        builder.setContentText("Download complete")
                                .setProgress(0,0,false);
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_WRITE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 2) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            } else finish();
        }
    }
}
