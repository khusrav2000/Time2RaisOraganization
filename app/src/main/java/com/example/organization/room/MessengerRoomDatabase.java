package com.example.organization.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.organization.Constants;
import com.example.organization.data.model.room.Dao.MessagesDao;
import com.example.organization.data.model.room.Dao.MessengerDao;
import com.example.organization.data.model.room.Messages;
import com.example.organization.data.model.room.Messenger;



@Database(entities = {Messenger.class, Messages.class}, version = 1,exportSchema = false)

public abstract class MessengerRoomDatabase extends RoomDatabase {

    public abstract MessengerDao MessengerDao();
    public abstract MessagesDao Messagesdao();

    private static volatile MessengerRoomDatabase INSTANCE;

    static MessengerRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MessengerRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MessengerRoomDatabase.class, Constants.DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
