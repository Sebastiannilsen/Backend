package no.ntnu.bicycle.model;



import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type customer order.
 * This will be added to the database.
 */
@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue
    private int id;

    private String email;
    private LocalDateTime dateAndTime;

    @ManyToOne(cascade = CascadeType.MERGE, fetch= FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;


    @OneToMany(cascade = CascadeType.MERGE, fetch= FetchType.EAGER)
    @Column(name = "products")
    private List<Product> products;


    /**
     * Constructor with parameters.
     * @param customer the customer ordering
     */
    public CustomerOrder(Customer customer) {
        this.customer = customer;
        this.products = customer.getShoppingCart();
        this.dateAndTime = LocalDateTime.now();
        this.email = customer.getEmail();
    }

    /**
     * Empty constructor
     */
    public CustomerOrder() {
    }

    /**
     * Gets local date and time
     * @return date and time
     */
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Sets date and time
     * @param dateAndTime date and time to be set
     */
    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    /**
     * Gets product
     * @return product
     */
     public List<Product> getProducts() {return products;}

    /**
     * Gets id
     * @return id
     */
    public Integer getId()
     {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Checks if order is valid
     * @return true if order is valid, false else
     */
    public boolean isValid() {
        return customer.isValid() && !products.isEmpty() && dateAndTime == null && "".equals(email);
    }
}
