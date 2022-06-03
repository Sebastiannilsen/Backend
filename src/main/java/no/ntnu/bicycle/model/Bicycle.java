package no.ntnu.bicycle.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Represents a resource: a bicycle.
 * This will be added in the database.
 */
@Entity
@Table(name = "bicycle")
public class Bicycle {
    @Id
    @GeneratedValue
    private long id;
    private String color;
    private String location;
    private int pricePerMinute;
    private String status;

    /**
     * Constructor with all parameters
     * @param color color of the bicycle
     * @param location location of the bicycle
     * @param pricePerMinute price per minute for the bicycle
     */
    public Bicycle(String color, String location, int pricePerMinute, String status) {
        this.color = color;
        this.pricePerMinute = pricePerMinute;
        this.location = location;
        this.status = status;
    }

    /**
     * Empty constructor
     */
    public Bicycle() {

    }

    /**
     * Getts the location
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location
     * @param location location to be set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the id
     * @return id
     */
    public long getId(){
        return this.id;
    }

    /**
     * Gets the color
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color
     * @param color color to be set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets the price per minute
     * @return price per minute
     */
    public int getPricePerMinute() {
        return pricePerMinute;
    }

    /**
     * Sets the price per minute
     * @param pricePerMinute
     */
    public void setPricePerMinute(int pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }

    /**
     * Sets the id
     * @param id id to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Checking if bicycle is available
     * @return "AVAILABLE" if the bicycle is available
     */
    public boolean isAvailable() {
        return Objects.equals(this.status, "AVAILABLE");
    }

    /**
     * Setting the status to available
     */
    public void setStatusToAvailable() {
        this.status = "AVAILABLE";
    }

    /**
     * Setting the status to rented
     */
    public void setStatusToRented(){
        this.status = "RENTED";
    }

    /**
     * Setting the status to disabled
     */
    public void setStatusToDisabled(){
        this.status = "DISABLED";
    }

    /**
     * Getting the status
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setting the status
     * @param status to be set. Could be rented, available or disabled.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusToNew() {
        this.status = "NEW";
    }
}
