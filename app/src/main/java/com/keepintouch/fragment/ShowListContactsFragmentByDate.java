package com.keepintouch.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.keepintouch.MainActivity;
import com.keepintouch.R;
import com.keepintouch.adapter.ContactsAdapter;
import com.keepintouch.model.Contacts;
import com.keepintouch.model.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

public class ShowListContactsFragmentByDate extends ListFragment implements
        SwipeActionAdapter.SwipeActionListener {
    private int check;
    private ArrayList<Contacts> contactsArrayList;
    private ContactsAdapter contactsAdapter;
    private SwipeActionAdapter mAdapter;
    private ShowListContactsFragmentByDate onRefreshSelected;
    private DatabaseHandler db;
//    public static ShowListContactsFragmentByDate newInstance(int check) {
//        ShowListContactsFragmentByDate fragment = new ShowListContactsFragmentByDate();
//        Bundle args = new Bundle();
//        args.putInt("someInt", check);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public ShowListContactsFragmentByDate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        check = getArguments().getInt("someInt");
        check = 0;
        contactsArrayList = new ArrayList<Contacts>();
        db = new DatabaseHandler(getActivity());
        List<Contacts> contacts = db.getAllContacts(check);
        for (Contacts cn : contacts) {
            contactsArrayList.add(new Contacts(cn.getId(), cn.getContact_name(), cn.getContact_number(), cn.getContact_date()));

        }



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactsAdapter = new ContactsAdapter(getActivity(), contactsArrayList);

        mAdapter = new SwipeActionAdapter(contactsAdapter);
        mAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(getListView());
        setListAdapter(mAdapter);
        mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
                .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_list_contacts, container, false);
//        ListView listView = (ListView)view.findViewById(R.id.list);

//        listView.setAdapter(contactsAdapter);
        MainActivity.editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                contactsAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });
        return view;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){

//        Toast.makeText(
//                getActivity(),
//                "Clicked " + mAdapter.getItem(position),
//                Toast.LENGTH_SHORT
//        ).show();
    }


    @Override
    public boolean hasActions(int position){
        return true;
    }


    @Override
    public boolean shouldDismiss(int position, int direction){
        return direction == SwipeDirections.DIRECTION_NORMAL_LEFT;
    }


    @Override
    public void onSwipe(int[] positionList, int[] directionList){
        for(int i=0;i<positionList.length;i++) {
            int direction = directionList[i];
            int position = positionList[i];
            String dir = "";
            Contacts contactsold = contactsArrayList.get(position);
            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left Normal";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
                    dir = "Right Normal";
                    break;
            }
            if(dir == "Left")
            {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String contact_date = df.format(c.getTime());
                db.updateContact(contactsArrayList.get(position).getId(), contact_date);
                Contacts contactsnew = new Contacts(contactsArrayList.get(position).getId(), contactsArrayList.get(position).getContact_name(),contactsArrayList.get(position).getContact_number(), contact_date);
                contactsArrayList.remove(position);
                contactsArrayList.add(contactsnew);
                contactsnew = null;
                contactsAdapter = new ContactsAdapter(getActivity(), contactsArrayList);
                mAdapter = new SwipeActionAdapter(contactsAdapter);
                mAdapter.setSwipeActionListener(this)
                        .setDimBackgrounds(true)
                        .setListView(getListView());
                setListAdapter(mAdapter);
                mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
                        .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
                        .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
                        .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);

            }
            else
            {
                if(dir == "Right")
                {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String contact_date = df.format(c.getTime());
                    db.updateContact(contactsArrayList.get(position).getId(), contact_date);

                    Contacts contactsnew = new Contacts(contactsArrayList.get(position).getId(), contactsArrayList.get(position).getContact_name(),contactsArrayList.get(position).getContact_number(), contact_date);
                    contactsArrayList.remove(position);
                    contactsArrayList.add(contactsnew);
                    contactsnew = null;

                    contactsAdapter = new ContactsAdapter(getActivity(), contactsArrayList);
                    mAdapter = new SwipeActionAdapter(contactsAdapter);
                    mAdapter.setSwipeActionListener(this)
                            .setDimBackgrounds(true)
                            .setListView(getListView());
                    setListAdapter(mAdapter);
                    mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.row_bg_left_far)
                            .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
                            .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
                            .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.row_bg_right);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contactsold.getContact_number()));
                    startActivity(callIntent);
                    contactsold = null;
                }
            }
            mAdapter.notifyDataSetChanged();

        }
    }

}
