package com.ironhack.CRMunit3.utils;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import com.ironhack.CRMunit3.repository.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;

import static com.ironhack.CRMunit3.utils.ScanInfo.*;

@Service
public class Command {


    public static Sound errorSound = new Sound("error.wav");
    public static Sound bipSound = new Sound("bip.wav");
    public static Sound exitSound = new Sound("exit.wav");


    SalesRepRepository salesRepRepository;
    LeadRepository leadRepository;
    ContactRepository contactRepository;
    OpportunityRepository opportunityRepository;
    AccountRepository accountRepository;

    public Command(SalesRepRepository salesRepRepository, LeadRepository leadRepository, ContactRepository contactRepository, OpportunityRepository opportunityRepository, AccountRepository accountRepository) {
        this.salesRepRepository = salesRepRepository;
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.opportunityRepository = opportunityRepository;
        this.accountRepository = accountRepository;
    }
    //method called in main
    public void commandReader(String userInput)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {

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
                                Integer salesRepId = askSalesRep();
                                SalesRep salesRep=salesRepRepository.findBySalesRepId(salesRepId);

                                //this method is defined below
                                newLead(name, phone, email, company, salesRep);
                                bipSound.playSound();
                            break;
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
                                System.out.println((char)27 + "[31mThere is no account created yet, you must create one");
                            }else {
                                Integer accountId=askAccountId();
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
                    System.out.println((char)27 + "[32mNew opportunity created!!\n"+opportunity);
                    bipSound.playSound();
                    break;
                case "show":
                    switch (arr[1]) {
                        //if the second word is leads enters here
                        case "leads":
                            //method below
                            showLeads();
                            break;
                        case "opportunities":
                            //if the second word is opportunities enters here
                            showOpportunities();
                            break;
                        case "sales":
                            //if the second word is opportunities enters here
                            showSalesReps();
                            break;
                        default:
                            //default to make sure every option is managed
                            System.out.println((char)27 + "[31mThat is not a valid command");
                            errorSound.playSound();
                    }
                    break;
                case "lookup":
                    switch (arr[1]) {
                        //if the second word is lead enters here
                        case "lead":
                            //method below
                            lookupLead(arr[2]);
                            break;

                        //if the second word is opportunity enters here
                        case "opportunity":
                            //method below
                            lookupOpportunity(arr[2]);
                            break;
                        default:
                            // AGAIN default to make sure every option is managed
                            System.out.println((char)27 + "[31mThat is not a valid command");
                            errorSound.playSound();
                    }
                    break;

                case "close-lost":
                    //method below
                    closeLost(arr[1]);
                    bipSound.playSound();
                    break;

                case "close-won":
                    //method below
                    closeWon(arr[1]);
                    bipSound.playSound();
                    break;

                case "report":
                    reportOptions(arr);
                    break;

                case "exit":
                    //ONLY COMMAND THAT EXITS THE APPLICATION
                    System.out.println((char)27 + "[46m" + (char)27 + "[30mThank you for using the best CRM in the world");
                    exitSound.playSound();
                    bipSound.closeSound();
                    errorSound.closeSound();
                    exitSound.closeSound();
                    break;

                default:
                    //if the first word is not equal to any of the above this comes up
                    System.out.println((char)27 + "[31mThat is not a valid command");
                }
            }catch(NumberFormatException e){
                System.out.println((char)27 + "[31mType a valid id");
                errorSound.playSound();
            }catch(NullPointerException e){
                System.out.println((char)27 + "[31mThat id does not exist");
                errorSound.playSound();
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println((char)27 + "[31mThat is not a valid command");
                errorSound.playSound();
            }
    }

    public SalesRep newSalesRep (String name){

        SalesRep salesRep=new SalesRep(name);

        salesRepRepository.save(salesRep);
        System.out.println((char)27 + "[32mNew Sales Rep created!!\n"+salesRep);
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
        System.out.println((char)27 + "[32mNew lead created!!\n"+lead);
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
        System.out.println((char)27 + "[32mAccount created!!\n");
        return account;
    }

    //Receives id of the Lead and the Lead list and erases
    public void removeLead(Lead lead){
        leadRepository.delete(lead);
    }


    public  void showSalesReps (){

        List<SalesRep> salesRepList = salesRepRepository.findAll();
//        If there are no leads left
        if (salesRepList.isEmpty()){
//            Out prints a message
            System.out.println((char)27 + "[31mYou don't have leads in this moment");
        }else {
            for (SalesRep salesRep : salesRepList) {
                System.out.println(salesRep);
                System.out.println("");
            }
        }
    }

    public  void showLeads (){

        List<Lead> leadList = leadRepository.findAll();
//        If there are no leads left
        if (leadList.isEmpty()){
//            Out prints a message
            System.out.println((char)27 + "[31mYou don't have leads in this moment");
        }else {
            for (Lead lead : leadList) {
                System.out.println(lead);
                System.out.println("");
            }
        }
    }


    public  void showOpportunities (){

        List<Opportunity> opportunityList = opportunityRepository.findAll();
        //If there are no opportunities yet
        if (opportunityList.isEmpty()){
//            Out prints a message
            System.out.println((char)27 + "[31mYou haven't created any opportunity yet");
        }else {
            for (Opportunity opportunity : opportunityList) {
                System.out.println(opportunity);
                System.out.println("");
            }
        }
    }

    // Takes the lead id and the lead List and shows its info
    public  void lookupLead (String id){
        //checking for invalid id
        Integer leadId = Integer.parseInt(id);
        if (leadId < 0){
            throw new NumberFormatException();
        }


        Lead lead = leadRepository.findByLeadId(leadId);
        //checking for null lead
        if (lead == null){
            throw new NullPointerException();
        }
        System.out.println(lead);
    }

    // Takes the lead id and the opportunity List and shows its info
    public  void lookupOpportunity (String id){
        //checking for invalid id
        Integer opportunityId = Integer.parseInt(id);
        if (opportunityId < 0){
            throw new NumberFormatException();
        }
        Opportunity opportunity = opportunityRepository.findByOpportunityId(opportunityId);

        //checking for null opportunity
        if (opportunity == null){
            throw new NullPointerException();
        }
        System.out.println(opportunity);
    }

    //Change opportunity status, receives opportunity id and List
    public  void closeLost (String id){
        //checking for invalid id
        Integer opportunityId = Integer.parseInt(id);
        if (opportunityId < 0){
            throw new NumberFormatException();
        }

        Opportunity opportunity = opportunityRepository.findByOpportunityId(opportunityId);

        //checking for null opportunity
        if (opportunity == null){
            throw new NullPointerException();
        }

        //status will be changed if it's not already set to closed-lost (that makes sense, right?)
        if (opportunity.getStatus() != Status.CLOSED_LOST){
            opportunity.setStatus(Status.CLOSED_LOST);
            opportunity.toString();
            System.out.println((char)27 + "[32mOpportunity closed-lost");
            opportunityRepository.save(opportunity);
        } else {
            System.out.println((char)27 + "[39mOpportunity was already closed-lost");
        }

    }

    //Change opportunity status, receives opportunity id and List
    public  void closeWon (String id){

        //checking for invalid id
        Integer opportunityId = Integer.parseInt(id);
        if (opportunityId < 0){
            throw new NumberFormatException();
        }

        Opportunity opportunity = opportunityRepository.findByOpportunityId(opportunityId);

        //checking for null opportunity
        if (opportunity == null){
            throw new NullPointerException();
        }

        //status will be changed if it's not already set to closed-won (that makes sense, right?)
        if (opportunity.getStatus() != Status.CLOSED_WON){
            opportunity.setStatus(Status.CLOSED_WON);
            opportunity.toString();
            System.out.println((char)27 + "[32mOpportunity closed-won");
            opportunityRepository.save(opportunity);
        } else {
            System.out.println((char)27 + "[39mOpportunity was already closed-won");
        }
    }

    public void reportOptions(String[] arr) throws InvalidObjectException {
        switch (arr[3]){
            case "salesrep":
                switch (arr[1]){
                    case "lead":
                        List<Object[]> leadBySalesRep = leadRepository.countBySalesRep();
                        for (Object[] o: leadBySalesRep) {
                            System.out.println("SalesRep " + o[0] + " has created " + o[1] + " leads.");
                        }
                        break;
                    case "opportunity":
                        List<Object[]> opBySalesRep = opportunityRepository.findNumberOfOpportunitiesPerSalesRep();
                        for (Object[] o: opBySalesRep) {
                            System.out.println("SalesRep " + o[0] + " has created " + o[1] + " opportunities.");
                        }
                        break;
                    case "closed-lost":
                        List<Object[]> opByStatusCL = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.CLOSED_LOST);
                        for (Object[] o: opByStatusCL) {
                            System.out.println("SalesRep " + o[0] + " has lost " + o[1] + " opportunities.");
                        }
                        break;
                    case "closed-won":
                        List<Object[]> opByStatusCW = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.CLOSED_WON);
                        for (Object[] o: opByStatusCW) {
                            System.out.println("SalesRep " + o[0] + " has won " + o[1] + " opportunities.");
                        }
                        break;
                    case "open":
                        List<Object[]> opByStatusO = opportunityRepository.findNumberOfOpportunitiesPerSalesRepWithStatus(Status.OPEN);
                        for (Object[] o: opByStatusO) {
                            System.out.println("SalesRep " + o[0] + " has " + o[1] + " opportunities open.");
                        }
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
                }
                break;
            case "product":
                switch (arr[1]){
                    case "opportunity":
                        List<Object[]> opByProduct = opportunityRepository.findNumberOfOpportunitiesPerProduct();
                        for (Object[] o: opByProduct) {
                            System.out.println("The product " + o[0] + " is ordered in " + o[1] + " opportunities.");
                        }
                        break;
                    case "closed-lost":
                        List<Object[]> opByProductCL = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.CLOSED_LOST);
                        for (Object[] o: opByProductCL) {
                            System.out.println("The product " + o[0] + " is ordered in " + o[1] + " lost opportunities.");
                        }
                        break;
                    case "closed-won":
                        List<Object[]> opByProductCW = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.CLOSED_WON);
                        for (Object[] o: opByProductCW) {
                            System.out.println("The product " + o[0] + " is ordered in " + o[1] + " won opportunities.");
                        }
                        break;
                    case "open":
                        List<Object[]> opByProductO = opportunityRepository.findNumberOfOpportunitiesPerProductWithStatus(Status.OPEN);
                        for (Object[] o: opByProductO) {
                            System.out.println("The product " + o[0] + " is ordered in " + o[1] + " open opportunities.");
                        }
                        break;
                    default:
                        throw new InvalidObjectException("Invalid object type");
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

}