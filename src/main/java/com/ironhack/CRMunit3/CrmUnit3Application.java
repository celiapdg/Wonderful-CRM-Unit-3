package com.ironhack.CRMunit3;

import com.ironhack.CRMunit3.enums.*;
import com.ironhack.CRMunit3.model.*;
import com.ironhack.CRMunit3.repository.*;
import com.ironhack.CRMunit3.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.*;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

import static com.ironhack.CRMunit3.utils.Command.*;

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

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		SpringApplication.run(CrmUnit3Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Create a Scanner to collect user input
		Scanner myScanner = new Scanner(System.in);

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
			Command command=new Command(salesRepRepository,  leadRepository, contactRepository,opportunityRepository, accountRepository);
			command.commandReader(userInput);
		}
		System.exit(0);
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
