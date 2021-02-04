package com.ironhack.CRMunit3.repository;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {
    public Opportunity findByOpportunityId(int id);

    @Query(value = "SELECT s.`name`, COUNT(o.opportunity_id) FROM opportunity o " +
            "RIGHT JOIN sales_rep s ON s.sales_rep_id = o.sales_rep_id GROUP BY s.sales_rep_id", nativeQuery = true)
    public List<Object[]> findNumberOfOpportunitiesPerSalesRep();

    // OPCION QUE NO HE PROBADO para la sig query
//    SELECT a.field1, b.field2
//    FROM (
//                    SELECT field1
//    FROM table1
//            WHERE field3 = 'value'
//            ) AS a
//    INNER JOIN table2 AS b ON a.field1 = b.field1

    @Query(value = "WITH opps AS (SELECT o.sales_rep_id, COUNT(o.opportunity_id) as count FROM opportunity o " +
            "WHERE o.status = :status) SELECT s.`name`, opps.count FROM opps " +
            "RIGHT JOIN sales_rep s ON s.sales_rep_id = opps.sales_rep_id GROUP BY s.sales_rep_id", nativeQuery = true)
    public List<Object[]> findNumberOfOpportunitiesPerSalesRepWithStatus(@Param("status") String status);

    @Query("SELECT o.product, COUNT(*) FROM Opportunity o GROUP BY o.product")
    public List<Object[]> findNumberOfOpportunitiesPerProduct();

    @Query("SELECT o.product, COUNT(*) FROM Opportunity o WHERE o.status = :status GROUP BY o.product")
    public List<Object[]> findNumberOfOpportunitiesPerProductWithStatus(@Param("status") Status status);

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

    @Query(value = "SELECT CAST(AVG(oo.count) AS double) FROM (SELECT COUNT(o.opportunity_id) AS count FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findAvgOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MAX(oo.count) AS double) FROM (SELECT COUNT(o.opportunity_id) AS count FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findMaxOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MIN(oo.count) AS double) FROM (SELECT COUNT(o.opportunity_id) AS count FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findMinOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(oo.count AS DOUBLE) FROM (SELECT COUNT(o.opportunity_id) AS count FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo ORDER BY count", nativeQuery = true)
    public List<Object[]> findOrderOpportunitiesByAccountId();

//  Quantity statistics grouped by product
    @Query("SELECT product, CAST(AVG(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findAvgQuantityGroupByProduct();

    @Query("SELECT product, CAST(MAX(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMaxQuantityGroupByProduct();

    @Query("SELECT product, CAST(MIN(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMinQuantityGroupByProduct();

    @Query(value = "SELECT CAST(quantity AS DOUBLE) FROM opportunity WHERE product=:product ORDER BY quantity", nativeQuery = true)
    public List<Object[]> findOrderedQuantity(@Param("product") String product);

}
