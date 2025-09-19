package com.ExtentReport;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.configure.Factory;

import base.BaseClass;

public class ExtentReport {

	public static ExtentReports extent;
	/**
	 * invoke the report begining of the execution
	 * @throws IOException 
	 */
	public static  void invokereport()  {
		if(Objects.isNull(extent)) {
			extent = new ExtentReports();

			//			ExtentSparkReporter failedreport=new ExtentSparkReporter("FailedTC.html").filter().statusFilter()
			//					.as(new Status[] {Status.FAIL,Status.SKIP}).apply();
			//			failedreport.config().setDocumentTitle("Failed TestReport");
			//			failedreport.config().setReportName("Failed Test Cases Report");
			//			failedreport.config().setTheme(Theme.DARK);

			File Configer = new File(System.getProperty("user.dir") + "/extentreport.xml");
			ExtentSparkReporter spark = new ExtentSparkReporter(Factory.getExtentReportFilePath());
			extent.setSystemInfo("OS Details", System.getProperty("os.name"));
			extent.setSystemInfo("OS verison", System.getProperty("os.version"));
			extent.setSystemInfo("Java version", System.getProperty("java.version"));
			try {
				spark.loadXMLConfig(Configer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			extent.attachReporter(spark);
		}
	}
	/**
	 * flush the report end of the execution
	 * @throws IOException 
	 */
	public static void flushreport() {
		if(Objects.nonNull(extent)) {
			System.out.println("run");
			extent.flush();
			try {
				 File reportFile = new File(Factory.getExtentReportFilePath());
		            if (reportFile.exists()) {
		            	ExtentManager.getExtentTest().info("Opening the Extent Report: " + reportFile);
		            	 if (System.getenv("JENKINS_HOME") == null && !GraphicsEnvironment.isHeadless()) {
		                     Desktop.getDesktop().browse(reportFile.toURI());
		                 } else {
		                     System.out.println("Headless environment detected (like Jenkins). Skipping opening report.");
		                 }
		            } else {
		            	ExtentManager.getExtentTest().fail("Extent Report file not found: " + reportFile);
		                System.out.println("Extent Report file does not exist: " + reportFile);
		            }
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * create a test name for an Extent report
	 * @param testcasename 
	 */
	public static void createTest(String testcasename) {
		ExtentTest test = extent.createTest(testcasename);  
		ExtentManager.setExtentTest(test);
	     ExtentManager.setParentTest(test);
	}
//	public static ExtentTest createChildTest(String childTestName) {
//	    ExtentTest parentTest = ExtentManager.getExtentTest(); // current parent
//	    ExtentTest child = parentTest.createNode(childTestName);
//	    ExtentManager.setExtentTest(child); // switch context to child for logging
//	    return child;
//	}
	 public static void createChildTest(String childTestName) {
	        ExtentTest parent = ExtentManager.getParentTest();  // safely get the stored parent
	        ExtentTest child = parent.createNode(childTestName); // create child
	        ExtentManager.setExtentTest(child); // switch context to child
	    }

	    // ✅ NEW — resets logging context back to parent
	    public static void resetToParent() {
	        ExtentManager.setExtentTest(ExtentManager.getParentTest());
	 
	    }
}
