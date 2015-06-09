package com.keepintouch.model;

/**
 * Created by nguyen on 4/21/2015.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactManagement";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_DATE = "date";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT," + KEY_DATE + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(Contacts contacts) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contacts.getContact_name()); // Contact Name
        values.put(KEY_PH_NO, contacts.getContact_number());
        values.put(KEY_DATE, contacts.getContact_date() + " ");

        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    public Contacts getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contacts contact = new Contacts(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contacts> getAllContacts(int y) {
        List<Contacts> contactList = new ArrayList<Contacts>();
        // Select All Query
        String selectQuery = "";
        if(y == 0)
        {
            selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY " + KEY_DATE + " ASC";
        }
        else
        {
            selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY " + KEY_NAME + " ASC";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setContact_name(cursor.getString(1));
                contact.setContact_number(cursor.getString(2));
                contact.setContact_date(cursor.getString(3));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getContact_name());
        values.put(KEY_PH_NO, contact.getContact_number());
        values.put(KEY_DATE, contact.getContact_date());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
    }

    public void updateContact(int position, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);

        // updating row
        db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(position) });
    }


    // Deleting single contact
    public void deleteContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
//        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void deleteAllRecordContacts()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
    }
}
