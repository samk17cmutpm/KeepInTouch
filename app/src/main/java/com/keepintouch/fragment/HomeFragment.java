package com.keepintouch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.keepintouch.MainActivity;
import com.keepintouch.R;
import com.keepintouch.adapter.NonSwipeableViewPager;
import com.keepintouch.adapter.SmartFragmentStatePagerAdapter;
import com.keepintouch.reusing.AddNewContact;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SmartFragmentStatePagerAdapter adapterViewPager;
    // Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public static Fragment refreshFragment(int position)
        {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new ShowListContactsFragmentByName();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new ShowListContactsFragmentByDate();
                default:
                    return null;
            }
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new ShowListContactsFragmentByDate();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new ShowListContactsFragmentByName();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            String condition = "";
            if(position == 0)
            {
                condition = "Date";
            }
            else
            {
                condition = "Name";
            }
            return "By " + condition;
        }

    }



    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        MainActivity.imageViewSearch.setVisibility(View.VISIBLE);
        MainActivity.imageViewImport.setVisibility(View.GONE);
        MainActivity.checkBoxUnCheck.setVisibility(View.GONE);
        MainActivity.imageViewSearch.setVisibility(View.VISIBLE);
        MainActivity.imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.editTextSearch.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        NonSwipeableViewPager vpPager = (NonSwipeableViewPager) view.findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabsStrip.setViewPager(vpPager);
        // Inflate the layout for this fragment
        FloatingActionButton abc = (FloatingActionButton)view.findViewById(R.id.pink_icon);
        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewContact doIt = new AddNewContact(getActivity());
                doIt.addNewContactFromFloatButton();
            }
        });

        return view;
    }

}
