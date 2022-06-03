package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.Payment;
import no.ntnu.bicycle.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic related to payments
 */
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Constructor for payment service
     * @param paymentRepository PaymentRepository
     */
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Gets all payment
     * @return list of payment
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Finds payment by ID
     * @param number int
     * @return the payment, or null if none is found by that number
     */
    public Payment findPaymentsById(int number) {
        Optional<Payment> payments = paymentRepository.findById(number);
        return payments.orElse(null);
    }

    /**
     * Adds new payment
     * @param payment Payment
     * @return true when payment added, false if payment was not found
     */
    public boolean addNewPayment(Payment payment) {
        boolean added = false;
        if (payment != null) { // && payments.isValid()
            Payment existingPayment = findPaymentsById(payment.getCheckNumber());
            if (existingPayment == null) {
                paymentRepository.save(payment);
                added = true;
            }
        }
        return added;
    }

    /**
     * Deletes payment
     * @param paymentNumber int
     * @return true when payment deleted, false when payment was not found in the database
     */
    public boolean deletePayment(int paymentNumber) {
        Optional<Payment> payment = paymentRepository.findById(paymentNumber);
        if (payment.isPresent()) {
            paymentRepository.delete(payment.get());
        }
        return payment.isPresent();
    }

    /**
     * Updates payment
     * @param paymentNumber int
     * @param payment Payment
     * @return null on success, error message on error
     */
    public String updatePayments(int paymentNumber, Payment payment) {
        String errorMessage = null;
        Payment existingsPayment = findPaymentsById(paymentNumber);
        if (existingsPayment == null) {
            errorMessage = "No payment with id" + paymentNumber + "exists";

        } else if (payment == null) {
            errorMessage = "Invalid payment data";
        } else if (payment.getCustomerNumber() != paymentNumber) {
            errorMessage = "Id does not match customer number";
        }

        if (errorMessage == null) {
            paymentRepository.save(payment);
        }
        return errorMessage;
    }




}
