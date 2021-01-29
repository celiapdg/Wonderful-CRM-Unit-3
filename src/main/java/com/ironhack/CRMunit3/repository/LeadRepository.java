package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {
}
