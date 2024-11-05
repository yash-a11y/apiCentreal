package com.example.apiCentreal.fido.API.modules;//package com.example.Fido_API.scraping.modules;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.TimeUnit;
//
//public class ScrapingWin {
//
//
//
//    public ArrayList window(int action){
//        // ChromeDriver automatically through webDriverManger
//
//        ArrayList arr = new ArrayList();
//        WebDriverManager.chromedriver().setup();
//
//        Scanner sc = new Scanner(System.in);
//
//        // initialize WebDriver
//        WebDriver driver = new ChromeDriver();
//
//        //initialize webDriverWait and set wait duration of 8 seconds.
//        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(8));
//
////        //initialize Search class object
////        Search search = new Search(driver);
//
//        //initialize Plans class object
////        Plans plans = new Plans(driver);
//
//        //initialize Device class object
//        Device device = new Device(driver);
//
////        //initialize BringOwn class object
////        BringOwn bringOwn = new BringOwn(driver);
//
//
//
//        // set up try and catch block for exception handling.
//        try {
//
//            // navigate to the webpage
//            driver.get("https://www.fido.ca/");
//
//            // maximize the window for better visibility
//            driver.manage().window().maximize();
//
//            // main menu takes users response and perform crawling for specific task
////            mainMenu mainMenu = new mainMenu(driver,plans,device);
//            arr = mainMenu.getMenu(action);
//
//        }
//        catch (InterruptedException ex)
//        {
//            ex.printStackTrace();
//        }
//        finally {
//            //close driver
//            driver.quit();
//        }
//
//        return  arr;
//    }
//    }
//
