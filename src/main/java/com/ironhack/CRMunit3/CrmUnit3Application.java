package com.ironhack.CRMunit3;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

import static com.ironhack.CRMunit3.utils.Command.*;

@SpringBootApplication
public class CrmUnit3Application implements CommandLineRunner {

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		SpringApplication.run(CrmUnit3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Create a Scanner to collect user input
		Scanner myScanner = new Scanner(System.in);

		//Create two lists to store both leads and opportunities
		HashMap<Integer, Lead> leadList=new HashMap<>();
		HashMap<Integer, Opportunity> opportunityList=new HashMap<>();
		HashMap<Integer, SalesRep> repList=new HashMap<>();

		//We create two Leads and opportunities for starters so you can easily test every command
//		leadList.put(0, new Lead("Ana Campos",
//				"647321563",
//				"ana@email.com",
//				"Transportes Campos S.L."));
//		leadList.put(1, new Lead("Carlos Botijo",
//				"647321593",
//				"carlos@email.com",
//				"Transportes Botijo S.L."));

//		Contact contact1 = createContact(leadList.get(0));
//		Contact contact2 = createContact(leadList.get(1));
//
//		createOpportunity(Product.BOX, 35, contact1, opportunityList);
//		createOpportunity(Product.FLATBED, 64, contact2, opportunityList);

		System.out.println((char)27 + "[46m" + (char)27 + "[30mWelcome to the best CRM in the world");

		//set userInput to an empty string to enter the next loop
		String userInput="";

		//This loops runs until the user chooses the exit option
		while (!userInput.equals("exit")){

			showMainMenu();
			// Get input from the user organized
			userInput = myScanner.nextLine()
					.toLowerCase()
					.trim();

			//Go to utils Command to the this method functionality
			commandReader(userInput, leadList, opportunityList, repList);
		}


	}
	public static void showMainMenu(){
		System.out.println((char)27 + "[49m" + (char)27 + "[39mWhat do you want to do?:");
		System.out.println("- new sales rep");
		System.out.println("- new lead");
		System.out.println("- convert \\id\\");
		System.out.println("- show opportunities");
		System.out.println("- show leads");
		System.out.println("- lookup opportunity \\id\\");
		System.out.println("- lookup lead \\id\\");
		System.out.println("- close-lost \\id\\");
		System.out.println("- close-won \\id\\");
		System.out.println("- exit");
	}
}
