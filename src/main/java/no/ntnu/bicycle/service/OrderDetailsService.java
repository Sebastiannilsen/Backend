package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.OrderDetails;
import no.ntnu.bicycle.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Business logic related to order details service
 */
@Service
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    /**
     * Gets all order details
     * @return list of order details
     */
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }

    /**
     * Finds payment by order number
     * @param orderNumber int. Order number that gets to be found
     * @return Payment or null if none found by that ID
     */
    public OrderDetails findPaymentByOrderNumber(int orderNumber){
        Optional<OrderDetails> payment = orderDetailsRepository.findById(orderNumber);
        return payment.orElse(null);
    }

    /**
     * Adds order details
     * @param orderDetails OrderDetails.
     * @return true when order detail is added, false on error
     */

    public boolean addOrderDetails(OrderDetails orderDetails) {
        boolean added = false;
        if (orderDetails != null) {
            OrderDetails existingOrder = findPaymentByOrderNumber(orderDetails.getOrderNumber());
            if (existingOrder == null) {
                orderDetailsRepository.save(orderDetails);
                added = true;
            }
        }
        return added;
    }

    /**
     * Deletes order details
     * @param orderNumber int.
     * @return true when order detail is deleted, false when order detail was not found in the database
     */
    public boolean deleteOrderDetails(int orderNumber) {
        Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(orderNumber);
        if (orderDetails.isPresent()) {
            orderDetailsRepository.delete(orderDetails.get());
        }
        return orderDetails.isPresent();
    }

    /**
     * Updates order details
     * @param orderNumber int
     * @param orderDetails OrderDetails
     * @return null on success, error message on error
     */
    @Transactional
    public String updateOrderDetails(int orderNumber, OrderDetails orderDetails) {
        String errorMessage = null;
        OrderDetails existingOrderDetails = findPaymentByOrderNumber(orderNumber);
        if (existingOrderDetails == null) {
            errorMessage = "No order detail with this ordernumber: " + orderNumber;
        } else if (orderDetails == null) {
            errorMessage = "Invalid ordernumber";
        } else if (orderDetails.getOrderNumber() != orderNumber) {
            errorMessage = "Ordernumber does not match";
        }
        if (errorMessage == null) {
            orderDetailsRepository.save(orderDetails);
        }
        return errorMessage;
    }
}
