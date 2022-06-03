package no.ntnu.bicycle.service;

import no.ntnu.bicycle.model.BillingAndShippingAddress;
import no.ntnu.bicycle.repository.BillingAndShippingAddressRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Business logic related to billing and shipping address
 */
@Service
public class BillingAndShippingAddressService {
    private BillingAndShippingAddressRepository billingAndShippingAddressRepository;

    /**
     * Constructor with the parameter billing and shipping address repository.
     * @param billingAndShippingAddressRepository BillingAndShippingAddressRepository
     */
    public BillingAndShippingAddressService(BillingAndShippingAddressRepository billingAndShippingAddressRepository){
        this.billingAndShippingAddressRepository = billingAndShippingAddressRepository;
    }

    /**
     * Making iterable to list
     * @param iterable Iterable<BillingAndShippingAddress>. Iterable that needs to be converted to list
     * @return list of billing and shipping addresses
     */
    public List<BillingAndShippingAddress> iterableToList(Iterable<BillingAndShippingAddress> iterable){
        List<BillingAndShippingAddress> list = new LinkedList<>();
        iterable.forEach(list::add);
        return list;
    }
}
