package net.firstlincoln.apps.smsinterceptor.models;

import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by webmaster on 09/02/2018.
 */

public class Sms {
    private String phoneNumber;
    private String content;
    private String dateReceived;


    //Constructor for individual elements
    public Sms(String phoneNumber, String content, String dateReceived) {
        this.phoneNumber = phoneNumber;
        this.content = content;
        this.dateReceived = dateReceived;
    }

    //Constructor for JSON object

    public Sms(JSONObject object){
        try {
            this.phoneNumber = object.getString("phone");
            this.content = object.getString("content");
            this.dateReceived = object.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Sms> fromJson(JSONArray jsonObjects) {
        ArrayList<Sms> users = new ArrayList<Sms>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new Sms(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }
}
