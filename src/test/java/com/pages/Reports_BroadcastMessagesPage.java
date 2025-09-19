package com.pages;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.ExtentReport.Extentlogger;
import base.BaseClass;

public class Reports_BroadcastMessagesPage extends BaseClass {

	@FindBy(xpath = "//span[@class='menu-item-text' and text()='Reports/System Messages']/../../following::div")
	public WebElement Reports;
	@FindBy(xpath = "//span[@class='menu-item-text' and text()='Broadcast Messages']")
	public WebElement MenuListBroadcastMessages;

	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown;
	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown2;
	@FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation')]//ul[1]/li[contains(text(),'TCH')]")
	public WebElement TCHoption;

	@FindBy(xpath = "//div[contains(text(),'Select Broadcast Messages') and @role='combobox']")
	public WebElement selectBMdropdown;
	@FindBy(xpath = "//div[contains(text(),'Select Broadcast Messages') and @role='combobox']")
	public WebElement selectBMdropdown1;

	@FindBy(xpath = "//div[@id=contains(.,'menu')]/following::ul//following::li")
	public List<WebElement> reportBMlist;

	@FindBy(xpath = "//button[contains(text(),'Generate')]")
	public WebElement generatebtn;

	@FindBy(xpath = "//div[contains(text(),'Generated Successfully')]/..")
	public WebElement generatedAlert;

	@FindBy(xpath = "//div[@class='ant-modal-body']")
	public WebElement GenerateResultscren;

	@FindBy(css="span[role='progressbar']")
	public WebElement loader;

	@FindBy(xpath="//button[contains(@class,'MuiButtonBase-root MuiIconButton-root')]/*[@data-testid='FileDownloadIcon']")
	public WebElement resultsdownloadbtn;

	@FindBy(xpath = "//button[@aria-label='Close']")
	public WebElement resultsClosebtn;

	@FindBy(xpath = "//div[contains(text(),'No data to download')]")
	public WebElement noDataAlert;

	/* Fedwire locators */
	/* Open  and close type */
	@FindBy(xpath = "//div[contains(@class,'ant-modal-body')]//textarea")
	public WebElement generatedreportText;

	@FindBy(xpath = "//input[@class='participation-radio-item' and @id='4']")
	public WebElement ParticipantBroadcastRadiobtn;

	@FindBy(xpath = "//input[@class='participation-radio-item' and @id='4']")
	public WebElement FedwireBroadcastRadiobtn;

	/* ----------------------------------------------------------------------- */

	//CONSTRUCTOR
	public Reports_BroadcastMessagesPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	/*----------------------Start of Broadcast Messages- TCH Methods--------------------------------------------------------------------*/	

	public void TCHPBping()	
	{

		clickelementwithname(bmreport.generatebtn,"Clicking Generate button");

		visibleofele(driver, bmreport.generatedAlert, "Generated alert");

		visibleofele(driver, bmreport.GenerateResultscren, "Generated Results Screen");
		boolean loaderstatus = waitForLoaderToDisappear(bmreport.loader,30,2);

		if(loaderstatus)
		{			
			System.out.println(prop.getProperty("GeratedReportNoData"));
			if (generatedreportText.getText().equalsIgnoreCase(prop.getProperty("GeratedReportNoData"))) 
			{
				Extentlogger.fail("Generated Report Shows : "+generatedreportText.getText());
				clickelementwithname(bmreport.resultsdownloadbtn, "Results Download btn");
				visibleofele(driver,bmreport.noDataAlert, "Results no data alert");
				clickelementwithname(bmreport.resultsClosebtn, "Results Close btn");
			}	
			else {			
				clickelementwithname(bmreport.resultsdownloadbtn, "Results Download btn");
				Extentlogger.pass("Report is Generated, and able to download.",true);
				clickelementwithname(bmreport.resultsClosebtn, "Results Close btn");
			}
		}
	}
}

/*----------------------End of Fedwire Methods----------------------------------------------------------------------*/	
