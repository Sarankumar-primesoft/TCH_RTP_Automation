package com.pages;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.ExtentReport.Extentlogger;
import base.BaseClass;

public class Reports_ParticpantFilePage extends BaseClass {

	@FindBy(xpath = "//span[@class='menu-item-text' and text()='Reports/System Messages']/../../following::div")
	public WebElement Reports;
	@FindBy(xpath = "//span[@class='menu-item-text' and text()='TCH Participant File']")
	public WebElement MenuListParticipantFile;

	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown;
	@FindBy(xpath = "//div[contains(@class,'MuiPaper-root MuiPaper-elevation')]//ul[1]/li[contains(text(),'TCH')]")
	public WebElement TCHoption;

	@FindBy(xpath = "//button[contains(text(),'Generate')]")
	public WebElement generatebtn;
	@FindBy(xpath = "//button[contains(text(),'Generate')]")
	public WebElement generatebtn1;

	@FindBy(xpath = "//div[contains(text(),'Generated Successfully')]/..")
	public WebElement generatedAlert;

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

	/* ----------------------------------------------------------------------- */

	//CONSTRUCTOR
	public Reports_ParticpantFilePage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	/*----------------------Start of Participant File- TCH Methods--------------------------------------------------------------------*/	

	public void TCHParticipantfile()	
	{

		clickelementwithname(PFreport.paymentservicedropdown, "Payment service dropdown");
		JSClick(driver,PFreport.TCHoption, "TCH Payment service.");

		clickelementwithname(PFreport.generatebtn,"Generate TCH participant file btn");
		visibleofele(driver, PFreport.generatedAlert, "Generated alert");

		visibleofele(driver, PFreport.GenerateResultscren, "Generated Results Screen");
		boolean loaderstatus = waitForLoaderToDisappear(PFreport.loader,30,2);

		if(loaderstatus)
		{			
			System.out.println(prop.getProperty("GeratedReportNoData"));
			if (generatedreportText.getText().equalsIgnoreCase(prop.getProperty("GeratedReportNoData"))) 
			{
				Extentlogger.fail("Generated Report Shows : "+generatedreportText.getText());
				clickelementwithname(PFreport.resultsdownloadbtn, "Results Download btn");
				visibleofele(driver,PFreport.noDataAlert, "Results no data alert");
				clickelementwithname(PFreport.resultsClosebtn, "Results Close btn");
			}	
			else {			
				clickelementwithname(PFreport.resultsdownloadbtn, "Results Download btn");
				Extentlogger.pass("Report is Generated, and able to download.",true);
				clickelementwithname(PFreport.resultsClosebtn, "Results Close btn");
			}
		}
	}
}
