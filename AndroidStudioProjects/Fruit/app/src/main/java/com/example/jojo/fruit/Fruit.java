package com.example.jojo.fruit;

public class Fruit {

    private String mName;
    private int mPrice;
    private int mWeight;

    /**
     * This constructor take parameters for a Fruit and creates a
     * Fruit instance when called.
     * @param name The name of the fruit.
     * @param price The price of the fruit (in pence).
     * @param weight The weight of the fruit (in grams).
     */
    public Fruit(String name, int price, int weight) {
        mName = name;
        mPrice = price;
        mWeight = weight;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public double getWeight() {
        return mWeight;
    }
}
