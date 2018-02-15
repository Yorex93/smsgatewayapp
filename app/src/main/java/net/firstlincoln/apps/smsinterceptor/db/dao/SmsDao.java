package net.firstlincoln.apps.smsinterceptor.db.dao;

/**
 * Created by webmaster on 09/02/2018.
 */

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

import java.util.List;


@Dao
public interface SmsDao {

    @Query("SELECT * FROM sms ORDER BY createdAt DESC")
    LiveData<List<SmsEntity>> getAll();

    @Query("SELECT * FROM sms WHERE status = 0 ORDER BY createdAt DESC")
    LiveData<List<SmsEntity>> getPending();

    @Query("SELECT * FROM sms WHERE status = 1 ORDER BY createdAt DESC")
    LiveData<List<SmsEntity>> getSuccessful();

    @Query("SELECT * FROM sms WHERE status = 2 ORDER BY createdAt DESC")
    LiveData<List<SmsEntity>> getFailed();

    @Query("SELECT * FROM sms WHERE id = :smsId")
    SmsEntity getSmsById(int smsId);

    @Query("SELECT * FROM sms WHERE status = 0 ORDER BY createdAt DESC")
    List<SmsEntity> getPendingList();

    @Query("SELECT * FROM sms WHERE status = :status ORDER BY createdAt DESC")
    List<SmsEntity> getByStatus(int status);

    @Insert
    void insert(SmsEntity sms);

    @Update
    void updateSms(SmsEntity sms);

    @Delete
    void deleteSms(SmsEntity sms);

    @Delete
    void deleteAll(List<SmsEntity> smsList);
}
