package com.keepintouch.adapter;

import android.content.Context;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.keepintouch.R;
import com.keepintouch.model.Contacts;
import com.keepintouch.model.ContactsImporting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by samnguyen on 4/8/15.
 */
public class ContactsAdapter extends ArrayAdapter<Contacts>  implements Filterable {
    public static final int[] SIDC_COLORS = {
            Color.parseColor("#362e8a"), Color.parseColor("#ef4437"),Color.parseColor("#e71f63"), Color.parseColor("#4554a4"),
            Color.parseColor("#478fcc"), Color.parseColor("#38a4dc"),Color.parseColor("#362e8a"), Color.parseColor("#cddc37"),
     Color.parseColor("#65499d")

    };
    public ArrayList<Contacts> arrayListContactsShowing;
    public ArrayList<Contacts> arrayListContactsShowingAfterFilter;
    public ItemFilter mFilter = new ItemFilter();
    private Context context;
    // View lookup cache
    private static class ViewHolder {
        TextView contactlistshowingname;
        TextView contactlistshowingphonenumber;
        TextView contactlistshowingdate;
        ImageView cotactlistshowingImage;
    }

    public ContactsAdapter(Context context, ArrayList<Contacts> contactsShowing) {
        super(context, R.layout.details_contact_for_showing, contactsShowing);
        this.arrayListContactsShowing = contactsShowing;
        this.arrayListContactsShowingAfterFilter = contactsShowing;
        this.context = context;
     }
    public int getCount() {
        return this.arrayListContactsShowingAfterFilter.size();
    }

    public Contacts getItem(int position) {
        return this.arrayListContactsShowingAfterFilter.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contacts contacts = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.details_contact_for_showing, parent, false);
            viewHolder.contactlistshowingname = (TextView) convertView.findViewById(R.id.textViewNameShowing);
            viewHolder.contactlistshowingphonenumber = (TextView) convertView.findViewById(R.id.textViewPhoneNumberShowing);
            viewHolder.contactlistshowingdate = (TextView) convertView.findViewById(R.id.textViewDateShowing);
            viewHolder.cotactlistshowingImage = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String dayagoShow = " ";
        String contact_date = sdf.format(c.getTime());
        try {
            Date d1 = sdf.parse(contacts.getContact_date());
            Date d2 = sdf.parse(contact_date);
            long diff = d2.getTime() - d1.getTime();
//            if(diff <= 12000000)
//            {
//                convertView.setBackgroundColor(Color.rgb(255, 128, 0));
//            }
//            else
//            {
//                convertView.setBackgroundColor(Color.rgb(255, 0, 0));
//            }
            Log.d("Check Minutes", diff + " ");
            dayagoShow = caculateDays(diff);

        } catch (ParseException ex) {
            Log.d("Wrong", "=======================================>");
        }
        String textDrawble = contacts.getContact_name().substring(0,1);
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(textDrawble.toUpperCase(), SIDC_COLORS[position % SIDC_COLORS.length]);
        // Populate the data into the template view using the data object
        viewHolder.contactlistshowingname.setText(contacts.getContact_name());
        viewHolder.contactlistshowingphonenumber.setText(contacts.getContact_number());
        viewHolder.contactlistshowingdate.setText(dayagoShow);
        viewHolder.cotactlistshowingImage.setImageDrawable(drawable);
        return convertView;
    }
    private String caculateDays(long diff)
    {
        long second = diff / 1000;
        if(second < 60)
        {
            return second + " sec ago";
        }
        else
        {
            long minute = second / 60;
            second = second % 60;

            if(minute < 60)
            {
                return minute + " mins ago";
            }
            else
            {
                long hour = minute / 60;
                minute = minute % 60;

                if(hour < 24)
                {
                    return hour + " hs " + minute + " mins ago";
                }
                else
                {
                    long day = hour / 24;
                    hour = hour % 24;
                    return day + " d" + hour + " hs ago";
                }
            }
        }
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
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            Log.d("XXXXXX", filterString);
            FilterResults results = new FilterResults();
            final ArrayList<Contacts> arrayListFromOriginal = arrayListContactsShowing;
            final ArrayList<Contacts> arrayListAfterFilter = new ArrayList<Contacts>();

            String filterableString ;

            for (int i = 0; i < arrayListFromOriginal.size(); i++) {
                filterableString = arrayListFromOriginal.get(i).getContact_name();
                Log.d("Filter", filterableString);
                if (filterableString.toLowerCase().contains(filterString)) {
                    Log.d("YYY", "OKBABY");
                    arrayListAfterFilter.add(arrayListFromOriginal.get(i));
                }

            }

            results.values = arrayListAfterFilter;
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayListContactsShowingAfterFilter = (ArrayList<Contacts>) results.values;
            notifyDataSetChanged();
        }

    }
}
