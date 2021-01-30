package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.Product;
import com.ironhack.CRMunit3.enums.Status;
import com.ironhack.CRMunit3.model.Contact;
import com.ironhack.CRMunit3.model.Lead;
import com.ironhack.CRMunit3.model.Opportunity;
import com.ironhack.CRMunit3.model.SalesRep;
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

    @BeforeEach
    void setUp() {
        SalesRep salesRep1 = new SalesRep("Pepa Floros");
        SalesRep salesRep2 = new SalesRep("Pepa Floros");
        Contact contact1 = new Contact("Pepa Pig", "676767676", "pepa@pig.pp", "Pigs");
        Contact contact2 = new Contact("Ana Cardo", "656565656", "ana@car.do", "Cards");

        salesRepRepository.saveAll(List.of(salesRep1, salesRep2));

        leadRepository.saveAll(List.of(new Lead("Pepa Flores", "666666666", "asda@asd.asd","caca", salesRep1),
                                       new Lead("Pipo Florín", "666444466", "asda@asd.asd","pedo", salesRep2)));

        contactRepository.saveAll(List.of(contact1,contact2));

        opportunityRepository.saveAll(List.of(new Opportunity(Product.BOX, 40, contact1, salesRep1),
                                              new Opportunity(Product.FLATBED, 23, contact2, salesRep2)));
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
    }

    @Test
    void findNumberOfOpportunitiesPerSalesRepWithStatus() {
        List <Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.OPEN);
        assertEquals(2,result.size());
        assertEquals(1L,result.get(0)[0]);
        assertEquals(1L,result.get(1)[0]);
    }
}