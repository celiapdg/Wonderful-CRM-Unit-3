package com.ironhack.CRMunit3.model;

import javax.persistence.*;
import java.util.Objects;

import static com.ironhack.CRMunit3.utils.Colors.*;
import static com.ironhack.CRMunit3.utils.Colors.ANSI_BLUE;

@Entity
@Table(name="`lead`")
public class Lead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leadId;
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

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public SalesRep getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(SalesRep salesRep) {
        this.salesRep = salesRep;
    }

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "Lead " + leadId +
                ANSI_RESET + ANSI_BLUE +
                "\nname = " + name +
                ", \nphone number = " + phoneNumber +
                ", \nemail = " + email +
                ", \ncompany name = " + companyName+
                ", \nsales rep = " + salesRep.getName() + "\n";
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
