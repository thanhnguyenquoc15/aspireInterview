package com.frame;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class CommonFunction {

	// Define Logger and driver
	protected Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 This method is used to select value from dropdown list.
	 *
	 * @param   driver Selenium WebDriver object
	 * @param   element Dropdown/Combo box object
	 * @param   sItem Item to select from dropdown
	 *
	 * @return  String - returns selected value from dropdown .
	 *
	 */
	public boolean selectDropDownOption(WebDriver driver, WebElement element, String sItem)
	{
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		try
		{
			Select selectedOption = new Select(element);
			//selectedOption.selectByValue(sItem);
			selectedOption.selectByVisibleText(sItem);
			return true;
		} catch (Exception e)
		{
			//new ExceptionHandler(e,Thread.currentThread().getStackTrace()[1].getMethodName());
			return false;
		}
	}
	
	/**
	 * This method is used to wait execution or process till user defined time.
	 *
	 * @param secs time specified in seconds
	 * @return Noting .
	 *
	 */
	public void wait(int secs)
	{
		// call a native sleep
		try
		{
			log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
			log.info("Adding wait for: " + secs + " seconds..");
			Thread.sleep(secs * 1000);
		} catch (Exception ex)
		{
			log.error("Exception occurred in {createDIR} method...");
		}
	}
	
	/**
	 * This method generate simulated UEN.
	 *
	 * @param secs time specified in seconds
	 * @return UEN in String .
	 *
	 */
	public String generateSimulateUEN()
	{
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//		do {
		int min = 10000000;
		int max = 999999999;
//		log.info("Random value in double from "+min+" to "+max+ ":");
		int random_int = (int)(Math.random() * (max - min + 1) + min);
		String firstHalf = String.valueOf(random_int);
		
		String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		min = 0;
		max = abc.length();
//		log.info("Random value in double from "+min+" to "+max+ ":");
		random_int = (int)(Math.random() * (max - min + 1) + min);		
		char letter = abc.charAt(random_int);
		
		String fullUEN = firstHalf + letter;
//		} while(fullUEN not in database); 
		return fullUEN;
		
	}
	
	/**
	 * This method is used to wait execution or process till user defined time.
	 *
	 * @param secs time specified in seconds
	 * @return Noting .
	 *
	 */
	public void selectDate(WebDriver driver, String selectedDate)
	{
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		// input data for birthDate must be (if day < 10 then "d-Mmm-yyyy" else "dd-Mmm-yyyy")
		String[] date = selectedDate.split("-");
		if (date[0].charAt(0) == '0') {
			date[0] = date[0].replaceAll("0", "");
		}
		String day = "//span[@class='block' and text() = '" + date[0] + "']";
		String month = "//span[@class='block' and text() = '" + date[1] + "']";
		String year = "//span[@class='block' and text() = '" + date[2] + "']";
		//this can be data driven
		WebElement selectDate = driver.findElement(By.xpath("//input[contains(@placeholder,'Set your date')]"));
		selectDate.click();
		
		WebElement calendar = driver.findElement(By.xpath("//div[@class='q-date__view q-date__calendar']"));
		List<WebElement> monthNyear = calendar.findElements(By.xpath(".//*[contains(@class,'flex')]//button"));
//		log.info("Select Year");
		monthNyear.get(1).click();
			int i = 0;
			while (i < 5) {
				try {
					driver.findElement(By.xpath(year)).click();
					break;
//				} catch (ElementNotVisibleException e) {
				} catch (Exception e) {
					//roll back 20 years
					driver.findElement(By.xpath("//div[@class='col-auto'][1]")).click();
					i++;
				}
			}
		//add try catch SlateElementException	
		calendar = driver.findElement(By.xpath("//div[@class='q-date__view q-date__calendar']"));	
		monthNyear = calendar.findElements(By.xpath(".//*[contains(@class,'flex')]//button"));
//		log.info("Select Month");
		monthNyear.get(0).click();
		this.wait(2);
		driver.findElement(By.xpath(month)).click();
		this.wait(2);
		//	log.info("Select Day");
		driver.findElement(By.xpath(day)).click();

	
	}
	

	
	/**
	 * This method generate simulated phone.
	 *
	 * @param secs time specified in seconds
	 * @return  in String .
	 *
	 */
	public String randomPhoneNumber()
	{
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
//		do {
		int min = 100000000;
		int max = 999999999;
		int random_int = (int)(Math.random() * (max - min + 1) + min);
		String phoneNumber = String.valueOf(random_int);
		return phoneNumber;
	}
	
	/**
	 * This method generate a random string.
	 *
	 * @param secs time specified in seconds
	 * @return  in String .
	 *
	 */
	public String randomString()
	{
		
		int min = 0;
		int max = 10000;
		int random_int = (int)(Math.random() * (max - min + 1) + min);
		String randomnumber = String.valueOf(random_int);
		String abc = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		min = 0;
		max = abc.length() - 1;
		random_int = (int)(Math.random() * (max - min + 1) + min);		
		char letter = abc.charAt(random_int);
		String result = randomnumber + letter;
		return result;
	}
	
	
	
	
}
