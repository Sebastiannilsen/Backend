package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.OrderDetails;
import no.ntnu.bicycle.service.OrderDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * REST API controller for orders details.
 */
@RestController("/order-details")
public class OrderDetailsController {
    OrderDetailsService orderDetailsService;

    /**
     * Constructor with order details service
     * @param orderDetailsService order details service
     */
    public OrderDetailsController(OrderDetailsService orderDetailsService) {
        this.orderDetailsService = orderDetailsService;
    }

    /**
     * Gets all order details
     * @return list of order details
     */
    @GetMapping
    public List<OrderDetails> getOrderDetails() {
        return orderDetailsService.getAllOrderDetails();
    }

    /**
     * Gets order details
     * @param orderNumber number of the order that needs details
     * @return 200 OK status on success, 404 not found if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetails> getOrderDetails(@PathParam("orderDetails")
                                                        @PathVariable("id")
                                                        int orderNumber) {
        ResponseEntity<OrderDetails> response;
        OrderDetails orderDetails = orderDetailsService.findPaymentByOrderNumber(orderNumber);
        if (orderDetails != null) {
            response = new ResponseEntity<>(orderDetails, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Registers new order detail
     * @param orderDetails order detail to be registered
     * @return 200 OK status on success, or 400 bad request if it does not register
     */
    @PostMapping
    public ResponseEntity<String> registerNewOrderDetail(@RequestBody OrderDetails orderDetails) {
        ResponseEntity<String> response;
        if (orderDetailsService.addOrderDetails(orderDetails)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Delete order detail
     * @param orderNumber number of the order that needs to be deleted
     */
    @DeleteMapping("/{id}")
    public void deleteOrderDetail(@PathVariable("id") int orderNumber) {
        orderDetailsService.deleteOrderDetails(orderNumber);
    }

    /**
     * Update order details
     * @param orderNumber number of the order that needs to be updated
     * @param orderDetails new order details
     * @return 200 OK status on success, or 400 bad request if it does not get updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrderDetail(@PathVariable int orderNumber,
                                                    @RequestBody OrderDetails orderDetails) {
        String errorMessage = orderDetailsService.updateOrderDetails(orderNumber, orderDetails);
        ResponseEntity<String> response;
        if (errorMessage == null) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

}
