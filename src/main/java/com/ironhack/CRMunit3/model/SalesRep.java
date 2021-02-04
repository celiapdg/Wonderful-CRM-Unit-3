package com.ironhack.CRMunit3.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

import static com.ironhack.CRMunit3.utils.Colors.*;
import static com.ironhack.CRMunit3.utils.Colors.ANSI_BOLD;

@Entity
public class SalesRep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer salesRepId;
    private String name;

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "salesRep")
    @Fetch(FetchMode.SUBSELECT)
    private List<Opportunity> opportunities;

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "salesRep")
    @Fetch(FetchMode.SUBSELECT)
    private List<Lead> leads;

    public SalesRep() {
    }

    public SalesRep( String name) {
        this.name = name;
    }

    public void addOpportunity(Opportunity opportunity){
        this.opportunities.add(opportunity);
    }

    public Integer getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(Integer salesRepId) {
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

    @Override
    public String toString() {
        return ANSI_CYAN + ANSI_BOLD +
                "SalesRep: " +salesRepId +
                ANSI_RESET + ANSI_BLUE +
                "\nname = " + name +
                "\nopportunities:\n" + opportunities +
                "\nleads:\n" + leads;
    }
}
