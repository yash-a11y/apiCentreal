package com.example.apiCentreal.fido.API.modules;


import com.example.apiCentreal.fido.API.obj.plan;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Plans {






    public ArrayList<plan> plans()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");


        WebDriverManager.chromedriver().setup();

        Scanner sc = new Scanner(System.in);

        // initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        //initialize webDriverWait and set wait duration of 8 seconds.


        ArrayList<plan> plansArr = new ArrayList<plan>();


        try {
            driver.get("https://www.fido.ca/");

            // maximize the window for better visibility
            driver.manage().window().maximize();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

            // locate dropdown menu on home page
            WebElement dropdown = driver.findElement(By.cssSelector("#geMainMenuDropdown_0 " +
                    ".has-dropdown a"));

            // perform click on it
            dropdown.click();

            // select prepaid plan option from dropdown menu
            WebElement prepaid = driver.findElement(By.cssSelector("#geMainMenuDropdown_0 .has-dropdown ul.list-none > li:nth-child(5)"));

            // perform click on it
            prepaid.click();

            // Implicit wait
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

            // initialize try and catch block for exception handling while write data in csv file

//            // remove previous data
//            fileWriter.flush();

            // crawl list of plans WebElement
            List<WebElement> plans = driver.findElements(By.cssSelector("div.dsa-vertical-tile__top"));

            // iterate through each plan
            for (WebElement plan : plans) {
                // extract title webElement from plan div
                WebElement title = plan.findElement(By.cssSelector("p.dsa-vertical-tile__heading"));
                // title string
                String titleStr = title.getAttribute("innerHTML");
                // clean title string before store in csv file to maintain csv file format
                // use regex to replace specific character from string
                titleStr = titleStr.replaceAll("<br>", " ").replaceAll("<sup>.*?</sup>", "").replaceAll("<[^>]*>", "")
                        .replaceAll("&amp;", "&")
                        .replaceAll(",", "")
                        .trim();

                //extract price
                WebElement price = plan.findElement(By.cssSelector("span.sr-only"));
                String priceStr = price.getText();

                // perform writing operation in csv file
//
//                fileWriter.append(titleStr);
//                fileWriter.append(",");
//                fileWriter.append(priceStr);
//                fileWriter.append(",");


                StringBuilder included = new StringBuilder();

                // extract list of features
                List<WebElement> features = plan.findElements(By.cssSelector("ul.dsa-vertical-tile__highlights > li"));

                // iterate through each feature
                for (WebElement feature : features) {
                    // feature String
                    String featureStr = feature.getText().replaceAll(",", " ");
                    included.append(featureStr);

                }


//                fileWriter.append(included.toString());
                plansArr.add(new plan(titleStr, priceStr, included.toString()));
                System.out.println(titleStr);

            }
        }
        catch (Exception e)
        {

        }



        return plansArr;

    }
}