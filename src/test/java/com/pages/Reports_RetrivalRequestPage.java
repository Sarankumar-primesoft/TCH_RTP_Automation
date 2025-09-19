package com.pages;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ExtentReport.ExtentReport;
import com.ExtentReport.Extentlogger;
import com.aventstack.extentreports.ExtentTest;

import base.BaseClass;

public class Reports_RetrivalRequestPage extends BaseClass {

	@FindBy(xpath = "//span[@class='menu-item-text' and text()='Reports/System Messages']/../../following::div")
	public WebElement Reports;
	@FindBy(xpath = "//span[@class='menu-item-text' and contains(text(),'Retrieval Request')]")
	public WebElement MenuListRetrivalRequest;
	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown;
	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown3;
	@FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation')]//ul[1]/li[contains(text(),'TCH')]")
	public WebElement TCHoption;
	@FindBy(xpath = "//p[contains(text(),'Select Message Type')]//following::div[contains(.,'Select Message Type') and @role='combobox']")
	public WebElement SelectmessagesTypeDropdown;
	@FindBy(xpath = "//p[contains(text(),'Select Messages')]//following::div[contains(.,'Select Message Type') and @role='combobox']")
	public WebElement SelectmessageDropdown;

	@FindBy(xpath = "//p[contains(text(),'Select Messages')]")
	public WebElement closedropdownusingLabel;

	@FindBy(xpath = "//p[contains(text(),'Select Message Type')]//following::li")
	public List<WebElement> MessageTypelist;

	@FindBy(xpath = "//p[contains(text(),'Select Messages')]//following::li")
	public List<WebElement> Messageslist;

	@FindBy(xpath = "//button[contains(text(),'View Message')]")
	public WebElement viewMessageBtn;

	@FindBy(xpath = "(//div[contains(@class,'ant-table-container')]//following::input)[2]")
	public WebElement messagesScreenCheckbox;

	@FindBy(xpath = "//button[contains(text(),'Retrieve Message')]")
	public WebElement RetrieveMessageBtn;

	@FindBy(xpath = "//p[contains(text(),'Admi.006 - Retrieval Request')]")
	public WebElement RetrievalRequestMessagesScreenHeader;

	@FindBy(xpath = "//button[contains(text(),'Back')]")
	public WebElement BackBtnRetrieveMessage;

	@FindBy(xpath = "//div[contains(text(),'Retrieve Message Executed Successfully')]/..")
	public WebElement RetrieveMsgExecutedAlert;

	@FindBy(xpath = "//div[@class='ant-modal-body']")
	public WebElement GenerateResultscren;

	@FindBy(xpath = "//div[contains(@class,'ant-modal-body')]//textarea")
	public WebElement generatedreportText;

	@FindBy(css="span[role='progressbar']")
	public WebElement loader;

	@FindBy(xpath="//button[contains(@class,'MuiButtonBase-root MuiIconButton-root')]/*[@data-testid='FileDownloadIcon']")
	public WebElement resultsdownloadbtn;

	@FindBy(xpath = "//button[@aria-label='Close']")
	public WebElement resultsClosebtn;

	@FindBy(xpath = "//div[contains(text(),'No data to download')]")
	public WebElement noDataAlert;

	@FindBy(xpath = "//input[@placeholder='Start date']")
	public WebElement Startdate;
	@FindBy(xpath = "//input[@placeholder='End date']")
	public WebElement Enddate;
	@FindBy(xpath = "//div[contains(text(),'Select Routing Number')]")
	public WebElement routingNumber;
	@FindBy(xpath = "//ul[@role='listbox']/li")
	public List<WebElement> routingNumbervalueinput;

	/* Fedwire locators */

	@FindBy(xpath = "//div[contains(text(),'Select Flow Type')]")
	public WebElement FlowType;

	@FindBy(xpath = "//div[contains(@id,'menu')]//ul//following::li")
	public List<WebElement> FlowTypevalue;

	@FindBy(xpath = "//input[contains(@id,'from')]")
	public WebElement seqFromNum;

	@FindBy(xpath = "//input[contains(@id,'to')]")
	public WebElement seqToNum;

	@FindBy(xpath = "//p[contains(text(),'Enter Message ID')]//following::input")
	public WebElement MessageId;


	/* ----------------------------------------------------------------------- */

	//CONSTRUCTOR
	public Reports_RetrivalRequestPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	/*----------------------Satrt of TCH Methods--------------------------------------------------------------------*/	
	/* TCH Flow Methods */

	public void TCHRRreport() throws InterruptedException	
	{
		selectoptionfromList(SelectmessageDropdown, Messageslist, prop.getProperty("TCHMessage"));

		JSClick(driver,RRreport.viewMessageBtn,"Clicking View Message button");

		visibleofele(driver, RRreport.RetrievalRequestMessagesScreenHeader, "Retrieval Request Messages Screen Header");

		Thread.sleep(1000);
		JSClick(driver,RRreport.messagesScreenCheckbox, "Selecting checkbox on Messages Screen.");

		scrollByVisibilityOfElement(driver,RetrieveMessageBtn);
		clickelementwithname(RetrieveMessageBtn,"Retrieve Message Btn");

		visibleofele(driver, RRreport.RetrieveMsgExecutedAlert, "Executed alert");

		visibleofele(driver, RRreport.GenerateResultscren, "Generated Results Screen");

		boolean loaderstatus = RRreport.waitForLoaderToDisappear(RRreport.loader,30,2);

		if(loaderstatus)
		{
			String generateddata= generatedreportText.getText();
			if (generateddata.equalsIgnoreCase(prop.getProperty("GeratedReportNoData"))) 
			{
				Extentlogger.fail("Generated Report Shows : "+generatedreportText.getText());
				clickelementwithname(RRreport.resultsdownloadbtn, "Results Download btn");
				visibleofele(driver,RRreport.noDataAlert, "Results no data alert");
				clickelementwithname(RRreport.resultsClosebtn, "Results Close btn");

			}	
			else {			
				clickelementwithname(RRreport.resultsdownloadbtn, "Results Download btn");
				Extentlogger.pass("Report is Generated, and able to download.",true);
				clickelementwithname(RRreport.resultsClosebtn, "Results Close btn");
				scrollByVisibilityOfElement(driver,BackBtnRetrieveMessage);
				clickelementwithname(BackBtnRetrieveMessage,"Retrieve Message Back Btn");
			}
		}	
	}
	/*----------------------End of TCH Methods----------------------------------------------------------------------*/	

	}

