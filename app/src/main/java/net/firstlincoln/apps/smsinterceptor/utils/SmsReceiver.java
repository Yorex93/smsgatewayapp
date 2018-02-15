package net.firstlincoln.apps.smsinterceptor.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

import net.firstlincoln.apps.smsinterceptor.db.database.AppDatabase;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

import java.lang.reflect.Array;


/**
 * Created by webmaster on 12/02/2018.
 */

public class SmsReceiver extends BroadcastReceiver {
//    final SmsManager sms = SmsManager.getDefault();
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    private boolean check1 = false;
    private boolean check2 = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            String search_term = preferences.getString("search_term", null);
            String search_type = preferences.getString("search_type", null);
            Boolean enable_interception = preferences.getBoolean("enable_interception", true);
            final Boolean auto_resolve = preferences.getBoolean("auto_resolve", true);

//            Log.d("Sms Receiver Class", "Search term is "+search_term);
//            Log.d("Sms Receiver Class", "Search type is "+search_type);
//            Log.d("Sms Receiver Class", "Search enabled is "+enable_interception);


            if(enable_interception && !search_type.isEmpty()){
                //Log.d("SMSReceiver", "Sms Received");
                Bundle bundle = intent.getExtras();
                try {
                    if (bundle != null) {
                        // A PDU is a "protocol data unit". This is the industrial standard for SMS message

                        Object[] pdusObj = (Object[]) bundle.get("pdus");

                        for (int i = 0; i < pdusObj.length; i++) {
                            // This will create an SmsMessage object from the received pdu
                            SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                            // Get sender phone number
                            String phoneNumber = sms.getDisplayOriginatingAddress();
                            final String sender = phoneNumber;
                            final String message = sms.getDisplayMessageBody().trim();

                            Boolean check_wild_card_begin = false;

                            Boolean check_wild_card_end = false;


                            if(!search_term.isEmpty()){
                                check_wild_card_end = search_term.startsWith("%");
                                check_wild_card_begin = search_term.endsWith("%");
                            }

//                            Log.d("Sms Receiver Class", "Wild card begin term is "+check_wild_card_begin);
//                            Log.d("Sms Receiver Class", "Wild card end term is "+check_wild_card_end);

                            search_term = search_term.replace("%","");

                            if(search_type.equals("0")){
                                String m = message;
                                String[] mArr = message.split(" ");

                                if(search_term.isEmpty()){
                                    check1 = true;
                                }else{
                                    if(check_wild_card_begin && check_wild_card_end){
                                        for(int counter = 0; i < mArr.length; i++){
                                            String word = mArr[counter];
                                            if(word.toLowerCase().contains(search_term)){
                                                check1 = true;
                                            }
                                        }
                                    } else if(check_wild_card_begin){

                                        for(int counter = 0; i < mArr.length; i++){
                                            String word = mArr[counter];
                                            if(word.toLowerCase().startsWith(search_term)){
                                                check1 = true;
                                            }
                                        }

                                    } else if (check_wild_card_end) {
                                        for(int counter = 0; i < mArr.length; i++){
                                            String word = mArr[counter];
                                            if(word.toLowerCase().endsWith(search_term)){
                                                check1 = true;
                                            }
                                        }
                                    } else {
                                        check1 = message.toLowerCase().contains(search_term.toLowerCase());
                                    }

                                }

                            }

                            if(search_type.equals("1")){
                                String m = message;
                                String[] mArr = message.split(" ");

                                if(search_term.isEmpty()){
                                    check2 = true;
                                }else {
                                    if(check_wild_card_begin && check_wild_card_end){

                                        check2 = mArr[0].toLowerCase().contains(search_term.toLowerCase());

                                    } else if(check_wild_card_begin){

                                        check2 = mArr[0].toLowerCase().startsWith(search_term.toLowerCase());

                                    } else if (check_wild_card_end) {

                                        check2 = mArr[0].toLowerCase().endsWith(search_term.toLowerCase());

                                    } else {

                                        check1 = mArr[0].toLowerCase().contains(search_term.toLowerCase());

                                    }
                                }
                            }

                            if(check1 || check2){
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase.getAppDatabase(context).smsDao().insert(new SmsEntity(message, sender, 0));

                                        if(auto_resolve){
                                            SmsResolver smsResolver = new SmsResolver(context);

                                            smsResolver.resolvePending();
                                        }

                                    }
                                }).start();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
