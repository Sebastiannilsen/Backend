package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.CustomerOrder;
import no.ntnu.bicycle.model.Product;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.OrderService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST API controller for orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    private CustomerService customerService;


    /**
     * Constructor with parameters
     * @param orderService order service
     */
    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    /**
     * Get all orders
     * HTTP get to /orders
     * @return List of all orders currently stored in the database.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public Object getAllOrders() {
       return orderService.getAll();
    }

    /**
     * Add an order to the database.
     * @return 200 OK status on success, 400 bad request on error
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> add(HttpEntity<String> http) {
        ResponseEntity<String> response;
    try {
        JSONObject json = new JSONObject(http.getBody());

        String email = json.getString("email");

        Customer customer = customerService.findCustomerByEmail(email);

        List<Product> list = customer.getShoppingCart();

        customer.clearShoppingCart();

        CustomerOrder customerOrder = new CustomerOrder(customer, list);

        if (orderService.addNewOrder(customerOrder)) {
            response = new ResponseEntity<>("Order created",HttpStatus.OK);
        } else {
            response = new ResponseEntity<>("Cold not create order",HttpStatus.BAD_REQUEST);
        }

    }catch (JSONException e){
        response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
        return response;
    }

    /**
     * Get a specific order
     * @param id Id of the order to be returned
     * @return Order with the given Id or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrder> getOne(@PathVariable Integer id) {
        ResponseEntity<CustomerOrder> response;
        CustomerOrder customerOrder = orderService.findOrderById(id);
        if (customerOrder != null) {
            response = new ResponseEntity<>(customerOrder, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Delete an order from the collection
     * @param id Id of the order to delete
     * @return 200 OK on success, 404 Not found on error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        ResponseEntity<String> response;
        if (orderService.deletingOrder(id)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Update an order in the repository
     * @param id Id of the order to update, from the URL
     * @param customerOrder New order data to store, from request body
     * @return 200 OK success, 400 bad request on error
     */
    @PutMapping
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody CustomerOrder customerOrder) {
        String errorMessage = orderService.update(id, customerOrder);
        ResponseEntity<String> response;
        if (errorMessage == null) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return  response;
    }
}
