package com.example.apiCentreal.fido.API.modules;


import com.example.apiCentreal.fido.API.obj.device;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Device {

//    private WebDriver driver;
//
//
//    public void deviceDetails(WebElement choice,String filter)
//    {
//
//
//    }
//    public Device(WebDriver driver)
//    {
//        this.driver = driver;
//
//    }


    public ArrayList<device> getDevices() throws InterruptedException {


        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");


        WebDriverManager.chromedriver().setup();

        Scanner sc = new Scanner(System.in);

        // initialize WebDriver
        WebDriver driver = new ChromeDriver(options);

        //initialize webDriverWait and set wait duration of 8 seconds.


        ArrayList<device> devices = new ArrayList<device>();



        driver.get("https://www.fido.ca/");

        // maximize the window for better visibility
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));


        // locate the Devices dropdown section using css selector
        WebElement devicesDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#geMainMenuDropdown_1 " +
                ".has-dropdown a")));
        // perform click
        devicesDropdown.click();


        // locate and perform click on smartphone option
        // traverse to new web page through hyperlink
        WebElement smartPhone = driver.findElement(By.cssSelector("#geMainMenuDropdown_1 .has-dropdown ul.list-none > li:nth-child(1)"));
        smartPhone.click();


        // locate mobile brands checkboxes and store it as list of webElement
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ds-checkbox.ds-checkbox")));
        List<WebElement> brands = driver.findElements(By.cssSelector("ds-checkbox.ds-checkbox input[type='checkbox']"));

        //set up implicitWait because crawling of checkboxes takes time
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

//        // prompt user to select any brand from below
//        System.out.println("Apply filter : \n1 : New\n" +
//                "2 : Certified Pre-owned\n" +
//                "3 : Samsung\n" +
//                "4 : Apple\n"+
//                "5 : Google\n" +
//                "6 : Motorola\n" +
//                "7 : TCL\n" +
//                "8 : Smartphone\n" +
//                "9 : Basic Phones\n");
//
//        int filter = sc.nextInt();


        // select user specified brand
        WebElement choice = brands.get(4);
        int i = 0;

        if(!choice.isSelected())
        {
            // exception handling
            // set up fileWriter to store crawl data of phones in .csv file
            try{

                // initialize js executor
                JavascriptExecutor jsex = (JavascriptExecutor) driver;

                // initialize CSVPrinter for proper csv file format
//                CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("Title","link","Price","Details"));
//
//                // remove previous content
//                fileWriter.flush();

                // wait for checkbox visibility using explicit wait to overcome "item not found" error
                wait.until(ExpectedConditions.visibilityOf(choice));

                // perform click on checkbox
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", choice);

                // crawl list of phones
                List<WebElement> phoneList = driver.findElements(By.cssSelector("div.dsa-nacTile__top"));

                // crawl list of phone details
                List<WebElement> phoneDetails = driver.findElements(By.cssSelector("div.dsa-nacTile__deviceInfo"));

                for(int j = 0; j< phoneDetails.size(); j++)
                {

                    // use window scroll to solve error of lazy loading image
                    jsex.executeScript("window.scrollTo(0, (document.body.scrollHeight)/3);");
                    Thread.sleep(2000);


                    WebElement title = phoneList.get(j).findElement(By.cssSelector("div.px-24.mb-24.pt-24"));
                    String photo = phoneList.get(j).findElement(By.xpath("//*[@id=\"mainContent\"]/ng-component/r-choose-phone/dsa-layout/div[2]/div/div[3]/div[2]/div[1]/r-nac-tile/ds-tile/div/div/div[1]/ds-picture/span/picture/img")).getAttribute("src");
                    WebElement price = phoneDetails.get(j).findElement(By.cssSelector("div.ds-price"));
                    String priceStr = price.getAttribute("aria-label");
                    List<WebElement> details = phoneDetails.get(j).findElements(By.cssSelector("div.mt-16"));


                    StringBuilder stringBuilder = new StringBuilder("");

                    for(WebElement detail : details)
                    {
                        stringBuilder.append(detail.getText()).append(",");
                    }

                    devices.add(new device(title.getText(),photo,priceStr,stringBuilder.toString()));

//                    csvPrinter.printRecord(title.getText(),photo,priceStr,stringBuilder);


                }

//                fileWriter.close();

            }
            catch (ElementClickInterceptedException e)
            {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);

            }



        }


        return devices;


    }









}