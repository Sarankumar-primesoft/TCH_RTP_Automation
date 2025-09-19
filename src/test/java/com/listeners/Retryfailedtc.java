package com.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retryfailedtc implements IRetryAnalyzer {
	private int retryCount=0;
	private int Maxretrycount=0;

	@Override
	public boolean retry(ITestResult result) {
		if(retryCount<Maxretrycount) {
			retryCount++;
			return true;
		}
		return false;


	}


}