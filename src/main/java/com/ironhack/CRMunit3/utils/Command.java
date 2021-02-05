package com.ironhack.CRMunit3.utils;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import com.ironhack.CRMunit3.repository.*;

import org.springframework.stereotype.*;

import java.io.InvalidObjectException;
import java.util.*;

import static com.ironhack.CRMunit3.utils.Colors.*;
import static com.ironhack.CRMunit3.utils.Math.median;
import static com.ironhack.CRMunit3.utils.ScanInfo.*;

@Service
public class Command {

    public static Sound errorSound = new Sound("sounds/error.wav");
    public static Sound bipSound = new Sound("sounds/bip.wav");
    public static Sound yuhuSound = new Sound("sounds/yuhu.wav");
    public static Sound exitSound = new Sound("sounds/exit.wav");

    SalesRepRepository salesRepRepository;
    LeadRepository leadRepository;
    ContactRepository contactRepository;
    OpportunityRepository opportunityRepository;
    AccountRepository accountRepository;

    public Command(SalesRepRepository salesRepRepository,
                   LeadRepository leadRepository,
                   ContactRepository contactRepository,
                   OpportunityRepository opportunityRepository,
                   AccountRepository accountRepository) {
        this.salesRepRepository = salesRepRepository;
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.opportunityRepository = opportunityRepository;
        this.accountRepository = accountRepository;
    }
    //method called in main
    public void commandReader(String userInput) {

        //separate the words in the input
        String[] arr = userInput.split(" ");

        //This try catch checks that the first word is one of the valid commands
        try {
            //If it is we check which one with a switch
            switch (arr[0]) {
                case "new":
                    switch (arr[1]){
                        case "sales":
                            String salesRepName = askName();
                            newSalesRep(salesRepName);
                            break;
                        case "lead":
                                //go to utils ScanInfo to check how these work
                                String name = askName();
                                String phone = askPhone();
                                String email = askEmail();
                                String company = askCompName();

                                SalesRep salesRep = null;
                                while (salesRep == null){
                                    Integer salesRepId = askSalesRep();
                                    salesRep = salesRepRepository.findBySalesRepId(salesRepId);
                                    if(salesRep == null) {
                                        System.out.println(ANSI_RED + "That sales rep does not exists");
                                    }
                                }

                                //this method is defined below
                                newLead(name, phone, email, company, salesRep);
                                yuhuSound.playSound();
                            break;
                        default:
                            System.out.println(ANSI_RED + "That is not a valid command");
                            errorSound.playSound();
                    }
                    break;
                case "convert":
                    //if the first word is convert  the method checks if the second is a number if it is not catches the error
                    int id = Integer.parseInt(arr[1]);
                    Lead lead=leadRepository.findByLeadId(id);
                    //This method is defined below
                    Contact contact = createContact(lead);

                    //go to utils ScanInfo to check how these work
                    Product product = askProduct();
                    int quantity = askQuantity();

                    //method implementation below
                    Opportunity opportunity = createOpportunity(product, quantity, contact, lead.getSalesRep());

                    Account account = new Account();

                    String answer=askNewAccount();

                    switch (answer){
                        case"n":
                            if (accountRepository.findAll().isEmpty()){
                                System.out.println(ANSI_RED + "There is no account created yet, you must create one");
                            }else {
                                Integer accountId = askAccountId();
                                account = accountRepository.findByAccountId(accountId);
                                account.getOpportunityList().add(opportunity);
                                account.getContactList().add(contact);
                                accountRepository.save(account);
                            break;
                            }
                        case"y":
                            //go to utils ScanInfo to check how these work
                            Industry industry = askIndustry();
                            int numOfEmployees = askEmployees();
                            String city = askCity();
                            String country = askCountry();

                            //the next two methods are also below
                            account = createAccount(industry, numOfEmployees, city, country, contact, opportunity);
                            break;
                    }

                    opportunity.setAccount(account);
                    opportunityRepository.save(opportunity);
                    contact.setAccountId(account);
                    contactRepository.save(contact);

                    removeLead(leadRepository.findByLeadId(id));
                    System.out.println(ANSI_GREEN + "New opportunity created!!\n"+opportunity);
                    yuhuSound.playSound();
                    break;
                case "show":
                    show(arr[1]);
                    break;
                case "lookup":
                    if (arr[1].equals("sales")) {
                        //method below
                        lookup(arr[1], arr[3]);
                    }else {
                        lookup(arr[1], arr[2]);
                    }
                    break;

                case "close-lost":
                case "close-won":
                    //method below
                    closeOpportunity(arr[0],arr[1]);
                    bipSound.playSound();
                    break;

                case "report":
                    reportOptions(arr);
                    break;

                case "statistics":
                    statistics();
                    break;

                case "exit":
                    //ONLY COMMAND THAT EXITS THE APPLICATION
                    System.out.println(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "Thank you for using the best CRM in the world");
                    exitSound.playSound();
                    bipSound.closeSound();
                    errorSound.closeSound();
                    exitSound.closeSound();
                    break;

                default:
                    //if the first word is not equal to any of the above this comes up
                    System.out.println(ANSI_RED + "That is not a valid command");
                    errorSound.playSound();
                }
            }catch(NumberFormatException e){
                System.out.println(ANSI_RED + "Type a valid "+arr[1]+" id");
                errorSound.playSound();
            }catch(NullPointerException e){
                System.out.println(ANSI_RED + "That "+arr[1]+" id does not exist");
                errorSound.playSound();
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println(ANSI_RED + "That is not a valid command");
                errorSound.playSound();
            }catch(InvalidObjectException e){
                System.out.println(ANSI_RED + "That is not a valid object");
                errorSound.playSound();
        }
    }

    public SalesRep newSalesRep (String name){

        SalesRep salesRep=new SalesRep(name);

        salesRepRepository.save(salesRep);
        System.out.println(ANSI_GREEN + "New Sales Rep created!!\n"+salesRep);
        yuhuSound.playSound();
        return salesRep;
    }

    //Receives the user input and creates Lead with automatic ID,
    // it also receives the Opportunities list to store the new one
    public  Lead newLead (String name, String phone,
                                String email,
                                String compName,
                                SalesRep salesRep){

        Lead lead = new Lead(name, phone, email, compName, salesRep);
        leadRepository.save(lead);
        System.out.println(ANSI_GREEN + "New lead created!!\n"+lead);
        return lead;

    }

    //Receives the lead info and creates Contact
    public  Contact createContact(Lead lead){
        String name = lead.getName();
        String phoneNumber = lead.getPhoneNumber();
        String email = lead.getEmail();
        String companyName = lead.getCompanyName();
        return contactRepository.save(new Contact(name, phoneNumber, email, companyName));
    }

    //Receives the user input, product and Contact and creates Opportunity with automatic ID,
    // it also receives the Opportunities list to store the new one
    public  Opportunity createOpportunity(Product product,int quantity,Contact decisionMaker, SalesRep salesRep){

        Opportunity opportunity = new Opportunity(product, quantity, decisionMaker, salesRep);
        return opportunityRepository.save(opportunity);
    }

    //Receives the user input, industry and Opportunity and creates Account
    public  Account createAccount(Industry industry,
                                        int numOfEmployees,
                                        String city,
                                        String country,
                                        Contact contact,
                                        Opportunity opportunity){
        Account account = accountRepository.save(new Account(industry, numOfEmployees, city, country, contact,opportunity)) ;
        System.out.println(ANSI_GREEN + "Account created!!\n");
        return account;
    }

    //Receives id of the Lead and the Lead list and erases
    public void removeLead(Lead lead){
        leadRepository.delete(lead);
    }

    public void show(String objectType) throws InvalidObjectException {
        switch (objectType) {
            case "lead", "leads" -> {
                List<Lead> leadList = leadRepository.findAll();
                if (leadList.size() > 0) {
                    for (Lead lead : leadList) {
                        System.out.println(lead + "\n");
                    }
                    bipSound.playSound();
                } else {
                    System.out.println(ANSI_RED + "There are no leads at the moment");
                    errorSound.playSound();
                }
            }
            case "opportunity", "opportunities" -> {
                List<Opportunity> opportunityList = opportunityRepository.findAll();
                if (opportunityList.size() > 0) {
                    for (Opportunity opportunity : opportunityList) {
                        System.out.println(opportunity + "\n");
                    }
                    bipSound.playSound();
                } else {
                    System.out.println(ANSI_RED + "There are no opportunities at the moment");
                    errorSound.playSound();
                }
            }
            case "contact", "contacts" -> {
                List<Contact> contactList = contactRepository.findAll();
                if (contactList.size() > 0) {
                    for (Contact contact : contactList) {
                        System.out.println(contact + "\n");
                    }
                    bipSound.playSound();
                } else {
                    System.out.println(ANSI_RED + "There are no contacts at the moment");
                    errorSound.playSound();
                }
            }
            case "account", "accounts" -> {
                List<Account> accountList = accountRepository.findAll();
                if (accountList.size() > 0) {
                    for (Account account : accountList) {
                        System.out.println(account + "\n");
                    }
                    bipSound.playSound();
                } else {
                    System.out.println(ANSI_RED + "There are no accounts at the moment");
                    errorSound.playSound();
                }
            }
            case "sales", "salesrep", "salesreps" -> {
                List<SalesRep> salesRepList = salesRepRepository.findAll();
                if (salesRepList.size() > 0) {
                    for (SalesRep salesRep : salesRepList) {
                        System.out.println(salesRep + "\n");
                    }
                    bipSound.playSound();
                } else {
                    System.out.println(ANSI_RED + "There are no sales reps at the moment");
                    errorSound.playSound();
                }
            }
            default -> throw new InvalidObjectException(ANSI_RED + "Invalid object type");
        }
    }


    public Object lookup(String objectType, String inputId) throws InvalidObjectException {
        Integer id = Integer.parseInt(inputId);
        if (id < 0){
            throw new NumberFormatException();
        }

        switch (objectType) {
            case "salesrep", "sales" -> {
                SalesRep salesRep = salesRepRepository.findBySalesRepId(id);
                System.out.println(salesRep.toString());
                return salesRep;
            }
            case "lead" -> {
                Lead lead = leadRepository.findByLeadId(id);
                System.out.println(lead.toString()); // id lead doesn't exist, it will throll NullPointerException
                return lead;
            }
            case "opportunity" -> {
                Opportunity opportunity = opportunityRepository.findByOpportunityId(id);
                System.out.println(opportunity.toString());
                return opportunity;
            }
            case "contact" -> {
                Contact contact = contactRepository.findByContactId(id);
                System.out.println(contact.toString());
                return contact;
            }
            case "account" -> {
                Account account = accountRepository.findByAccountId(id);
                System.out.println(account.toString());
                return account;
            }
            default -> throw new InvalidObjectException(ANSI_RED + "Invalid object type");
        }
    }

    //Change opportunity status, receives opportunity id and List
    public void closeOpportunity(String closeType, String id){
        //checking for invalid id
        Checker.checkValidId(id);
        Integer opportunityId = Integer.parseInt(id);
        Opportunity opportunity = opportunityRepository.findByOpportunityId(opportunityId);

        //checking for null opportunity
        if (opportunity == null){
            throw new NullPointerException();
        }

        //status will be changed if it's not already set to closed (that makes sense, right?)
        if (opportunity.getStatus() == Status.CLOSED_LOST){
            System.out.println(ANSI_RESET + "Opportunity was already closed-lost");
        }else if (opportunity.getStatus() == Status.CLOSED_WON){
            System.out.println(ANSI_RESET + "Opportunity was already closed-won");
        }else{
            switch (closeType) {
                case "close-won" -> {
                    opportunity.setStatus(Status.CLOSED_WON);
                    System.out.println(opportunity);
                    System.out.println(ANSI_GREEN + "Opportunity closed-won");
                    opportunityRepository.save(opportunity);
                }
                case "close-lost" -> {
                    opportunity.setStatus(Status.CLOSED_LOST);
                    System.out.println(opportunity);
                    System.out.println(ANSI_GREEN + "Opportunity closed-lost");
                    opportunityRepository.save(opportunity);
                }
            }
        }
    }

    public void reportOptions(String[] arr) throws InvalidObjectException {
        List<Object[]> resultList;
        switch (arr[3]){
            case "salesrep":
            case "sales":
                switch (arr[1]){
                    case "lead":
                        resultList = leadRepository.countBySalesRep();
                        System.out.println(ANSI_BLUE + "Leads created by SalesRep:");
                        break;
                    case "opportunity":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerSalesRep();
                        System.out.println(ANSI_BLUE + "Opportunities created by sales rep");
                        break;
                    case "closed-lost":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.CLOSED_LOST.toString());
                        System.out.println(ANSI_BLUE + "Opportunities CLOSED-LOST by sales rep");
                        break;
                    case "closed-won":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.CLOSED_WON.toString());
                        System.out.println(ANSI_BLUE + "Opportunities CLOSED-WON by sales rep");
                        break;
                    case "open":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.OPEN.toString());
                        System.out.println(ANSI_BLUE + "Opportunities currently OPEN by sales rep");
                        break;
                    default:
                        throw new InvalidObjectException(ANSI_RED + "Invalid object type");
                }
                if (resultList.size()>0){
                    for (Object[] o: resultList) {
                        System.out.println("SalesRep " + o[0] + ": " + o[1]);
                    }
                }else{
                    System.out.println(ANSI_RED + "No results");
                }

                break;
            case "product":
                switch (arr[1]){
                    case "opportunity":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerProduct();
                        System.out.println(ANSI_BLUE + "Number of products ordered by each opportunity");
                        break;
                    case "closed-lost":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.CLOSED_LOST);
                        System.out.println(ANSI_BLUE + "Number of products ordered by each lost opportunity");
                        break;
                    case "closed-won":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.CLOSED_WON);
                        System.out.println(ANSI_BLUE + "Number of products ordered by each won opportunity");
                        break;
                    case "open":
                        resultList = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.OPEN);
                        System.out.println(ANSI_BLUE + "Number of products ordered by each open opportunity");
                        break;
                    default:
                        throw new InvalidObjectException(ANSI_RED + "Invalid object type");
                }
                if (resultList.size()>0){
                    for (Object[] o: resultList) {
                        System.out.println("Product: " + o[0] + ", opportunities: " + o[1]);
                    }
                }else{
                    System.out.println(ANSI_RED + "No results");
                }
                break;
            case "city":
                switch (arr[1]){
                    case "opportunity":
                        List<Object[]> opByCity = opportunityRepository.findNumberOfOpportunitiesPerCity();
                        for (Object[] o: opByCity) {
                            System.out.println("The city " + o[0] + " appears in " + o[1] + " opportunities.");
                        }
                        break;
                    case "closed-lost":
                        List<Object[]> opByCityCL = opportunityRepository.findNumberOfOpportunitiesPerCityWithStatus(Status.CLOSED_LOST);
                        for (Object[] o: opByCityCL) {
                            System.out.println("The city " + o[0] + " appears in " + o[1] + " lost opportunities.");
                        }
                        break;
                    case "closed-won":
                        List<Object[]> opByCityCW = opportunityRepository.findNumberOfOpportunitiesPerCityWithStatus(Status.CLOSED_WON);
                        for (Object[] o: opByCityCW) {
                            System.out.println("The city " + o[0] + " appears in " + o[1] + " won opportunities.");
                        }
                        break;
                    case "open":
                        List<Object[]> opByCityO = opportunityRepository.findNumberOfOpportunitiesPerCityWithStatus(Status.OPEN);
                        for (Object[] o: opByCityO) {
                            System.out.println("The city " + o[0] + " appears in " + o[1] + " open opportunities.");
                        }
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            case "country":
                switch (arr[1]){
                    case "opportunity":
                        List<Object[]> opByCountry = opportunityRepository.findNumberOfOpportunitiesPerCountry();
                        for (Object[] o: opByCountry) {
                            System.out.println("The country " + o[0] + " appears in " + o[1] + " opportunities.");
                        }
                        break;
                    case "closed-lost":
                        List<Object[]> opByCountryCL = opportunityRepository.findNumberOfOpportunitiesPerCountryWithStatus(Status.CLOSED_LOST);
                        for (Object[] o: opByCountryCL) {
                            System.out.println("The country " + o[0] + " appears in " + o[1] + " lost opportunities.");
                        }
                        break;
                    case "closed-won":
                        List<Object[]> opByCountryCW = opportunityRepository.findNumberOfOpportunitiesPerCountryWithStatus(Status.CLOSED_WON);
                        for (Object[] o: opByCountryCW) {
                            System.out.println("The country " + o[0] + " appears in " + o[1] + " won opportunities.");
                        }
                        break;
                    case "open":
                        List<Object[]> opByCountryW = opportunityRepository.findNumberOfOpportunitiesPerCountryWithStatus(Status.OPEN);
                        for (Object[] o: opByCountryW) {
                            System.out.println("The country " + o[0] + " appears in " + o[1] + " open opportunities.");
                        }
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            case "industry":
                switch (arr[1]){
                    case "opportunity":
                        List<Object[]> opByIndustry = opportunityRepository.findNumberOfOpportunitiesPerIndustry();
                        for (Object[] o: opByIndustry) {
                            System.out.println(o[1] + " opportunities refer to " + o[0] + " industry.");
                        }
                        break;
                    case "closed-lost":
                        List<Object[]> opByIndustryCL = opportunityRepository.findNumberOfOpportunitiesPerIndustryWithStatus(Status.CLOSED_LOST);
                        for (Object[] o: opByIndustryCL) {
                            System.out.println(o[1] + " lost opportunities refer to " + o[0] + " industry.");
                        }
                        break;
                    case "closed-won":
                        List<Object[]> opByIndustryCW = opportunityRepository.findNumberOfOpportunitiesPerIndustryWithStatus(Status.CLOSED_WON);
                        for (Object[] o: opByIndustryCW) {
                            System.out.println(o[1] + " won opportunities refer to " + o[0] + " industry.");
                        }
                        break;
                    case "open":
                        List<Object[]> opByIndustryO = opportunityRepository.findNumberOfOpportunitiesPerIndustryWithStatus(Status.OPEN);
                        for (Object[] o: opByIndustryO) {
                            System.out.println(o[1] + " opportunities refer to " + o[0] + " industry.");
                        }
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            default:
                throw new InvalidObjectException("Invalid object type");
        }
    }

    public void statistics() throws InvalidObjectException{

        System.out.println(ANSI_BLUE + "Choose between Max, Min, Median and Mean \nfrom EmployeeCount, Quantity and Opps per account");
        Scanner scanner= new Scanner(System.in);
        String response=scanner.nextLine().toLowerCase().trim();
        String[] arr = response.split(" ");

        switch (arr[0]){

            case "min":
                switch (arr[1]){
                    case "employeecount":
                        Object[] employeeCount= accountRepository.findMinEmployeeCount();
                        System.out.println("The minimun employee count is " + employeeCount[0]);

                        break;
                    case"quantity":
                        List <Object[]> products=opportunityRepository.findMinQuantityGroupByProduct();
                        for (Object[] o: products) {
                            System.out.println("The minimun order of " + o[0] + " is "+o[1]);
                        }

                        break;
                    case "opps":
                    case "opportunities":
                        Object[] opps= opportunityRepository.findMinOpportunitiesByAccountId();
                        System.out.println("The minimun number of opportunities is " + opps[0]);
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
               break;
            case "max":
                switch (arr[1]){
                    case "employeecount":
                        Object[] employeeCount= accountRepository.findMaxEmployeeCount();
                        System.out.println("The maximum employee count is " + employeeCount[0]);
                        break;
                    case"quantity":
                        List <Object[]> products=opportunityRepository.findMaxQuantityGroupByProduct();
                        for (Object[] o: products) {
                            System.out.println("The maximum order of " + o[0] + " is "+o[1]);
                        }
                        break;
                    case "opps":
                    case "opportunities":
                        Object[] opps = opportunityRepository.findMaxOpportunitiesByAccountId();
                        System.out.println("The maximum number of opportunities is " + opps[0]);
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            case "median":
                switch (arr[1]){
                    case "employeecount":
                        List<Object[]> employeeCount= accountRepository.orderEmployeeCount();
                        System.out.println("The median of employee count is: " + median(employeeCount));
                        break;
                    case"quantity":
                        Product[] products={Product.BOX, Product.HYBRID, Product.FLATBED};
                        for (Product productType: products) {
                            List<Object[]> quantities = opportunityRepository.findOrderedQuantity(productType.toString());
                            System.out.println("The median of "+productType+" is: " + median(quantities));
                        }
                        break;
                    case "opps":
                    case "opportunities":
                        List<Object[]> oppsCount = opportunityRepository.findOrderOpportunitiesByAccountId();
                        ;
                        System.out.println("The median of opportunities per account is: " + median(oppsCount));
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            case "mean":
            case "avg":
                switch (arr[1]){
                    case "employeecount":
                        Object[] employeeCount= accountRepository.findMeanEmployeeCount();
                        System.out.println("The average employee count is " + employeeCount[0]);
                        break;
                    case"quantity":
                        List <Object[]> products=opportunityRepository.findAvgQuantityGroupByProduct();
                        for (Object[] o: products) {
                            System.out.println("The average order of " + o[0] + " is "+o[1]);
                        }
                        break;
                    case "opps":
                        Object[] opps= opportunityRepository.findAvgOpportunitiesByAccountId();
                        System.out.println("The average number of opportunities is " + opps[0]);
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            default:
                throw new InvalidObjectException("Invalid object type");
        }


    }



}