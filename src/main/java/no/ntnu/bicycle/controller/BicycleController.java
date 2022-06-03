package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Bicycle;
import no.ntnu.bicycle.service.BicycleRentalOrderService;
import no.ntnu.bicycle.service.BicycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller for bicycle.
 */
@RestController
@RequestMapping("/api/bicycle")
public class BicycleController {

    private BicycleService bicycleService;
    private BicycleRentalOrderService bicycleRentalOrderService;

    /**
     * Constructor with bicycle service
     * @param bicycleService bicycle service
     */
    public BicycleController(BicycleService bicycleService){
        this.bicycleService = bicycleService;
    }

    /**
     * Gets all bicycles
     * @return list of all bicycles
     */
    @GetMapping
    public ResponseEntity<List<Bicycle>> getAllBicycles() {
        ResponseEntity<List<Bicycle>> response;
        try {
            response = new ResponseEntity<>(bicycleService.getAllBicycles(), HttpStatus.OK) ;
        }catch (Exception e){
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Gets all available bicycles
     * @return list of all available bicycles
     */
    @GetMapping
    @RequestMapping("/available")
    public List<Bicycle> getAllAvailableBicycles() {
        return bicycleService.getAllAvailableBicycles();
    }

    /**
     * Adds a bicycle
     * @param bicycle Bicycle
     * @return HTTP 200 OK if bicycle added, 400 if it does not get added
     */
    @PostMapping
    public ResponseEntity<String> addBicycle(@RequestBody Bicycle bicycle) {
        ResponseEntity<String> response;
        if (bicycleService.addBicycle(bicycle)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Deletes a bicycle
     * @param bicycle Bicycle
     * @return HTTP 200 OK if deleted, else not found
     */
    @PutMapping
    public ResponseEntity<String> updateBicycle(@RequestBody Bicycle bicycle) {
        ResponseEntity<String> response;
        try{
            bicycleService.findBicycleById(bicycle.getId());
            bicycleService.updateBicycle(bicycle);
            response = new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Updates a bicycle
     * @param bikeId int
     * @return HTTP 200 OK updated, 400 if not
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBicycle(@PathVariable("id") int bikeId) {
        ResponseEntity<String> response;
        if(bicycleService.deleteBicycle(bikeId)){
            response = new ResponseEntity<>(HttpStatus.OK);
        }else{
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }


}
