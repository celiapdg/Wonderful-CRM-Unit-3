package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpportunityRepositoryTest {

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

        Account account = new Account(Industry.OTHER, 40, "Albacete", "ESSSSPAÑA", contact1, opportunity1);
        accountRepository.save(account);
        opportunity1.setAccount(account);
        opportunityRepository.save(opportunity1);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
    }

    @Test
    void findByOpportunityId() {
    }

    @Test
    void findNumberOfOpportunitiesPerSalesRep() {
        List <Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerSalesRep();
        assertEquals(2,result.size());
        assertEquals(1L,result.get(0)[1]);
        assertEquals(2L,result.get(1)[1]);
    }

    @Test
    void findNumberOfOpportunitiesPerSalesRepWithStatus() {
        List <Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.OPEN);
        assertEquals(1,result.size());
        assertEquals(1L,result.get(0)[1]);
    }

    @Test
    void findNumberOfOpportunitiesPerCity() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerCity();
        assertEquals(1L, result.get(0)[1]);
        assertEquals("Albacete",result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerCountry() {
    }

    @Test
    void findNumberOfOpportunitiesPerCountryWithStatus() {
    }

    @Test
    void findNumberOfOpportunitiesPerIndustry() {
    }

    @Test
    void findNumberOfOpportunitiesPerIndustryWithStatus() {
    }
}