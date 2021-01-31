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

    @Query("SELECT a.city, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account GROUP BY a.city")
    public List<Object[]> findNumberOfOpportunitiesPerCity();

    @Query("SELECT a.city, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account WHERE o.status = :status GROUP BY a.city")
    public List<Object[]> findNumberOfOpportunitiesPerCityWithStatus(@Param("status") Status status);

    @Query("SELECT a.country, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account GROUP BY a.country")
    public List<Object[]> findNumberOfOpportunitiesPerCountry();

    @Query("SELECT a.country, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account WHERE o.status = :status GROUP BY a.country")
    public List<Object[]> findNumberOfOpportunitiesPerCountryWithStatus(@Param("status") Status status);

    @Query("SELECT a.industry, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account GROUP BY a.industry")
    public List<Object[]> findNumberOfOpportunitiesPerIndustry();

    @Query("SELECT a.industry, COUNT(*) FROM Opportunity o JOIN Account a ON a.accountId = o.account WHERE o.status = :status GROUP BY a.industry")
    public List<Object[]> findNumberOfOpportunitiesPerIndustryWithStatus(@Param("status") Status status);

    @Query(value = "SELECT CAST(AVG(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public List<Object[]> findAvgOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MAX(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public List<Object[]> findMaxOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MIN(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public List<Object[]> findMinOpportunitiesByAccountId();

//    quantity
    @Query("SELECT product, CAST(AVG(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findAvgGroupByProduct();
    @Query("SELECT product, CAST(MAX(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMaxGroupByProduct();
    @Query("SELECT product, CAST(MIN(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMinGroupByProduct();

}
