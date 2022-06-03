package no.ntnu.bicycle.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * The type payment.
 * This will be added to the database.
 */
@Entity
public class Payment {
    @Id
    @GeneratedValue
    private int customerNumber;
    private int checkNumber;
    private LocalDateTime paymentDate;
    private int amount;

    /**
     * Empty constructor
     */
    public Payment() {
    }

    /**
     * Constructor with parameters
     * @param customerNumber customer number
     * @param checkNumber check number
     * @param paymentDate payment date
     * @param amount amount
     */
    public Payment(int customerNumber, int checkNumber, LocalDateTime paymentDate, int amount) {
        this.customerNumber = customerNumber;
        this.checkNumber = checkNumber;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }

    /**
     * Gets customer number
     * @return customer number
     */
    public int getCustomerNumber() {
        return customerNumber;
    }

    /**
     * Sets the customer number
     * @param customerNumber customer number to be set
     */
    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * Gets the check number
     * @return check number
     */
    public int getCheckNumber() {
        return checkNumber;
    }

    /**
     * Sets the check number
     * @param checkNumber check number to be set
     */
    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    /**
     * Gets payment date
     * @return payment date
     */
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets payment date
     * @param paymentDate payment date to be set
     */
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Gets amount
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets amount
     * @param amount amount to be set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
