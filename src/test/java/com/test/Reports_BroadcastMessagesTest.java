package com.test;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.pages.Reports_BroadcastMessagesPage;

import base.BaseClass;

public class Reports_BroadcastMessagesTest extends BaseClass{

	@Test
	public void BroadcastMessages_TCH_PB() throws InterruptedException
	{
		ExtentReport.createTest("Broadcast Messages TCH - Participant Broadcast Test");
		bmreport = new Reports_BroadcastMessagesPage(driver);

		scrollByVisibilityOfElement(driver, bmreport.Reports);

		visibleofele(driver, bmreport.MenuListBroadcastMessages, "Broadcast Messages");
		JSClick(driver, bmreport.MenuListBroadcastMessages, "Navigating to Broadcast Messages");
		
		
		// Get report types from config
		String[] BMTypeFromConfig = prop.getProperty("TCHBMType").split(",");

		for (String BMType: BMTypeFromConfig) {
			ExtentReport.createChildTest(BMType);
			// Step 1: Reopen the dropdown			
			clickelementwithname(bmreport.selectBMdropdown, "Opening BM dropdown");

			// Step 2: Re-fetch the list to avoid stale element
			List<WebElement> bmTypelist = bmreport.reportBMlist;

			boolean matchFound = false;
			for (WebElement webElement : bmTypelist) 
			{
				String BMTypeName = webElement.getText().trim();
				if (BMTypeName.equalsIgnoreCase(BMType.trim())) {
					// Step 3: Click matching element
					webElement.click();

					// Step 4: Call corresponding method
					switch (BMType.trim().toUpperCase()) {
					case "PING":
						bmreport.TCHPBping();
						break;
					default:
						System.out.println("No method defined for: " + BMType);
					}

					// Optional: refresh page if needed
					refresh();
					matchFound = true;
					break;
				}
			}

			if (!matchFound) 
				System.out.println("Report type not found in dropdown: " + BMType);
			ExtentReport.resetToParent();
		}
	}
	
}
