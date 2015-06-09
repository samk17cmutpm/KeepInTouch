package com.keepintouch.model;

/**
 * Created by nguyen on 4/21/2015.
 */
public class ContactsImporting {
    private String contactImportingName;
    private Boolean contactImportingCheckBox;
    private String contactImportingPhoneNo;

    public String getContactImportingPhoneNo() {
        return contactImportingPhoneNo;
    }

    public void setContactImportingPhoneNo(String contactImportingPhoneNo) {
        this.contactImportingPhoneNo = contactImportingPhoneNo;
    }

    public ContactsImporting(String contactImportingName, String contactImportingPhoneNo) {

        this.contactImportingName = contactImportingName;
        this.contactImportingPhoneNo = contactImportingPhoneNo;
        this.contactImportingCheckBox = true;
    }

    public ContactsImporting(String contactImportingName) {
        this.contactImportingName = contactImportingName;
        this.contactImportingCheckBox = true;
    }

    public String getContactImportingName() {
        return contactImportingName;
    }

    public void setContactImportingName(String contactImportingName) {
        this.contactImportingName = contactImportingName;
    }

    public Boolean getContactImportingCheckBox() {
        return contactImportingCheckBox;
    }

    public void setContactImportingCheckBox(Boolean contactImportingCheckBox) {
        this.contactImportingCheckBox = contactImportingCheckBox;
    }
}
