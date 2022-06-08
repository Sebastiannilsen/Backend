package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Bicycle;
import no.ntnu.bicycle.model.BicycleRentalOrder;
import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.service.BicycleRentalOrderService;
import no.ntnu.bicycle.service.BicycleService;
import no.ntnu.bicycle.service.CustomerService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/rental")
public class RentalOrderController {

    private BicycleService bicycleService;
    private BicycleRentalOrderService bicycleRentalOrderService;
    private CustomerService customerService;

    public RentalOrderController(BicycleService bicycleService, BicycleRentalOrderService bicycleRentalOrderService, CustomerService customerService){
        this.bicycleService = bicycleService;
        this.bicycleRentalOrderService = bicycleRentalOrderService;
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BicycleRentalOrder>> getAllBicycleRentalOrders(){
        return new ResponseEntity<>(bicycleRentalOrderService.getAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBicycleRentalOrder(@PathVariable("id") long orderId){
        ResponseEntity<String> response;
        String errorMessage = bicycleRentalOrderService.deleteRentalOrder(orderId);
        if (errorMessage == null) {
            response = new ResponseEntity<>("Rental " + orderId +
                    " successfully deleted.", HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    /**
     * Creating bike rental order
     * @param http HttpEntity<String>
     * @return 200 OK if bike rental ordered, 400 bad request if not
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<String> createBikeRentalOrder(HttpEntity<String> http){
        ResponseEntity<String> response;
        try {
            JSONObject jsonObject = new JSONObject(http.getBody());

            long id = Long.parseLong(jsonObject.getString("bikeId"));

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            if (!email.equals("anonymousUser")) {
                if (!bicycleRentalOrderService.findBicycleRentalOrderById(id).isPresent()) {
                    Customer customer = customerService.findCustomerByEmail(email);
                    Bicycle bicycle = bicycleService.findBicycleById(id);
                    if (bicycle.isAvailable()) {

                        BicycleRentalOrder order = new BicycleRentalOrder(bicycle, customer, bicycle.getPricePerMinute());

                        bicycleRentalOrderService.addBicycleRentalOrder(order);

                        bicycle.setStatusToRented();

                        bicycleService.updateBicycle(bicycle);

                        response = new ResponseEntity<>("Created new order with orderId: " + order.getId(), HttpStatus.OK);
                    }else{
                        response = new ResponseEntity<>("Bike not available for rent", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    response = new ResponseEntity<>("An order with that orderId already exist", HttpStatus.BAD_REQUEST);
                }
            }else{
                response = new ResponseEntity<>("Need to log in", HttpStatus.UNAUTHORIZED);
            }
        }catch (JSONException e){
            response = new ResponseEntity<>("Posted fields are invalid",HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Ending bicycle order
     * @param http String
     * @return HTTP 200 OK if order ended and total price, HTTP not found if order is not found
     */
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/end")
    public ResponseEntity<String> endBicycleOrder(HttpEntity<String> http){

        JSONObject jsonObject = new JSONObject(http.getBody());

        int id = jsonObject.getInt("orderId");
        String endLocationLat = jsonObject.getString("endLocationLat");
        String endLocationLon = jsonObject.getString("endLocationLon");

        if (bicycleRentalOrderService.findBicycleRentalOrderById(id).isPresent()){
            BicycleRentalOrder order = bicycleRentalOrderService.findBicycleRentalOrderById(id).get();
            if (order.getTotalPrice() == 0 && order.getRentalEndTime() == null) {

                int totalPrice = bicycleRentalOrderService.endBicycleRentalOrderAndReturnTotalPrice(order, endLocationLat, endLocationLon);

                long bicycleId = bicycleRentalOrderService.getBicycleIdFromOrderId(id);

                Bicycle bicycle = bicycleService.findBicycleById(bicycleId);
                bicycle.setStatusToAvailable();
                bicycle.setLocation(endLocationLat + "," + endLocationLon);
                bicycleService.updateBicycle(bicycle);

                return new ResponseEntity<>("Rental ended. Total price is: " + totalPrice + " NOK", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Order already ended",HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>("No order found with that order id",HttpStatus.NOT_FOUND);
        }
    }
}
