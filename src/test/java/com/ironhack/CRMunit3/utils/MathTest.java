package com.ironhack.CRMunit3.utils;

import com.ironhack.CRMunit3.enums.Industry;
import com.ironhack.CRMunit3.enums.Product;
import com.ironhack.CRMunit3.enums.Status;
import com.ironhack.CRMunit3.model.*;
import com.ironhack.CRMunit3.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.ironhack.CRMunit3.utils.Colors.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MathTest {

    @Autowired
    SalesRepRepository salesRepRepository;
    @Autowired
    LeadRepository leadRepository;
    @Autowired
    OpportunityRepository opportunityRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    AccountRepository accountRepository;

    @BeforeAll
    public static void init(){
        System.out.println(ANSI_RED_BACKGROUND + "Remember to comment lines 46 to 57 from " +
                "CrmUnit3Application.java before running the tests :D" + ANSI_RESET);
    }

    @BeforeEach
    void setUp() {
        SalesRep salesRep1 = new SalesRep("Pepa Floros");
        SalesRep salesRep2 = new SalesRep("Husd Hasd");

        Contact contact1 = new Contact("Pepa Pig", "676767676", "pepa@pig.pp", "Pigs");
        Contact contact2 = new Contact("Ana Cardo", "656565656", "ana@car.do", "Cards");
        Contact contact3 = new Contact("Hula Hop", "656565656", "hu@la.hop", "Huli");

        Opportunity opportunity1 = new Opportunity(Product.BOX, 40, contact1, salesRep1);
        Opportunity opportunity2 = new Opportunity(Product.FLATBED, 23, contact2, salesRep2);
        Opportunity opportunity3 = new Opportunity(Product.HYBRID, 77, contact3, salesRep2);

        opportunity2.setStatus(Status.CLOSED_LOST);
        opportunity3.setStatus(Status.CLOSED_WON);

        salesRepRepository.saveAll(List.of(salesRep1, salesRep2));

        leadRepository.saveAll(List.of(new Lead("Pepa Flores", "666666666", "asda@asd.asd","caca", salesRep1),
                new Lead("Pipo Florín", "666444466", "asda@asd.asd","pedo", salesRep2),
                new Lead("Rosa Asor", "626262626", "ros@aa.sor", "pis", salesRep1)));

        contactRepository.saveAll(List.of(contact1,contact2, contact3));

        opportunityRepository.saveAll(List.of(opportunity1, opportunity2, opportunity3));

        Account account1 = new Account(Industry.OTHER, 40, "Albacete", "ESSSSPAÑA",
                List.of(contact1,contact2), List.of(opportunity1,opportunity2));
        Account account2 = new Account(Industry.MEDICAL, 75, "Buguibugui", "EZPAÑA", contact1, opportunity3);
        accountRepository.saveAll(List.of(account1,account2));
        opportunity1.setAccount(account1);
        opportunity2.setAccount(account1);
        opportunity3.setAccount(account2);
        opportunityRepository.saveAll(List.of(opportunity1,opportunity2,opportunity3));
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
    }

    @Test
    void medianTest() {
        List<Object[]> result = opportunityRepository.findOrderOpportunitiesByAccountId();
        assertEquals(1.5, Math.median(result));
        result = opportunityRepository.findOrderedQuantity(Product.HYBRID.toString());
        assertEquals( 77.0, Math.median(result));
        result = accountRepository.orderEmployeeCount();
        assertEquals(57.5, Math.median(result));
    }
}