package com.ironhack.CRMunit3.utils;

import com.ironhack.CRMunit3.enums.Industry;
import com.ironhack.CRMunit3.enums.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static com.ironhack.CRMunit3.utils.Colors.ANSI_RED_BACKGROUND;
import static com.ironhack.CRMunit3.utils.Colors.ANSI_RESET;
import static com.ironhack.CRMunit3.utils.ScanInfo.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScanInfoTest {

    @Test
    void test_askName_validName() {
        String name = "Pepa Pig";
        InputStream in = new ByteArrayInputStream(name.getBytes());
        scanner = new Scanner(in);
        assertEquals(name, askName());
    }

    @Test
    void test_askSalesRep_validSalesRep() {
        String id = "1";
        Integer id1;
        id1 = Integer.parseInt(id);
        InputStream in = new ByteArrayInputStream(id.getBytes());
        scanner = new Scanner(in);
        assertEquals(id1, askSalesRep());
    }

    @Test
    void test_askPhone_validPhone() {
        String phone = "635698745";
        InputStream in = new ByteArrayInputStream(phone.getBytes());
        scanner = new Scanner(in);
        assertEquals(phone, askPhone());
    }

    @Test
    void test_askEmail_validEmail() {
        String email = "uwu@uwu.es";
        InputStream in = new ByteArrayInputStream(email.getBytes());
        scanner = new Scanner(in);
        assertEquals(email, askEmail());
    }

    @Test
    void test_askProduct_validProduct() {
        String product = "BOX";
        InputStream in = new ByteArrayInputStream(product.getBytes());
        scanner = new Scanner(in);
        assertEquals(Product.BOX, askProduct());
    }

    @Test
    void test_askquantity_validProduct() {
        String quantity = "1";
        Integer num;
        num = Integer.parseInt(quantity);
        InputStream in = new ByteArrayInputStream(quantity.getBytes());
        scanner = new Scanner(in);
        assertEquals(num, askQuantity());
    }

    @Test
    void test_askIndustry_validIndustry() {
        String industry = "PRODUCE";
        InputStream in = new ByteArrayInputStream(industry.getBytes());
        scanner = new Scanner(in);
        assertEquals(Industry.PRODUCE, askIndustry());
    }

    @Test
    void test_askEmployees_validEmployees() {
        String employees = "1";
        Integer num;
        num = Integer.parseInt(employees);
        InputStream in = new ByteArrayInputStream(employees.getBytes());
        scanner = new Scanner(in);
        assertEquals(num, askEmployees());
    }

    @Test
    void test_askCity_validCity() {
        String city = "Madrid";
        InputStream in = new ByteArrayInputStream(city.getBytes());
        scanner = new Scanner(in);
        assertEquals(city, askCity());
    }

    @Test
    void test_askCountry_validCountry() {
        String country = "España";
        InputStream in = new ByteArrayInputStream(country.getBytes());
        scanner = new Scanner(in);
        assertEquals(country, askCountry());
    }

}