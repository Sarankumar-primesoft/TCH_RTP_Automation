package com.test;

import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.pages.Reports_ParticpantFilePage;

import base.BaseClass;

public class Reports_ParticipantFileTest extends BaseClass{

	@Test
	public void ParticipantFile_TCH() throws InterruptedException
	{
//		refresh();
		ExtentReport.createTest("TCH Participant File");
		PFreport = new Reports_ParticpantFilePage(driver);
		ExtentReport.createChildTest("Participant File TCH Service");
		scrollByVisibilityOfElement(driver, PFreport.Reports);

		visibleofele(driver, PFreport.MenuListParticipantFile, "TCH Participant file");
		clickelementwithname(PFreport.MenuListParticipantFile, "Navigating to TCH Participant file");
		Thread.sleep(1000);
		PFreport.TCHParticipantfile();
		
		ExtentReport.resetToParent();	
	}
	

}
