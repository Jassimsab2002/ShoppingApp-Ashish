package com.shop.shoppingapp.module;

public class Notification {
    String Title , Body ;
    public Notification(String Title , String Body){

    }

    public Notification(){}

    public String getTitle() {
        return Title;
    }

    public Notification setTitle(String title) {
        Title = title;
        return this;
    }

    public String getBody() {
        return Body;
    }

    public Notification setBody(String body) {
        Body = body;
        return this;
    }
}
