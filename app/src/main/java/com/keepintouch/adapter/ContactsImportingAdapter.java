package com.keepintouch.adapter;


import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.keepintouch.R;
import com.keepintouch.model.ContactsImporting;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by samnguyen on 4/8/15.
 */
public class ContactsImportingAdapter extends ArrayAdapter<ContactsImporting> {
    public static final int[] SIDC_COLORS_IMPORT = {
            Color.parseColor("#362e8a"), Color.parseColor("#ef4437"),Color.parseColor("#e71f63"), Color.parseColor("#4554a4"),
            Color.parseColor("#478fcc"), Color.parseColor("#38a4dc"),Color.parseColor("#362e8a"), Color.parseColor("#cddc37"),
            Color.parseColor("#65499d")

    };
    // View lookup cache
    private static class ViewHolder {
        TextView contactimportingname;
        CheckBox contactimportingcb;
        TextView contactlistshowingphonenumber;
        ImageView cotactlistshowingImage;
    }

    public ContactsImportingAdapter(Context context, ArrayList<ContactsImporting> contactsImportings) {
        super(context, R.layout.details_contact_for_importing, contactsImportings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ContactsImporting contactsImporting = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.details_contact_for_importing, parent, false);
            viewHolder.contactimportingname = (TextView) convertView.findViewById(R.id.textViewContactNameImport);
            viewHolder.contactimportingcb = (CheckBox) convertView.findViewById(R.id.checkBoxContactImport);
            viewHolder.contactlistshowingphonenumber = (TextView) convertView.findViewById(R.id.textViewPhoneNumberShowing);
            viewHolder.cotactlistshowingImage = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.contactimportingcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                     @Override
                                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                         if (isChecked) {
                                                                             contactsImporting.setContactImportingCheckBox(true);
                                                                         } else {
                                                                             contactsImporting.setContactImportingCheckBox(false);
                                                                         }

                                                                     }
                                                                 }
        );
        String textDrawble = contactsImporting.getContactImportingName().substring(0, 1);
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(textDrawble.toUpperCase(), SIDC_COLORS_IMPORT[position % SIDC_COLORS_IMPORT.length]);

        // Populate the data into the template view using the data object
        viewHolder.contactimportingname.setText(contactsImporting.getContactImportingName());
        viewHolder.contactimportingcb.setChecked(contactsImporting.getContactImportingCheckBox());
        viewHolder.contactlistshowingphonenumber.setText(contactsImporting.getContactImportingPhoneNo());
        viewHolder.cotactlistshowingImage.setImageDrawable(drawable);
        return convertView;
    }
    public int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}