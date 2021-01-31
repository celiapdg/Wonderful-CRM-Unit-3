package com.ironhack.CRMunit3;
import com.ironhack.CRMunit3.repository.*;
import com.ironhack.CRMunit3.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

import static com.ironhack.CRMunit3.utils.Colors.*;

@SpringBootApplication
public class CrmUnit3Application implements CommandLineRunner {
	@Autowired
	SalesRepRepository salesRepRepository;
	@Autowired
	OpportunityRepository opportunityRepository;
	@Autowired
	LeadRepository leadRepository;
	@Autowired
	ContactRepository contactRepository;
	@Autowired
	AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(CrmUnit3Application.class, args);
	}

	@Override
	public void run(String... args) {
		//Create a Scanner to collect user input
		Scanner myScanner = new Scanner(System.in);

		System.out.println(ANSI_CYAN_BACKGROUND + ANSI_BLACK + "Welcome to the best CRM in the world");

		//set userInput to an empty string to enter the next loop
		String userInput="";

		Command command = new Command(salesRepRepository,  leadRepository, contactRepository,opportunityRepository, accountRepository);

		//This loops runs until the user chooses the exit option
		while (!userInput.equals("exit")){

			showMainMenu();
			// Get input from the user organized
			userInput = myScanner.nextLine()
					.toLowerCase()
					.trim();

			//Go to utils Command to the this method functionality
			command.commandReader(userInput);
		}



	}
	public static void showMainMenu(){
		System.out.println(ANSI_RESET + "What do you want to do?:");
		System.out.println("- new sales rep");
		System.out.println("- new lead");
		System.out.println("- convert " + ANSI_BOLD + "\\opportunityId\\" + ANSI_RESET);
		System.out.println("- show " + ANSI_BOLD + "\\objectInPlural\\" + ANSI_RESET);
		System.out.println("- lookup " + ANSI_BOLD + "\\object\\ \\id\\" + ANSI_RESET);
		System.out.println("- close-lost " + ANSI_BOLD + "\\opportunityId\\" + ANSI_RESET);
		System.out.println("- close-won " + ANSI_BOLD + "\\opportunityId\\" + ANSI_RESET);
		System.out.println("- report " + ANSI_BOLD + "\\object\\" + ANSI_RESET + " by " + ANSI_BOLD + "\\attribute\\" + ANSI_RESET);
		System.out.println("- exit");
	}
}
