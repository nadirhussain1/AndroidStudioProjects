package veripark.billcalculator.models;

/**
 * Created by nadirhussain on 12/11/2015.
 */
public class Category {


    private int id;
    private int rate;
    private int limit;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
