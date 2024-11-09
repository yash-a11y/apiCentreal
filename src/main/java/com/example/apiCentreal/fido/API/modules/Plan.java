package com.example.apiCentreal.fido.API.modules;


import com.example.apiCentreal.fido.API.obj.plan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Plan {



    public  ArrayList<plan> plans()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");


        ArrayList<plan> finalPlans = new ArrayList<>();

        WebDriverManager.chromedriver().setup();

        Scanner sc = new Scanner(System.in);

        // initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        //initialize webDriverWait and set wait duration of 8 seconds.


        ArrayList<plan> plansArr = new ArrayList<plan>();

        List<String> planDetail = new ArrayList<>();
        planDetail.add("Voicemail (up to 3 messages)2\n" +
                "Unlimited incoming text, picture and video messaging in Canada3\n" +
                "Call Display with Name Display\n" +
                "4G LTE network speeds so you can call, text, or stream with coverage that keeps up with you4\n" +
                "Plan features not available with Tablet Data-only Plans");

        planDetail.add("Voicemail (up to 3 messages)2\n" +
                "Unlimited incoming text, picture and video messaging in Canada3\n" +
                "Call Display with Name Display");
        planDetail.add("3G network speeds for light browsing and for standard definition video quality when streaming9\n" +
                "Voicemail (up to 3 messages)2\n" +
                "Unlimited incoming text, picture and video messaging in Canada3\n" +
                "Call Display\n" +
                "You must Bring Your Own Phone to use Fido Basic plans.");


        try {
            driver.get("https://www.fido.ca/");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // maximize the window for better visibility
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // locate dropdown menu on home page
            WebElement dropdown = driver.findElement(By.cssSelector("#geMainMenuDropdown_0 " +
                    ".has-dropdown a"));

            // perform click on it
            dropdown.click();

            // select prepaid plan option from dropdown menu
            WebElement prepaid = driver.findElement(By.cssSelector("#geMainMenuDropdown_0 .has-dropdown ul.list-none > li:nth-child(1)"));

            // perform click on it
            prepaid.click();

            // Implicit wait

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cdk-overlay-0")));

            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("trident-cta-byod")));
            button.click();

            // initialize try and catch block for exception handling while write data in csv file

//            // remove previous data
//            fileWriter.flush();

            // crawl list of plans WebElement
//            List<WebElement> plans = driver.findElements(By.cssSelector("div.dsa-vertical-tile__top"));
//            WebElement element = driver.findElement(By.cssSelector("div.step-content.ng-star-inserted"));

            WebElement tab = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ds-stepper-id-1\"]/ds-step[1]/div/div[1]/div/div")));
            js.executeScript("window.scrollTo(0, arguments[0].getBoundingClientRect().top + window.scrollY - 100);", tab);


            for(int i=0;i<3;i++)
            {
                String detail = planDetail.get(i);
                WebElement b2 = driver.findElement(By.id("ds-tabs-0-tab-"+i));
                b2.click();

                Thread.sleep(5000);



                List<WebElement> newplans = driver.findElements(By.cssSelector("div.p-16 .p-md-24"));



                for(WebElement each: newplans)
                {
                    WebElement divElement = each.findElement(By.className("flex-column"));
                    WebElement pElement = divElement.findElement(By.tagName("p"));
                    WebElement price = each.findElement(By.className("ds-price__amountDollars"));


                    String name = pElement.getText();
                    String priceStr = price.getText();
                    System.out.print("Plan detail : "+ detail);

                    finalPlans.add(new plan(name,priceStr,detail));

                }
            }


        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }finally {
            driver.quit();
        }


        return finalPlans;



    }
}