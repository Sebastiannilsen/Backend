package no.ntnu.bicycle.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Represents a resource: orderdetails. This will be stored in the database
 */
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue
    private int orderNumber;
    private int productCode;
    private int quantityOrdered;
    private int priceEach;

    /**
     * Empty constructor
     */
    public OrderDetails() {
    }

    /**
     * Constructor with order number, product code quantity ordered and price for each.
     * @param orderNumber order number
     * @param productCode product code
     * @param quantityOrdered how many ordered
     * @param priceEach how much they cost separately
     */
    public OrderDetails(int orderNumber, int productCode, int quantityOrdered, int priceEach) {
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
    }

    /**
     * Gets order number
     * @return
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets order number
     * @param orderNumber
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Gets product code
     * @return
     */
    public int getProductCode() {
        return productCode;
    }

    /**
     * Sets product code
     * @param productCode
     */
    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets quantity ordered
     * @return quantity ordered
     */
    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    /**
     * Sets quantity ordered
     * @param quantityOrdered quantity ordered
     */
    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    /**
     * Gets price for each product
     * @return price
     */
    public int getPriceEach() {
        return priceEach;
    }

    /**
     * Sets price for each product
     * @param priceEach price
     */
    public void setPriceEach(int priceEach) {
        this.priceEach = priceEach;
    }
}
