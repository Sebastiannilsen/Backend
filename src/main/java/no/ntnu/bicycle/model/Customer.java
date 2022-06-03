package no.ntnu.bicycle.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Customer.
 */
@Entity(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dob;
    private int phone;
    private Integer age;
    private boolean active = true;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private BillingAndShippingAddress address;
    @Enumerated
    private Role role;

    @ManyToMany
    @JoinTable(name="shoppingCart", joinColumns = @JoinColumn(name="customerId"), inverseJoinColumns = @JoinColumn(name="productId"))
    //@OneToMany(targetEntity = Product.class, cascade = CascadeType.MERGE)
    //@JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<Product> shoppingCart;

    //@ManyToMany
    //@JoinTable(name="skriv nokka her", joinColumns = @JoinColumn(name="customerId"), inverseJoinColumns = @JoinColumn(name="productId"))
    //private List<Product> products;


    //@OneToMany
    //@JsonIgnore
    //private Set<CustomerOrder> orders = new HashSet<>();

    /**
     * Instantiates a new Customer.
     */
    public Customer() {

    }

    /**
     * Instantiates a new Customer.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param dob       the dob
     * @param phone     the phone
     * @param password  the password
     */
    @JsonCreator
    public Customer(@JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email,
                    @JsonProperty("dob") String dob, @JsonProperty("phone") int phone, @JsonProperty("password") String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = LocalDate.parse(dob);
        this.phone = phone;
        this.password = password;
        LocalDate today = LocalDate.now(); // Today's date is 10th Jan 2022
        Period p = Period.between(LocalDate.parse(dob), today);
        this.age = p.getYears();
        this.role = Role.ROLE_USER;
        this.address = null;
        this.shoppingCart = new ArrayList<>();
    }


    /**
     * Instantiates a new Customer.
     *
     * @param firstName the first name
     * @param lastName  the last name
     * @param email     the email
     * @param dob       the dob
     * @param phone     the phone
     * @param password  the password
     * @param role      the role
     */
    public Customer(String firstName, String lastName, String email, String dob, int phone, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = LocalDate.parse(dob);
        this.phone = phone;
        this.password = password;

        this.role = role;
        LocalDate today = LocalDate.now(); // Today's date is 10th Jan 2022
        Period p = Period.between(LocalDate.parse(dob), today);
        this.age = p.getYears();
        this.address = null;
        this.shoppingCart = new ArrayList<>();
    }


    /**
     * Getters and setters
     *
     * @return values boolean
     */
    @JsonIgnore
    public boolean isValid() {
        return !"".equals(firstName);
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }


    /**
     * Gets phone.
     *
     * @return the phone
     */
    public int getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets role.
     *
     * @return the role
     */
    public Role getRole() {
        return role;
    }


    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Getting shopping cart list
     * @return shopping cart
     */
    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Adding a product to shopping cart
     * @param product product to add in shopping cart
     */
    public void addProductToShoppingCart(Product product) {
        this.shoppingCart.add(product);
    }

    /**
     * Removing a product from shopping cart
     * @param product product to remove from shopping cart
     */
    public void removeFromShoppingCart(Product product){
        this.shoppingCart.remove(product);
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets dob.
     *
     * @return the dob
     */
    public LocalDate getDob() {
        return dob;
    }

    /**
     * Sets dob.
     *
     * @param dob the dob
     */
    public void setDob(String dob) {
        this.dob = LocalDate.parse(dob);
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Gets address.
     *
     * @return the address
     */
    public BillingAndShippingAddress getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(BillingAndShippingAddress address) {
        this.address = address;
    }

    /**
     * Update age.
     */
    public void updateAge(){
        LocalDate today = LocalDate.now();
        Period p = Period.between(this.dob, today);
        this.age = p.getYears();
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /*@Override
    public String toString() {
        return "Customer{" +
                            "id=" + id +
                            ", firstName='" + firstName + '\'' +
                            ", lastName='" + lastName + '\'' +
                            ", email='" + email + '\'' +
                            ", dob=" + dob +
                            ", phone=" + phone +
                            ", age=" + age +
                            ", password=" + password +
                '}';
    }*/

    /**
     * Adding an order to all the orders
     * @param order the order to add
     */

    //public void addOrder(CustomerOrder order) {
    //    orders.add(order);
    //}
}
