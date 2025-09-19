package com.test;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.pages.BatchPage;

import base.BaseClass;

public class MultiBatchsTest extends BaseClass{

	@Test
	public void BatchsRun() throws InterruptedException
	{
		ExtentReport.createTest("Batchs Run");
		batchpage=new BatchPage(driver);

		// Get report types from config
		String[] BatchPaymentServiceFromConfig = prop.getProperty("BatchPaymentService").split(",");

		for (String ServiceTypeName : BatchPaymentServiceFromConfig) {

			ExtentReport.createChildTest(ServiceTypeName);
			// Step 1: Reopen the dropdown

			clickelementwithname(batchpage.ServiceTypeDropdown,"Service Type Drop down");

			
			 {
				clickelementwithname(batchpage.TCHOption,"Fedwire select from list");
			}

			clickelementwithname(batchpage.tabbatch,"Switch to Batch tab");

			// Get Batch names from config
			String[] BatchNamesFromConfig = prop.getProperty(ServiceTypeName).split(",");

			JSClick(driver,batchpage.batchfiletoggle, "Changing toggle to Batch file");

			for (String BTnames: BatchNamesFromConfig ) {
				System.out.println(BTnames);
				//			batchpage.tabsearch.sendKeys(Keys.CLEAR);
				batchpage.tabsearch.sendKeys(Keys.CONTROL + "a");
				batchpage.tabsearch.sendKeys(Keys.DELETE);

				sendkeys(batchpage.tabsearch,BTnames);

				JSClick(driver,batchpage.chkbatchcheckbox,"Check box select");
				Thread.sleep(1000);
			}
			clickelementwithname(batchpage.btnplaybutton, "Run btn");
			refresh();
		}
	}

}
