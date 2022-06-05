package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.Customer;
import no.ntnu.bicycle.model.Role;
import no.ntnu.bicycle.repository.CustomerRepository;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Business logic related to Customer service
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Constructor with the parameter customer repository
     * @param customerRepository customer repository
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Gets all customers
     * @return list of all customers
     */
    public List<Customer> getAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    /**
     * Finds customer by id
     * @param id long. Customer id that needs to be found
     * @return
     */
    public Customer findCustomerById(long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.get();
        /*if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Cannot find the customer");
        }
        return optionalCustomer.get();*/
    }

    /**
     * Finds customer by email
     * @param email String. Email to be found
     * @return customer
     */
    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        return customer.get();
    }

    /**
     * Adds a new customer
     * @param customer Customer. customer to be added.
     * @return true if the customer got added, false if it did not get added
     */
    public boolean addNewCustomer(Customer customer) {
        boolean added = false;
        if (customer != null && customer.isValid()) {
            try {
                findCustomerById(customer.getId());
            }catch (NoSuchElementException e) {
                customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
                customer.updateAge();
                customer.setRole(Role.ROLE_USER);
                customerRepository.save(customer);
                added = true;
            }
        }
        return added;
    }

    /**
     * Resets password
     * @param email String.
     * @return new mail with a new password,
     */
    public String resetPassword(String email){
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        CharacterRule alphabets = new CharacterRule(EnglishCharacterData.Alphabetical);
        CharacterRule digits = new CharacterRule(EnglishCharacterData.Digit);
        String generatedPassword = passwordGenerator.generatePassword(10,alphabets,digits);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            customer.get().setPassword(new BCryptPasswordEncoder().encode(generatedPassword));
            updateCustomer(customer.get().getId(),customer.get());
        }else{
            generatedPassword = null;
        }

        return generatedPassword;
    }

    /**
     * Deletes a customer
     * @param customerId int. Customer Id that gets deleted
     */
    public String deleteCustomer(int customerId) {
        Optional<Customer> customer = customerRepository.findById((long) customerId);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
            return "customer deleted successfully";
        }
        return "customer does not exist in DB";
    }

    /**
     * Updates a customer
     * @param customerId long. Id that gets updated
     * @param customer Customer. customer that gets updated.
     * @return null on success, error message on error
     */
    @Transactional
    public String updateCustomer(long customerId, Customer customer) {
     String errorMessage = null;
     Customer existingCustomer = findCustomerById(customerId);
     if (existingCustomer == null) {
         errorMessage = "No customer with id " + customerId + "exists";
     } else if (customer == null || !customer.isValid()) {
         errorMessage = "Invalid data";
     } else if (customer.getId() != customerId) {
         errorMessage = "Id does not match";
     }

     if (errorMessage == null) {
         customerRepository.save(customer);
     }
     return errorMessage;

    }
}
