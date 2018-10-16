package com.example.toolbardemo;

public class FruitBean {

    private String fruitName;
    private int fruitId;

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public int getFruitId() {
        return fruitId;
    }

    public void setFruitId(int fruitId) {
        this.fruitId = fruitId;
    }

    public FruitBean(String fruitName, int fruitId) {
        this.fruitName = fruitName;
        this.fruitId = fruitId;
    }
}
