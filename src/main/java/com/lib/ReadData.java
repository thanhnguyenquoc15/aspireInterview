package com.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.openqa.selenium.WebDriver;

public class ReadData {


	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;

	// SuitePath
	public static final String SUITE_PATH = System.getProperty("user.dir");
	

	public static final String userDir = System.getProperty("user.dir");

	public static final String testDataDir = "\\src\\main\\java\\com\\data\\";
		
	private static final String EXCELFILELOCATION = SUITE_PATH + testDataDir + "TestCaseData\\";
	
	private static final String DEFAULTPROPERTIES = SUITE_PATH + testDataDir +"PropertiesFile\\default.properties";
	private static final String PROPERTIESFILE = SUITE_PATH + testDataDir + "PropertiesFile\\" + getPropertyFile();
	
	public static final String TestURL = readPropertyValue("TestURL");
	
	public static final String User = readPropertyValue("User");
	
	public static final String Pass = readPropertyValue("Pass");
	
	public static final String BROWSER = readPropertyValue("BROWSER");


	// SSH Detail

	public static final String User_SSH = readPropertyValue("User_SSH");
	public static final String Pass_SSH = readPropertyValue("Pass_SSH");
	
	// Log4j property file location
    public static String LOGGER_PROPERTY_FILE = SUITE_PATH + "src\\main\\java\\com\\data\\PropertiesFile\\log4j.properties";

 // Log Folder Path
 	public static String LOG_FOLDER_PATH = SUITE_PATH + "\\log\\";
	// Navigate Web element browser
	
	public static WebDriver WEBDRIVER;
	
	// Installer path 
	
	public static String INSTALLER_PATH = SUITE_PATH + "src\\main\\java\\installer\\EPM\\";
	
	
	
	public static void loadExcelFile(String fileName, String sheetName) throws IOException
	{		
		// Import excel sheet.
		File src=new File(EXCELFILELOCATION + fileName);
		// Load the file.
		FileInputStream finput = new FileInputStream(src);
		// Load the workbook
		workbook = new XSSFWorkbook(finput);
		// Load the sheet in which data is stored.
		sheet = workbook.getSheet(sheetName);
		finput.close();	
	}
	// read data from excel file 
	@DataProvider(name = "dataMap")
	private static Object[][] dataReader(ITestContext context) throws Exception
	{
		String dataFile  = context.getCurrentXmlTest().getParameter("dataFile");
		String dataSheet = context.getCurrentXmlTest().getParameter("dataSheet");
		if(sheet == null)
		{
			loadExcelFile(dataFile,dataSheet);
		}		
		int lastRowNum  = sheet.getLastRowNum();
		int lastCellNum = sheet.getRow(0).getLastCellNum();
		Object[][] myDataMap = new Hashtable[lastRowNum][1];
		
// Read full excel data file		
		for (int i = 0; i < lastRowNum; i++)
		{
			Map<Object, Object> datamap = new Hashtable<Object, Object>();
			for (int j = 0;j < lastCellNum; j++) {
				try
				{
				datamap.put(sheet.getRow(0).getCell(j).toString(), sheet.getRow(i+1).getCell(j).toString());				
				}
				catch (Exception e)
				{
				datamap.put(sheet.getRow(0).getCell(j).toString(), "");	
				}
			}
			myDataMap[i][0] = datamap;			
		}		
		return myDataMap;
	}
	


	
	// Read data from Properties file using Value
	public static String readPropertyValue(String sKey)
	{
		String sValue;
		FileInputStream fs = null;
		try
		{
			fs = new FileInputStream(PROPERTIESFILE);
			Properties objConfigOther = new Properties();
			objConfigOther.load(fs);
			sValue = objConfigOther.getProperty(sKey);
		} catch (Exception e)
		{
			sValue = null;
		} finally
		{
			try
			{
				if (fs != null)
				{
					fs.close();
				}
			} catch (Exception e)
			{
			}
		}
		return sValue;
	}
	
	private static String getPropertyFile()
	{
		String sValue;
		FileInputStream fs = null;
		try
		{
			fs = new FileInputStream(DEFAULTPROPERTIES);
			Properties objConfigOther = new Properties();
			objConfigOther.load(fs);
			sValue = objConfigOther.getProperty("PROPERTY_FILE");
		} catch (Exception e)
		{
			sValue = null;
		} finally
		{
			try
			{
				if (fs != null)
				{
					fs.close();
				}
			} 
			catch (Exception e)
			{
			}
		}
		return sValue;
	}


	
}
