package com.agromall.android.imagedownloader.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.agromall.android.imagedownloader.data.model.Image;

import java.util.List;

@Dao
public interface ImagesDao {

    @Query("SELECT * FROM images")
    LiveData<List<Image>> getImages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image image);

    @Update
    void updateImage(Image image);

}
