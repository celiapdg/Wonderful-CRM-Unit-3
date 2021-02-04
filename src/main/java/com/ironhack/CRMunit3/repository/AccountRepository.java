package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    public Account findByAccountId(Integer id);

    @Query("SELECT AVG(a.employeeCount) FROM Account a")
    public Object[] findMeanEmployeeCount();

    @Query("SELECT employeeCount FROM Account a ORDER BY employeeCount")
    public List<Object[]> orderEmployeeCount();

    @Query("SELECT MIN(a.employeeCount) FROM Account a")
    public Object[] findMinEmployeeCount();

    @Query("SELECT MAX(a.employeeCount) FROM Account a")
    public Object[] findMaxEmployeeCount();
}
