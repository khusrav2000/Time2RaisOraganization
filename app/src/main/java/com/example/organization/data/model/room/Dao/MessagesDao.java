package com.example.organization.data.model.room.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.organization.data.model.room.Messages;


import java.util.Date;
import java.util.List;

@Dao
public interface MessagesDao {

    @Insert()
    long insert(Messages messages);

    @Query("DELETE FROM messages WHERE messages_id = :messagesId")
    void deleteById(int messagesId);

    @Query("SELECT * FROM messages WHERE messenger_id = :messengerId")
    LiveData<List<Messages>> getAllMessagesByMessengerId(int messengerId);

    @Query("SELECT * FROM messages WHERE messages_id = :messages_id")
    Messages getMessageById(int messages_id);

    @Query("SELECT * FROM messages")
    LiveData<List<Messages>> getAllMessage();
    /*@Query("SELECT max(date_time_post) FROM messages WHERE messenger_id = :messengerId")
    LiveData<Date> getLastMessageTime(int messengerId);
*/
    @Query("SELECT * FROM messages WHERE doc_id = :doc_id")
    Messages getMessageByDocId(String doc_id);

    @Query("SELECT count(*) FROM messages WHERE isnew = :typeMessages")
    LiveData<Integer> getAllCauntsNewMessage(boolean typeMessages);

    @Query("SELECT count(*) FROM messages WHERE isnew = :typeMessages and messenger_id = :messengerId")
    LiveData<Integer> getCauntNewMessage(boolean typeMessages, int messengerId);



    @Update
    void updateMessengers(Messages messenger);

    @Query("UPDATE messages SET doc_id = :doc_id WHERE messages_id =:messages_id")
    void updateDocId(String doc_id, int messages_id);



}
