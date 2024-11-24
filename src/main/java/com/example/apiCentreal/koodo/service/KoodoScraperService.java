package com.example.apiCentreal.koodo.service;

import com.example.apiCentreal.koodo.model.KoodoPlan;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KoodoScraperService {

    private WebDriver driver;

    // Initialize the WebDriver
    private void initializeDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
            this.driver = new ChromeDriver(options);
        }
    }

    // Shutdown WebDriver
    public void shutdownDriver() {
        if (driver != null) {
            driver.quit();
            driver = null; // Nullify to allow reinitialization
        }
    }

    public List<KoodoPlan> scrapePrepaidPlans() {
        List<KoodoPlan> plans = new ArrayList<>();
        initializeDriver(); // Ensure driver is initialized

        try {
            // Navigate to the Koodo Mobile prepaid plans page
            driver.get("https://www.koodomobile.com/en/prepaid-plans");

            // Wait for the prepaid plans to load
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".app-container")));

            // Extracting plan types from tabs
            List<WebElement> tabs = driver.findElements(By.cssSelector(".KDS_Tabs-modules__tabButton___1RuCu"));
            for (WebElement tab : tabs) {
                String planType = tab.getText();

                // Click on the tab to load its content
                tab.click();

                // Extract based on plan type
                switch (planType) {
                    case "4G Base Plans":
                        plans.addAll(extract4GBasePlans(planType));
                        break;

                    case "360 Day Plans":
                        plans.addAll(extract360DayPlans(planType));
                        break;

                    case "Booster Add-ons":
                        plans.addAll(extractBoosterAddons(planType));
                        break;

                    default:
                        System.out.println("No specific extraction logic for: " + planType);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plans; // Do not quit driver here to allow reuse
    }

    private List<KoodoPlan> extract4GBasePlans(String planType) {
        List<KoodoPlan> plans = new ArrayList<>();

        List<WebElement> planCards = driver.findElements(By.cssSelector(".KDS_Item-modules__item___1fILq"));
        for (WebElement planCard : planCards) {
            String planDetails = planCard.findElement(By.cssSelector(".KDS_Item-modules__children__wrapper___3a1Xn")).getText();

            String[] lines = planDetails.split("\n");
            String price = lines[1] ;
            String validity = lines[3];
            String details = String.join(", ", List.of(lines).subList(4, lines.length));

            KoodoPlan plan = new KoodoPlan(planType, validity, details, price);
            plans.add(plan);
        }

        return plans;
    }

    private List<KoodoPlan> extract360DayPlans(String planType) {
        return extract4GBasePlans(planType); // Logic is the same, reuse the method
    }

    private List<KoodoPlan> extractBoosterAddons(String planType) {
        List<KoodoPlan> plans = new ArrayList<>();

        List<WebElement> planCards = driver.findElements(By.cssSelector(".KDS_Item-modules__item___1fILq"));
        for (WebElement planCard : planCards) {
            String planDetails = planCard.findElement(By.cssSelector(".KDS_Item-modules__children__wrapper___3a1Xn")).getText();

            // Split the details into lines
            String[] lines = planDetails.split("\n");

            // Debugging: Print the plan details and the resulting lines
            System.out.println("Plan Details: " + planDetails);
            System.out.println("Lines: " + Arrays.toString(lines));

            // Validate array length to avoid ArrayIndexOutOfBoundsException
            if (lines.length < 2) {
                System.out.println("Unexpected plan format: " + planDetails);
                continue; // Skip this card if the format is not as expected
            }

            // Safely extract fields
            String price = lines[1];
            String details = (lines.length > 2) ? lines[2] : "Details not available";

            // Create a KoodoPlan object and add it to the list
            KoodoPlan plan = new KoodoPlan(planType, "NA", details, price);
            plans.add(plan);
        }

        return plans;
    }

}
