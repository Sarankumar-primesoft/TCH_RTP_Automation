package com.pages;

import java.util.List;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ExtentReport.Extentlogger;

import base.BaseClass;

public class Reports_AdministrationRequestPage extends BaseClass {

    @FindBy(xpath = "//span[@class='menu-item-text' and text()='Reports/System Messages']/../../following::div")
    public WebElement Reports;

    @FindBy(xpath = "//span[@class='menu-item-text' and text()='Administration Request']")
    public WebElement MenuListAdmin;

    @FindBy(xpath = "//div[contains(.,'Select Administration Message') and @role='combobox']")
    public WebElement selectReportType;

    @FindBy(xpath = "//button[contains(text(),'Generate')]")
    public WebElement generatebtn;

    @FindBy(xpath = "//div[contains(text(),'Account Report Request Generated Successfully')]/..")
    public WebElement generatedAlert;

    @FindBy(xpath = "//div[@class='ant-modal-body']")
    public WebElement GenerateResultscren;

    @FindBy(css = "span[role='progressbar']")
    public WebElement loader;

    // Keep icon locator as fallback; some apps put icon inside a button element
    @FindBy(xpath = "//button[.//*[@data-testid='FileDownloadIcon']]")
    public WebElement resultsdownloadbtn;

    @FindBy(xpath = "//button[@aria-label='Close']")
    public WebElement resultsClosebtn;

    @FindBy(xpath = "//div[contains(text(),'No data to download')]")
    public WebElement noDataAlert;

    /* ---------------- New: By locators for fresh lookup and waits ---------------- */
    // Update these if your dropdown markup differs. This aims at common Material-UI/ANT patterns:
    private By reportTypeOptions = By.xpath("//div[contains(@class,'MuiPaper-root') or contains(@class,'MuiMenu-paper')]//ul//li");
    private By reportResultLocator = By.xpath("//div[contains(@class,'ant-modal-body') or contains(@class,'report-result')]");
    private By loadingSpinnerLocator = By.cssSelector("span[role='progressbar']");
    // Anchor link inside modal that points to an XML file (if available)
    private By resultsDownloadAnchor = By.xpath("//div[@class='ant-modal-body']//a[contains(translate(@href,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '.xml')]");
    // Modal container (for waiting invisibility)
    private By resultsModalContainer = By.xpath("//div[contains(@class,'ant-modal-root') or contains(@class,'MuiDialog-root')]");

    // Constructor
    public Reports_AdministrationRequestPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    // Always return fresh list of options
    public List<WebElement> getReportTypeList() {
        return driver.findElements(reportTypeOptions);
    }

    public By getReportTypeOptionsLocator() {
        return reportTypeOptions;
    }

    public By getReportResultLocator() {
        return reportResultLocator;
    }

    public By getLoadingSpinnerLocator() {
        return loadingSpinnerLocator;
    }

    public By getResultsModalContainer() {
        return resultsModalContainer;
    }

    public By getResultsDownloadAnchorLocator() {
        return resultsDownloadAnchor;
    }

    /* ---------------- Helper method: click download (tries anchor -> button) ---------------- */
    public boolean clickDownloadAndCloseIfPresent(long waitSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // 1) Try to click an anchor link to XML (preferred if the server exposes file href)
            try {
                WebElement anchor = wait.until(ExpectedConditions.visibilityOfElementLocated(resultsDownloadAnchor));
                if (anchor != null && anchor.isDisplayed()) {
                    js.executeScript("arguments[0].click();", anchor); // JS click to avoid interception
                    Extentlogger.pass("Clicked download anchor (xml) via JS", true);
                    // allow some time for browser to start download - but we cannot check filesystem here
                    // Then close modal
                    wait.until(ExpectedConditions.elementToBeClickable(resultsClosebtn));
                    js.executeScript("arguments[0].click();", resultsClosebtn);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(resultsModalContainer));
                    return true;
                }
            } catch (TimeoutException t1) {
                // anchor not found - fallback to button
            }

            // 2) Try to click the download button/icon (fallback)
            try {
                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(resultsdownloadbtn));
                js.executeScript("arguments[0].click();", btn);
                Extentlogger.pass("Clicked download button via JS", true);
                // close modal after clicking download
                wait.until(ExpectedConditions.elementToBeClickable(resultsClosebtn));
                js.executeScript("arguments[0].click();", resultsClosebtn);
                wait.until(ExpectedConditions.invisibilityOfElementLocated(resultsModalContainer));
                return true;
            } catch (TimeoutException t2) {
                // download button not present/clickable
            }

            // 3) If no download, see if "No data to download" alert appears and handle it
            try {
                WebElement noData = wait.until(ExpectedConditions.visibilityOf(noDataAlert));
                if (noData != null && noData.isDisplayed()) {
                    Extentlogger.fail("No data to download displayed", true);
                    // close modal
                    if (resultsClosebtn != null) {
                        wait.until(ExpectedConditions.elementToBeClickable(resultsClosebtn));
                        js.executeScript("arguments[0].click();", resultsClosebtn);
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(resultsModalContainer));
                    }
                    return false;
                }
            } catch (TimeoutException t3) {
                // no-data alert not present either
            }

        } catch (Exception e) {
            System.out.println("Exception in clickDownloadAndCloseIfPresent: " + e.getMessage());
        }

        // As last resort, try to close modal if it's open
        try {
            if (resultsClosebtn != null && resultsClosebtn.isDisplayed()) {
                js.executeScript("arguments[0].click();", resultsClosebtn);
                WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait2.until(ExpectedConditions.invisibilityOfElementLocated(resultsModalContainer));
            }
        } catch (Exception ignored) { }

        return false;
    }

    /* ---------------- Report action methods (unified) ---------------- */
    // I consolidated the repeated code into a single helper to reduce duplication.
    public void generateAndHandleResult() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        clickelementwithname(this.generatebtn, "Clicking Generate button");
        // Wait for alert/modal to appear
        try {
            wait.until(ExpectedConditions.visibilityOf(this.generatedAlert));
        } catch (TimeoutException te) {
            // sometimes generate action directly opens the modal - wait for modal
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(this.resultsModalContainer));
            } catch (TimeoutException t2) {
                // neither alert nor modal showed - log and return
                Extentlogger.fail("Generate did not show success alert or results modal", true);
                return;
            }
        }

        // wait for results modal or results area
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(this.getReportResultLocator()));
        } catch (TimeoutException e) {
            // maybe still loading - ensure loader appears then disappears
        }

        // Wait for loader to appear (if any) then disappear
        try {
            this.assertLoaderAppears(this.loader);
            boolean loaderstatus = this.waitForLoaderToDisappear(this.loader, 60, 2); // extend time if reports are heavy
            if (!loaderstatus) {
                Extentlogger.fail("Loader did not disappear within timeout", true);
            }
        } catch (Exception ignore) {
            // if no loader available, continue
        }

        // Try to click download & close modal
        boolean downloaded = clickDownloadAndCloseIfPresent(30);
        if (downloaded) {
            Extentlogger.pass("Report generated and download attempted", true);
        } else {
            Extentlogger.info("Download not available or no-data case handled", true);
        }
    }

    // Individual wrappers - kept for backward compatibility
    public void EchoReport() {
        generateAndHandleResult();
    }

    public void SignOnReport() {
        generateAndHandleResult();
    }

    public void SignOffReport() {
        generateAndHandleResult();
    }

    public void DatabaseReport() {
        generateAndHandleResult();
    }
}
