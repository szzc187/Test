package com.example.szzc.test;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity{

    final int PERMISSION_SEND_SMS = 1;
    public static final String PREFS_NAME = "MyPrefsFile";

    //// Life Cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(getBaseContext(), BootCompleteReceiver.class));
        setContentView(R.layout.activity_main);
        // sprawdza czy już tworzył skrót i jeśli nie to robi
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isMakeScut = preferences.contains("makeScut");
        boolean isWasAlarmSet = preferences.contains("WasAlarmSet");
        if(!isMakeScut) {
            CreateShortcut();
        }

        //sprawdza czy mniejszy większy niż Android 6
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_SEND_SMS);
            }
        }
        else{
            if(!isWasAlarmSet) {
                StartAlarmService();
            }
    }
        }

    protected void onStart(){

        super.onStart();
    };

    protected void onResume(){

        super.onResume();
    };

    @Override
    protected void onPause(){
        super.onPause();
    };

    @Override
    protected void onStop(){
        super.onStop();
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
    };

    // Press back again to close
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Wciśnij jeszcze raz wstecz jeżeli chcesz wyjść", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /////               Funkcje              ////////////

    // uruchamia się jeśli android 6 albo wyżej
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS:
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    boolean isWasAlarmSet = preferences.contains("WasAlarmSet");
                    if(!isWasAlarmSet) {
                        StartAlarmService();
                    }

                }
                break;
        }
    }

    //inicjacja menu aktywności głównej
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String ktoryElement = "";

        switch (item.getItemId()) {

            case R.id.item1:
                ktoryElement = "pierwszy";
                break;
            case R.id.item2:
                ktoryElement = "drugi";
                break;
            case R.id.item3:
                ktoryElement = "trzeci";
                break;
            case R.id.item4:
                ktoryElement = "czwarty";
                break;
            case R.id.item5:
                ktoryElement = "piąty";
                break;
            case R.id.item6:
                ktoryElement = "szósty";
                finish();
                System.exit(0);
                break;
            default:
                ktoryElement = "żaden";

        }

        Toast.makeText(getApplicationContext(), "Element: " + ktoryElement,
                Toast.LENGTH_LONG).show();

        return true;
    }

    //Service SMS
    public void StartAlarmService(){
            Context ctx = getApplicationContext();
            Intent serviceIntent = new Intent(ctx, SmsAlarm.class);
            startService(serviceIntent);
    }

    ///Skrót
    public void CreateShortcut(){
        Intent HomeScreenShortCut = new Intent(getApplicationContext(),
                MainActivity.class);
        HomeScreenShortCut.setAction(Intent.ACTION_MAIN);
        HomeScreenShortCut.putExtra("duplicate", false);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, HomeScreenShortCut);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Test");
        addIntent.putExtra(
                Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(
                        getApplicationContext(),
                        R.mipmap.ic_jokes
                ));
        addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("makeScut",1);
        editor.apply();
    }

}










