package no.ntnu.bicycle.controller;

import no.ntnu.bicycle.model.Payment;
import no.ntnu.bicycle.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * REST API controller for payment.
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    /**
     * Constructor for Payment controller with a parameter
     * @param paymentService PaymentService. payment service
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Gets payment
     * @return list of all payments
     */
    @GetMapping
    public List<Payment> getPayment() {return paymentService.getAllPayments();}

    /**
     * Gets one payment
     * @param paymentNumber int. Number of payment
     * @return 200 OK status on success or 404 not found if it does not exist
     */
    @GetMapping("{id}")
    public ResponseEntity<Payment> getOnePayment(@PathParam("payment")
                                                 @PathVariable("id")
                                                 int paymentNumber){
        ResponseEntity<Payment> response;
        Payment payment = paymentService.findPaymentsById(paymentNumber);
        if (payment != null) {
            response = new ResponseEntity<>(payment, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return response;
    }

    /**
     * Registers new payment
     * @param payment Payment. payment to be registered
     * @return 200 OK status on success or 400 bad request if it does not get registered
     */
    @PostMapping
    public ResponseEntity<String> registerNewPayment(@RequestBody Payment payment) {
        ResponseEntity<String> response;
        if (paymentService.addNewPayment(payment)) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Deletes payment
     * @param paymentId int. Payment to be deleted
     */
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable("id")
                              int paymentId) {
        paymentService.deletePayment(paymentId);
    }

    /**
     * Update payment
     * @param id int. that needs to be updated
     * @param payment Payment. payment that needs to be updated.
     * @return 200 OK status on success, or 400 bad request if it does not get updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id,
                                         @RequestBody Payment payment) {
        String errorMessage = paymentService.updatePayments(id, payment);
        ResponseEntity<String> response;
        if (errorMessage == null) {
            response = new ResponseEntity<>(HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        return response;
    }



}
