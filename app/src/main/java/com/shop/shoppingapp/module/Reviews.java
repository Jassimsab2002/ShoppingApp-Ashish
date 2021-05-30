package com.shop.shoppingapp.module;

public class Reviews {

    String Name , Comment , Rating;

    public Reviews(String Name , String Comment , String Rating){
        this.Name = Name ;
        this.Comment = Comment ;
        this.Rating = Rating ;
    }

    public Reviews(){}

    public String getName() {
        return Name;
    }

    public Reviews setName(String name) {
        Name = name;
        return this;
    }

    public String getComment() {
        return Comment;
    }
    public void setComment(String Comment){
        this.Comment = Comment ;
    }
    public String getRating() {
        return Rating;
    }

    public Reviews setRating(String rating) {
        Rating = rating;
        return this;
    }
}
