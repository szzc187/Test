package com.example.szzc.test;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity{

   public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions= new String[]{
            //tutaj dodawać kolejne permissions do realtime permissions
          //  Manifest.permission.READ_PHONE_STATE,
            //Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS

    };

    //// Life Cycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DataIn.SmsServiceOn == 1){
        startService(new Intent(getBaseContext(), BootCompleteReceiver.class));
        }
        setContentView(R.layout.activity_main);
        AppInstallTime();
        // sprawdza czy już tworzył skrót i jeśli nie to robi
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isMakeScut = preferences.contains("makeScut");
        boolean isMakeScut2 = preferences.contains("makeScut2");
        boolean isWasAlarmSet = preferences.contains("WasAlarmSet");
        if(!isMakeScut && !isMakeScut2) {
            CreateShortcut();
            CreateShortcut2();
        }
        // starsze androidy permissions z manifestu
        if (checkPermissions()){
            if(!isWasAlarmSet) {
                if(DataIn.SmsServiceOn == 1) {
                    StartAlarmService();
                }
                //getPhoneNumber();

        }

        }}

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

    // uruchamia się jeśli android 6 albo wyżej -- realtime permissions idzie do checkPermission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    boolean isWasAlarmSet = preferences.contains("WasAlarmSet");
                    if(!isWasAlarmSet) {
                        if(DataIn.SmsServiceOn == 1) {
                            StartAlarmService();
                        }
                        // getPhoneNumber();
                    }
                } else {
                    finish();
                    System.exit(0);
                }
                return;
            }
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

    ///Skrót do programu
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

    //URL szortkat
    public void CreateShortcut2() {
        String urlString = DataIn.adressUrl;
        String urlStringIconName = DataIn.adressUrlIconName;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        intent.putExtra("duplicate", false);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, urlStringIconName);
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
        editor.putInt("makeScut2", 1);
        editor.apply();
    }

    //zapis czasu instalacji
    public void AppInstallTime(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAppInstallTime = preferences.contains("appinstalltime");
        if(!isAppInstallTime){
            Calendar cal = Calendar.getInstance();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("appinstalltime",cal.getTimeInMillis());
            editor.apply();
        }
    }

    //check permission
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    //get numer telefonu
    private void getPhoneNumber(){
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Toast.makeText(this, "Numer telefonu: "+mPhoneNumber, Toast.LENGTH_SHORT).show();
    }
}










