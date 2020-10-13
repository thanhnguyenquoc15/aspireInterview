package com.frame;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.lib.BrowserFactory;

public class RegisterPage {

	// contrustor
	public RegisterPage(WebDriver dr) {
		BrowserFactory.driver = dr;
	}

	private WebDriver driver = BrowserFactory.driver;

	CommonFunction funcObj = new CommonFunction();

	// Define logger
	protected Logger log = Logger.getLogger(this.getClass().getName());

	// Page Objects
	@FindBy(xpath = "//input[@type=\"text\"]")
	public WebElement username;

	@FindBy(xpath = "//*[@role='presentation']")
	public WebElement selectCountry;

	@FindBy(xpath = "//button[@type='submit']")
	public WebElement submitBtn;

	@FindBy(xpath = "//span[contains(text(),'Verify')]")
	public WebElement verifyBtn;

	@FindBy(xpath = "//a[contains(text(),'Register')]")
	public WebElement registerBtn;

	@FindBy(xpath = "//*[contains(text(),'Login with email')]")
	public WebElement loginByEmail;

	@FindBy(xpath = "//*[contains(text(),'Login with phone')]")
	public WebElement loginByPhone;

	@FindBy(xpath = "//*[contains(text(),'Continue')]")
	public WebElement continueBtn;

	@FindBy(xpath = "//input[@data-cy='register-person-name']")
	public WebElement personalNameTextBox;

	@FindBy(xpath = "//input[@data-cy='register-person-email']")
	public WebElement emailAddressTextBox;

	@FindBy(xpath = "//input[@data-cy='register-person-phone']")
	public WebElement phoneNumberTextBox;

	@FindBy(xpath = "//input[@data-cy='register-person-heard-about']")
	public WebElement referalDropBox;

	@FindBy(xpath = "//input[@data-cy='register-person-heard-about-details']")
	public WebElement referalDetails;

	@FindBy(xpath = "//div[@role='checkbox']")
	public WebElement agreeCheckBox;

	/**
	 * description: Method login using phone number and hard code OTP
	 * 
	 * @param country       of phone and phone number
	 * @param verifyMessage is the error message that Web page should display
	 *                      errorCode @throws
	 */
	public void loginWithPhone(String country, String phoneNumber, String verifyMessage) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

		log.info("Select Country for Phone Number");
		selectCountry.click();
		String selectedCountry = "//div[contains(@class,'q-item') and contains(text(),'" + country + "')]";
		driver.findElement(By.xpath(selectedCountry)).click();
		log.info("Fill in Phone Number");
		driver.findElement(By.xpath("//input[@name='phone']")).sendKeys(phoneNumber);
		log.info("Click Login");
		this.submitBtn.click();
		if (verifyMessage.length() > 1) {
			log.info("Verify error message");
			try {
//				The selected phone is invalid.
//				Registered phone number must be at least 11 characters.
//				Registered phone number may not be greater than 15 characters.
				driver.findElement(By.xpath("//div[contains(text(),'" + verifyMessage + "')]"));
//			} catch (ElementNotVisibleException e) {
			} catch (Exception e) {
				Assert.assertTrue(false, "There is no error message show up");
				return;
			}
		}
		WebElement OTPToken = driver.findElement(By.xpath("//input[contains(@data-cy,'digit-input-pin')]"));
		log.info("Fill in OTP and click Login");
		String simulateOTP = "123456";
		OTPToken.click();
		Actions action = new Actions(driver);
		action.sendKeys(simulateOTP).perform();
		this.verifyBtn.click();
	}

	/**
	 * description: Method login using Email Address number and hard code OTP @param
	 * errorCode @throws
	 */
	public void loginWithEmail(String emailAddress, String verifyMessage) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Actions action = new Actions(driver);
		this.loginByEmail.click();
		log.info("Fill in Email Address");
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys(emailAddress);
		log.info("Click Login");
		this.submitBtn.click();
		if (verifyMessage.length() > 1) {
			log.info("Verify error message");
			try {
//				Registered email address is required.
//				The selected email is invalid.
//				Registered email address must be a valid email address.
				driver.findElement(By.xpath("//div[contains(text(),'" + verifyMessage + "')]"));
//				} catch (ElementNotVisibleException e) {
			} catch (Exception e) {
				Assert.assertTrue(false, "There is no error message show up");
				return;
			}
		}
		log.info("Input OTP");
		this.getNfillOTP();
		this.verifyBtn.click();
	}

	/**
	 * description: Method to Register new user account @param personalName @param
	 * country @param phoneNumber @param emailAddress @param Referral @throws
	 */
	public void registerAccount(String personalName, String country, String phoneNumber, String emailAddress,
			String referral) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Actions action = new Actions(driver);
		log.info("Select Country" + country + " for Phone Number");
		selectCountry.click();
		String selectedCountry = "//div[contains(@class,'q-item') and contains(text(),'" + country + "')]";
		driver.findElement(By.xpath(selectedCountry)).click();
		log.info("Fill " + phoneNumber + " in Phone Number");
		driver.findElement(By.xpath("//input[@name='phone']")).sendKeys(phoneNumber);
		log.info("Click Register Linktext");
		this.registerBtn.click();
		log.info("Fill in Personal Name + Email Address + Referal");
		this.personalNameTextBox.sendKeys(personalName);
		this.emailAddressTextBox.sendKeys(emailAddress);

		String verify = this.phoneNumberTextBox.getAttribute("value");
		Assert.assertTrue(verify.contains(phoneNumber),
				"Register page doesn't display Phone Number that already fill in previous page");

		// input Referral should like this format "Referral|REFERCODE" even with blank
		// details "Referral| "
		String[] referralData = referral.split("\\|");
//		funcObj.selectDropDownOption(driver, referalDropBox, referralData[0]);
		this.referalDropBox.click();
		this.referalDropBox.clear();
		this.referalDropBox.sendKeys(referralData[0]);
		funcObj.wait(1);
		driver.findElement(By.xpath("//div[contains(text(), '" + referralData[0] + "')]")).click();
		funcObj.wait(2);
		try {
			if (this.referalDetails.isDisplayed()) {
				this.referalDetails.sendKeys(referralData[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Agree to User Agreement and Continue");
		this.agreeCheckBox.click();
		funcObj.wait(1);
		this.continueBtn.click();
		funcObj.wait(2);
		try {
			driver.findElement(By.xpath("//div[contains(text(),'Account exists, try login instead!')]"));
			log.info("error message show up ===> Account exists, try login instead! ");
			// i[@role='img'] exit btn
			// span[@class='block' and contains(text(),'Login')] login btn
			return;
		} catch (Exception e) {
			log.info("Account doesn't exitsts");
		}

		log.info("Input OTP");
		this.getNfillOTP();
		driver.findElement(By.xpath("//span[@class='block' and contains(text(),'Verify')]")).click();
		funcObj.wait(5);

		try {
			driver.findElement(By.xpath("//*[contains(text(),'Wohoo!')]"));
			driver.findElement(By.xpath(
					"//*[contains(text(),'You have successfully verified your phone number. You’re on to a great start!')]"));
//			Assert.assertEquals(actual, expected, message);
		} catch (Exception e) {
			Assert.assertTrue(false, "some error happen there is no verification Page");
		}
		funcObj.wait(1);
		this.continueBtn.click();

	}

	/**
	 * description: Method to register personal Details @param birthDate @param
	 * Gender @param nationality @param emailAddress @param @throws
	 */
	public void registerPersonalDetails(String personalName, String nationality, String phoneNumber,
			String emailAddress, String birthDate, String gender, String registerPurpose, String verifyErrorMessage) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		// Navigate to Personal register Page
		try {
			driver.findElement(By.xpath("//div[contains(text(),'Is your business incorporated in Singapore?')]"));
//		} catch (ElementNotVisibleException e) {
		} catch (Exception e) {
			// Navigate codes
		}
		driver.findElement(By.xpath("//div[contains(text(),'Is your business incorporated in Singapore?')]"));
		log.info("Select Business Registered");
		driver.findElement(By.xpath("//*[contains(text(),'business is registered')]/../..//span[@class='block']"))
				.click();
		funcObj.wait(3);
		List<WebElement> detailsInfo = driver.findElements(By.xpath("//div[@role='checkbox']"));

		// save summary Personal Details in to Infomation Table
		Hashtable<String, String> detailsInfoTable = new Hashtable<String, String>();

		for (WebElement Detail : detailsInfo) {
			String header = Detail.findElement(By.xpath(".//div[contains(@class,'q-anchor')]")).getText().trim();
			String value = Detail.getAttribute("aria-checked").toString();
			detailsInfoTable.put(header, value);
		}
		log.info("Click Get Started in \"Information needed\" Page");
		driver.findElement(By.xpath("//span[@class='block' and contains(text(),'Get Started')]")).click();
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Name Field");
		if (detailsInfoTable.get("Full name").toString().equals("true")) {
			log.info("Verify if personal Name is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='person-edit-name']//div[2]")).getText()
					.toString();
			Assert.assertTrue(verify.contains(personalName), "Personal Name is not correct");
		} else if (detailsInfoTable.get("Full name").toString().equals("false")) {
			log.info("Input Personal Name into text box");
			driver.findElement(By.xpath("//*[@label='Your personal name']//input")).sendKeys(personalName);
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Phone Field");
		if (detailsInfoTable.get("Mobile phone").toString().equals("true")) {
			log.info("Verify if Mobile Phone is displayed correctly");
			// +84 need to handle later
			String phoneHeader = driver.findElement(By.xpath("//*[@data-cy='person-edit-phone']//div[2]")).getText()
					.toString();
			// +84xxxxxxxxxx
			String phoneNumb = driver.findElement(By.xpath("//*[@data-cy='person-edit-phone']")).getAttribute("value")
					.toString();
			Assert.assertTrue(phoneNumb.contains(phoneNumber), "Mobile Phone is not correct");
		} else if (detailsInfoTable.get("Mobile phone").toString().equals("false")) {
			log.info("Input Mobile Phone into text box");
			driver.findElement(By.xpath("//*[@label='Phone number']//input")).sendKeys(phoneNumber);
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Email address Field");
		if (detailsInfoTable.get("Email address").toString().equals("true")) {
			log.info("Verify if Mobile Phone is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='person-edit-email']//div[2]")).getText()
					.toString().toLowerCase();
			Assert.assertTrue(emailAddress.toLowerCase().contains(verify), "Email address is not correct");
		} else if (detailsInfoTable.get("Email address").toString().equals("false")) {
			log.info("Input Email address into text box");
			driver.findElement(By.xpath("//*[@label='Email address']//input")).sendKeys(emailAddress);
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Date of birth Field");
		// input data for birthDate must be (if day < 10 then "d-Mmm-yyyy" else
		// "dd-Mmm-yyyy")

		if (detailsInfoTable.get("Date of birth").toString().equals("true")) {
			log.info("Verify if Date of birth is displayed correctly");
			// need to fix this xpath
			String verify = driver.findElement(By.xpath("")).toString();
			// need to check format of this birth Date
			Assert.assertTrue(verify.contains(birthDate), "Date of birth is not correct");
		} else if (detailsInfoTable.get("Date of birth").toString().equals("false")) {
			log.info("Input Date of birth address into text box");
			funcObj.selectDate(driver, birthDate);
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Nationality Field");
		if (detailsInfoTable.get("Nationality").toString().equals("true")) {
			log.info("Verify if Nationality is displayed correctly");
			// need to fix this xpath
			String verify = driver.findElement(By.xpath("")).toString();
			Assert.assertTrue(verify.contains(nationality), "Nationality is not correct");
		} else if (detailsInfoTable.get("Nationality").toString().equals("false")) {
			log.info("Input Nationality into text box");
			driver.findElement(By.xpath("//*[@label='Nationality']//input")).sendKeys(nationality);

			driver.findElement(By.xpath("//div[contains(text(), '" + nationality + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Gender Field");
		if (detailsInfoTable.get("Gender").toString().equals("true")) {
			log.info("Verify if Gender is displayed correctly");
			// need to fix this xpath
			String verify = driver.findElement(By.xpath("//*[@data-cy='kyc-gender']//div[2]")).getText().toString();
			Assert.assertTrue(verify.contains(gender), "Gender is not correct");
		} else if (detailsInfoTable.get("Gender").toString().equals("false")) {
			log.info("Input Gender into text box");
			driver.findElement(By.xpath("//*[@label='Gender']//input")).click();
			driver.findElement(By.xpath("//div[contains(text(), '" + gender + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Input Register Purpose Field");
		driver.findElement(By.xpath("//input[@data-cy='person-edit-purpose']")).click();
		driver.findElement(By.xpath("//div[contains(text(), '" + registerPurpose + "')]")).click();
		funcObj.wait(1);
		this.submitBtn.click();
		//some time verify Emails OTP display here, sometime not
		try {
			log.info("Fill in OTP and click Verify");
			this.getNfillOTP();
			driver.findElement(By.xpath("//span[@class='block' and contains(text(),'Verify')]")).click();
			funcObj.wait(2);

			try {
				driver.findElement(By.xpath("//*[contains(text(),'Hurray!')]"));
				driver.findElement(By.xpath("//*[contains(text()," + "'You have successfully verified your email.']"));
//				Assert.assertEquals(actual, expected, message);
			} catch (ElementNotVisibleException e) {
			} catch (Exception e) {
				Assert.assertTrue(false, "some error happen there is no verification Page");
			}
			this.continueBtn.click();

		} catch (Exception e) {
			log.info("There is no Email OTP display");
		}

		
	}

	/**
	 * description: Method to register personal Details @param birthDate @param
	 * Gender @param nationality @param emailAddress @param
	 * 
	 * @throws
	 */
	public void registerBusinessDetails(String businessName, String registrationType, String UEN, String role,
			String businessCountry, String industry, String subIndustry, String verifyErrorMessage) {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Actions action = new Actions(driver);
		try {
			driver.findElement(By.xpath("//div[contains(@class,'text-weight') and contains(text(),'Business')]"));
//		} catch (ElementNotVisibleException e) {
		} catch (Exception e) {
			// Navigate codes
		}
		driver.findElement(By.xpath("//div[contains(@class,'text-weight') and contains(text(),'Business')]"));

		List<WebElement> detailsInfo = driver.findElements(By.xpath("//div[@role='checkbox']"));
		Hashtable<String, String> detailsInfoTable = new Hashtable<String, String>();

		for (WebElement Detail : detailsInfo) {
			String header = Detail.findElement(By.xpath(".//div[contains(@class,'q-anchor')]")).getText().trim();
			String value = Detail.getAttribute("aria-checked").toString();
			detailsInfoTable.put(header, value);
		}

		log.info("Click Continue in \"Information needed\" Page");
		this.continueBtn.click();
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Business Name Field");
		if (detailsInfoTable.get("Business Name").toString().equals("true")) {
			log.info("Verify if Business Name is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-name']//div[2]")).toString();
			Assert.assertTrue(verify.contains(businessName), "Business Name is not correct");
		} else if (detailsInfoTable.get("Business Name").toString().equals("false")) {
			log.info("Input Business Name into text box");
			driver.findElement(By.xpath("//*[@label='Business Name']//input")).sendKeys(businessName);
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Registration Type Field");
		if (detailsInfoTable.get("Registration Type").toString().equals("true")) {
			log.info("Verify if Registration Type is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-registration-type']//div[2]"))
					.toString();
			Assert.assertTrue(verify.contains(registrationType), "Registration Type is not correct");
		} else if (detailsInfoTable.get("Registration Type").toString().equals("false")) {
			log.info("Input Registration Type into text box");
			driver.findElement(By.xpath("//*[@label='Registration Type']//input")).sendKeys(registrationType);
			driver.findElement(By.xpath("//div[contains(text(), '" + registrationType + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Business Registration Number (UEN) Field");
		if (detailsInfoTable.get("Business Registration Number (UEN)").toString().equals("true")) {
			log.info("Verify if Business Registration Number (UEN) is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-registration-numer']//div[2]"))
					.toString();
			Assert.assertTrue(verify.contains(UEN), "Business Registration Number (UEN) is not correct");
		} else if (detailsInfoTable.get("Business Registration Number (UEN)").toString().equals("false")) {
			log.info("Input Business Registration Number (UEN) into text box");
			WebElement uenElement = driver
					.findElement(By.xpath("//*[@label='Business Registration Number (UEN)']//input"));
			if (verifyErrorMessage.equals("yes")) {
				log.info("Verify Invalid UEN error message");
				uenElement.sendKeys("InvalidUEN");
				driver.findElement(By.xpath("//div[@class='new-form__inputs']")).click();
				try {
					driver.findElement(
							By.xpath("//*[contains(text(), 'Business Registration Number (UEN) is invalid')]"));
//					} catch (ElementNotVisibleException e) {
				} catch (Exception e) {
					Assert.assertTrue(false, "Error Invalid UEN message doesn't display");
				}
				uenElement.clear();
				try {
					driver.findElement(
							By.xpath("//*[contains(text(), 'Business Registration Number (UEN) is required.')]"));
//				} catch (ElementNotVisibleException e) {
				} catch (Exception e) {

					Assert.assertTrue(false, "Error UEN is Required message doesn't display");
				}
			} else {
				log.info("Fill valid Business Registration Number (UEN)");
				driver.findElement(By.xpath("//*[@label='Business Registration Number (UEN)']//input")).sendKeys(UEN);
			}
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify What is your role? Field");
		if (detailsInfoTable.get("What is your role?").toString().equals("true")) {
			log.info("Verify if What is your role? is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='kyc-gender']//div[2]")).toString();
			Assert.assertTrue(verify.contains(role), "What is your role? is not correct");
		} else if (detailsInfoTable.get("What is your role?").toString().equals("false")) {
			log.info("Input What is your role? into text box");
			driver.findElement(By.xpath("//*[@label='What is your role?']//input")).sendKeys(role);
			driver.findElement(By.xpath("//div[contains(text(), '" + role + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Country Field");
		if (detailsInfoTable.get("Country").toString().equals("true")) {
			log.info("Verify if Country is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-country']//div[2]"))
					.toString();
			Assert.assertTrue(verify.contains(businessCountry), "Country is not correct");
		} else if (detailsInfoTable.get("Country").toString().equals("false")) {
			log.info("Input Country into text box");
			driver.findElement(By.xpath("//*[@label='Country']//input")).sendKeys(businessCountry);
			driver.findElement(By.xpath("//div[contains(text(), '" + businessCountry + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Industry Field");
		if (detailsInfoTable.get("Industry").toString().equals("true")) {
			log.info("Verify if Industry is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-industry']//div[2]"))
					.toString();
			Assert.assertTrue(verify.contains(industry), "Industry is not correct");
		} else if (detailsInfoTable.get("Industry").toString().equals("false")) {
			log.info("Input Industry into text box");
			driver.findElement(By.xpath("//*[@label='Industry']//input")).sendKeys(industry);
			driver.findElement(By.xpath("//div[contains(text(), '" + industry + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("Verify Sub Industry Field");
		if (detailsInfoTable.get("Sub Industry").toString().equals("true")) {
			log.info("Verify if Sub Industry is displayed correctly");
			String verify = driver.findElement(By.xpath("//*[@data-cy='register-business-sub-industry']//div[2]"))
					.toString();
			Assert.assertTrue(verify.contains(subIndustry), "Industry is not correct");
		} else if (detailsInfoTable.get("Sub Industry").toString().equals("false")) {
			log.info("Input Sub Industry into text box");
			driver.findElement(By.xpath("//*[@label='Sub Industry']//input")).sendKeys(subIndustry);
			driver.findElement(By.xpath("//div[contains(text(), '" + subIndustry + "')]")).click();
		}
		log.info("--------------------------------------------------------------------------------------------");
		log.info("--------------------------------------------------------------------------------------------");
		log.info("--------------------------------------------------------------------------------------------");
		this.submitBtn.click();
		funcObj.wait(5);
		try {
			Assert.assertTrue(driver.getTitle().contains("Verification completed"), "error happen");
			;
			driver.findElement(By.xpath("//title[contains(text(),'Verification completed | Aspire')]"));
			driver.findElement(By.xpath("//*[contains(text(),'You are amazing and you know it')]"));
			driver.findElement(By.xpath("//*[contains(text(),"
					+ "'You have successfully completed the KYC processs and we have received your documents.']"));
//			Assert.assertEquals(actual, expected, message);
//			} catch (ElementNotVisibleException e) {
		} catch (Exception e) {
			Assert.assertTrue(false, "some error happen there is no verification Page");
		}
		this.continueBtn.click();
	}

	/**
	 * description: pending @param @throws
	 */
	public void rateApsireExperience() {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	/**
	 * description: pending @param @throws
	 */
	public void verifyAccountIsUnderReviewed() {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	/**
	 * description: get OTP then fill in OTP Token @param @throws
	 */
	private void getNfillOTP() {
		log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		WebElement OTPToken = driver.findElement(By.xpath("//input[contains(@data-cy,'digit-input-pin')]"));
		log.info("Fill in OTP and click Login");
		String simulateOTP = "123456";
		OTPToken.click();
		Actions action = new Actions(driver);
		action.sendKeys(simulateOTP).perform();
	}

}
