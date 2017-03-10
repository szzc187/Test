package com.example.szzc.test;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import java.util.Calendar;
import java.util.Date;

import static com.example.szzc.test.MainActivity.PREFS_NAME;

public class SmsAlarm extends IntentService {

    public SmsAlarm() {
        super("SmsAlarm");
    }

    @Override
    protected void onHandleIntent (@Nullable Intent serviceIntent) {
        Date now = new Date();
        long nowLong = now.getTime();

        if(DataIn.whenStopLong > nowLong) {
//sprawdza czy był ustawiony alarm , jeśli nie smsa nie wysyła idzie dalej do ustawiania alarmu
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isWasAlarmSet = preferences.contains("WasAlarmSet");
//wysłij sms jeśli był ustawiony alarm
            if(isWasAlarmSet) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(DataIn.number, null, DataIn.message, null, null);
            }
//Ustawianie alarmu
            RandomInt randomInt = new RandomInt();
            long losowa = randomInt.RandomInt(DataIn.low,DataIn.high) * DataIn.firstDelay;

            Context context = getApplicationContext();
            Calendar cal = Calendar.getInstance();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent servicePendingIntent =
                    PendingIntent.getService(context,
                            666,
                            serviceIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);

            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis() + losowa,
                    DataIn.repeatDelay,
                    servicePendingIntent
            );
//Dodaje info do SharedPreferences, że alarm został ustawiony
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("WasAlarmSet", 1);
            editor.apply();

            }
        }
    }