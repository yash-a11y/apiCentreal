package com.example.apiCentreal.tellus.service;

import com.example.apiCentreal.tellus.model.TelusPlan;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanScrapingService {

    public List<TelusPlan> scrapeTelusPlans() {
        List<TelusPlan> telusPlans = new ArrayList<>();
        // Set ChromeOptions to enable headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Optional: Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);  // Initiate ChromeDriver with options

//        WebDriver driver = new ChromeDriver();
        try {
            // Open the Telus homepage
            driver.get("https://www.telus.com/en");
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for the cookie banner and close it if present
            try {
                WebElement cookieBannerAcceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("close-cookies-notice-banner-en-desktop")));
                cookieBannerAcceptButton.click();
            } catch (Exception e) {
                System.out.println("No cookie banner displayed or already handled.");
            }

            // Navigate to Mobility section and Plans
            WebElement mobilityButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#main-nav-list-item-0")));
            mobilityButton.click();



            WebElement plansLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#\\32 BbaY5q5pcNDHnTG33q3eyZm5CgUTsycaGQMEwUgYI4")));

            // Scroll to the element using JavaScript Executor
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", plansLink);
            Thread.sleep(500); // Add a short delay to allow scrolling to complete

            plansLink.click();
            Thread.sleep(500);Thread.sleep(500);Thread.sleep(500);
            // Retrieve plan elements
            List<WebElement> planElements = driver.findElements(By.className("component_planPadding__s746_"));
            for (WebElement planElement : planElements) {
                String planText = planElement.getText();
                //System.out.println(planText);
                TelusPlan plan = parsePlanText(planText);
                telusPlans.add(plan);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return telusPlans;
    }

    private TelusPlan parsePlanText(String planText) {
        String[] details = planText.split("\n");  // Split details by line
        String planName = details.length > 0 ? details[0] : "";
        String planPrice = details.length > 3 ? details[3] : "";
        String planType = details.length > 2 ? details[2] : "";
        String planDetails = details.length > 5 ? details[5] : "";

        return new TelusPlan(planType, planName, planDetails, planPrice);
    }
}
