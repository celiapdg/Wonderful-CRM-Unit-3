package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface SalesRepRepository extends JpaRepository<SalesRep, Integer> {
    public SalesRep findBySalesRepId(int id);
}
