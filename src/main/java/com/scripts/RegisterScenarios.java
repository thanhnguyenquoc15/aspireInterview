package com.scripts;

import java.io.IOException;
import java.text.ParseException;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.frame.RegisterPage;
import com.lib.ReadData;

public class RegisterScenarios extends HelperClass{

	protected RegisterPage RegisterPageObj;
	
	@Override
	public void performBeforeMethodOperation() {
		// TODO Auto-generated method stub
		RegisterPageObj = PageFactory.initElements(driver, RegisterPage.class);
		
		Logger log = Logger.getLogger(this.getClass().getName());
	}

	@Override
	public void performAfterMethodOperation() {
		// TODO Auto-generated method stub
		
	}

	@Test
	(dataProvider = "dataMap", dataProviderClass = ReadData.class)
	public void basicSuccessFlow(Hashtable testData) throws Exception,
    													  ParseException,
    													  IOException 
	{
		log.info("Test Data is: "+ testData);
		String personalName = testData.get("Personal Name").toString();
		String country = testData.get("Country").toString();
//		String phoneNumber = testData.get("Phone Number").toString();
		
		
		int min = 100000000;
		int max = 999999999;
		int random_int = (int)(Math.random() * (max - min + 1) + min);
		String phoneNumber = String.valueOf(random_int);
		 min = 0;
		 max = 10000;
		 random_int = (int)(Math.random() * (max - min + 1) + min);
		String randomnumber = String.valueOf(random_int);
		String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		min = 0;
		max = abc.length();
//		log.info("Random value in double from "+min+" to "+max+ ":");
		random_int = (int)(Math.random() * (max - min + 1) + min);		
		char letter = abc.charAt(random_int);
		
		
		String emailAddress = randomnumber + testData.get("Email Address").toString() + letter;
//		input Referral should like this format "ReferralType|REFERCODE" even with blank details "Referral| "
		String referral = testData.get("Referral").toString();
		String businessName = testData.get("Business Name").toString();
		
		
		log.info("Test case Start");
		log.info("---------------------------------------------------------------------");
		log.info("start register");
//		try {
			
		log.info("Start Register Account");
		RegisterPageObj.registerAccount(personalName,
										country,
										phoneNumber,
										emailAddress,
										referral);
		
		log.info("Start Register Personal Details for Account");
		RegisterPageObj.registerPersonalDetails(personalName, 
												testData.get("Nationality").toString(), 
												phoneNumber, 
												emailAddress, 
												testData.get("Date Of Birth").toString(), 
												testData.get("Gender").toString(), 
												testData.get("Register Purpose").toString(), 
												testData.get("Personal Error Message").toString());
		
		log.info("Start Register Business Details for Account");
		RegisterPageObj.registerBusinessDetails(businessName, 
												testData.get("Registration Type").toString(),
												testData.get("UEN").toString(),
												testData.get("Business Role").toString(),
												testData.get("Business Country").toString(), 
												testData.get("Industry").toString(),  
												testData.get("Sub Industry").toString(), 
												testData.get("Business Error Message").toString());
		
		log.info("Test case End");
		log.info("---------------------------------------------------------------------");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (testData.get("CleanUp").equals("yes")) {
//				log.info("Clean Up Register Accout in Database");
//				//code here
//			}
//			//other system clean up	
//			
//		}
		
		
		
	}
	
	
	
	
}
