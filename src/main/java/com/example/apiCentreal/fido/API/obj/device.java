package com.example.apiCentreal.fido.API.obj;

public class device {


    String title;
    String photo;
    String price;
    String detail;


    //title.getText(),photo,priceStr,stringBuilder
    public device() {
    }


    public device(String title,String photo, String price, String detail) {
        this.photo = photo;
        this.title = title;
        this.price = price;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
