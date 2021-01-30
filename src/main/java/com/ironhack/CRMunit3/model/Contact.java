package com.ironhack.CRMunit3.model;

import javax.persistence.*;
import java.util.Objects;
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int contactId;
    private String contactName;
    private String phoneNumber;
    private String email;
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account accountId;

    public Contact() {
    }

    public Contact(String name,
                   String phoneNumber,
                   String email,
                   String companyName) {
        this.contactName = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
    }

    public String getName() {
        return contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getContactId() {
        return contactId;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Contact: " +
                "\n  name = " + contactName +
                ", \n  phoneNumber = " + phoneNumber +
                ", \n  email = " + email  +
                ", \n  companyName = " + companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(contactName, contact.contactName) &&
               Objects.equals(phoneNumber, contact.phoneNumber) &&
               Objects.equals(email, contact.email) &&
               Objects.equals(companyName, contact.companyName);
    }
}
