package com.keepintouch.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.keepintouch.MainActivity;
import com.keepintouch.R;
import com.keepintouch.adapter.ContactsImportingAdapter;
import com.keepintouch.model.Contacts;
import com.keepintouch.model.ContactsImporting;
import com.keepintouch.model.DatabaseHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class SettingFragment extends Fragment {
    private TextView textView;
    private Switch mySwitch;
    private SeekBar seekBar;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String CHECK = "Key";
    public static final String PROGRESS = "Keyprogress";
    private Button buttonOk;
    private Button buttonRemoveAllContacts;

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }
    public SettingFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.imageViewSearch.setVisibility(View.GONE);
        MainActivity.editTextSearch.setVisibility(View.GONE);
        MainActivity.imageViewImport.setVisibility(View.GONE);
        MainActivity.checkBoxUnCheck.setVisibility(View.GONE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mySwitch = (Switch) view.findViewById(R.id.switchAlarm);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        textView = (TextView) view.findViewById(R.id.textViewSeekBar);
        buttonRemoveAllContacts = (Button) view.findViewById(R.id.buttonRemoveDatabase);
        buttonRemoveAllContacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please make sure that you want to delete all !");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsynTaskForRemovingAllContacts abc = new AsynTaskForRemovingAllContacts();
                        abc.execute();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedpreferences.contains(CHECK))
        {
            mySwitch.setChecked(false);
            mySwitch.setText("Off");
            seekBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
        else
        {
            Log.d("Check ++++++++>>>", sharedpreferences.getInt(PROGRESS, 1) + "");
            String checkToogle = sharedpreferences.getString(CHECK, "");
            if(checkToogle.equals("checked"))
            {
                mySwitch.setChecked(true);
                mySwitch.setText("On");
                seekBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
            else
            {
                mySwitch.setChecked(false);
                mySwitch.setText("Off");
//                seekBar.setVisibility(View.INVISIBLE);
                seekBar.setActivated(false);
                textView.setVisibility(View.INVISIBLE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(CHECK, "unchecked");
                editor.commit();
            }
        }

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                       seekBar.setVisibility(View.VISIBLE);
                       textView.setVisibility(View.VISIBLE);
                       editor.putString(CHECK, "checked");
                       mySwitch.setText("On");
                }else
                {
                       seekBar.setVisibility(View.INVISIBLE);
                       textView.setVisibility(View.INVISIBLE);
                       editor.putString(CHECK, "unchecked");
                       mySwitch.setText("Off");
                }
                editor.commit();

            }
        });
        seekBar.setProgress(sharedpreferences.getInt(PROGRESS, 1));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getActivity(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt(PROGRESS, progress);
                editor.commit();

                String global = "";
                if(progress <= 12)
                {
                    global = " AM";
                }
                else
                {
                    global = " PM";
                }
                textView.setText("We Will Alert The Message For You At: " + progress + global);
                //Toast.makeText(getActivity(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    private class AsynTaskForRemovingAllContacts extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            this.dialog = new ProgressDialog(getActivity());
            this.dialog.setMessage("Deleting...");
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... arg) {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            db.deleteAllRecordContacts();
            return null;
        }
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Intent intent=new Intent (getActivity(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Your contacts have been removed succefully !", Toast.LENGTH_LONG).show();
        }
    }
}
