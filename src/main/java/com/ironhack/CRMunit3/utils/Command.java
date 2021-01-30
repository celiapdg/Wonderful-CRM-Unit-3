package com.ironhack.CRMunit3.utils;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import com.ironhack.CRMunit3.repository.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
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

                    String answer=askNewAccount();

                    switch (answer){
                        case"n":
                            if (accountRepository.findAll().isEmpty()){
                                System.out.println((char)27 + "[31mThere is no account created yet, you must create one");
                            }else {
                                Integer accountId=askAccountId();
                                Account account=accountRepository.findByAccountId(accountId);
                                account.getOpportunityList().add(opportunity);
                                account.getContactList().add(contact);
                                accountRepository.save(account);
                                opportunity.setAccountId(account);
                                opportunityRepository.save(opportunity);
                                contact.setAccountId(account);
                                contactRepository.save(contact);
                            break;
                            }
                        case"y":

                            //go to utils ScanInfo to check how these work
                            Industry industry = askIndustry();
                            int numOfEmployees = askEmployees();
                            String city = askCity();
                            String country = askCountry();

                            //the next two methods are also below
                            Account account = createAccount(industry, numOfEmployees, city, country, contact, opportunity);
                            break;

                    }



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
        Account account=accountRepository.save(new Account(industry, numOfEmployees, city, country, contact,opportunity)) ;
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

}