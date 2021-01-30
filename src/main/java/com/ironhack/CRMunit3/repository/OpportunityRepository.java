package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.Status;
import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {
    public Opportunity findByOpportunityId(int id);

    @Query("SELECT COUNT(*) FROM Opportunity GROUP BY salesRep")
    public List<Object[]> findNumberOfOpportunitiesPerSalesRep();

    @Query("SELECT COUNT(*) FROM Opportunity WHERE status = :status GROUP BY salesRep")
    public List<Object[]> findNumberOfOpportunitiesPerSalesRepWithStatus(Status status);

    @Query("SELECT COUNT(*) FROM Opportunity GROUP BY product")
    public List<Object[]> findNumberOfOpportunitiesPerProduct();

    @Query("SELECT COUNT(*) FROM Opportunity WHERE status = :status GROUP BY product")
    public List<Object[]> findNumberOfOpportunitiesPerProductWithStatus(Status status);

    @Query("SELECT COUNT(*) FROM Opportunity GROUP BY city")
    public List<Object[]> findNumberOfOpportunitiesPerCity();

    @Query("SELECT COUNT(*) FROM Opportunity WHERE status = :status GROUP BY city")
    public List<Object[]> findNumberOfOpportunitiesPerCityWithStatus(Status status);

    @Query("SELECT COUNT(*) FROM Opportunity GROUP BY country")
    public List<Object[]> findNumberOfOpportunitiesPerCountry();

    @Query("SELECT COUNT(*) FROM Opportunity WHERE status = :status GROUP BY country")
    public List<Object[]> findNumberOfOpportunitiesPerCountryWithStatus(Status status);

    @Query("SELECT COUNT(*) FROM Opportunity GROUP BY industry")
    public List<Object[]> findNumberOfOpportunitiesPerIndustry();

    @Query("SELECT COUNT(*) FROM Opportunity WHERE status = :status GROUP BY industry")
    public List<Object[]> findNumberOfOpportunitiesPerIndustryWithStatus(Status status);
}
