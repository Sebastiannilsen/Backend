package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.mail.EmailSenderService;
import no.ntnu.bicycle.model.Bicycle;
import no.ntnu.bicycle.model.BicycleRentalOrder;
import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.CustomerOrder;
import no.ntnu.bicycle.service.BicycleRentalOrderService;
import no.ntnu.bicycle.service.BicycleService;
import no.ntnu.bicycle.service.CustomerService;
import no.ntnu.bicycle.service.OrderService;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * REST API controller for orders.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private BicycleRentalOrderService bicycleRentalOrderService;
    private CustomerService customerService;
    private BicycleService bicycleService;


    private EmailSenderService emailSenderService;

    /**
     * Constructor with parameters
     * @param orderService order service
     * @param bicycleRentalOrderService bicycle rental order service
     * @param customerService customer service
     * @param bicycleService bicycle service
     */
    public OrderController(OrderService orderService, BicycleRentalOrderService bicycleRentalOrderService, CustomerService customerService, BicycleService bicycleService, EmailSenderService emailSenderService) {
        this.orderService = orderService;
        this.bicycleRentalOrderService = bicycleRentalOrderService;
        this.customerService = customerService;
        this.bicycleService = bicycleService;
        this.emailSenderService = emailSenderService;
    }

    /**
     * Get all orders
     * HTTP get to /orders
     * @param localDateTime Product. When specified, get all orders with a product including this substring, case-insensitive.
     * @param email Customer. When specified, get all orders from a customer including this substring, case-insensitive.
     * @return List of all orders currently stored in the database.
     */
    @GetMapping()
    public Object getAllOrders(@RequestParam(required = false) String email,
                               @RequestParam(required = false) LocalDateTime localDateTime) {
        if (localDateTime != null) {
            if (email != null && !"".equals(email)) {
                return orderService.getAllOrdersByCustomerAndProduct(email, localDateTime);
            } else {
                return orderService.getAllOrdersByDateAndTime(localDateTime);
            }
        } else if(email != null && !"".equals(email)) {
            return orderService.getAllOrdersByEmail(email);
        } else {
            return orderService.getAll();
        }
    }

    /**
     * Add an order to the database.
     * @param customerOrder order to be added, from HTTP response body
     * @return 200 OK status on success, 400 bad request on error
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> add(@RequestBody CustomerOrder customerOrder) {
        ResponseEntity<String> response;
        if (orderService.addNewOrder(customerOrder)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
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

    /**
     * Creating bike rental order
     * @param entity HttpEntity<String>
     * @return 200 OK if bike rental ordered, 400 bad request if not
     */
    @PostMapping(value = "/rental", consumes = "application/json")
    public ResponseEntity<Long> createBikeRentalOrder(HttpEntity<String> entity){

        //JSONObject json = new JSONObject(entity.getBody());

        JSONObject jsonObject = new JSONObject(entity.getBody());

        long id = Long.parseLong(jsonObject.getString("bikeId"));
        int pricePerMinute = Integer.parseInt(jsonObject.getString("pricePerMinute"));

        ResponseEntity<Long> response;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        try {
            Customer customer = customerService.findCustomerByEmail(email);
            Bicycle bicycle = bicycleService.findBicycleById(id);

            BicycleRentalOrder order = new BicycleRentalOrder(bicycle, customer, pricePerMinute);

            bicycleRentalOrderService.addBicycleRentalOrder(order);

            bicycle.setStatusToRented();

            bicycleService.updateBicycle(bicycle);

            emailSenderService.sendEmail(email, "Rental confirmation", "Follow this link to end your order: http://localhost:8080/rental/confirmation/" + order.getId());

            response = new ResponseEntity<>(order.getId(),HttpStatus.OK);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Getting location from order
     * @param id long
     * @return 200 OK if location found, else 404 not found
     */
    @GetMapping(value = "/confirmation/{id}", consumes = "application/json")
    public ResponseEntity<String> getLocationFromOrder(@PathVariable("id")long id){
        try {
            BicycleRentalOrder order = bicycleRentalOrderService.findBicycleRentalOrderById(id);

            return new ResponseEntity<>(order.getLocation(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Ending bicycle order
     * @param entity String
     * @return HTTP 200 OK if order ended and total price, HTTP not found if order is not found
     */
    @PostMapping("/addBicycleOrder")
    public ResponseEntity<Integer> endBicycleOrder(@RequestBody String entity){
        int id = Integer.parseInt(entity.split(",")[0]);
        String endLocationLat = entity.split(",")[1];
        String endLocationLon = entity.split(",")[2];

        try{
            BicycleRentalOrder order = bicycleRentalOrderService.findBicycleRentalOrderById(id);

            int totalPrice = bicycleRentalOrderService.endBicycleRentalOrderAndReturnTotalPrice(order, endLocationLat, endLocationLon);

            bicycleService.setStatusToAvailable(bicycleRentalOrderService.getBicycleIdFromOrderId(id));

            return new ResponseEntity<>(totalPrice,HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*@GetMapping
    public ResponseEntity<List<CustomerOrder>> getOrdersByCustomerEmail(){
        ResponseEntity<List<CustomerOrder>> response;
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            response =  new ResponseEntity<>(orderService.getAllOrdersByCustomerEmail(email),HttpStatus.OK);
        }catch (NoSuchElementException e){
            response =  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }*/
}
