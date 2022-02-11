package edu.neu.madcourse.assignment1;

public class ItemCard {

    private final int imageSource;
    private final String itemName;
    private final String itemDesc;


    //Constructor
    public ItemCard(int imageSource, String itemName, String itemDesc) {
        this.imageSource = imageSource;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
    }

    //Getters for the imageSource, itemName and itemDesc
    public int getImageSource() {
        return imageSource;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemName() {
        return itemName ;
    }





}