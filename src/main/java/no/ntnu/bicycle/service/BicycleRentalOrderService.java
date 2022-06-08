package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.BicycleRentalOrder;
import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.CustomerOrder;
import no.ntnu.bicycle.repository.BicycleRentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Business logic related to Bicycle rental orders
 */
@Service
public class BicycleRentalOrderService {
    @Autowired
    BicycleRentalOrderRepository bicycleRentalOrderRepository;

    /**
     * Constructor for bicycle rental order
     * @param bicycleRentalOrderRepository BicycleRentalOrderRepository
     */
    public BicycleRentalOrderService(BicycleRentalOrderRepository bicycleRentalOrderRepository){
        this.bicycleRentalOrderRepository = bicycleRentalOrderRepository;
    }

    /**
     * Finds bicycle rental order by id
     * @param id long. Bicycle id
     * @return The bicycle rental
     */
    public Optional<BicycleRentalOrder> findBicycleRentalOrderById(long id){
        return bicycleRentalOrderRepository.findById(id);
    }

    /**
     * Adds bicycle rental order
     * @param bicycleRentalOrder BicycleRentalOrder
     * @return true when added, false on error
     */
    public boolean addBicycleRentalOrder(BicycleRentalOrder bicycleRentalOrder){
        if(findBicycleRentalOrderById(bicycleRentalOrder.getId()).isPresent()){
            return false;

        }else{
            bicycleRentalOrderRepository.save(bicycleRentalOrder);
            return true;
        }
    }

    public int endBicycleRentalOrderAndReturnTotalPrice(BicycleRentalOrder bicycleRentalOrder, String latEndLocation, String lonEndLocation) throws NoSuchElementException{
        findBicycleRentalOrderById(bicycleRentalOrder.getId());

        bicycleRentalOrder.setRentalEndTime(LocalDateTime.now());

        double distance = bicycleRentalOrder.getDistanceBetweenStartAndEndLocation(Double.parseDouble(latEndLocation), Double.parseDouble(lonEndLocation));


        long elapsedTimeInMinutes = bicycleRentalOrder.getElapsedTimeInMinutes();

        long price = elapsedTimeInMinutes * bicycleRentalOrder.getPricePerMinute();


        if (distance > 500){
            bicycleRentalOrder.setTotalPrice(Integer.parseInt(String.valueOf(price + 100)));
        }else{
            bicycleRentalOrder.setTotalPrice(Integer.parseInt(String.valueOf(price)));
        }

        bicycleRentalOrderRepository.save(bicycleRentalOrder);

        return bicycleRentalOrder.getTotalPrice();
    }

    public long getBicycleIdFromOrderId(int orderId) {
        try{
            BicycleRentalOrder bicycleRentalOrder = findBicycleRentalOrderById(orderId).get();

            return bicycleRentalOrder.getBicycle().getId();
        }catch (NoSuchElementException e){
            return -1;
        }
    }

    /**
     * Gets all customer orders
     * @return list of customer order
     */
    public List<BicycleRentalOrder> getAll() {
        return iterableToList(bicycleRentalOrderRepository.findAll());
    }

    /**
     * Makes iterable to list
     * @param iterable Iterable<CustomerOrder>
     * @return list of customer orders
     */
    public List<BicycleRentalOrder> iterableToList(Iterable<BicycleRentalOrder> iterable) {
        List<BicycleRentalOrder> list = new LinkedList<>();
        iterable.forEach(list::add);
        return list;
    }

    public String deleteRentalOrder(long orderId) {
        String errorMessage = null;
        Optional<BicycleRentalOrder> rentalOrder = bicycleRentalOrderRepository.findById(orderId);
        if (rentalOrder.isPresent()) {
            bicycleRentalOrderRepository.delete(rentalOrder.get());
        } else {
            errorMessage = "Rental not found in database";
        }
        return errorMessage;
    }
}
