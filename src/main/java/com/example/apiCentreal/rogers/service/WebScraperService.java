package com.example.apiCentreal.rogers.service;


import com.example.apiCentreal.rogers.models.Plan;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WebScraperService {

    private WebDriver initializeWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Optional: Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }
    public List<Plan> scrapePlans() {
        List<Plan> plans = new ArrayList<>();
        WebDriver driver = initializeWebDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.get("https://www.rogers.com");

//            // Search for "mobile plans"
//            WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ge-search")));
//            search.sendKeys("mobile plans");
//            search.sendKeys(Keys.RETURN);

//            // Wait for page to load
//            wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(driver.getCurrentUrl())));
//            driver.navigate().back();

            // Scrape 5G Mobile Plans
            navigateToPlansPage(driver, wait);
            List<Plan> fiveGPlans = scrapePlans(driver, "5G Mobile Plan", "/plans?icid=R_WIR_CMH_6WMCMZ");
            plans.addAll(fiveGPlans);

            // Scrape Prepaid Plans
            navigateToPlansPage(driver, wait);
            List<Plan> prepaidPlans = scrapePlans(driver, "Prepaid Plan", "/plans/prepaid?icid=R_WIR_CMH_EIZF9L");
            plans.addAll(prepaidPlans);

//            // Scrape Bring Your Own Device Plans
//            navigateToPlansPage(driver, wait);
//            WebElement mobilePlansLink = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("a[href='/phones/bring-your-own-device?icid=R_WIR_CMH_PL5IQK']")));
//            mobilePlansLink.click();

//            // Close modal popup if it appears
//            WebElement modalCloseButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("ds-modal__closeButton")));
//            modalCloseButton.click();

            // Add any additional scraping logic as needed

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return plans;
    }

//    public List<Phone> scrapePhones() {
//        List<Phone> phones = new ArrayList<>();
//        WebDriver driver = initializeWebDriver();
//        driver.manage().window().maximize();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
//
//        try {
//            driver.get("https://www.rogers.com");
//
//            // Navigate to Phones page
//            navigateToPhonesPage(driver, wait);
//            WebElement phoneLink = wait.until(ExpectedConditions.elementToBeClickable(
//                    By.cssSelector("a[href='/phones/?icid=R_WIR_CMH_GJJPYK']")));
//            phoneLink.click();
//
//            // Scrape Smartphones
//            phones = scrapeSmartphones(driver, wait);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit();
//        }
//
//        return phones;
//    }

    private void navigateToPlansPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // Click on "Mobile"
        WebElement mobileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("geMainMenuDropdown_0")));
        mobileLink.click();

        // Click on "Plans"
        WebElement plansLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[aria-label='Plans']")));
        plansLink.click();

        // Wait briefly to ensure the plans page loads
        Thread.sleep(2000);
    }

    private void navigateToPhonesPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
        // Click on "Mobile"
        WebElement mobileLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("geMainMenuDropdown_0")));
        mobileLink.click();

//        WebElement phoneLink = wait.until(ExpectedConditions.elementToBeClickable(
//                By.cssSelector("a[href='/phones/?icid=R_WIR_CMH_GJJPYK']")));
//        phoneLink.click();
        // Click on "Phones"
        WebElement phoneLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[aria-label='Phones']")));
        phoneLink.click();

        // Wait briefly to ensure the page loads
        Thread.sleep(2000);
    }

    private List<Plan> scrapePlans(WebDriver driver, String planType, String planUrl) {
        List<Plan> plans = new ArrayList<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.rogers.com" + planUrl);

        // Wait for the plans section to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dsa-vertical-tile__top")));

        // Define selectors based on plan type
        String planTileClass = "dsa-vertical-tile__top";
        String planNameClass = "dsa-vertical-tile__heading";
        String priceCss = "span.sr-only";
        String priceClass = "ds-price__amountDollars";

        String planDetailsCss = null;
        String planDetailsClass = null;

        if (planType.equals("5G Mobile Plan")) {
            planDetailsCss = "p.dsa-vertical-tile__highlightBody ul>li";
        } else if (planType.equals("Prepaid Plan")) {
            planDetailsClass = "dsa-vertical-tile__highlight";
        }

        // Extract plans
        List<WebElement> planElements = driver.findElements(By.className(planTileClass));

        for (WebElement planElement : planElements) {
            try {
                String planName = planElement.findElement(By.className(planNameClass)).getText();
                StringBuilder planDetails = new StringBuilder();

                if (planType.equals("5G Mobile Plan")) {
                    List<WebElement> dataElements = planElement.findElements(By.cssSelector(planDetailsCss));
                    for (WebElement dataElement : dataElements) {
                        planDetails.append(dataElement.getText()).append("; ");
                    }
                } else if (planType.equals("Prepaid Plan")) {
                    List<WebElement> dataElements = planElement.findElements(By.className(planDetailsClass));
                    for (WebElement dataElement : dataElements) {
                        planDetails.append(dataElement.getText()).append("; ");
                    }
                }

                String price = planElement.findElement(By.className(priceClass)).getText();
                Plan plan = new Plan(planType, planName, planDetails.toString().replace(",", ";"), price);
                plans.add(plan);
            } catch (NoSuchElementException e) {
                // Handle missing elements if necessary
                e.printStackTrace();
            }
        }

        return plans;
    }

//    private List<Phone> scrapeSmartphones(WebDriver driver, WebDriverWait wait) throws InterruptedException {
//        List<Phone> phones = new ArrayList<>();
//
//        // Scroll to the bottom to trigger lazy loading
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
//
//        // Wait for smartphones section to load
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("dsa-nacTile")));
//
//        // Define selectors
//        String phoneTileClass = "dsa-nacTile";
//        String phoneNameClass = "text-title-5";
//        String priceCss = "div.ds-price";
//        String conditionClass = "text-body-sm";
//        String fullPriceCss = "p.text-body-sm.mb-0.text-semi";
//        String imageXpath = ".//picture/img"; // Relative XPath
//
//        List<WebElement> phoneElements = driver.findElements(By.className(phoneTileClass));
//
//        for (WebElement phoneElement : phoneElements) {
//            try {
//                String phoneName = phoneElement.findElement(By.className(phoneNameClass)).getText();
//                String price = phoneElement.findElement(By.cssSelector(priceCss)).getAttribute("aria-label");
//                String condition = phoneElement.findElement(By.className(conditionClass)).getText();
//                condition = condition.contains("Save & Return") ? "Save & Return" : "Pay with the Bill";
//                String fullPrice = phoneElement.findElement(By.cssSelector(fullPriceCss)).getText().replaceAll("[^\\d.]", "");
//
//                // Extract image URL
//                WebElement imgElement = phoneElement.findElement(By.xpath(imageXpath));
//                js.executeScript("arguments[0].scrollIntoView(true);", imgElement);
//                String imageUrl = imgElement.getAttribute("src");
//
//                Phone phone = new Phone(phoneName, price, condition, "$" + fullPrice, imageUrl);
//                phones.add(phone);
//            } catch (NoSuchElementException e) {
//                // Handle missing elements if necessary
//                e.printStackTrace();
//            }
//        }
//
//        return phones;
//    }
}
