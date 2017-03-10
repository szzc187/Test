package com.example.szzc.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by szzc on 08.03.17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//Usuwa info z SharedPreferences o ustawieniu alarmu, żeby sms nie poszedł na starcie
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("WasAlarmSet");
        editor.apply();
// uruchamia service
        Intent service = new Intent(context, SmsAlarm.class);
        context.startService(service);
    }
}


