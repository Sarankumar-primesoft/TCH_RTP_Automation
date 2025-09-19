package com.configure;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import base.BaseClass;

public class Factory extends BaseClass{
	/**
 * page factory init elements classes
 */


private static final String exentreportfolder= System.getProperty("user.dir") + "/extent-report/";
private static String extentreportfilepath = "";

public static String getExtentReportFilePath()  {
	if (extentreportfilepath.isEmpty()) {
		try {
			extentreportfilepath = createReportPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return extentreportfilepath;
}
/**
 * 
 * @return
 * @throws IOException
 */
public static String createReportPath() throws IOException {
	if (prop == null) {
		loadProperties();  // Ensure properties are loaded
	}

	if (prop.getProperty("overridereports").equalsIgnoreCase("no")) {
		return exentreportfolder + getcurrenttime() +"/"+getcurrenttime()+"report.html";
	} else {
		return exentreportfolder+ "/report.html";
	}
}

//public static String getcurrenttime() {
//	Date currentdate = new Date();
//	String datafilename = currentdate.toString().replace(" ","-").replace(":", "-");
//	return datafilename;
//}
public static String getcurrenttime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
    return LocalDateTime.now().format(formatter);
}

}
