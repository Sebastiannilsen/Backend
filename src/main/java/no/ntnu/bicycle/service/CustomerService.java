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
        if (customer.isPresent()){
            return customer.get();
        }else{
            return null;
        }
    }

    public boolean isCustomerAlreadyInDatabase(long id){
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.isPresent();
    }

    /**
     * Finds customer by email
     * @param email String. Email to be found
     * @return customer
     */
    public Customer findCustomerByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()){
            return customer.get();
        }else{
            return null;
        }
    }

    /**
     * Adds a new customer
     * @param customer Customer. customer to be added.
     * @return true if the customer got added, false if it did not get added
     */
    public String addNewCustomer(Customer customer){
        String errorMessage = "";
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            errorMessage = "Customer already exists";
        }
        else if (!customer.isValid()) {
            errorMessage = "Customer not added. Invalid name.";
        }
        else if (!customer.isEmailValid()) {
            errorMessage = "Customer not added. Invalid email";
        }
        else if (!customer.isPasswordValid()) {
            errorMessage = "Customer not added. Invalid password";
        }
        else{
            customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
            customer.updateAge();
            customer.setRole(Role.ROLE_USER);
            customerRepository.save(customer);
        }
        return errorMessage;
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
            updateCustomer(customer.get());
        }else{
            generatedPassword = null;
        }

        return generatedPassword;
    }

    /**
     * Deletes a customer
     * @param customerId int. Customer Id that gets deleted
     * @return
     */
    public String deleteCustomer(long customerId) {
            String errorMessage = null;
            Optional<Customer> customer = customerRepository.findById(customerId);
            if (customer.isPresent()) {
                customerRepository.delete(customer.get());
            } else {
                errorMessage = "Customer not found in database";
            }
            return errorMessage;
        }

       /* if (customerRepository.existsById(customer.get().getId())) {
            try{
                customerRepository.delete(customer.get());
        } catch (Exception e){
            throw e;
        }
        }else {
            errorMessage =
            return "Customer does not exist in DB";
        }*/

    /**
     * Updates a customer
     * @param customer Customer. customer that gets updated.
     * @return null on success, error message on error
     */
    @Transactional
    public String updateCustomer(Customer customer) {
     String errorMessage = "";
     Customer existingCustomer = findCustomerByEmail(customer.getEmail());
     if (existingCustomer == null) {
         errorMessage = "No customer with email " + customer.getEmail() + "exists";
     } else if (!customer.isValid()) {
         errorMessage = "Invalid data";
     } else if (!customer.getEmail().equals(existingCustomer.getEmail())) {
         errorMessage = "Email does not match";
     }

     if (errorMessage.isEmpty()) {
         customerRepository.save(customer);
     }
     return errorMessage;

    }
}
