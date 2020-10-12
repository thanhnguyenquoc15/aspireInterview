package com.scripts;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.lib.BrowserFactory;
import com.lib.ReadData;

public abstract class HelperClass {
		
	protected Logger log = Logger.getLogger(this.getClass().getName());
	
    public abstract void performBeforeMethodOperation();
    public abstract void performAfterMethodOperation();

    protected ReadData ReadData;
    protected BrowserFactory BrowserFactory;
    
	public static WebDriver driver; 
	
	@BeforeSuite
	public void beforeSuite(ITestContext context){
	log.debug("Entering into Method : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	log.info("in @BeforeSuite"); 
	
//	EPLoginPage loginPage = PageFactory.initElements(driver, EPLoginPage.class);
//	loginPage.loginEP(ReadData.EPUser,ReadData.EPPass);
	HelperClass.driver = BrowserFactory.getDriver(ReadData.BROWSER);

	}
	

	 
	@BeforeClass
	public void beforeClass(){
	log.info("in @BeforeClass");
	}
	 
//	@BeforeMethod(alwaysRun = true)
	@BeforeMethod
	public void beforeMethodClass() {
		log.info("in @BeforeMethod");
		performBeforeMethodOperation();
		try {
			driver.get("https://" + ReadData.TestURL);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	 
	@AfterMethod
	public void close()
	{
	log.info("in @AfterMethod");	
	driver.manage().deleteAllCookies();
	}

	 
	@AfterClass
	public void afterClass(){
	log.info("in @AfterClass");
	}
//	 
	@AfterSuite
	public void afterSuite() throws IOException{
	log.info("in @AfterSuite"); 
	driver.quit();
	}
//	
	
	
	
	
}
