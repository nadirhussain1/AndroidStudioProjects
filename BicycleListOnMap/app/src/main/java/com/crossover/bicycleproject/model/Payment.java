
package com.crossover.bicycleproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Payment {

    public Payment(String number,String name){
        this.number=number;
        this.name=name;
        this.expiration="11/20";
        this.code="754";
    }
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("expiration")
    @Expose
    private String expiration;
    @SerializedName("code")
    @Expose
    private String code;

    /**
     * 
     * @return
     *     The number
     */
    public String getNumber() {
        return number;
    }

    /**
     * 
     * @param number
     *     The number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The expiration
     */
    public String getExpiration() {
        return expiration;
    }

    /**
     * 
     * @param expiration
     *     The expiration
     */
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

}
