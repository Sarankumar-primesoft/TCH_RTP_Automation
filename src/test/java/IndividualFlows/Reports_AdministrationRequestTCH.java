package IndividualFlows;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.ExtentReport.ExtentReport;
import com.pages.Reports_AdministrationRequestPage;

import base.BaseClass;

public class Reports_AdministrationRequestTCH extends BaseClass {

    @Test
    public void AdministrationRequest_TCH() throws InterruptedException {
        ExtentReport.createTest("Administration Request Summary TCH Test");
        acreport = new Reports_AdministrationRequestPage(driver);

        scrollByVisibilityOfElement(driver, acreport.Reports);

        visibleofele(driver, acreport.MenuListAdmin, "Administration Request");
        JSClick(driver, acreport.MenuListAdmin, "Navigating to Administration Request Reporting");

        String raw = prop.getProperty("TCHReportType");
        if (raw == null || raw.trim().isEmpty()) {
            System.out.println("TCHReportType property is empty or missing");
            return;
        }

        String[] reportTypesFromConfig = raw.split(",");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        for (String r : reportTypesFromConfig) {
            String reportType = r == null ? "" : r.trim();
            if (reportType.isEmpty())
                continue;

            ExtentReport.createChildTest(reportType);

            boolean matchFound = false;

            // Reopen the dropdown for each iteration and wait until options are visible
            clickelementwithname(acreport.selectReportType, "Reopening Report Type dropdown");
            wait.until(ExpectedConditions.visibilityOfElementLocated(acreport.getReportTypeOptionsLocator()));

            int attempts = 0;
            while (attempts < 4 && !matchFound) {
                attempts++;
                List<WebElement> options = acreport.getReportTypeList();
                for (WebElement option : options) {
                    try {
                        String optionText = option.getText().trim();
                        if (optionText.equalsIgnoreCase(reportType) ||
                                optionText.toLowerCase().contains(reportType.toLowerCase())) {

                            // Sometimes the option is not clickable directly - use JS if necessary
                            wait.until(ExpectedConditions.elementToBeClickable(option));
                            try {
                                option.click();
                            } catch (Exception clickEx) {
                                // fallback to JS click
                                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                            }

                            // Now wait for Generate button to be clickable (ensures selection processed)
                            try {
                                wait.until(ExpectedConditions.elementToBeClickable(acreport.generatebtn));
                            } catch (Exception e) {
                                // continue anyway - generate may still be present
                            }

                            // Call corresponding method (normalized)
                            String key = reportType.toLowerCase();
                            switch (key) {
                                case "echo":
                                case "echo report":
                                    acreport.EchoReport();
                                    break;
                                case "signon":
                                case "sign-on":
                                    acreport.SignOnReport();
                                    break;
                                case "signoff":
                                case "sign-off":
                                    acreport.SignOffReport();
                                    break;
                                case "database":
                                    acreport.DatabaseReport();
                                    break;
                                default:
                                    System.out.println("No method defined for: " + reportType);
                            }

                            // After handling result, ensure results modal is closed before next iteration
                            try {
                                wait.until(ExpectedConditions.invisibilityOfElementLocated(acreport.getResultsModalContainer()));
                            } catch (Exception ex) {
                                // try to close modal if still present
                                try {
                                    if (acreport.resultsClosebtn.isDisplayed()) {
                                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", acreport.resultsClosebtn);
                                        wait.until(ExpectedConditions.invisibilityOfElementLocated(acreport.getResultsModalContainer()));
                                    }
                                } catch (Exception ignore) {
                                }
                            }

                            // small pause until selectReportType is clickable again (ensures page stable)
                            wait.until(ExpectedConditions.elementToBeClickable(acreport.selectReportType));

                            matchFound = true;
                            break;
                        }
                    } catch (StaleElementReferenceException sere) {
                        // refetch options in next attempt
                        System.out.println("Stale option encountered for '" + reportType + "' - retrying (attempt " + attempts + ")");
                        break;
                    } catch (Exception ex) {
                        System.out.println("Exception while processing option '" + reportType + "': " + ex.getMessage());
                    }
                } // for options
            } // retry attempts

            if (!matchFound) {
                System.out.println("Report type not found in dropdown: " + reportType);
            }

            ExtentReport.resetToParent();
        } // for each configured report type
    } // test
}
