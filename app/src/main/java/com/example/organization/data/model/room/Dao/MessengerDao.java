package com.example.organization.data.model.room.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.organization.data.model.room.Messenger;


import java.util.List;

@Dao
public interface MessengerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Messenger messenger);

    @Query("DELETE FROM messenger WHERE messenger_id = :messengerId")
    void deleteById(int messengerId);

    @Query("SELECT * FROM messenger")
    LiveData<List<Messenger>> getAllMessengers();

    @Query("SELECT * FROM messenger WHERE messenger_id = :messengerId")
    LiveData<Messenger> getMessengersById(int messengerId);


    @Update
    void updateMessengers(Messenger messenger);

    @Query("DELETE FROM messenger WHERE messenger_id = 0")
    void  deleteAllZeroMessengers();


}
