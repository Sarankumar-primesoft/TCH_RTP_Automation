package com.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import base.BaseClass;

public class LoginPage extends BaseClass{
	
	@FindBy(xpath = "//input[@placeholder='Enter your email address']")
	public WebElement username;
	
	@FindBy(xpath = "//input[@id='password']")
	public WebElement password;
	@FindBy(xpath = "(//button[contains(text(),'Sign In')])[2]")
	public WebElement signbtn;
	
	public LoginPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}
	

}
