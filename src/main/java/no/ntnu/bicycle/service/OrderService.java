package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.CustomerOrder;
import no.ntnu.bicycle.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Business logic related to orders
 */
@Service
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    /**
     * Constructor for order repository
     * @param orderRepository OrderRepository
     */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Makes iterable to list
     * @param iterable Iterable<CustomerOrder>
     * @return list of customer orders
     */
    public List<CustomerOrder> iterableToList(Iterable<CustomerOrder> iterable) {
        List<CustomerOrder> list = new LinkedList<>();
        iterable.forEach(list::add);
        return list;
    }

    /**
     * Gets all customer orders
     * @return list of customer order
     */
    public List<CustomerOrder> getAll() {
        return iterableToList(orderRepository.findAll());
    }

    /**
     * Finds the order by id
     * @param id Integer
     * @return The order or null if none found by that ID
     */
    public CustomerOrder findOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * Adds new customer order
     * @param customerOrder CustomerOrder
     * @return true when order added, false when not added
     */
    public String addNewOrder(CustomerOrder customerOrder) {

        String errorMessage = null;
        if (!canBeAdded(customerOrder)) {
            errorMessage = "Order did not get added.";
        } else if (!customerOrder.isValid()) {
            errorMessage = "Order did not get added. Plase check your input.";
        } else {
            orderRepository.save(customerOrder);

        }
        return errorMessage;
        /*boolean added = false;
        if (canBeAdded(customerOrder)) {
            orderRepository.save(customerOrder);
            added = true;
        }
        return added;*/
    }

    /**
     * Checking if customer order is not null
     * @param customerOrder CustomerOrder
     * @return true if it´s not null, false if it is
     */
    private boolean canBeAdded(CustomerOrder customerOrder) {
        return customerOrder != null;
    }

    /**
     * Deletes an order
     * @param orderId int
     * @return true when order is deleted, false when order was not found in the database
     */
    public boolean deletingOrder(int orderId) {
    boolean deleted = false;
    if (findOrderById(orderId) != null) {
        orderRepository.deleteById(orderId);
        deleted = true;
        }
    return deleted;
    }

    /**
     * Updates an order
     * @param id int
     * @param customerOrder CustomerOrder
     * @return null on success, error message on error
     */
    public String update(int id, CustomerOrder customerOrder) {
        CustomerOrder existingCustomerOrder = findOrderById(id);
        String errorMessage = null;
        if (existingCustomerOrder == null) {
            errorMessage = "No customerOrder with " + id + "found";
        }
        if (customerOrder == null) {
            errorMessage = "Wrong data in request body";
        } else if (customerOrder.getId() != id) {
            errorMessage = "Wrong id, does not match";
        }

        if (errorMessage == null) {
            orderRepository.save(customerOrder);
        }
        return errorMessage;
    }
//!TODO what do we do about these?
    public Optional<CustomerOrder> getAllOrdersByDateAndTime(LocalDateTime dateAndTime) {
        return null;
        //return orderRepository.findByDateAndTime(dateAndTime);
    }

    public Optional<CustomerOrder> getAllOrdersByEmail(String email) {
        return orderRepository.findByEmail(email);
    }


    public List<CustomerOrder> getAllOrdersByCustomerAndProduct(String customer, LocalDateTime product) {
      //  return orderRepository.findByCustomerNameAndProduct(customer,product);
        return null;
    }

}
