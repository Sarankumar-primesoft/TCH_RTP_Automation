package com.test;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.ExtentReport.Extentlogger;
import com.pages.ExecutionsSummaryPage;

import IndividualFlows.TCHBatchTest;
import base.BaseClass;

public class ExecutionsSummaryTest extends BaseClass{

	public String expectedBatchName;
	@Test
		public void TCHBatchExecutionSummaryResults() throws InterruptedException {
		    ExtentReport.createTest("TCH Batch Execution Summary Results");
		    exectuionpage = new ExecutionsSummaryPage(driver);        
		    
		    scrollByVisibilityOfElement(driver, exectuionpage.ExecutionsTab);
		    clickelementwithname(exectuionpage.TestExecutionTab, "Navigation to Test Execution Tab");
		    
		    JSClick(driver, exectuionpage.batchfiletoggle, "Changing toggle to Batch file");
	    
	        visibleofele(driver, exectuionpage.paymentservicedropdown, "Payment service dropdown");
	        clickelementwithname(exectuionpage.paymentservicedropdown, "Payment service dropdown");
	        clickelementwithname(exectuionpage.TCHOption, "TCH select from list");
		    
		    
		    expectedBatchName = prop.getProperty("TCHBatchName");
		    String actualBatchName = exectuionpage.batchNameClmn.getText().trim();
	
		    for(int i=0; i<5; i++) {
		        if (actualBatchName.equals(expectedBatchName)) {
		            Extentlogger.pass("Expected Batch Name is Equals to Actual Batch Name. Expected : "+expectedBatchName+", Actual Batch Name : "+actualBatchName, true);
		            break;
		        } else {
		            clickelement(exectuionpage.refreshbtn);
		            Thread.sleep(1000); // Small wait after refresh
		            actualBatchName = exectuionpage.batchNameClmn.getText().trim();
		            System.out.println("Actual Batch Name : "+actualBatchName);
		        }
		        if (!actualBatchName.equals(expectedBatchName)) {
		            Extentlogger.fail("Failed to find expected batch after 5 attempts. Actual Batch Name: " + actualBatchName);
		            return; // Exit if we didn't find the right batch
		        }
		    }
		    System.out.println(TCHBatchTest.TCHstartTime);
		    System.out.println(((System.currentTimeMillis() -TCHBatchTest.TCHstartTime)/1000)/60);
		    
		    // Wait for pending test cases to become 0
		   
		    long maxWaitTime = 10 * 60 * 1000; // 10 minutes in milliseconds
		    boolean pendingZero = false;
		    
		    while (System.currentTimeMillis() -TCHBatchTest.TCHstartTime < maxWaitTime) {
		        // Hover over results column to make tooltip visible
		        actions.moveToElement(exectuionpage.resultsclmn).perform();
		        Thread.sleep(1000); // Wait for tooltip to appear
		        
		        try {
		            // Get the tooltip element that contains the pending count
		            WebElement tooltipElement = driver.findElement(By.xpath("//div[contains(@class,'MuiTooltip-tooltip')]/span/p[4]"));
		            String tooltipText = tooltipElement.getText();
		            System.out.println("Tooltip text: " + tooltipText);
		            
		            // Check if pending count is 0
		            if (tooltipText.contains("Test cases Pending: 0")) {
		                pendingZero = true;
		                Extentlogger.pass(((System.currentTimeMillis() -TCHBatchTest.TCHstartTime)/1000)/60+" -minutes taken to complete.");
		                Extentlogger.pass("All test cases processed - Pending count is 0");
		                List<WebElement> tooltip = exectuionpage.tooltip;
		                for (WebElement webElement : tooltip) {
		        			System.out.println(webElement.getText());
		        			Extentlogger.info(webElement.getText());
		        		}
		                break;
		            }
		            
		            // If not zero, click refresh and wait
		            clickelement(exectuionpage.refreshbtn);
		            Thread.sleep(5000); // Wait 5 seconds before checking again
		            
		        } catch (Exception e) {
		            Extentlogger.info("Error while checking tooltip: " + e.getMessage());
		            // If there's an error, still try to refresh
		            clickelement(exectuionpage.refreshbtn);
		            Thread.sleep(5000);
		        }
		    }
		    
		    if (!pendingZero) {
		        Extentlogger.fail("Pending test cases did not reach 0 within 10 minutes");
		    }
		    
		    clickelementwithname(exectuionpage.downloadicon, "Download btn");
//		    verifyResultsDownload();	
	}		public void verifyResultsDownload() throws InterruptedException {
		// Wait for file to download (up to 15 minutes)
	    String downloadDir = "C:\\Users\\DELL\\Downloads\\"; // Adjust path as needed
	    String expectedFileName = expectedBatchName + ".XLSX"; // Adjust pattern as needed
	    File downloadedFile = null;
	    long downloadStartTime = System.currentTimeMillis();
	    long downloadTimeout = 10 * 60 * 1000; // 15 minutes
	    
	    while (System.currentTimeMillis() - downloadStartTime < downloadTimeout) {
	        File[] files = new File(downloadDir).listFiles();
	        for (File file : files) {
	            if (file.getName().contains(expectedBatchName) && file.getName().endsWith(".XLSX")) {
	                downloadedFile = file;
	                break;
	            }
	        }
	        
	        if (downloadedFile != null) {
	            Extentlogger.pass("File downloaded successfully: " + downloadedFile.getName());
	            break;
	        }
	        
	        Thread.sleep(30000); // Check every 30 seconds
	    }
	    
	    if (downloadedFile == null) {
	        Extentlogger.fail("File download failed - file not found after 15 minutes");
	    } else if (downloadedFile.length() == 0) {
	        Extentlogger.fail("File downloaded but is empty");
	    }		
	}

	public void verifyResultsDownload1() throws InterruptedException {
		// Wait for file to download (up to 15 minutes)
		String downloadDir = "C:\\Users\\DELL\\Downloads\\"; // Adjust path as needed
		String expectedFileName = expectedBatchName + ".XLSX"; // Adjust pattern as needed
		File downloadedFile = null;
		long downloadStartTime = System.currentTimeMillis();
		long downloadTimeout = 10 * 60 * 1000; // 15 minutes

		while (System.currentTimeMillis() - downloadStartTime < downloadTimeout) {
			File[] files = new File(downloadDir).listFiles();
			for (File file : files) {
				if (file.getName().contains(expectedBatchName) && file.getName().endsWith(".XLSX")) {
					downloadedFile = file;
					break;
				}
			}

			if (downloadedFile != null) {
				Extentlogger.pass("File downloaded successfully: " + downloadedFile.getName());
				break;
			}

			Thread.sleep(30000); // Check every 30 seconds
		}

		if (downloadedFile == null) {
			Extentlogger.fail("File download failed - file not found after 15 minutes");
		} else if (downloadedFile.length() == 0) {
			Extentlogger.fail("File downloaded but is empty");
		}		
	}
}