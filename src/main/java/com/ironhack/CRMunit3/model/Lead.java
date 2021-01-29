package com.ironhack.CRMunit3.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leadId;
    private String name;
    private String phoneNumber;
    private String email;
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    private SalesRep salesRep;


    public Lead() {
    }


    public Lead(String name,
                String phoneNumber,
                String email,
                String companyName, SalesRep salesRep) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
        this.salesRep = salesRep;
    }

    public String getName() {
        return name;
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

    public int getLeadId() {
        return leadId;
    }

    public SalesRep getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(SalesRep salesRep) {
        this.salesRep = salesRep;
    }

    @Override
    public String toString() {
        return (char)27 + "[34mLead " + leadId +
                "\nname = " + name +
                ", \nphoneNumber = " + phoneNumber +
                ", \nemail = " + email +
                ", \ncompanyName = " + companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return phoneNumber == lead.phoneNumber &&
               Objects.equals(name, lead.name) &&
               Objects.equals(email, lead.email) &&
               Objects.equals(companyName, lead.companyName);
    }
}
