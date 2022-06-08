package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Bicycle;
import no.ntnu.bicycle.service.BicycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * REST API controller for bicycle.
 */
@RestController
@RequestMapping("/api/bicycle")
public class BicycleController {

    private BicycleService bicycleService;

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

        response = new ResponseEntity<>(bicycleService.getAllBicycles(), HttpStatus.OK) ;

        return response;
    }

    /**
     * Gets one specific bike
     * @param bikeId ID of the bike to be returned
     * @return Bike with the given ID or status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bicycle> getOneBicycle(@PathParam("bike") @PathVariable("id") int bikeId) {
        Bicycle bicycle = bicycleService.findBicycleById(bikeId);
        if (bicycle != null) {
            return new ResponseEntity<>(bicycle,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> addBicycle(@RequestBody Bicycle bicycle) {
        ResponseEntity<String> response;
        if (bicycle.isValid()) {
            bicycle.setStatusToNew();
            if (bicycleService.addBicycle(bicycle)) {
                response = new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                response = new ResponseEntity<>("Bicycle already in system",HttpStatus.BAD_REQUEST);
            }
        }else{
            response = new ResponseEntity<>("Not a valid bicycle",HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Deletes a bicycle
     * @param bicycle Bicycle
     * @return HTTP 200 OK if deleted, else not found
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<String> updateBicycle(@RequestBody Bicycle bicycle) {
        ResponseEntity<String> response;
        if (bicycle.isValid()) {
            if (bicycleService.findBicycleById(bicycle.getId()) != null) {
                if (bicycleService.updateBicycle(bicycle)) {
                    response = new ResponseEntity<>("Bike was updated", HttpStatus.OK);
                } else {
                    response = new ResponseEntity<>("Could not update bike", HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new ResponseEntity<>("Bike does not exist", HttpStatus.BAD_REQUEST);
            }
        }else{
            response = new ResponseEntity<>("One or more of the fields are invalid", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Updates a bicycle
     * @param bikeId int
     * @return HTTP 200 OK updated, 400 if not
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBicycle(@PathVariable("id") int bikeId) {
        ResponseEntity<String> response;
        if (bicycleService.findBicycleById(bikeId) != null) {
            if (bicycleService.deleteBicycle(bikeId)) {
                response = new ResponseEntity<>("Bike deleted",HttpStatus.OK);
            }else{
                response = new ResponseEntity<>("Could not delete the bike",HttpStatus.BAD_REQUEST);
            }
        }else{
            response = new ResponseEntity<>("Could not find bike with that id",HttpStatus.BAD_REQUEST);
        }
        return response;
    }


}
