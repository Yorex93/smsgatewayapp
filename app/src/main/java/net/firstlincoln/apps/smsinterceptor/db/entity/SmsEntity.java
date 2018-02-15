package net.firstlincoln.apps.smsinterceptor.db.entity;

import net.firstlincoln.apps.smsinterceptor.db.DateConverter;
import net.firstlincoln.apps.smsinterceptor.models.SmsInterface;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 * Created by webmaster on 09/02/2018.
 */

@Entity(tableName = "sms")
@TypeConverters(DateConverter.class)
public class SmsEntity implements SmsInterface {

    @PrimaryKey(autoGenerate = true)
    private int id;


    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "createdAt")
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SmsEntity(String content, String phone, int status) {
        this.content = content;
        this.phone = phone;
        this.status = status;
        this.createdAt = new Date();
    }

    @Ignore
    public SmsEntity() {

    }
}
