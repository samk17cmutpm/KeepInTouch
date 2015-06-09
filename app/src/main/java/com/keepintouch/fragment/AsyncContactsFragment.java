package com.keepintouch.fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.keepintouch.MainActivity;
import com.keepintouch.R;
import com.keepintouch.adapter.ContactsImportingAdapter;
import com.keepintouch.model.Contacts;
import com.keepintouch.model.ContactsImporting;
import com.keepintouch.model.DatabaseHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class AsyncContactsFragment extends Fragment {
    private ContactsImportingAdapter contactsImportingAdapter;
    private ArrayList<ContactsImporting> contactsImportings;
    private ArrayList<Contacts> contactsArrayList;
    private View view;
    private ListView listView;
    private static Boolean check_uncheck = false;


    public static AsyncContactsFragment newInstance(String param1, String param2) {
        AsyncContactsFragment fragment = new AsyncContactsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AsyncContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsImportings = new ArrayList<ContactsImporting>();
        MainActivity.imageViewSearch.setVisibility(View.GONE);
        MainActivity.editTextSearch.setVisibility(View.GONE);
        MainActivity.imageViewImport.setVisibility(View.VISIBLE);
        MainActivity.checkBoxUnCheck.setVisibility(View.VISIBLE);
        MainActivity.checkBoxUnCheck.setChecked(true);
        MainActivity.checkBoxUnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked) {
                      for (int i = 0; i < contactsImportings.size(); i++) {
                      contactsImportings.get(i).setContactImportingCheckBox(true);
                      }
                  }
                 else
                 {
                      for (int i = 0; i < contactsImportings.size(); i++) {
                      contactsImportings.get(i).setContactImportingCheckBox(false);
                      }
                 }
                       contactsImportingAdapter.notifyDataSetChanged();
             }
        }
        );

        MainActivity.imageViewImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsynTaskForImportingContacts abc = new AsynTaskForImportingContacts(contactsImportings);
                abc.execute();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_async_contacts, container, false);
        listView = (ListView)view.findViewById(R.id.listView);
        AsynTaskForGettingHeaderOfDepartment abc = new AsynTaskForGettingHeaderOfDepartment();
        abc.execute();
        return view;
    }
    private class AsynTaskForGettingHeaderOfDepartment extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        private ArrayList<ContactsImporting> arrayList;

        @Override
        protected void onPreExecute() {

            this.dialog = new ProgressDialog(getActivity());
            this.dialog.setMessage("Please wait loading contacts..");
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.show();
            arrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... arg) {
            DatabaseHandler db = new DatabaseHandler(getActivity()); //Read from database
            List<Contacts> contacts = db.getAllContacts(1);
            ArrayList<String> arrCheck = new ArrayList<String>();
            for (Contacts cn : contacts) {
                arrCheck.add(cn.getContact_name());
            }

            ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            int y = 0;
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            for(int i = 0; i < arrCheck.size(); i++)
                            {
                                if(name.equals(arrCheck.get(i)))
                                {
                                    y = 1;
                                    break;
                                }
                            }
                            if(y == 0)
                            {
                                arrayList.add(new ContactsImporting(name, phoneNo));
                            }
                        }
                        pCur.close();
                    }
                }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            contactsImportings = arrayList;
            contactsImportingAdapter = new ContactsImportingAdapter(getActivity(), contactsImportings);
            listView.setAdapter(contactsImportingAdapter);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }

        }
    }

    private class AsynTaskForImportingContacts extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        private ArrayList<ContactsImporting> contactsImportingsAsync;
        private int tempNumber;
        @Override
        protected void onPreExecute() {
            this.dialog = new ProgressDialog(getActivity());
            this.dialog.setMessage("Importing...");
            this.dialog.setCancelable(false);
            this.dialog.setIndeterminate(false);
            this.dialog.show();
        }
        public AsynTaskForImportingContacts(ArrayList<ContactsImporting> arrayList)
        {
            this.contactsImportingsAsync = arrayList;
            tempNumber = 0;
        }
        @Override
        protected String doInBackground(String... arg) {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            ArrayList<Contacts> contactsArrayListAsync = new ArrayList<Contacts>();
            String date = DateFormat.getDateInstance() + "";
            for(int i = 0; i < contactsImportingsAsync.size(); i++)
            {
                if(contactsImportingsAsync.get(i).getContactImportingCheckBox())
                {

                    tempNumber++;
                    contactsArrayListAsync.add(new Contacts(contactsImportingsAsync.get(i).getContactImportingName(), contactsImportingsAsync.get(i).getContactImportingPhoneNo(), 0));
                }

            }
            for(int i = 0; i < contactsArrayListAsync.size(); i++)
            {
                db.addContact(contactsArrayListAsync.get(i));
            }

            return null;
        }
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Intent intent=new Intent (getActivity(),MainActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "You have added : " + tempNumber + "  new Contacts !", Toast.LENGTH_LONG).show();
        }
    }

}
