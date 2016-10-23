package com.android.habitapp.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Hitesh on 10/22/2016.
 */

public class AlarmReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Constants.LOG_TAG + " onReceive", "who started");
        Intent service_inIntent=new Intent(context,Alarm_service.class);
        service_inIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        context.startService(service_inIntent);
    }
}
