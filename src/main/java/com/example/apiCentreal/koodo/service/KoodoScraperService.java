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
import java.util.List;

@Service
public class KoodoScraperService {

    private WebDriver driver;

    public KoodoScraperService() {
        // Set the path to your ChromeDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Enable headless mode
        options.addArguments("--window-size=1920,1080");  // Set a window size for consistent rendering
        this.driver = new ChromeDriver(options);
    }

    public List<KoodoPlan> scrapePrepaidPlans() {
        List<KoodoPlan> plans = new ArrayList<>();

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
                System.out.println("Extracting plans for: " + planType);

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
        } finally {
            // Close the browser
            driver.quit();
        }

        return plans;
    }

    private List<KoodoPlan> extract4GBasePlans(String planType) {
        List<KoodoPlan> plans = new ArrayList<>();

        List<WebElement> planCards = driver.findElements(By.cssSelector(".KDS_Item-modules__item___1fILq"));
        for (WebElement planCard : planCards) {
            String planDetails = planCard.findElements(By.cssSelector(".KDS_Item-modules__item__button___In_p4"))
                    .get(0).findElement(By.cssSelector(".KDS_Item-modules__item__wrapper--hover___10TpD"))
                    .findElement(By.cssSelector(".KDS_Item-modules__item__highlights___3blvQ"))
                    .findElement(By.cssSelector(".KDS_Item-modules__children__wrapper___3a1Xn")).getText();

            String[] lines = planDetails.split("\n");
            String price = lines[1] + lines[2]; // Combine parts for price
            String validity = lines[3];
            StringBuilder detailsBuilder = new StringBuilder();
            for (int i = 4; i < lines.length; i++) {
                if (i > 4) {
                    detailsBuilder.append(", "); // Add a comma before appending further details
                }
                detailsBuilder.append(lines[i]);
            }
            String details = detailsBuilder.toString();

            // Add to the list
            KoodoPlan plan = new KoodoPlan(planType,validity, details,price);
            plans.add(plan);
        }

        return plans;
    }

    private List<KoodoPlan> extract360DayPlans(String planType) {
        List<KoodoPlan> plans = new ArrayList<>();

        List<WebElement> planCards = driver.findElements(By.cssSelector(".KDS_Item-modules__item___1fILq"));
        for (WebElement planCard : planCards) {
            String planDetails = planCard.findElements(By.cssSelector(".KDS_Item-modules__item__button___In_p4"))
                    .get(0).findElement(By.cssSelector(".KDS_Item-modules__item__wrapper--hover___10TpD"))
                    .findElement(By.cssSelector(".KDS_Item-modules__item__highlights___3blvQ"))
                    .findElement(By.cssSelector(".KDS_Item-modules__children__wrapper___3a1Xn")).getText();

            String[] lines = planDetails.split("\n");
            String price =  lines[1] + lines[2]; // Combine parts for price
            String validity = lines[3];
            StringBuilder detailsBuilder = new StringBuilder();
            for (int i = 4; i < lines.length; i++) {
                if (i > 4) {
                    detailsBuilder.append(", "); // Add a comma before appending further details
                }
                detailsBuilder.append(lines[i]);
            }
            String details = detailsBuilder.toString();

            // Add to the list
            KoodoPlan plan = new KoodoPlan(planType,validity, details,price);
            plans.add(plan);
        }

        return plans;
    }

    private List<KoodoPlan> extractBoosterAddons(String planType) {
        List<KoodoPlan> plans = new ArrayList<>();

        List<WebElement> planCards = driver.findElements(By.cssSelector(".KDS_Item-modules__item___1fILq"));
        for (WebElement planCard : planCards) {
            String planDetails = planCard.findElements(By.cssSelector(".KDS_Item-modules__item__button___In_p4"))
                    .get(0).findElement(By.cssSelector(".KDS_Item-modules__item__wrapper--hover___10TpD"))
                    .findElement(By.cssSelector(".KDS_Item-modules__item__highlights___3blvQ"))
                    .findElement(By.cssSelector(".KDS_Item-modules__children__wrapper___3a1Xn")).getText();
            String[] lines = planDetails.split("\n");
            String price = lines[1]; // Combine parts for price
            String validity = "NA";
            String details = lines[2];

            // Add to the list
            KoodoPlan plan = new KoodoPlan(planType,validity, details,price);
            plans.add(plan);
        }

        return plans;
    }
}
