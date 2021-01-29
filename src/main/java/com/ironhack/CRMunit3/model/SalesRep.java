package com.ironhack.CRMunit3.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class SalesRep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salesRepId;
    private String name;

    @OneToMany(mappedBy = "salesRep")
    private List<Opportunity> opportunities;

    @OneToMany(mappedBy = "salesRep")
    private List<Lead> leads;

    public SalesRep() {
    }

    public SalesRep( String name) {
        this.name = name;
    }
    public int getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        this.salesRepId = salesRepId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Opportunity> getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(List<Opportunity> opportunities) {
        this.opportunities = opportunities;
    }

    public List<Lead> getLeads() {
        return leads;
    }

    public void setLeads(List<Lead> leads) {
        this.leads = leads;
    }
}
