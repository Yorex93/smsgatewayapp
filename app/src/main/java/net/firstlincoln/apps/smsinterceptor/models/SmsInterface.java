package net.firstlincoln.apps.smsinterceptor.models;

import java.util.Date;

/**
 * Created by webmaster on 09/02/2018.
 */

public interface SmsInterface {
    Date getCreatedAt();
    String getPhone();
    String getContent();
    int getId();
    int getStatus();
}

