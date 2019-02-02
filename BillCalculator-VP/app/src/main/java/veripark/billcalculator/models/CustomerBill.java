package veripark.billcalculator.models;

/**
 * Created by nadirhussain on 29/10/2015.
 */
public class CustomerBill {

    public CustomerBill(String serviceNumber, int reading) {
        this.serviceNumber = serviceNumber;
        this.reading = reading;
    }

    private int reading;
    private String serviceNumber;


    public int getReading() {
        return reading;
    }

    public void setReading(int reading) {
        this.reading = reading;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }
}
