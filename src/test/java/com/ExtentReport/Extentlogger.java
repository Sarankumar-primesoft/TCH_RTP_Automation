package com.ExtentReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.aventstack.extentreports.MediaEntityBuilder;
import base.BaseClass;


public class Extentlogger extends BaseClass {

//	public static Logger log =Logger.getLogger(Extentlogger.class);
//	static {
//		PropertyConfigurator.configure("log4j.properties");
//	}
	/**
	 * take a screenshot for base64
	 * @return string
	 */
	public static String captureScreenshotToFile(String testName) {
	    File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	    String screenshotPath = System.getProperty("user.dir") + File.separator + "extent-report"
	            + File.separator + "screenshots" + File.separator + testName + "_" + System.currentTimeMillis() + ".png";

	    try {
	        File dest = new File(screenshotPath);
	        dest.getParentFile().mkdirs();  // Ensure directory exists
	        Files.copy(src.toPath(), dest.toPath());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return screenshotPath;
	}
//	private static boolean isFileScreenshotMode() {
//	    return prop.getProperty("screenshot.mode").equalsIgnoreCase("file");
//	}
//
//
//	public static void pass(String message, boolean needscreenshot) {
//	    if (prop.getProperty("passedstepsscreenshot").equalsIgnoreCase("yes") && needscreenshot) {
//	        if (isFileScreenshotMode()) {
//	            String path = captureScreenshotToFile(message.replace(" ", "_"));
//	            ExtentManager.getExtentTest().pass(message,
//	                MediaEntityBuilder.createScreenCaptureFromPath(path).build());
//	        } else {
//	            ExtentManager.getExtentTest().pass(message,
//	                MediaEntityBuilder.createScreenCaptureFromBase64String(Base64image()).build());
//	        }
//	    } else {
//	        pass(message);
//	    }
//	}
//	public static void fail(String message, boolean needscreenshot) {
//	    if (prop.getProperty("failedstepsscreenshot").equalsIgnoreCase("yes") && needscreenshot) {
//	        if (isFileScreenshotMode()) {
//	            String path = captureScreenshotToFile(message.replace(" ", "_"));
//	            ExtentManager.getExtentTest().fail(message + " is failed",
//	                MediaEntityBuilder.createScreenCaptureFromPath(path).build());
//	        } else {
//	            ExtentManager.getExtentTest().fail(message + " is failed",
//	                MediaEntityBuilder.createScreenCaptureFromBase64String(Base64image()).build());
//	        }
//	    } else {
//	        fail(message);
//	    }
//	}
//
//	public static void skip(String message, boolean needscreenshot) {
//	    if (prop.getProperty("skippedstepsscreenshot").equalsIgnoreCase("yes") && needscreenshot) {
//	        if (isFileScreenshotMode()) {
//	            String path = captureScreenshotToFile(message.replace(" ", "_"));
//	            ExtentManager.getExtentTest().skip(message + " is Skipped",
//	                MediaEntityBuilder.createScreenCaptureFromPath(path).build());
//	        } else {
//	            ExtentManager.getExtentTest().skip(message + " is Skipped",
//	                MediaEntityBuilder.createScreenCaptureFromBase64String(Base64image()).build());
//	        }
//	    } else {
//	        skip(message);
//	    }
//	}
//
//	
//	public static void info(String message, boolean needscreenshot) {
//	    if (prop.getProperty("infostepsscreenshot").equalsIgnoreCase("yes") && needscreenshot) {
//	        if (isFileScreenshotMode()) {
//	            String path = captureScreenshotToFile(message.replace(" ", "_"));
//	            ExtentManager.getExtentTest().info(message,
//	                MediaEntityBuilder.createScreenCaptureFromPath(path).build());
//	        } else {
//	            ExtentManager.getExtentTest().info(message,
//	                MediaEntityBuilder.createScreenCaptureFromBase64String(Base64image()).build());
//	        }
//	    } else {
//	        info(message);
//	    }
//	}
//
	
	public static String Base64image() {
		//used "data:image/png;base64," to solve the screenshot not displayed in extentreport on the jenkins/headless mode executed
		return "data:image/png;base64," + ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
	}
	/**
	 * passed withonly message
	 * @param message enter the message
	 */
	public static void pass(String message) {
		ExtentManager.getExtentTest().pass(message);
//		log.info(message);
	}


	/**
	 * Failed log with only message
	 * @param message enter the message
	 */
	public static void fail(String message) {

		ExtentManager.getExtentTest().fail(message);
//		log.error(message);
	}
	
	/**
	 * information log with only messsage
	 * @param Testinfo Enter the info message
	 */
	public static void info(String message) {
		ExtentManager.getExtentTest().info(message);
//		log.info(message);
	}
	
	public static void info(String message,boolean needscreenshot) {
		if(prop.getProperty("infostepsscreenshot").equalsIgnoreCase("yes")&& needscreenshot) {
//			log.info(message);
			ExtentManager.getExtentTest().info(message,
					MediaEntityBuilder.createScreenCaptureFromBase64String(Extentlogger.Base64image()).build());
		}
		else {
			info(message);
		}

//		log.info(message);
	}
	/**
	 * Passed log message along with screenshot 
	 * @param message Enter the message 
	 * @param needscreenshot need screenshot? (true or false)
	 */
	public static void pass(String message,boolean needscreenshot) {
		if(prop.getProperty("passedstepsscreenshot").equalsIgnoreCase("yes")&& needscreenshot) {
//			log.info(message);
			ExtentManager.getExtentTest().pass(message.concat(" is passed"),
					MediaEntityBuilder.createScreenCaptureFromBase64String(Extentlogger.Base64image()).build());
		}else {
			pass(message);
		}
	}
	/**
	 * Failed log message along with screenshot 
	 * @param message Enter the message
	 * @param needscreenshot need screenshot? (true or false)
	 */
	public static void fail(String message,boolean needscreenshot) {
		if(prop.getProperty("failedstepsscreenshot").equalsIgnoreCase("yes")&& needscreenshot) {
//			log.error(message);
			ExtentManager.getExtentTest().fail(message.concat(" is failed"),
					MediaEntityBuilder.createScreenCaptureFromBase64String(Extentlogger.Base64image()).build());
		}else {
			fail(message);
		}
	}
	/**
	 * Skipped log message along with screenshot
	 * @param message Enter the message
	 * @param needscreenshot need screenshot? (true or false)
	 */
	public static void skip(String message,boolean needscreenshot) {
		if(prop.getProperty("skippedstepsscreenshot").equalsIgnoreCase("yes")&& needscreenshot) {
//			log.info(message);
			ExtentManager.getExtentTest().skip(message.concat(" is Skipped"),
					MediaEntityBuilder.createScreenCaptureFromBase64String(Extentlogger.Base64image()).build());
		}else {
			skip(message);
		}
	}
	/**
	 * Skipped log with only message
	 * @param message Enter the message
	 */
	private static void skip(String message) {
		ExtentManager.getExtentTest().skip(message);
//		log.info(message);
	}

}
