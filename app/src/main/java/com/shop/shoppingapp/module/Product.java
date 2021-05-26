package com.shop.shoppingapp.module;

public class Product {

    String Title , Price , StoreName , Description , ImageUrl , Details ;

    public Product (){}

    public Product (String Title , String Price , String StoreName , String Description , String ImageUrl , String Details ){
        this.Title = Title ;
        this.Price = Price ;
        this.StoreName = StoreName ;
        this.Description = Description ;
        this.Details = Details ;
        this.ImageUrl = ImageUrl ;
    }

    public String getImageUrl(){
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl){
        this.ImageUrl = ImageUrl ;
    }
    public String getTitle() {
        return Title;
    }

    public  String getDetails(){
        return Details ;
    }
    public void setDetails(String Details){
        this.Details = Details ;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }




}
