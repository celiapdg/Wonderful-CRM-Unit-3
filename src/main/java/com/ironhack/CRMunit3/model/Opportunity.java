package com.ironhack.CRMunit3.model;

import com.ironhack.CRMunit3.enums.*;

import javax.persistence.*;
import java.util.Objects;

import static com.ironhack.CRMunit3.utils.Colors.*;

@Entity
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int opportunityId;
    @Enumerated(EnumType.STRING)
    private Product product;
    private int quantity;

    @OneToOne
    @JoinColumn(name="contact_id")
    private Contact decisionMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    private SalesRep salesRep;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Opportunity() {
    }

    public Opportunity(Product product, int quantity,
                       Contact decisionMaker, SalesRep salesRep) {
        this.quantity = quantity;
        this.product = product;
        this.decisionMaker = decisionMaker;
        this.salesRep = salesRep;
        this.status = Status.OPEN;
    }

    public Account getAccountId() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public Contact getDecisionMaker() {
        return decisionMaker;
    }

    public Status getStatus() {
        return status;
    }

    public int getOpportunityId() {
        return opportunityId;
    }

    public int getQuantity() {
        return quantity;
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
                "Opportunity " + opportunityId +
                ANSI_RESET + ANSI_BLUE +
                "\nproduct = " + product +
                "\namount = " + quantity +
                ", \nstatus = " + status +
                ", \nsales rep = " + salesRep.getName() +
                ", \n" + decisionMaker + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opportunity that = (Opportunity) o;
        return quantity == that.quantity &&
               product == that.product &&
               Objects.equals(decisionMaker, that.decisionMaker) &&
                status == that.status;
    }
}
