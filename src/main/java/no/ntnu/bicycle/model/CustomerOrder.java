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
    @JoinColumn(name = "product_id",referencedColumnName="id")
    private List<Product> products;


    /**
     * Constructor with parameters.
     * @param customer the customer ordering
     * @param products the list of products
     */
    public CustomerOrder(Customer customer,List<Product> products) {
        this.customer = customer;
        this.products = products;
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
}
