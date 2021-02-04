package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {
    public Lead findByLeadId(int id);

    @Query(value = "SELECT s.`name`, CAST(COUNT(l.lead_id) AS double) FROM `lead` l " +
            "RIGHT JOIN sales_rep s ON s.sales_rep_id = l.sales_rep_id GROUP BY l.sales_rep_id", nativeQuery = true)
    public List<Object[]> countBySalesRep();
}
