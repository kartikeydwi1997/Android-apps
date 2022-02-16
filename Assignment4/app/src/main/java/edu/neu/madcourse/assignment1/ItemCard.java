package edu.neu.madcourse.assignment1;

public class ItemCard {

    private final int imageSource;
    private final String itemShortName;
    private final String itemUrl;


    //Constructor
    public ItemCard(int imageSource, String itemShortName, String itemUrl) {
        this.imageSource = imageSource;
        this.itemShortName = itemShortName;
        this.itemUrl = itemUrl;
    }

    //Getters for the imageSource, getItemShortName and getItemUrl
    public int getImageSource() {
        return imageSource;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public String getItemShortName() {
        return itemShortName ;
    }





}