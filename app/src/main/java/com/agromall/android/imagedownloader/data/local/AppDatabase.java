package com.agromall.android.imagedownloader.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.agromall.android.imagedownloader.data.local.dao.ImagesDao;
import com.agromall.android.imagedownloader.data.model.Image;

@Database(entities = {Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ImagesDao imagesDao();

    private static AppDatabase INSTANCE;

    private static final Object lock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (lock) {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "Agromall.db").build();
            }
            return INSTANCE;
        }
    }

}
