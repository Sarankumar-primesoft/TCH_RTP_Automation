package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BaseClass;

public class BatchPage extends BaseClass {
	

	@FindBy(xpath = "(//div[@id='demo-simple-select-autowidth'])[1]")
	public WebElement ServiceTypeDropdown;
	@FindBy(xpath = "//li[text()='TCH']")
	public WebElement TCHOption;
	@FindBy(xpath = "//span[text()='Batches']")
	public WebElement tabbatch;
	
	@FindBy(xpath = "//div[text()='My Batches']/../span//input")
	public WebElement batchfiletoggle;
	
	@FindBy(xpath = "(//input[contains(@placeholder,'Search here')])[1]")
	public WebElement tabsearch;
	@FindBy(xpath = "(//input[@type='checkbox'])[2]")
	public WebElement chkbatchcheckbox;
	@FindBy(xpath = "(//input[@type='checkbox'])[2]")
	public WebElement chkbatchcheckbox1;
	@FindBy(xpath = "//button[@variant='contained']")
	public WebElement btnplaybutton;
	@FindBy(xpath = "//div[text()='Batch executed successfully']")
	public WebElement lblBatchsuccMsg;
	
	
	public BatchPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	


}
