package com.test;

import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.pages.LoginPage;

import base.BaseClass;

public class LoginTest extends BaseClass {

	@Test
	public void Login() {
	ExtentReport.createTest("Login");
	logintest=new LoginPage(driver);

	sendkeys(logintest.username, prop.getProperty("username"));
	sendkeys(logintest.password, prop.getProperty("password"));
	JSClick(driver,logintest.signbtn,"Signin btn");
	
	}
}
