package com.test;

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

public class Reports_AdministrationRequestTest extends BaseClass {

    @Test
    public void AdministrationRequest_TCH() throws InterruptedException {
        ExtentReport.createTest("Administration Request Summary TCH Test");
        acreport = new Reports_AdministrationRequestPage(driver);

        scrollByVisibilityOfElement(driver, acreport.Reports);
        visibleofele(driver, acreport.MenuListAdmin, "Administration");
        JSClick(driver, acreport.MenuListAdmin, "Navigating to Administration Request");

        String raw = prop.getProperty("TCHReportType");
        if (raw == null || raw.trim().isEmpty()) {
            System.out.println("TCHReportType property is empty or missing");
            return;
        }

        String[] reportTypesFromConfig = raw.split(",");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        for (String r : reportTypesFromConfig) {
            String reportType = r == null ? "" : r.trim();
            if (reportType.isEmpty()) continue;

            ExtentReport.createChildTest(reportType);

            boolean matchFound = false;

            // Reopen the dropdown for each report type
            clickelementwithname(acreport.selectReportType, "Reopening Report Type dropdown");

            // wait for options to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(acreport.getReportTypeOptionsLocator()));

            int attempts = 0;
            while (attempts < 3 && !matchFound) {
                attempts++;
                List<WebElement> options = acreport.getReportTypeList();

                for (WebElement option : options) {
                    try {
                        String optionText = option.getText().trim();
                        if (optionText.equalsIgnoreCase(reportType) ||
                                optionText.toLowerCase().contains(reportType.toLowerCase())) {

                            wait.until(ExpectedConditions.elementToBeClickable(option));
                            option.click();

                            // Wait for report result to appear or for loader to disappear
                            wait.until(ExpectedConditions.or(
                                    ExpectedConditions.visibilityOfElementLocated(acreport.getReportResultLocator()),
                                    ExpectedConditions.invisibilityOfElementLocated(acreport.getLoadingSpinnerLocator())
                            ));

                            // Dispatch to correct report method using normalized key
                            String key = reportType.toLowerCase();
                            switch (key) {
                                case "echo":
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

                            // wait for any final loader to disappear (safety)
                            wait.until(ExpectedConditions.invisibilityOfElementLocated(acreport.getLoadingSpinnerLocator()));

                            // If you must refresh between reports, do it here and then re-open menu & re-fetch
                            // driver.navigate().refresh();
                            matchFound = true;
                            break;
                        }
                    } catch (StaleElementReferenceException sere) {
                        System.out.println("Stale element - refetching options for: " + reportType);
                        break; // will cause re-fetch in the next attempt
                    } catch (Exception ex) {
                        System.out.println("Exception while selecting option: " + ex.getMessage());
                    }
                }
            } // end retry attempts

            if (!matchFound) {
                System.out.println("Report type not found in dropdown: " + reportType);
            }

            ExtentReport.resetToParent();
        } // end for reportTypes
    }
}
