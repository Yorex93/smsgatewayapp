package net.firstlincoln.apps.smsinterceptor;

import android.app.Application;
import android.arch.persistence.room.Room;

import net.firstlincoln.apps.smsinterceptor.db.database.AppDatabase;

/**
 * Created by webmaster on 12/02/2018.
 */

public class App extends Application{
    public static App INSTANCE;
    private static final String DATABASE_NAME = "sms-database";

    private AppDatabase database;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public AppDatabase getDB() {
        return database;
    }

}
