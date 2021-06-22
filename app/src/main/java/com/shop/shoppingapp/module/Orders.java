package com.shop.shoppingapp.module;

public class Orders  {

    String Image , UserId , Title , Arrive , Price , OrderId , ProductId ;

    public Orders () {}
    public Orders (String Image , String UserId , String Title , String Arrive , String Price , String OrderId , String ProductId){
        this.Image = Image ;
        this.UserId = UserId ;
        this.Title = Title ;
        this.Arrive = Arrive ;
        this.Price = Price ;
        this.OrderId = OrderId;
        this.ProductId = ProductId ;
    }
    public String getProductId(){return ProductId ;}
    public void setProductId(String ProductId){
        this.ProductId = ProductId ;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArrive() {
        return Arrive;
    }

    public void setArrive(String arrive) {
        Arrive = arrive;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }



}
