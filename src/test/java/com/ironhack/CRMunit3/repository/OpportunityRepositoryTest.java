package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.*;
import java.util.List;

import static com.ironhack.CRMunit3.utils.Colors.ANSI_RED_BACKGROUND;
import static com.ironhack.CRMunit3.utils.Colors.ANSI_RESET;
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
    void validId_findByOpportunityId() {
        int id = opportunityRepository.findAll().get(0).getOpportunityId();
        Opportunity result = opportunityRepository.findByOpportunityId(id);
        assertEquals("Albacete", result.getAccountId().getCity());
    }

    @Test
    void notValidId_findByOpportunityId() {
        assertEquals(null, opportunityRepository.findByOpportunityId(54546));
    }

    @Test
    void findNumberOfOpportunitiesPerSalesRep() {
        List <Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerSalesRep();
        assertEquals(2,result.size());
        assertEquals(new BigInteger(String.valueOf(1)),result.get(0)[1]);
        assertEquals(new BigInteger(String.valueOf(2)),result.get(1)[1]);
    }

    @Test
    void findNumberOfOpportunitiesPerSalesRepWithStatus() {
        List <Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.OPEN.toString());
        assertEquals(2,result.size());
        assertEquals(new BigInteger(String.valueOf(1)), result.get(0)[1]);
    }

    @Test
    void findNumberOfOpportunitiesPerCity() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerCity();
        assertEquals(2L, result.get(0)[1]);
        assertEquals("Albacete",result.get(0)[0]);
    }


    @Test
    void findNumberOfOpportunitiesPerCityWithStatus() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerCityWithStatus(Status.OPEN);
        assertEquals(1L, result.get(0)[1]);
        assertEquals("Albacete", result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerCountry() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerCountry();
        assertEquals(2L, result.get(0)[1]);
        assertEquals("ESSSSPAÑA", result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerCountryWithStatus() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerCountryWithStatus(Status.OPEN);
        assertEquals(1L, result.get(0)[1]);
        assertEquals("ESSSSPAÑA", result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerIndustry() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerIndustry();
        assertEquals(2L, result.get(0)[1]);
        assertEquals(Industry.OTHER, result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerIndustryWithStatus() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerIndustryWithStatus(Status.OPEN);
        assertEquals(1L, result.get(0)[1]);
        assertEquals(Industry.OTHER, result.get(0)[0]);
    }


    @Test
    void findAvgOpportunitiesByAccountId() {
        Object[] result = opportunityRepository.findAvgOpportunitiesByAccountId();
        assertEquals((double) 1.5, result[0]);
    }

    @Test
    void findMaxOpportunitiesByAccountId() {
        Object[] result = opportunityRepository.findMaxOpportunitiesByAccountId();
        assertEquals((double) 2, result[0]);
    }

    @Test
    void findMinOpportunitiesByAccountId() {
        Object[] result = opportunityRepository.findMinOpportunitiesByAccountId();
        assertEquals((double) 1, result[0]);
    }

    @Test
    void findOrderOpportunitiesByAccountId() {
        List<Object[]> result = opportunityRepository.findOrderOpportunitiesByAccountId();
        assertEquals(1.0, result.get(0)[0]);
        assertEquals(2.0, result.get(1)[0]);
    }

    @Test
    void findAvgQuantityGroupByProduct() {
        List<Object[]> result = opportunityRepository.findAvgQuantityGroupByProduct();
        assertEquals(3, result.size());
        assertEquals((double) 40, result.get(0)[1]);
    }

    @Test
    void findMaxQuantityGroupByProduct() {
        List<Object[]> result = opportunityRepository.findMaxQuantityGroupByProduct();
        assertEquals(3, result.size());
        assertEquals((double) 40, result.get(0)[1]);
    }

    @Test
    void findMinQuantityGroupByProduct() {
        List<Object[]> result = opportunityRepository.findMinQuantityGroupByProduct();
        assertEquals(3, result.size());
        assertEquals((double) 40, result.get(0)[1]);
    }

    @Test
    void findOrderedQuantity() {
        List<Object[]> result = opportunityRepository.findOrderedQuantity(Product.HYBRID.toString());
        assertEquals(1, result.size());
        assertEquals( 77.0, result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerProduct() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerProduct();
        assertEquals(1L, result.get(0)[1]);
        assertEquals(Product.BOX, result.get(0)[0]);
    }

    @Test
    void findNumberOfOpportunitiesPerProductWithStatus() {
        List<Object[]> result = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.OPEN);
        assertEquals(1L, result.get(0)[1]);
        assertEquals(Product.BOX, result.get(0)[0]);
    }

}