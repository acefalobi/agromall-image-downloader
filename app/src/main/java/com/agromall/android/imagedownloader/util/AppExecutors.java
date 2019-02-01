package com.agromall.android.imagedownloader.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {

    public Executor diskIO, mainThread;

    private static final int THREAD_COUNT = 3;

    public AppExecutors() {
        diskIO = new DiskIOThreadExecutor();
        mainThread = new MainThreadExecutor();
    }

    private class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler;

        MainThreadExecutor() {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }

    private class DiskIOThreadExecutor implements Executor {

        private ExecutorService diskIO;

        DiskIOThreadExecutor() {
            diskIO = Executors.newSingleThreadExecutor();
        }

        @Override
        public void execute(Runnable runnable) {
            diskIO.execute(runnable);
        }
    }

}
