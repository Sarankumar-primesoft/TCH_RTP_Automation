package com.pages;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.ExtentReport.Extentlogger;

import base.BaseClass;

public class ExecutionsSummaryPage extends BaseClass {
	
	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement paymentservicedropdown;
	@FindBy(xpath = "//li[text()='TCH']")
	public WebElement TCHOption;

	@FindBy(xpath = "//span[text()='Executions']")
	public WebElement ExecutionsTab;
	
	@FindBy(xpath = "//span[contains(text(),'Test Execution')]")
	public WebElement TestExecutionTab;

	@FindBy(xpath = "//div[text()='Batch Files']/../span//input")
	public WebElement batchfiletoggle;
	
	@FindBy(xpath = "(//tbody[@class='ant-table-tbody']//td[contains(@class,'ant-table-row')]//following::td[2])[1]")
	public WebElement batchNameClmn;
	
	@FindBy(xpath = "(//tbody[@class='ant-table-tbody']//td[contains(@class,'ant-table-row')]//following::td[7])[1]")
	public WebElement resultsclmn;

	@FindBy(xpath = "//*[name()='svg' and @data-testid='RefreshIcon']")
	public WebElement refreshbtn;

	@FindBy(xpath = "(//tbody[@class='ant-table-tbody']//td[contains(@class,'ant-table-row')]//following::td[2])[1]")
	public WebElement executedBatchName;
	
	@FindBy(xpath = "//tbody[@class='ant-table-tbody']//td[contains(@class,'ant-table-row')]//following::td[7]")
	public WebElement BatchResultsStatus;

	@FindBy(xpath = "//div[contains(@class,'MuiTooltip-tooltip')]/span/p")
	public List<WebElement> tooltip;

	@FindBy(xpath = "(//tbody[@class='ant-table-tbody']//td[contains(@class,'ant-table-row')]//following::td[8])[1]/span")
	public WebElement downloadicon;

	
	public ExecutionsSummaryPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	
}
