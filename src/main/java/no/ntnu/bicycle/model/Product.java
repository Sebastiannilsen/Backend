package no.ntnu.bicycle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Represents a resource: a product. We store Product object in the database.
 */
@Entity
public class Product {
    @Id
    @GeneratedValue
    private int id;
    private String productName;
    private String imageUrl;
    private String color;
    private String description;
    private int price;

    @ManyToMany(mappedBy = "shoppingCart")
    @JsonIgnore
    private List<Customer> customer;

    /**
     * Constructor with parameters
     * @param productName product name
     * @param color color of the product
     * @param imageUrl url to the image
     * @param description a short description of the product
     * @param price price for product
     */
    public Product(String productName, String color, String imageUrl,String description, int price) {
        this.productName = productName;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }

    /**
     * Empty constructor
     */
    public Product() {
    }

    /**
     * Gets color
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets color
     * @param color to be set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets image URL
     * @return imageURL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets image URL
     * @param imageUrl to be set
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setting the description
     * @param description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     * @param id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets product name
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets product name
     * @param productName name for product
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets price for product
     * @return price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets price of product
     * @param price of the product
     */
    public void setPrice(int price) {
        this.price = price;
    }

    @JsonIgnore
    public boolean isValid() {
        return (this.productName != null && this.color != null && this.imageUrl != null && this.description != null && this.price != 0);
    }
}
