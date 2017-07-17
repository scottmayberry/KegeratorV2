package com.example.sctma.kegeratorv1;

/**
 * Created by SMAYBER8 on 7/12/2017.
 */

public class KegInfo {
    private String name;
    private String kegSize;
    private String style;
    private double totalPrice;
    private double spent;
    private double fee;
    private double savings;
    private double beersLeft;
    private String purchaser;
    private boolean active;

    private double pricePerBeer;
    private double pricePerOunce;

    public KegInfo()
    {}

    public KegInfo(String name, String kegSize, String style, double spent, double fee, double savings, String purchaser, boolean active) {
        this.name = name;
        this.kegSize = kegSize;
        this.style = style;
        this.spent = spent;
        this.fee = fee;
        this.savings = savings;
        this.purchaser = purchaser;
        this.active = active;
        setPrices();
        beersLeft = kegSizeToBeers(kegSize);
    }

    public double getPricePerBeer() {
        return pricePerBeer;
    }

    public double getPricePerOunce() {
        return pricePerOunce;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public double getBeersLeft() {
        return beersLeft;
    }

    public void setBeersLeft(double beersLeft) {
        this.beersLeft = beersLeft;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKegSize() {
        return kegSize;
    }

    public void setKegSize(String kegSize) {
        this.kegSize = kegSize;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void setPrices() {
        this.totalPrice = this.spent + this.fee + this.savings;
        double kegSizeD = kegSizeToBeers(kegSize);
        pricePerBeer = totalPrice/kegSizeD;
        pricePerOunce = pricePerBeer/12.0;
    }
    public static double kegSizeToBeers(String kegSize)
    {
        switch(kegSize)
        {
            case "Half": return 165;
            case "Cornelius": return 53;
            case "Quarter": return 82;
            case "Sixth": return 55;
        }
        return 165;
    }
    public static int kegSizeToSpinnerPosition(String kegSize)
    {
        switch(kegSize)
        {
            case "Half": return 0;
            case "Cornelius": return 1;
            case "Quarter": return 2;
            case "Sixth": return 3;
        }
        return 0;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
        setPrices();
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
        setPrices();
    }

    public double getSavings() {
        return savings;
    }

    public void setSavings(double savings) {
        this.savings = savings;
        setPrices();
    }

    public String getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(String purchaser) {
        this.purchaser = purchaser;
    }

    public String getRoundedBeersLeft()
    {
        return "" + (Math.round(beersLeft*100)/100);
    }
    public String getRoundedPricePerBeer()
    {
        return "" + (Math.round(pricePerBeer*100.0)/100.0);
    }
    public String getRoundedPricePerOunce()
    {
        return "" + (Math.round(pricePerOunce*1000.0)/1000.0);
    }
}