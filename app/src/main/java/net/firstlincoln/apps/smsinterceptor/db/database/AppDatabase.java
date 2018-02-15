package net.firstlincoln.apps.smsinterceptor.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import net.firstlincoln.apps.smsinterceptor.db.dao.SmsDao;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

/**
 * Created by webmaster on 09/02/2018.
 */

@Database(entities = {SmsEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract SmsDao smsDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "sms-database").build();
        }
        return INSTANCE;
    }

}
