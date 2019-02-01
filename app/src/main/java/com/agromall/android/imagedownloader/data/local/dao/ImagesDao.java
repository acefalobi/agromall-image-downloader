package com.agromall.android.imagedownloader.data.local.dao;

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
    List<Image> getImages();

    @Query("SELECT * FROM images WHERE imageId = :imageId")
    Image getImage(String imageId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(Image image);

    @Update
    void updateImage(Image image);

}
