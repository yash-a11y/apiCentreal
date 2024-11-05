package com.example.apiCentreal.fido.API.obj;

import com.fasterxml.jackson.annotation.JsonProperty;

public class plan {
    public plan() {
    }


    private String name;
    private String price;
    private String details;


    public plan(String planName,String price,String details)
    {
        this.name = planName;
        this.price = price;
        this.details = details;
    }


    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPlanprice() {
        return price;
    }

    public void setPlanprice(String price) {
        this.price = price;
    }

    public String getPlanname() {
        return name;
    }

    public void setPlanname(String planName) {
        this.name = planName;
    }
}
