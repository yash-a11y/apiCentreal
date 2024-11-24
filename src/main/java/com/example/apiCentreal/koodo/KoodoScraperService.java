package com.example.apiCentreal.koodo;


import io.github.bonigarcia.wdm.WebDriverManager;
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

    }

    public List<KoodoPlan> scrapePrepaidPlans() {
        List<KoodoPlan> plans = new ArrayList<>();
        WebDriver driver = null;

        try {
            // Initialize WebDriver here
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);

            // Rest of your scraping code...

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            if (driver != null) {
                driver.quit();
            }
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