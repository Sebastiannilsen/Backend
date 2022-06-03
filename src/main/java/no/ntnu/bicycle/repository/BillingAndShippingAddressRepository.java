package no.ntnu.bicycle.repository;

import no.ntnu.bicycle.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface BillingAndShippingAddressRepository extends CrudRepository<Customer, Long> {
}
