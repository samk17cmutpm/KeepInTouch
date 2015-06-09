package com.keepintouch.reusing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keepintouch.MainActivity;
import com.keepintouch.R;
import com.keepintouch.model.Contacts;
import com.keepintouch.model.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by thangtran on 04/06/2015.
 */
public class AddNewContact {
    private Activity activity;
    public AddNewContact(Activity activity)
    {
        this.activity = activity;
    }
    public void addNewContactFromFloatButton()
    {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        final View mainLayout = inflater.inflate(R.layout.dialog_add_new_contact, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        // Set up the input
// Set up the buttons
        builder.setView(mainLayout);
        builder.setPositiveButton("Add New", new DialogInterface.OnClickListener() {
            private String nameContact;
            private String numberContact;

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editTextNameContact = (EditText)mainLayout.findViewById(R.id.contactname);
                EditText editTextNumberContact = (EditText)mainLayout.findViewById(R.id.contactnumber);
                nameContact = editTextNameContact.getText().toString();
                numberContact = editTextNumberContact.getText().toString();
                DatabaseHandler db = new DatabaseHandler(activity);
                db.addContact(new Contacts(nameContact, numberContact, 0));
                Intent intent = new Intent (activity, MainActivity.class);
                activity.startActivity(intent);
                Toast.makeText(activity, "You have added successfully a new Contact !", Toast.LENGTH_LONG).show();
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
}
