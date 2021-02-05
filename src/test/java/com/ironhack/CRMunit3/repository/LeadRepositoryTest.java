package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import static com.ironhack.CRMunit3.utils.Colors.ANSI_RED_BACKGROUND;
import static com.ironhack.CRMunit3.utils.Colors.ANSI_RESET;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LeadRepositoryTest {
    @Autowired
    SalesRepRepository salesRepRepository;

    @Autowired
    LeadRepository leadRepository;

    @BeforeAll
    public static void init(){
        System.out.println(ANSI_RED_BACKGROUND + "Remember to comment lines 46 to 57 from " +
                "CrmUnit3Application.java before running the tests :D" + ANSI_RESET);
    }

    @BeforeEach
    void setUp() {
        SalesRep salesRep1 = new SalesRep("Pepa Floros");
        SalesRep salesRep2 = new SalesRep("Husd Hasd");
        salesRepRepository.saveAll(List.of(salesRep1, salesRep2));

        Lead lead1 = new Lead("Pepa Flores", "666666666", "asda@asd.asd","caca", salesRep1);
        Lead lead2 = new Lead("Pipo Florín", "666444466", "asda@asd.asd","pedo", salesRep2);
        lead1.setSalesRep(salesRep1);
        lead2.setSalesRep(salesRep2);

        leadRepository.saveAll(List.of(lead1, lead2));
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
    }

    @Test
    void findByLeadId() {
//      Let's look for Pepa Flores:
        int id = leadRepository.findAll().get(0).getLeadId();
        System.out.println();
//      and confirm this is Pepa:
        assertEquals("Pepa Flores", leadRepository.findByLeadId(id).getName());
    }

    @Test
    void countBySalesRep() {
        List<Object[]> result = leadRepository.countBySalesRep();
        assertEquals("Pepa Floros", result.get(0)[0]);
        assertEquals(1.0, result.get(0)[1]);
        assertEquals("Husd Hasd", result.get(1)[0]);
        assertEquals(1.0, result.get(1)[1]);
    }
}