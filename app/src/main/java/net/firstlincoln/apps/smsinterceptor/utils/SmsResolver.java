package net.firstlincoln.apps.smsinterceptor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.firstlincoln.apps.smsinterceptor.db.database.AppDatabase;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * Created by webmaster on 13/02/2018.
 */

public class SmsResolver {

    private Context context;

    public SmsResolver(Context context){
        this.context = context;
    }

    public void resolvePending(){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<SmsEntity> pendingSms = AppDatabase.getAppDatabase(context).smsDao().getPendingList();
                if(pendingSms.isEmpty()){

                }else {
                    JSONArray jsonArrayOfPendingSms = new JSONArray();

                    JSONObject jsonObjectSms = new JSONObject();

                    for (int i = 0; i < pendingSms.size(); i++) {
                        SmsEntity sms = pendingSms.get(i);
                        JSONObject jsonSms = new JSONObject();
                        try {
                            jsonSms.put("smsId", sms.getId());
                            jsonSms.put("phone", sms.getPhone());
                            jsonSms.put("content", sms.getContent());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArrayOfPendingSms.put(jsonSms);
                    }

                    try {
                        jsonObjectSms.put("pendingSms", jsonArrayOfPendingSms);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Sms Resolver", "Attempting to resolve " + jsonArrayOfPendingSms.toString() + "");

                    String url = preferences.getString("post_url", null);

                    if (url != null) {
                        RequestQueue queue = Volley.newRequestQueue(context);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonObjectSms,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(final JSONObject response) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    JSONArray resolvedSms = response.getJSONArray("resolvedSms");

                                                    for (int i = 0; i < resolvedSms.length(); i++) {

                                                        JSONObject sms = resolvedSms.getJSONObject(i);

                                                        // Get the current student (json object) data
                                                        int id = sms.getInt("smsId");
                                                        int status = sms.getInt("status");


                                                        SmsEntity smsEntity = AppDatabase.getAppDatabase(context).smsDao().getSmsById(id);

                                                        smsEntity.setStatus(status);

                                                        AppDatabase.getAppDatabase(context).smsDao().updateSms(smsEntity);

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                        queue.add(jsonObjectRequest);
                    }
                }

            }
        }).start();


    }

    public void resolveSingle(SmsEntity sms){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d("Sms Resolver", "Attempting to resolve single pending sms");
            }
        }).start();
    }
}
