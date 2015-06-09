package com.keepintouch.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nguyen on 4/20/2015.
 */
public class Contacts {
    private int id;
    private String contact_name;
    private String contact_number;
    private String contact_date;
    public Contacts()
    {

    }

    public Contacts(int id, String contact_name, String contact_number, String contact_date) {
        this.id = id;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.contact_date = contact_date;

    }

    public Contacts(int id, String contact_name, String contact_number, long contact_timeago) {

        this.contact_name = contact_name;
        this.contact_number = contact_number;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.contact_date = df.format(c.getTime());
    }

    public Contacts(String contact_name, String contact_number, long contact_timeago) {

        this.contact_name = contact_name;
        this.contact_number = contact_number;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.contact_date = df.format(c.getTime());
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_date() {
        return contact_date;
    }

    public void setContact_date(String contact_date) {
        this.contact_date = contact_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}


