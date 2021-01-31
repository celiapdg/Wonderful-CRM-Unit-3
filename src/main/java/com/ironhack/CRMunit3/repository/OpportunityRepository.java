package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.Status;
import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {
    public Opportunity findByOpportunityId(int id);

    @Query("SELECT o.salesRep, COUNT(*) FROM Opportunity o GROUP BY o.salesRep")
    public List<Object[]> findNumberOfOpportunitiesPerSalesRep();

    @Query("SELECT o.salesRep, COUNT(*) FROM Opportunity o WHERE o.status = :status GROUP BY o.salesRep")
    public List<Object[]> findNumberOfOpportunitiesPerSalesRepWithStatus(@Param("status") Status status);

    @Query("SELECT o.product, COUNT(*) FROM Opportunity o GROUP BY o.product")
    public List<Object[]> findNumberOfOpportunitiesPerProduct();

    @Query("SELECT o.product, COUNT(*) FROM Opportunity o WHERE o.status = :status GROUP BY o.product")
    public List<Object[]> findNumberOfOpportunitiesPerProductWithStatus(@Param("status") Status status);

//   TODO: Joins

//    @Query("SELECT COUNT(*), salesRep FROM Opportunity GROUP BY city")
//    public List<Object[]> findNumberOfOpportunitiesPerCity();
//
//    @Query("SELECT COUNT(*), salesRep FROM Opportunity WHERE status = :status GROUP BY city")
//    public List<Object[]> findNumberOfOpportunitiesPerCityWithStatus(@Param("status") Status status);
//
//    @Query("SELECT COUNT(*), salesRep FROM Opportunity GROUP BY country")
//    public List<Object[]> findNumberOfOpportunitiesPerCountry();
//
//    @Query("SELECT COUNT(*), salesRep FROM Opportunity WHERE status = :status GROUP BY country")
//    public List<Object[]> findNumberOfOpportunitiesPerCountryWithStatus(@Param("status") Status status);
//
//    @Query("SELECT COUNT(*), salesRep FROM Opportunity GROUP BY industry")
//    public List<Object[]> findNumberOfOpportunitiesPerIndustry();
//
//    @Query("SELECT COUNT(*), salesRep FROM Opportunity WHERE status = :status GROUP BY industry")
//    public List<Object[]> findNumberOfOpportunitiesPerIndustryWithStatus(@Param("status") Status status);
}
