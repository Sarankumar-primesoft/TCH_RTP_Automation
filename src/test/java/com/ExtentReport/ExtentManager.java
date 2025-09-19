package com.ExtentReport;

import com.aventstack.extentreports.ExtentTest;

public class ExtentManager {

		private static ThreadLocal<ExtentTest> extTest = new ThreadLocal<>();
		private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
		
		static ExtentTest getExtentTest() {
			return extTest.get();
		}

		static void setExtentTest(ExtentTest test) {
			extTest.set(test);
		}

		static void unload() {
			extTest.remove();
			parentTest.remove();
		}
		 static void setParentTest(ExtentTest test) {
		        parentTest.set(test);
		    }

		    static ExtentTest getParentTest() {
		        return parentTest.get();
		    }
	}
