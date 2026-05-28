package com.klinker.android.send_message;

import android.content.Context;
import android.os.Build;
import android.telephony.SmsManager;

import java.lang.reflect.Method;

public class SmsManagerFactory {

    public static SmsManager createSmsManager(Context context, Settings settings) {
        return createSmsManager(context, settings.getSubscriptionId());
    }

    public static SmsManager createSmsManager(Context context, int subscriptionId) {
        SmsManager manager;

        if (Build.VERSION.SDK_INT >= 31) { // Build.VERSION_CODES.S
            manager = context.getSystemService(SmsManager.class);
            if (subscriptionId != Settings.DEFAULT_SUBSCRIPTION_ID) {
                try {
                    // lp2 does not have this method, cant compile with it.
                    Method m = SmsManager.class.getMethod("createForSubscriptionId", int.class);
                    manager = (SmsManager) m.invoke(manager, subscriptionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Pre-API 31: fall back to the old static methods
            if (subscriptionId != Settings.DEFAULT_SUBSCRIPTION_ID) {
                try {
                    manager = SmsManager.getSmsManagerForSubscriptionId(subscriptionId);
                } catch (Exception e) {
                    e.printStackTrace();
                    manager = SmsManager.getDefault();
                }
            } else {
                manager = SmsManager.getDefault();
            }
        }

        return manager != null ? manager : SmsManager.getDefault();
    }
}