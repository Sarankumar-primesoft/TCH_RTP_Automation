package IndividualFlows;

import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.ExtentReport.Extentlogger;
import com.pages.BatchPage;

import base.BaseClass;

public class TCHBatchTest extends BaseClass{

	public static long TCHstartTime ;
	
	@Test
	public void Batchcreation() throws InterruptedException
	{
		ExtentReport.createTest("TCH Batchs Run");
		batchpage=new BatchPage(driver);
		 
		clickelementwithname(batchpage.ServiceTypeDropdown,"Service Type Drop down");
		
		if(prop.getProperty("BTServiceNameFN").equalsIgnoreCase("TCH"))
		{
		clickelementwithname(batchpage.TCHOption,"Fedwire select from list");
		}
		else {
			System.out.println("Service Type is not listed");
			Extentlogger.fail("Service Type is not listed");
		}
		
		clickelementwithname(batchpage.tabbatch,"Switch to Batch tab");
		
		JSClick(driver,batchpage.batchfiletoggle, "Changing toggle to Batch file");
		
		sendkeys(batchpage.tabsearch,prop.getProperty("TCHBatchName"));
		Extentlogger.info("Batch Name : "+prop.getProperty("TCHBatchName"));
		JSClick(driver,batchpage.chkbatchcheckbox,"Check box select");

		clickelementwithname(batchpage.btnplaybutton, "Run btn");
		TCHstartTime = System.currentTimeMillis();
		
//		visibleofele(driver,batchpage.lblBatchsuccMsg, "Sucess msg");
//		assertEquals(batchpage.lblBatchsuccMsg.getText(), prop.getProperty("BatchRunsuccmsg"),"Batch Run Sucess msg");
		
		
	}
	
}
