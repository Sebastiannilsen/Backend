package no.ntnu.bicycle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import no.ntnu.bicycle.model.BillingAndShippingAddress;
import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST API controller for customer.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Constructor with parameters
     * @param customerService customer service
     */
    public CustomerController(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    /**
     * Gets all customers
     * HTTP get
     * @return list of all customers
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Gets one specific customer
     * @param customerId ID of the customer to be returned
     * @return Customer with the given ID or status 404
     */
    @GetMapping("/{id}")
    public Customer getOneCustomer(@PathParam("costumer")
                                   @PathVariable("id") int customerId) {
        return customerService.findCustomerById(customerId);
    }

    /**
     * Gets the customer that is logged in
     * @return Customer by email, 404 not found or 403 forbidden.
     */
    @GetMapping("/authenticated-customer")
    public ResponseEntity<Customer> getLoggedInCustomer(){
        ResponseEntity<Customer> response;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String customerEmail = auth.getName();
        Customer customer = customerService.findCustomerByEmail(customerEmail);

        if (customer != null) {
            response = new ResponseEntity<>(customer, HttpStatus.OK);
        } else if (customer == null){
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    /**
     * Gets address of customer by email
     * @return Address of customer by email, 404 not found or 403 forbidden.
     */
    @GetMapping("/authenticated-address")
    public ResponseEntity<BillingAndShippingAddress> getAddressOfCustomerByEmail(){
        ResponseEntity<BillingAndShippingAddress> response;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String customerEmail = auth.getName();
        Customer customer = customerService.findCustomerByEmail(customerEmail);

        if (customer != null) {
            response = new ResponseEntity<>(customer.getAddress(), HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Updates address of customer
     * @param address address to be updated
     * @return 200 OK status on success, 404 not found or 403 forbidden.
     */
    @PostMapping("/authenticated-address")
    public ResponseEntity<String> updateAddressOfCustomer(@RequestBody BillingAndShippingAddress address) {
        ResponseEntity<String> response;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String customerEmail = auth.getName();
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        if (customer != null) {
            customer.setAddress(address);
            customerService.updateCustomer(customer);
            response = new ResponseEntity<>("Address updated", HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("Address could not be found", HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Registers new customer
     * @param customer customer to be registered
     * @return 200 OK status on success = welcome mail from KRRR,
     * or 400 bad request and prints out that mail could not be sent
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> registerNewCustomer(@RequestBody Customer customer) throws JsonProcessingException {
        ResponseEntity<String> response;
        String errorMessage = customerService.addNewCustomer(customer);
        if (errorMessage.isEmpty()) {
            response = new ResponseEntity<>("Customer " + customer.getFirstName() +
                    " " + customer.getLastName() + " added", HttpStatus.CREATED);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Reset password
     * @param emailObject email to the password that needs to be reset
     * @return 200 OK status on success = mail with new password to the email given,
     * 400 bad request or 404 not found
     */
    @PostMapping(value = "/reset-password", consumes = "application/json")
    public ResponseEntity<String> resetPassword(@RequestBody String emailObject) {
        String[] stringArray = emailObject.split("\"" );
        String email = stringArray[3];
        ResponseEntity<String> response = null;
        Customer customer = customerService.findCustomerByEmail(email);
        if (customer != null && customer.isValid()) {
            String generatedPassword = customerService.resetPassword(email);
            if (generatedPassword != null){
                response = new ResponseEntity<>("Your new password is: " + generatedPassword, HttpStatus.OK);
            }
        } else {
            response = new ResponseEntity<>("You are not logged in", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * !TODO documentation here
     * Updates password
     * @param emailAndNewAndOldPassword Email, new and old password
     * @return 200 OK status on success =
     * 401 Unauthorized =
     * 404 not found =
     */
    @PostMapping(value = "/update-password", consumes = "application/json")
    public ResponseEntity<String> updatePassword(@RequestBody String emailAndNewAndOldPassword){
        ResponseEntity<String> response;

        String[] stringArray = emailAndNewAndOldPassword.split("\"" );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        String oldPassword = stringArray[3];
        String newPassword = stringArray[7];
        Customer customer = customerService.findCustomerByEmail(email);
        if (customer.isValid() && customer.isPasswordValid() && customer.isEmailValid()){
             if(new BCryptPasswordEncoder().matches(oldPassword, customer.getPassword())){
                customer.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                customerService.updateCustomer(customer);
                 response = new ResponseEntity<>(HttpStatus.OK);
            }else{
                 response = new ResponseEntity<>("Old password doesn't match", HttpStatus.UNAUTHORIZED);
             }
        }else{
            response = new ResponseEntity<>("Given customer is not valid", HttpStatus.NOT_FOUND);
        }
        return response;
    }


    /**
     * Deletes a customer
     * @param customerId customer to be deleted
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") int customerId){
        ResponseEntity<String> response;
        String errorMessage = customerService.deleteCustomer(customerId);
        if (errorMessage == null) {
            response = new ResponseEntity<>("Customer " + customerId +
                    " successfully deleted.", HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    /**
     * Update customer
     * @param customer customer that needs to be updated
     * @return 200 OK status on success or 400 bad request if it does not get updated
     */
    @PutMapping()
    public ResponseEntity<String> update(@RequestBody Customer customer) {
        String errorMessage = customerService.updateCustomer(customer);
        ResponseEntity<String> response;
        if (errorMessage.isEmpty()) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Add product to cart
     * @param idJsonObject String
     * @return 200 OK if product added to cart, 400 bad request if not
     */
    @PostMapping(value = "/addToCart", consumes = "application/json")
    public ResponseEntity<String> addProductToCart(@RequestBody String idJsonObject){
        if (idJsonObject != null) {
            String[] stringArray = idJsonObject.split("\"" );
            int id = Integer.parseInt(stringArray[3]);
            Product product = productService.getProductById(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            if (email.equals("anonymousUser")){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }else{
                Customer customer = customerService.findCustomerByEmail(email);
                customer.addProductToShoppingCart(product);
                customerService.updateCustomer(customer);
                return new ResponseEntity<>(HttpStatus.OK);
            }}
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Deletes a product in the cart
     * @param id the id of the product
     * @return
     */
    @DeleteMapping(value = "/deleteProductInCart")
    public ResponseEntity<String> deleteProductInCart(@RequestBody int id)
            throws HttpServerErrorException.InternalServerError {
        ResponseEntity<String> response;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer customer = customerService.findCustomerByEmail(email);

        if (customer != null && customer.isValid()) {
            customer.removeFromShoppingCart(productService.getProductById(id));
            customerService.updateCustomer(customer);
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> internalServerError(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
