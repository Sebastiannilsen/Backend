package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Bicycle;
import no.ntnu.bicycle.model.BicycleRentalOrder;
import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.service.BicycleRentalOrderService;
import no.ntnu.bicycle.service.BicycleService;
import no.ntnu.bicycle.service.CustomerService;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * Creating bike rental order
     * @param http HttpEntity<String>
     * @return 200 OK if bike rental ordered, 400 bad request if not
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Long> createBikeRentalOrder(HttpEntity<String> http){

        JSONObject jsonObject = new JSONObject(http.getBody());

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

            response = new ResponseEntity<>(order.getId(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            e.printStackTrace();
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    /**
     * Ending bicycle order
     * @param entity String
     * @return HTTP 200 OK if order ended and total price, HTTP not found if order is not found
     */
    @PostMapping("/end")
    public ResponseEntity<Integer> endBicycleOrder(@RequestBody String entity){
        int id = Integer.parseInt(entity.split(",")[0]);
        String endLocationLat = entity.split(",")[1];
        String endLocationLon = entity.split(",")[2];

        try{
            Optional<BicycleRentalOrder> order = bicycleRentalOrderService.findBicycleRentalOrderById(id);

            int totalPrice = bicycleRentalOrderService.endBicycleRentalOrderAndReturnTotalPrice(order.get(), endLocationLat, endLocationLon);

            bicycleService.setStatusToAvailable(bicycleRentalOrderService.getBicycleIdFromOrderId(id));

            return new ResponseEntity<>(totalPrice,HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
