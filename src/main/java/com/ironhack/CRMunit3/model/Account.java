package com.ironhack.CRMunit3.model;

import com.ironhack.CRMunit3.enums.Industry;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ironhack.CRMunit3.utils.Colors.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Enumerated(EnumType.STRING)
    private Industry industry;
    private int employeeCount;
    private String city;
    private String country;

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "account")
    @Fetch(FetchMode.SUBSELECT)
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "account")
    @Fetch(FetchMode.SUBSELECT)
    private List<Opportunity> opportunityList = new ArrayList<>();
    //This allows us to generate ids that don't repeat


    public Account() {
    }

    public Account(Industry industry, int employeeCount, String city, String country,
                   Contact contact, Opportunity opportunity) {
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.industry = industry;
        this.contactList.add(contact);
        this.opportunityList.add(opportunity);
    }

    public Account(Industry industry, int employeeCount, String city, String country,
                   List<Contact> contacts, List<Opportunity> opportunities) {
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.industry = industry;
        for (Contact contact : contacts){
            this.contactList.add(contact);
        }
        for (Opportunity opportunity : opportunities){
            this.opportunityList.add(opportunity);
        }
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public List<Opportunity> getOpportunityList() {
        return opportunityList;
    }

    public int getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "Account " + accountId +
                ANSI_RESET + ANSI_BLUE +
                "\nindustry = " + industry +
                "\nemployeeCount = " + employeeCount +
                ", \ncity = " + city +
                ", \ncountry = " + country +
                ", \nopportunityList:\n" + opportunityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return employeeCount == account.employeeCount && industry == account.industry && Objects.equals(city, account.city) && Objects.equals(country, account.country) && Objects.equals(contactList, account.contactList) && Objects.equals(opportunityList, account.opportunityList);
    }
}
