package com.keepintouch;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.keepintouch.adapter.FragmentNavigationDrawer;
import com.keepintouch.broadcastreciever.MyAlarmReceiver;
import com.keepintouch.fragment.AsyncContactsFragment;
import com.keepintouch.fragment.HomeFragment;
import com.keepintouch.fragment.SettingFragment;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    private FragmentNavigationDrawer dlDrawer;
    public static EditText editTextSearch;
    public static ImageView imageViewImport;
    public static CheckBox checkBoxUnCheck;
    public static ImageView imageViewSearch;

    //public static Boolean checkAlarm = false;
    public SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.check);
        imageViewImport = (ImageView) findViewById(R.id.imageViewImport);
        imageViewImport.setVisibility(View.GONE);

        checkBoxUnCheck = (CheckBox) findViewById(R.id.checkBoxContactImport);
        checkBoxUnCheck.setVisibility(View.GONE);

        imageViewSearch = (ImageView) findViewById(R.id.imageViewSearch);

        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        editTextSearch.setVisibility(View.GONE);
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);

        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), toolbar,
                R.layout.drawer_nav_item, R.id.flContent);
        dlDrawer.addNavItem("Home", "Contacts", HomeFragment.class);
        dlDrawer.addNavItem("Import Contacts", "Import", AsyncContactsFragment.class);
        dlDrawer.addNavItem("Setting", "Settings", SettingFragment.class);
//        dlDrawer.addNavItem("Save Backup", "Save Backup", SaveBackUpFragment.class);

        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }
        sharedpreferences = getSharedPreferences(SettingFragment.MyPREFERENCES, Context.MODE_PRIVATE);
        String checkToAlarm = sharedpreferences.getString(SettingFragment.CHECK,"");
        if(checkToAlarm.equals("checked")) {
            scheduleAlarm();
        }




    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
        int intervalMillis = 5000; // 5 seconds
        Date dat  = new Date();//initializes to now
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, sharedpreferences.getInt(SettingFragment.PROGRESS, 1));//set the alarm time
        cal_alarm.set(Calendar.MINUTE, 0);
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){//if its in the past increment
            cal_alarm.add(Calendar.DATE,1);
        }
        //calendar.set(Calendar.MINUTE, 00);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
        alarm.set(AlarmManager.RTC, cal_alarm.getTimeInMillis(), pIntent);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
            // Uncomment to hide menu items
            // menu.findItem(R.id.mi_test).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
}
