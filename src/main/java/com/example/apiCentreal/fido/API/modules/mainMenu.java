package com.example.apiCentreal.fido.API.modules;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public class mainMenu {

    private WebDriver driver;

    private Plans plans;
    private Device device;

    public mainMenu(WebDriver webDriver, Plans plans
            , Device device)
    {
        this.driver = webDriver;
        this.plans = plans;
        this.device = device;

    }

    public ArrayList getMenu(int action) throws InterruptedException {

        ArrayList arr = new ArrayList();
        switch (action)
        {

            case 1:
            {
                 arr = plans.plans();
                System.out.println("plans details are scraped successfully, and stored in CSV file...");
            }
            break;
            case 2:
            {

                device.getDevices();
                System.out.println("Devices details are scraped successfully, and stored in CSV file...");

            }
            break;

            default:{System.out.println("please select valid option");}
        }
        return arr;
    }
}