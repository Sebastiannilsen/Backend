package no.ntnu.bicycle.model;

import javax.persistence.*;

/**
 * Represents a resource: a billing and shipping address.
 * This will be stored in the database.
 */
@Entity
@Table(name = "address")
public class BillingAndShippingAddress {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String country;
    private String postalCode;
    private String city;
    //@OneToOne(mappedBy = "address")
    //private Customer customer;

    /**
     * Constructor with all parameters
     * @param firstName customers first name
     * @param lastName customers last name
     * @param address customers address
     * @param country customers country
     * @param postalCode customers postcode
     * @param city customers city
     */
    public BillingAndShippingAddress(String firstName, String lastName, String address, String country, String postalCode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.country = country;
        this.postalCode = postalCode;
        this.city = city;
    }

    /**
     * Empty constructor
     */
    public BillingAndShippingAddress() {

    }

    /**
     * Gets the id
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the id
     * @param id id to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     * @param firstName first name to be set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     * @param lastName last name to be set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets address
     * @return adress
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address
     * @param address address to be set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets country
     * @return country to be set
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country
     * @param country country to be set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets postal code
     * @return postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code
     * @param postalCode postal code to be set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city
     * @param city city to to be set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets postal
     * @return postal code and city
     */
    public String getPostal(){
        return postalCode + " " + city;
    }
}
