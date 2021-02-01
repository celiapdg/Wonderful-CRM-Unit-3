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

    //TODO: hau que hacer right join aquí para que salgan también los salesrep que no tienen ninguna opportunity asociada.
    // Además, no se puede hacer count(*) porque cuenta las filas con opportunity NULL, hay que especificar la
    // columna o.opportunity_id. Ddejo una query pa copiar y pegar arriba, solo habría que añadir el WHERE con el parámetro.
    // Importante hacer el GROUP BY con el sales_rep_id de la tabla sales_rep (s)
    @Query("SELECT o.salesRep, COUNT(*) FROM Opportunity o WHERE o.status = :status GROUP BY o.salesRep")
    public List<Object[]> findNumberOfOpportunitiesPerSalesRepWithStatus(@Param("status") Status status);

    // TODO: aqui y en las siguientes no hace falta ningún JOIN
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

    @Query(value = "SELECT CAST(AVG(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findAvgOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MAX(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findMaxOpportunitiesByAccountId();

    @Query(value = "SELECT CAST(MIN(oo.opps) AS double) FROM (SELECT COUNT(o.opportunity_id) AS opps FROM `account` a " +
            "LEFT JOIN opportunity o ON a.account_id = o.account_id GROUP BY a.account_id) AS oo", nativeQuery = true)
    public Object[] findMinOpportunitiesByAccountId();

//    quantity
    //TODO: diría que aquí no hace falta el group by, queremos la cantidad media/min/max de productos en general,
    // no la media/min/max por cada tipo de pdto
    @Query("SELECT product, CAST(AVG(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findAvgQuantityGroupByProduct();
    @Query("SELECT product, CAST(MAX(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMaxQuantityGroupByProduct();
    @Query("SELECT product, CAST(MIN(quantity) AS double) FROM Opportunity GROUP BY product")
    public List<Object[]> findMinQuantityGroupByProduct();

    @Query("SELECT quantity FROM Opportunity WHERE product=:product ORDER BY quantity ")
    public List<Object[]> findOrderedQuantity(@Param("product") Product product);

}
