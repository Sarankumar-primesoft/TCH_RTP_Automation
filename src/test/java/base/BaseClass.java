package base;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.ExtentReport.ExtentReport;
import com.ExtentReport.Extentlogger;
import com.configure.Factory;
import com.pages.BatchPage;
import com.pages.ExecutionsSummaryPage;
import com.pages.LoginPage;
import com.pages.Reports_AccountReportingPage;
import com.pages.Reports_BroadcastMessagesPage;
import com.pages.Reports_ParticpantFilePage;
import com.pages.Reports_RetrivalRequestPage;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	public static Properties prop;
	public static SoftAssert Softassert;
	public static Actions actions;

	public static WebDriver driver;
	static String BrowserName;
	static String BrowserVersion;
	public static Capabilities capabilities;
	public static JavascriptExecutor js;
	public static LoginPage logintest;
	public static BatchPage batchpage;
	public static Reports_AccountReportingPage acreport;
//	public static Reports_Fedwire_AccountReportingPage FWacreport;
	public static Reports_BroadcastMessagesPage bmreport;
	public static Reports_RetrivalRequestPage RRreport;
	public static Reports_ParticpantFilePage PFreport;
	public static ExecutionsSummaryPage exectuionpage;

	public BaseClass() {

		PageFactory.initElements(driver, this);
	}

	public static void loadProperties() {
		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/Configuration/config.properties");
			prop.load(ip);
		} catch (Exception e) {
			throw new RuntimeException("Error loading config.properties", e);
		}
	}

	@BeforeSuite
	public void setup() throws IOException {
		ExtentReport.invokereport();
		ExtentReport.createTest("Setup Execution");

		Softassert = new SoftAssert();
		loadProperties();
		WebDriverManager.chromedriver().setup();
		WebDriverManager.firefoxdriver().setup();
//		WebDriverManager.edgedriver().setup();

		String browserName = prop.getProperty("browser");

		switch (browserName.toLowerCase()) {

	    case "chrome":
	        ChromeOptions chromeOptions = new ChromeOptions();

	        String downloadPath = System.getProperty("user.dir") + File.separator + "extent-report";
	        cleanDownloadFolder(downloadPath);
	        File file = new File(downloadPath);
	        if (!file.exists())
	            file.mkdirs();

	        Map<String, Object> chromePrefs = new HashMap<>();
	        chromePrefs.put("download.default_directory", downloadPath);
	        chromePrefs.put("download.prompt_for_download", false);
	        chromePrefs.put("plugins.always_open_pdf_externally", true);
	        chromePrefs.put("profile.default_content_settings.popups", 0);
	        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
	        chromePrefs.put("credentials_enable_service", false);
	        chromePrefs.put("profile.password_manager_enabled", false);
	        chromeOptions.setExperimentalOption("prefs", chromePrefs);
	        chromeOptions.addArguments("--disable-save-password-bubble");
	        chromeOptions.addArguments("--disable-features=AutofillServerCommunication,PasswordManagerEnableSaving,PasswordChangeDetection");
	        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
	        chromeOptions.addArguments("--password-store=basic");
	        chromeOptions.addArguments("--no-first-run");
	        // 🔍 Detect Jenkins environment
	        boolean isJenkins = System.getenv("JENKINS_HOME") != null;

	        if (isJenkins) {
	            chromeOptions.addArguments("--headless=new");
	            chromeOptions.addArguments("--window-size=1920,1080");
	            chromeOptions.addArguments("--disable-gpu");
	            chromeOptions.addArguments("--no-sandbox");
	            chromeOptions.addArguments("--disable-dev-shm-usage");
	        }

	        driver = new ChromeDriver(chromeOptions);

	        // Apply window size for both modes (helps ensure consistency)
	        driver.manage().window().setSize(new Dimension(1920, 1080));

	        js = (JavascriptExecutor) driver;
	        actions = new Actions(driver);
	        break;
		case "firefox":
			driver = new FirefoxDriver();
			break;
//		case "microsoftedge":
//			driver = new EdgeDriver();
//			break;
		default:
			throw new RuntimeException("Invalid browser name in config file: " + browserName);
		}

		// Initialize capabilities after driver creation
		capabilities = ((RemoteWebDriver) driver).getCapabilities();
		BrowserName = capabilities.getBrowserName();
		BrowserVersion = capabilities.getBrowserVersion();

		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("prefs",
//				Map.of("download.default_directory", "C:\\Downloads", "profile.default_content_settings.popups", 0,
//						"profile.content_settings.exceptions.automatic_downloads.*.setting", 1));
//		options.addArguments("--headless=new"); // or just "--headless" for older versions
//		options.addArguments("--window-size=1920,1080");// IMPORTANT!
//		options.addArguments("--disable-gpu"); // Optional but recommended

		System.out.println("driver : " + driver);

		implicitWait(10);
		pageLoadTimeOut(driver, 40);

		driver.manage().window().maximize();
		launchUrl(driver, prop.getProperty("testurl"));
		if (driver.getTitle().equalsIgnoreCase(prop.getProperty("homepage-title"))) {
			System.out.println(driver.getTitle());
		}

	}

	@AfterSuite
	public void tearDown() throws Exception {
		if (driver != null) {
			Browserinfo();
			System.out.println("WebDriver closed.");
			ExtentReport.flushreport();
			zipExtentReportFolder();
			driver.quit();
		}
	}

	/*
	 * -----------------------------------------------------------------------------
	 * ---------------------------------------
	 */
	/* Methods Created for Reports/System Messages in Payapt */
	public void cleanDownloadFolder(String path) {
		File downloadDir = new File(path);
		if (downloadDir.exists() && downloadDir.isDirectory()) {
			for (File file : downloadDir.listFiles()) {
				if (!file.isDirectory()) {
					file.delete(); // Delete files only
				}
			}
		}
	}

	public void zipExtentReportFolder() throws IOException {
		String timestamp = Factory.getcurrenttime(); // get timestamp
		String sourceDirPath = System.getProperty("user.dir") + File.separator + "extent-report";
		String parentDir = System.getProperty("user.dir");

		// Path of the zip file folder
		String zipFilePath = sourceDirPath + File.separator + "ExtentReport.zip";

		try (FileOutputStream fos = new FileOutputStream(zipFilePath); ZipOutputStream zos = new ZipOutputStream(fos)) {

			Path sourceDir = Paths.get(sourceDirPath);

			Files.walk(sourceDir).filter(path -> !Files.isDirectory(path))
					.filter(path -> !path.getFileName().toString().equals("ExtentReport.zip")) // 👈 exclude zip itself
					.forEach(path -> {
						try {
							String entryName = "extent-report-" + timestamp + File.separator
									+ sourceDir.relativize(path).toString().replace("\\", "/");

							zos.putNextEntry(new ZipEntry(entryName));
							Files.copy(path, zos);
							zos.closeEntry();
						} catch (IOException e) {
							e.printStackTrace();
						}
					});

		}
	}

	public void routingnumberselect(List<WebElement> ListElement, String Routingnum) {
		boolean found = false;

		for (WebElement dropdownvalue : ListElement) {
			String visibleText = dropdownvalue.getText().trim();

			// Scroll into view before checking
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownvalue);

			if (visibleText.equalsIgnoreCase(Routingnum)) {
				dropdownvalue.click();
				found = true;
				Extentlogger.pass("Routing number '" + Routingnum + "' selected successfully from dropdown.");
				System.out.println("Routing number '" + Routingnum + "' selected successfully from dropdown.");
				break;
			}
		}

		if (!found) {
			Extentlogger.fail("Routing number '" + Routingnum + "' not found in dropdown after scrolling.");
			System.out.println("Routing number '" + Routingnum + "' not listed in dropdown.");

		}
	}

	public void selectoptionfromList(WebElement DropdownElement, List<WebElement> Listelements, String value) {
		clickelementwithname(DropdownElement, "Select dropdown.");
		boolean matchFound = false;
		String expectedValue = value;
		for (WebElement option : Listelements) {
			String optionText = option.getText().trim();
			if (optionText.equalsIgnoreCase(expectedValue.trim())) {
				clickelementwithname(option, optionText);
				System.out.println("Selected: " + optionText);
				matchFound = true;
				break;
			}
		}

		if (!matchFound) {
			System.out.println("Dropdown value not found: " + expectedValue);
		}
	}

	// Method to wait until loader disappears
	public boolean waitForLoaderToDisappear(WebElement element, int max, int interval) {
		int maxWaitTimeInSeconds = max;
		int pollIntervalInSeconds = interval;

		for (int elapsed = 0; elapsed < maxWaitTimeInSeconds; elapsed += pollIntervalInSeconds) {
			try {
				if (!element.isDisplayed()) {
					System.out.println("Loader disappeared at second: " + elapsed);
					return true;
				} else {
					System.out.println("Loader still visible at second: " + elapsed);
				}
			} catch (Exception e) {
				// Element is no longer in the DOM
				System.out.println("Loader element not found at second: " + elapsed + ". Assuming it's gone.");
				Extentlogger.info("Loader element not found at second: " + elapsed + ". Assuming it's gone.");
				return true;
			}

			try {
				Thread.sleep(pollIntervalInSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Loader is still visible after waiting " + maxWaitTimeInSeconds + " seconds.");
		Extentlogger.fail("Loader is still visible after waiting " + maxWaitTimeInSeconds + " seconds.", true);
		return false;
	}

	// Date Picker for the Calendar Field.
	public void datePicker() throws InterruptedException {
		// Step 1: Calculate previous valid weekday
		LocalDate today = LocalDate.now();
		// LocalDate today = LocalDate.of(2025, 6, 2); // Test case for June 2nd it will
		// select may 30, because previous dates fall on weekend
		LocalDate date = today.minusDays(1);

		WebElement Startdate = driver.findElement(By.xpath("//input[@placeholder='Start date']"));
		WebElement Enddate = driver.findElement(By.xpath("//input[@placeholder='End date']"));

		// Handle weekend case
		while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			date = date.minusDays(1);
		}

		// Handle month-end case (if today is first of month, select last business day
		// of previous month)
		if (today.getDayOfMonth() == 1) {
			date = today.minusDays(1); // Last day of previous month
			// Adjust for weekend if needed
			while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
				date = date.minusDays(1);
			}
		}

		// Select both Start and End Dates
		selectDateFromCalendar(Startdate, date, "Start Date");
		selectDateFromCalendar(Enddate, date, "End Date");
	}

	private void selectDateFromCalendar(WebElement dateField, LocalDate targetDate, String label)
			throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		dateField.click(); // Open the calendar popup

		// Adjust calendar view
		adjustCalendarToTarget(targetDate, wait);

		// Select the actual date - now with improved logic to get the most recent
		// occurrence
		selectCorrectDateFromDualCalendar(targetDate, wait);

		Extentlogger.pass("Selected " + label + ": " + targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}

	private void adjustCalendarToTarget(LocalDate targetDate, WebDriverWait wait) throws InterruptedException {
		By leftMonthBtnLocator = By.xpath("(//button[@class='ant-picker-month-btn'])[1]");
		By leftYearBtnLocator = By.xpath("(//button[@class='ant-picker-year-btn'])[1]");
		By rightMonthBtnLocator = By.xpath("(//button[@class='ant-picker-month-btn'])[2]");
		By rightYearBtnLocator = By.xpath("(//button[@class='ant-picker-year-btn'])[2]");

		WebElement prevMonthArrow = driver.findElement(By.xpath("//button[@class='ant-picker-header-prev-btn']"));
		WebElement nextMonthArrow = driver.findElement(By.xpath("//button[@class='ant-picker-header-next-btn']"));

		while (true) {
			String leftMonthText = getFullMonthName(
					wait.until(ExpectedConditions.visibilityOfElementLocated(leftMonthBtnLocator)).getText());
			int leftMonth = Month.valueOf(leftMonthText.toUpperCase()).getValue();
			int leftYear = Integer.parseInt(driver.findElement(leftYearBtnLocator).getText());

			String rightMonthText = getFullMonthName(
					wait.until(ExpectedConditions.visibilityOfElementLocated(rightMonthBtnLocator)).getText());
			int rightMonth = Month.valueOf(rightMonthText.toUpperCase()).getValue();
			int rightYear = Integer.parseInt(driver.findElement(rightYearBtnLocator).getText());

			boolean targetMatchesLeft = (leftMonth == targetDate.getMonthValue() && leftYear == targetDate.getYear());
			boolean targetMatchesRight = (rightMonth == targetDate.getMonthValue()
					&& rightYear == targetDate.getYear());

			if (targetMatchesLeft || targetMatchesRight) {
				break;
			}

			LocalDate leftDate = LocalDate.of(leftYear, leftMonth, 1);
			if (leftDate.isAfter(targetDate.withDayOfMonth(1))) {
				JSClick(driver, prevMonthArrow, "");
			} else {
				JSClick(driver, nextMonthArrow, "");
			}

			Thread.sleep(500);
		}
	}

	private void selectCorrectDateFromDualCalendar(LocalDate targetDate, WebDriverWait wait) {
		String targetDayStr = String.valueOf(targetDate.getDayOfMonth());
		String targetMonth = targetDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
		int targetYear = targetDate.getYear();

		System.out.println("Looking for date: " + targetMonth + " " + targetYear + ", day " + targetDayStr);

		// Find all calendar panels (left and right)
		List<WebElement> calendarPanels = driver.findElements(By.xpath("//div[contains(@class,'ant-picker-panel')]"));

		for (int i = 0; i < calendarPanels.size(); i++) {
			WebElement panel = calendarPanels.get(i);
			// Get the month/year from the panel header
			WebElement header = panel.findElement(By.xpath(".//div[contains(@class,'ant-picker-header-view')]"));
			String headerText = header.getText().replace("\n", " ").trim();
			boolean match = headerText.equalsIgnoreCase(targetMonth + " " + targetYear);
			System.out.println(header.getText());
			System.out.println(match);
			// Check if this panel contains our target month/year
			if (match) {
				System.out.println("Found matching panel for target month/year");

				// More precise XPath to find the date cell within this panel
				List<WebElement> dateCells = panel.findElements(By.xpath(
						".//following::div//tbody//td[contains(@class,'ant-picker-cell ant-picker-cell-in-view') or contains(@class,'ant-picker-cell ant-picker-cell-range-start ant-picker-cell-range-end ant-picker-cell-in-view')]"
								+ "/div[text()='" + targetDayStr + "']"));

				System.out.println("Found " + dateCells.size() + " matching dates in this panel");

				if (!dateCells.isEmpty()) {
					System.out.println("Clicking date: " + dateCells.get(0).getText());
					try {
						// Scroll into view and click with JavaScript if regular click fails
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
								dateCells.get(0));
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateCells.get(0));
						System.out.println("Successfully clicked date");
						return;
					} catch (Exception e) {
						System.out.println("Click failed, trying alternative method");
						new Actions(driver).moveToElement(dateCells.get(0)).click().perform();
						return;
					}
				}
			}
		}

		throw new RuntimeException(
				"Target date not found in calendar: " + targetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
	}

	private String getFullMonthName(String shortMonthName) {
		Map<String, String> monthMap = new HashMap<>();
		monthMap.put("Jan", "JANUARY");
		monthMap.put("Feb", "FEBRUARY");
		monthMap.put("Mar", "MARCH");
		monthMap.put("Apr", "APRIL");
		monthMap.put("May", "MAY");
		monthMap.put("Jun", "JUNE");
		monthMap.put("Jul", "JULY");
		monthMap.put("Aug", "AUGUST");
		monthMap.put("Sep", "SEPTEMBER");
		monthMap.put("Oct", "OCTOBER");
		monthMap.put("Nov", "NOVEMBER");
		monthMap.put("Dec", "DECEMBER");

		return monthMap.getOrDefault(shortMonthName, shortMonthName.toUpperCase());

	}

	/* END of Creaeted methods for Reports/System Messages in Payapt */
	/*
	 * -----------------------------------------------------------------------------
	 * ---------------------------------------
	 */

	/* Common Utility Methods */

	public static void clickusingaction(WebElement ele) throws InterruptedException {

		Actions ac = new Actions(driver);
		ac.click(ele).build().perform();
	}

	public static void navigateback() throws InterruptedException {
		Thread.sleep(2000);
		driver.navigate().back();
	}

	public static void refresh() throws InterruptedException {
		driver.navigate().refresh();
		Thread.sleep(2000);
	}

	public void assertEquals(String actual, String expected, String ele) {
		SoftAssert softAssert = new SoftAssert();
		boolean result = actual.equals(expected);

		// String elementName = getElementName(ele); // Fetch element name dynamically
		String logMessage = "Assertion Validation on Element " + ele + ": Expected='" + expected + "', Actual='"
				+ actual + "'";

		if (result) {
			Extentlogger.pass(logMessage, true);
			// logger.info(logMessage);
		} else {
			Extentlogger.fail(logMessage);
			// logger.error(logMessage);
		}
		softAssert.assertEquals(actual, expected, "Mismatch Found!");
	}

	public void assertTrue(boolean condition, String ele, String message) {
		SoftAssert softAssert = new SoftAssert();

		String logMessage = "Assertion Validation on Element " + ele + ": " + message;

		if (condition) {
			Extentlogger.pass(logMessage);
			// logger.info(logMessage);
		} else {
			Extentlogger.fail(logMessage);
			// logger.error(logMessage);
		}

		softAssert.assertTrue(condition, "Condition Failed: " + message);
	}

	public void scrollByVisibilityOfElement(WebDriver driver, WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", ele);
		Extentlogger.info("Scrolled to " + getElementName(ele));
		// logger.info("Scrolled to "+getElementName(ele));

	}

	public void clickelement(WebElement element) {
		if (element == null) {
			throw new IllegalStateException("WebElement is null. Ensure PageFactory.initElements() was called.");
		}
		try {
			Waitforclickable(element, 10);
			String text = Gettext(element);
			element.click();
			if (!text.isEmpty()) {
				Extentlogger.pass(text + " is Clicked", false);
				// logger.info(text + " is Clicked", false);
			}
		} catch (WebDriverException e) {
			System.err.println("Failed to click element: " + e.getMessage());
			Extentlogger.fail(getElementName(element) + " is not Clicked", false);
			// logger.error(getElementName(element)+ " is not Clicked", false);
		}
	}

	public void clickelementwithname(WebElement element, String elename) {
		try {
			Waitforclickable(element, 10);
			
			String text = element.getText().isEmpty() ? elename : element.getText();
			element.click();
			System.out.println(text + " is Clicked");
			Extentlogger.pass(text + " is Clicked", true);
			// logger.info(text + " is Clicked",true);

		} catch (Exception e) {
			System.err.println("Failed to click element: " + elename + " " + e.getMessage());
			Extentlogger.fail("Failed to click element: " + elename + " " + e.getMessage(), true);
			// logger.error("Failed to click element: "+elename+" " + e.getMessage(),true);
		}
	}

	public static void Waitforclickable(WebElement element, int seconds) {
		if (element == null) {
			throw new IllegalArgumentException("Element is null, cannot wait for clickability.");
		}
		new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.elementToBeClickable(element));
	}

	public static String Gettext(WebElement element) {
		return element.getText();
	}

	public static Boolean isdisplay(WebElement element) {
		if (!Gettext(element).isEmpty()) {
			Extentlogger.info(Gettext(element) + " is Display: " + element.isDisplayed());
			// logger.info(Gettext(element)+" is Display: "+element.isDisplayed());
			return element.isDisplayed();
		} else {
			Extentlogger.info(Gettext(element) + " is not Displayed: " + element.isDisplayed());
			// logger.info(Gettext(element)+" is not Displayed: "+element.isDisplayed());
			return element.isDisplayed();
		}
	}

	public static void sendkeys(WebElement element, String value) {
		element.sendKeys(value, Keys.ENTER);
		String placeholder = Getattribute(element, "placeholder");
		if (placeholder != null && !placeholder.isEmpty()) {
			Extentlogger.pass(value + " is entered in " + placeholder + " successfully", false);
			// logger.info(value + " is entered in " + placeholder + " successfully",
			// false);
		} else {
			Extentlogger.pass(value + " is entered successfully", false);
			// logger.info(value + " is entered successfully", false);
		}
	}

	public static void entersendkeys(WebElement element) {
		element.sendKeys(Keys.ENTER);
	}

	public static void tabsendkeys(WebElement element) {
		element.sendKeys(Keys.TAB);
	}

	public static void escsendkeys(WebElement element) {
		element.sendKeys(Keys.ESCAPE);
	}

	public static void activeelementsendkeys(String value) {
		driver.switchTo().activeElement().sendKeys(value);
	}

	private static String Getattribute(WebElement element, String attributename) {
		return element.getAttribute(attributename);
	}

	public static boolean JSClick(WebDriver driver, WebElement ele, String locatorName) {
		boolean flag = false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", ele);
			flag = true;

		}

		catch (Exception e) {
			throw e;

		} finally {
			if (flag) {
				System.out.println("Click Action is performed on " + locatorName);
				Extentlogger.pass("Click Action is performed on " + locatorName.toString());
				// logger.info("Click Action is performed on "+locatorName.toString());
			} else if (!flag) {
				System.out.println("Click Action is not performed on " + locatorName);
			}
		}
		return flag;
	}

	public static boolean launchUrl(WebDriver driver, String url) {
		try {
			driver.get(url);
			Extentlogger.info("Successfully launched \"" + url + "\"");
			// logger.info("Successfully launched \"" + url + "\"");
			return true;
		} catch (Exception e) {
			// System.out.println("Failed to launch \"" + url + "\"");
			Extentlogger.info("Failed to launch \"" + url + "\"");
			// logger.info("Failed to launch \"" + url + "\"");
			return false;
		}
	}

	public static void implicitWait(int seconds) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
	}

	public void visibleofele(WebDriver driver, WebElement element, String elename) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(element));
		Extentlogger.info("Element is Visibile: " + elename, true);
		// logger.info("Element is Visibile: "+elename);
	}

	public static Boolean invisibilityofelement(WebDriver driver, WebElement element, String name) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		Extentlogger.info(name + " Element is now invisibile", true);
		// logger.info(name+" Element is now invisibile");
		return wait.until(ExpectedConditions.invisibilityOf(element));
	}

	public static void pageLoadTimeOut(WebDriver driver, int timeOut) {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut));
		Extentlogger.info("Page load timeout.");
		// logger.info("Page load timeout.");

	}

	public static String clickRandomElement(List<WebElement> elements) {
		if (elements == null || elements.isEmpty()) {
			System.out.println("No elements found to click.");
			Extentlogger.info("No elements found to click.");
			// logger.info("No elements found to click.");
			return null;
		}
		Random random = new Random();
		System.out.println(elements.size());
		int randomIndex = random.nextInt(elements.size()); // Selects a random index
		WebElement randomElement = elements.get(randomIndex);
		System.out.println(randomElement);
		System.out.println("random index" + randomIndex);
		System.out.println(randomElement.getAttribute("alt"));
		return randomElement.getAttribute("alt");
	}

	public static String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String directory = System.getProperty("user.dir") + "/Screenshots/";
		new File(directory).mkdirs(); // Ensure the directory exists
		String destination = directory + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public String getElementName(WebElement element) {
		try {
			for (Field field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (field.get(this) == element) {
					return field.getName();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UnknownElement";
	}

	public void switchframe(WebElement element) {
		driver.switchTo().frame(element);
	}

	public static void Browserinfo() {
		ExtentReport.extent.setSystemInfo("URL", prop.getProperty("testurl"));
		ExtentReport.extent.setSystemInfo("Username", prop.getProperty("username"));
		ExtentReport.extent.setSystemInfo("Password", prop.getProperty("password"));

		ExtentReport.extent.setSystemInfo("Browser Name", BrowserName);
		ExtentReport.extent.setSystemInfo("Browser Version", BrowserVersion);
		ExtentReport.extent.setSystemInfo("Executed By", prop.getProperty("executedby"));
	}

	public void assertLoaderAppears(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement loadingElement = wait.until(ExpectedConditions.visibilityOf(element));
		if (loadingElement.isDisplayed()) {
			System.out.println("Loader is displayed.");
			Extentlogger.info("Loader is displayed.", true);
		} else {
			System.out.println("Loader is not displayed.");
			Extentlogger.info("Loader is not displayed.");
		}
	}

	/* END of Common Utility Methods */
	/*
	 * -----------------------------------------------------------------------------
	 * -----------------------------------------------
	 */

}
